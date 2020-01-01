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

package io.github.rohitawate.notehero.ingestion;

import io.github.rohitawate.notehero.ingestion.renderer.NoteRenderer;
import io.github.rohitawate.notehero.ingestion.renderer.NoteRendererFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IngestionThread implements Runnable {
	private final IngestionController controller;
	private final String filePath;
	private String renderedNote = "";
	private boolean successful;

	public IngestionThread(IngestionController controller, String filePath) {
		this.controller = controller;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		try {
			String noteSource = readNoteFromDisk();
			NoteRenderer renderer = NoteRendererFactory.get(filePath, noteSource, this);

			if (renderer == null) {
				logError("Unknown source format: " + filePath);
				successful = false;
				return;
			}

			renderedNote = renderer.render();
			successful = true;
		} catch (IOException e) {
			logError("Could not read note: " + getFilePath());
			successful = false;
		}
	}

	private String readNoteFromDisk() throws IOException {
		Path path = Paths.get(getFilePath());
		return new String(Files.readAllBytes(path));
	}

	public String getRenderedNote() {
		return renderedNote;
	}

	public void logInfo(String msg) {
		controller.appendLog(msg, IngestionController.LogColors.BLUE);
	}

	public void logWarning(String msg) {
		controller.appendLog(msg, IngestionController.LogColors.YELLOW);
	}

	public void logError(String msg) {
		controller.appendLog(msg, IngestionController.LogColors.RED);
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean wasSuccessful() {
		return successful;
	}
}
