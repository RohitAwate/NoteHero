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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenderThread implements Runnable {
	final String filePath;
	private String renderedNote;
	private final RenderController controller;

	public RenderThread(String filePath, RenderController controller) {
		this.filePath = filePath;
		this.controller = controller;
	}

	private String readNoteFromDisk() throws IOException {
		Path path = Paths.get(filePath);
		return new String(Files.readAllBytes(path));
	}

	@Override
	public void run() {
		NoteRenderer renderer;
		try {
			String noteSource = readNoteFromDisk();

			if (filePath.endsWith(".md")) {
				renderer = new MarkdownRenderer(noteSource, this);
				renderedNote = renderer.render();
			}
		} catch (IOException e) {
			logError("Could not read note: " + filePath);
		}
	}

	public String getRenderedNote() {
		return renderedNote;
	}

	void logInfo(String msg) {
		controller.appendLog(msg, RenderController.LogColors.BLUE);
	}

	void logWarning(String msg) {
		controller.appendLog(msg, RenderController.LogColors.YELLOW);
	}

	void logError(String msg) {
		controller.appendLog(msg, RenderController.LogColors.RED);
	}
}
