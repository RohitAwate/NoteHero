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

package io.github.rohitawate.notehero.ingestion.renderer;

import io.github.rohitawate.notehero.ingestion.IngestionThread;

import java.io.File;

public class NoteRendererFactory {
	public static NoteRenderer get(String filePath, String noteSource, IngestionThread ingestionThread) {
		String fileName = new File(filePath).getName();

		if (fileName.endsWith(".md") || fileName.endsWith(".markdown") || fileName.endsWith(".mdown") || fileName.endsWith(".mkd")) {
			return new MarkdownRenderer(noteSource, ingestionThread);
		} else {
			return null;
		}
	}
}
