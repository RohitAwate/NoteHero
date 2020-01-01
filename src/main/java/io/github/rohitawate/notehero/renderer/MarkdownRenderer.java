/*
 * Copyright 2020 Rohit Awate.
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

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import io.github.rohitawate.notehero.config.ConfigProcessor;
import io.github.rohitawate.notehero.config.ConfigProcessorFactory;
import io.github.rohitawate.notehero.ingestion.IngestionThread;
import io.github.rohitawate.notehero.models.NoteConfig;

import java.util.Arrays;

/**
 * Implementation of NoteRenderer for the Markdown
 * source format.
 */
class MarkdownRenderer implements NoteRenderer {
	private String noteSource;
	private NoteConfig config;
	private String renderedNote;

	private IngestionThread ingestionThread;

	MarkdownRenderer(String noteSource, IngestionThread ingestionThread) {
		this.noteSource = noteSource;
		this.ingestionThread = ingestionThread;
	}

	@Override
	public String render() {
		if (renderedNote != null) return renderedNote;

		// First process the YAML Front Matter
		ConfigProcessor configProcessor = ConfigProcessorFactory.get("yaml", noteSource, ingestionThread);
		if (configProcessor == null) {
			ingestionThread.getLogger().logError("Could not produce ConfigProcessor for YAML Front Matter");
			return "";
		}

		// This will remove the YFM from the note source and parse it into a NoteConfig instance
		this.config = configProcessor.getParsedConfig();
		// Re-assigning YFM-stripped note source
		this.noteSource = configProcessor.getStrippedNote();

		// Set the options and extensions for flexmark's parser
		MutableDataSet parserOptions = new MutableDataSet();
		parserOptions.set(Parser.EXTENSIONS, Arrays.asList(GitLabExtension.create(),
				TablesExtension.create(), StrikethroughExtension.create()));

		// Render using flexmark
		Parser parser = Parser.builder(parserOptions).build();
		Node noteNode = parser.parse(noteSource);
		HtmlRenderer renderer = HtmlRenderer.builder(parserOptions).build();

		// save rendered HTML to local variable
		renderedNote = renderer.render(noteNode);

		return renderedNote;
	}

	@Override
	public String getRenderedNote() {
		return renderedNote != null ? renderedNote : render();
	}

	@Override
	public NoteConfig getConfig() {
		if (config == null) render();
		return config;
	}
}
