/*
 * Copyright 2019-2020 Rohit Awate
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

import io.github.rohitawate.notehero.logging.Log;
import io.github.rohitawate.notehero.logging.Logger;
import io.github.rohitawate.notehero.models.Note;
import io.github.rohitawate.notehero.renderer.NoteRenderer;
import io.github.rohitawate.notehero.renderer.NoteRendererFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IngestionController {
	final Logger logger = new Logger(Log.Level.WARNING);

	private final List<String> candidateFilePaths;

	public IngestionController(List<String> candidateFilePaths) {
		this.candidateFilePaths = candidateFilePaths;
	}

	public void ingestAll() {
		Note[] notes = new Note[candidateFilePaths.size()];

		for (int i = 0; i < candidateFilePaths.size(); i++) {
			String currentPath = candidateFilePaths.get(i);
			try {
				notes[i] = processNote(readNoteFromDisk(currentPath), currentPath);
			} catch (IOException e) {
				logger.logWarning("Error while reading file: " + currentPath);
			}
		}
	}

	private Note processNote(String noteSource, String filePath) {
		NoteRenderer renderer = NoteRendererFactory.get(noteSource, filePath, this);

		if (renderer == null) {
			logger.logError("Unknown file format: " + filePath);
			return null;
		}

		return new Note(renderer.render(), renderer.getConfig());
	}

	private String readNoteFromDisk(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		return new String(Files.readAllBytes(path));
	}

	public Logger getLogger() {
		return logger;
	}
}