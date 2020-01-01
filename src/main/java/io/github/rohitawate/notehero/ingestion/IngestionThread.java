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

import io.github.rohitawate.notehero.logging.Logger;
import io.github.rohitawate.notehero.renderer.NoteRenderer;
import io.github.rohitawate.notehero.renderer.NoteRendererFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class IngestionThread implements Runnable {
	private final String filePath;
	private String renderedNote = "";
	private boolean successful;
	private final Logger logger;
	private final CountDownLatch latch;

	public IngestionThread(IngestionController controller, String filePath, CountDownLatch latch) {
		this.logger = controller.logger;
		this.filePath = filePath;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			String noteSource = readNoteFromDisk();
			NoteRenderer renderer = NoteRendererFactory.get(filePath, noteSource, this);

			if (renderer == null) {
				logger.logError("Unknown source format: " + filePath);
				successful = false;
				return;
			}

			renderedNote = renderer.render();
			successful = true;
		} catch (IOException e) {
			logger.logError("Could not read note: " + getFilePath());
			successful = false;
		} finally {
			latch.countDown();
		}
	}

	private String readNoteFromDisk() throws IOException {
		Path path = Paths.get(getFilePath());
		return new String(Files.readAllBytes(path));
	}

	public String getRenderedNote() {
		return renderedNote;
	}

	public String getFilePath() {
		return filePath;
	}

	public boolean wasSuccessful() {
		return successful;
	}

	public Logger getLogger() {
		return logger;
	}
}
