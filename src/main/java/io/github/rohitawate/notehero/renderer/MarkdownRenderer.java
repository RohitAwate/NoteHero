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

import java.util.Arrays;

/**
 * Implementation of NoteRenderer for the Markdown
 * source format.
 */
class MarkdownRenderer implements NoteRenderer {
	private String noteSource;
	private NoteConfig config;
	private String renderedNote;

	private RenderThread renderThread;

	MarkdownRenderer(String noteSource, RenderThread renderThread) {
		this.noteSource = noteSource;
		this.renderThread = renderThread;
	}

	@Override
	public String render() {
		if (renderedNote != null) return renderedNote;

		// First process the YAML Front Matter
		YAMLFrontMatterProcessor yfmProcessor = new YAMLFrontMatterProcessor(noteSource, renderThread);
		// This will remove the YFM from the note source and parse it into a NoteConfig instance
		this.config = yfmProcessor.getParsedConfig();
		// Re-assigning YFM-stripped note source
		this.noteSource = yfmProcessor.getStrippedNote();

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
