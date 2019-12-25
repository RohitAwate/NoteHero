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

/**
 * Implementation of ConfigProcessor for processing configuration data
 * of the YAML Front Matter format.
 */
class YAMLFrontMatterProcessor implements ConfigProcessor {
    private String noteSource;
    private String yamlSource = "";

    // Set to true if stripConfig finds no YAML Front Matter.
    // Used to skip subsequent calls.
    private boolean noFrontMatter;
    private RenderController controller;

    public YAMLFrontMatterProcessor(String noteSource, RenderController controller) {
        this.noteSource = noteSource;
        this.controller = controller;
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
            controller.logWarning("YAML Front Matter not found at start of file.");
            noFrontMatter = true;
            return;
        }

        // The delimiters must be on separate lines.
        // Thus, we ensure that the noteSource is more than the combined
        // length of the opening delimiter (3) and the newline (1).
        if (noteSource.length() == 4) {
            controller.logWarning("YAML Front Matter not terminated.");
            noFrontMatter = true;
            return;
        }

        // Searching for closing delimiter from index 4 (3 hyphens + \n)
        // to skip the opening one
        int end = noteSource.indexOf(yfmDelimiter, 4);
        if (end == -1) {
            controller.logWarning("YAML Front Matter not terminated.");
            noFrontMatter = true;
            return;
        }

        // We only store the actual YAML thus skipping the
        // opening delimiter (4 = 3 hyphens + newline)
        yamlSource = noteSource.substring(4, end);

        // We store the actual note stripped of the YAML Front Matter.
        // It begins after the closing delimiter (3 hyphens).
        // We trim to get rid of any whitespace between the Front Matter
        // and the actual content.
        noteSource = noteSource.substring(end + 3);
        noteSource = noteSource.trim();
    }

    @Override
    public NoteConfig parseConfig() {
        return null;
    }

    /**
     * @return Note source stripped of YAML Front Matter
     */
    @Override
    public String getStrippedNote() {
        // Check if yamlSource is empty (i.e. possibly not yet stripped) and
        // if noFrontMatter is false (confirming the possibility). In that case,
        // stripConfig is called to produce the return value.
        if (yamlSource.equals("") && !noFrontMatter) {
            stripConfig();
        }

        return noteSource;
    }

    /**
     * @return Stripped YAML Front Matter
     */
    @Override
    public String getStrippedConfig() {
        // Check if yamlSource is empty (i.e. possibly not yet stripped) and
        // if noFrontMatter is false (confirming the possibility). In that case,
        // stripConfig is called to produce the return value.
        if (yamlSource.equals("") && !noFrontMatter) {
            stripConfig();
        }

        return yamlSource;
    }
}
