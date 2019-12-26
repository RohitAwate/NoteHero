/*
 * Copyright 2019 Rohit Awate.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.rohitawate.notehero.renderer;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;
import java.util.Stack;

/**
 * Implementation of ConfigProcessor for processing configuration data
 * of the YAML Front Matter format.
 */
class YAMLFrontMatterProcessor implements ConfigProcessor {
	private String noteSource;
	private String yamlSource = "";
	private NoteConfig config;

	// Set to true if stripConfig finds no YAML Front Matter.
	// Used to skip subsequent calls.
	private boolean noFrontMatter;
	private RenderThread renderThread;

	public YAMLFrontMatterProcessor(String noteSource, RenderThread renderThread) {
		this.noteSource = noteSource;
		this.renderThread = renderThread;
	}

	/**
	 * Tries to find the Front Matter delimiters i.e. three hyphens "---".
	 * If both are found, removes this substring from the source and saves both.
	 * If none or just one of them are found, errors are appended to the build log.
	 */
	@Override
	public void stripConfig() {
		// Check if stripConfig found no YAML Front Matter in a previous call.
		// If true, we skip further calls here.
		if (noFrontMatter) {
			return;
		}

		final String yfmDelimiter = "---";

		// Get rid of leading/trailing whitespaces
		noteSource = noteSource.trim();

		// The source MUST start with the opening delimiter
		if (!noteSource.startsWith(yfmDelimiter)) {
			renderThread.logWarning("YAML Front Matter not found at start of file.");
			noFrontMatter = true;
			return;
		}

		// The delimiters must be on separate lines.
		// Thus, we ensure that the noteSource is more than the combined
		// length of the opening delimiter (3) and the newline (1).
		if (noteSource.length() == 4) {
			renderThread.logWarning("YAML Front Matter not terminated.");
			noFrontMatter = true;
			return;
		}

		// Searching for closing delimiter from index 4 (3 hyphens + \n)
		// to skip the opening one
		int end = noteSource.indexOf(yfmDelimiter, 4);
		if (end == -1) {
			renderThread.logWarning("YAML Front Matter not terminated.");
			noFrontMatter = true;
			return;
		}

		// We only store the actual YAML thus skipping the
		// opening delimiter (4 = 3 hyphens + newline)
		yamlSource = noteSource.substring(4, end);
		yamlSource = yamlSource.trim();

		// We store the actual note stripped of the YAML Front Matter.
		// It begins after the closing delimiter (3 hyphens).
		// We trim to get rid of any whitespace between the Front Matter
		// and the actual content.
		noteSource = noteSource.substring(end + 3);
		noteSource = noteSource.trim();
	}

	/**
	 * Parses the YAML Front Matter and returns a NoteConfig
	 * instance initialized with values from YFM or default ones.
	 * YAML Front Matter should contain a YAML object with the key "notehero"
	 * which gets loaded into NoteConfig.
	 *
	 * @return NoteConfig instance initialized with values from "notehero" object
	 * from the YAML Front Matter
	 */
	@Override
	public NoteConfig getParsedConfig() {
		if (config != null) {
			return config;
		}

		// Check if yamlSource is empty (i.e. possibly not yet stripped) and
		// if noFrontMatter is false (confirming the possibility). In that case,
		// stripConfig is called so that we have a value to parse.
		if (yamlSource.isEmpty() && !noFrontMatter) {
			stripConfig();
		}

        /*
             We need to return a NoteConfig instance initialized with default values
             in the following cases:
             - yamlSource is still empty
             - yamlSource contains no "notehero" object
             - "notehero" object contains no values for optional attributes

             NoteHero expects 4 attributes in the aforementioned object:
              1. title (required)
              2. sudo (optional, defaults to true)
              3. slug (optional, refer DESIGN.md for default values)
              4. categories (optional, refer DESIGN.md for default values)

             For details about how and where these attributes are used, refer DESIGN.md.
         */

		if (yamlSource.isEmpty()) return generateDefaultConfig();

		Yaml yaml = new Yaml();
		Map<String, Object> yamlMap = yaml.load(yamlSource);
		if (!yamlMap.containsKey("notehero")) return generateDefaultConfig();

		Map<String, String> noteHero = (Map<String, String>) yamlMap.get("notehero");

		String title = noteHero.getOrDefault("title", generateDefaultTitle(renderThread.filePath));
		String slug = noteHero.getOrDefault("slug", generateDefaultSlug(renderThread.filePath));

		boolean sudo = true;    // defaults to true
		if (noteHero.containsKey("sudo")) {
			sudo = Boolean.getBoolean(noteHero.get("sudo"));
		}

		String[] categories;
		if (noteHero.containsKey("categories")) {
			categories = noteHero.get("categories").split("\\s>\\s");
		} else {
			categories = generateDefaultCategories();
		}

		config = new NoteConfig(title, categories, slug, sudo);

		return config;
	}

	private String generateDefaultTitle(String filePath) {
		// extract filename
		String fileName = getFileName(filePath);

		// identify fileName type
		// - fileName with spaces
		// - snake_case
		// - CamelCase
		//
		// Separate into individual words and then concatenate by adding spaces.

		StringBuilder titleBuilder = new StringBuilder();
		if (fileName.contains(" ")) {
			// example: 'Hello world' becomes 'Hello world' thus preserving the case
			// of each word
			String[] tokens = fileName.split("\\s+");

			for (String token : tokens) {
				titleBuilder.append(token);
				titleBuilder.append(" ");
			}
		} else if (fileName.contains("_")) {
			// example: 'hello_Word' becomes 'Hello World'
			String[] tokens = fileName.split("_");

			for (String token : tokens) {
				token = token.toLowerCase();
				titleBuilder.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
				titleBuilder.append(" ");
			}
		} else {
			// example: 'HelloWorld' becomes 'Hello World'
			char c;
			for (int i = 0; i < fileName.length(); i++) {
				c = fileName.charAt(i);
				if (i != 0 && Character.isUpperCase(c)) {
					titleBuilder.append(" ");
				}

				titleBuilder.append(c);
			}
		}

		return titleBuilder.toString();
	}

	/**
	 * Uses the note's location in the filesystem relative to the
	 * notes/ directory as a means to deduce the categories.
	 * Generates the category hierarchy based on that of the note's
	 * container directory.
	 * <p>
	 * For example, if the file is located in notes/math/Calculus/Integration and Differentiation/,
	 * then the deduced categories are as Math > Calculus > Integration and Differentiation.
	 *
	 * @return String array containing the deduced categories in decreasing order
	 * of hierarchy
	 */
	private String[] generateDefaultCategories() {
		// Obtain the file's path
		String filePath = renderThread.filePath;

		// NoteHero expects all note sources to reside within
		// notes/ directory. Thus, all paths would contain it.
		// We generate categories based on the directory hierarchy
		// within the notes/ directory. Thus, we take the substring
		// starting after notes/.
		filePath = filePath.substring(filePath.indexOf("notes/") + 6);

		// If the note resides within notes/, then filePath should
		// be empty by now and thus leave no room for us to deduce
		// any categories. We return an empty string array.
		if (filePath.isEmpty()) return new String[]{};

		// Else, note resides within some directory hierarchy within notes/.
		// In this case, we extract the directory names to build up
		// our string array of categories.
		File fd = new File(filePath);
		Stack<String> categoryStack = new Stack<>();

		String category;
		while ((category = fd.getParent()) != null) {
			// Clean up the directory name
			category = generateDefaultTitle(category);

			// Push to stack since our category hierarchy
			// is the reverse of the directories'
			categoryStack.push(category);
		}

		String[] categories = new String[categoryStack.size()];
		int i = 0;
		while (!categoryStack.empty()) {
			categories[i++] = categoryStack.pop();
		}

		return categories;
	}

	private String generateDefaultSlug(String filePath) {
		String fileName = getFileName(filePath);

		// identify fileName type
		// - fileName with spaces
		// - snake_case
		// - CamelCase
		//
		// Separate into individual words, convert them to lowercase and
		// then concatenate by adding hyphens.

		StringBuilder slugBuilder = new StringBuilder();
		if (fileName.contains(" ")) {
			// example: 'Hello world' becomes 'hello-world' thus preserving the case
			// of each word
			String[] tokens = fileName.split("\\s+");

			for (String token : tokens) {
				slugBuilder.append(token.toLowerCase());
				slugBuilder.append("-");
			}
		} else if (fileName.contains("_")) {
			// example: 'hello_Word' becomes 'hello-world'
			String[] tokens = fileName.split("_");

			for (String token : tokens) {
				slugBuilder.append(token.toLowerCase());
				slugBuilder.append("-");
			}
		} else {
			// example: 'HelloWorld' becomes 'hello-world'
			char c;
			for (int i = 0; i < fileName.length(); i++) {
				c = fileName.charAt(i);
				if (i != 0 && Character.isUpperCase(c)) {
					slugBuilder.append("-");
				}

				slugBuilder.append(c);
			}
		}

		return slugBuilder.toString();
	}

	@NotNull
	private String getFileName(String filePath) {
		// extract fileName
		File fd = new File(filePath);
		String fileName = fd.getName();

		// remove extension
		fileName = fileName.split("\\.")[0];

		// remove all characters except upper/lower case alphabet,
		// numbers, underscores and spaces
		fileName = fileName.replaceAll("[^a-zA-Z0-9_\\s]+", "");

		return fileName;
	}

	/**
	 * Creates a NoteConfig instance initialized with default values.
	 *
	 * @return NoteConfig instance initialized with default values
	 */
	private NoteConfig generateDefaultConfig() {
		return new NoteConfig(generateDefaultTitle(renderThread.filePath), generateDefaultCategories(),
				generateDefaultSlug(renderThread.filePath), true);
	}

	/**
	 * @return Note source stripped of YAML Front Matter
	 */
	@Override
	public String getStrippedNote() {
		// Check if yamlSource is empty (i.e. possibly not yet stripped) and
		// if noFrontMatter is false (confirming the possibility). In that case,
		// stripConfig is called to produce the return value.
		if (yamlSource.isEmpty() && !noFrontMatter) {
			stripConfig();
		}

		return noteSource;
	}

	/**
	 * @return Stripped YAML Front Matter
	 */
	@Override
	public String getConfigString() {
		// Check if yamlSource is empty (i.e. possibly not yet stripped) and
		// if noFrontMatter is false (confirming the possibility). In that case,
		// stripConfig is called to produce the return value.
		if (yamlSource.isEmpty() && !noFrontMatter) {
			stripConfig();
		}

		return yamlSource;
	}
}
