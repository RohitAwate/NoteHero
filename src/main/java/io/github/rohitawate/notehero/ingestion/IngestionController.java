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

import io.github.rohitawate.notehero.database.BuildAccessor;
import io.github.rohitawate.notehero.database.NoteAccessor;
import io.github.rohitawate.notehero.database.Transaction;
import io.github.rohitawate.notehero.logging.Log;
import io.github.rohitawate.notehero.logging.Logger;
import io.github.rohitawate.notehero.models.Build;
import io.github.rohitawate.notehero.models.Note;
import io.github.rohitawate.notehero.renderer.NoteRenderer;
import io.github.rohitawate.notehero.renderer.NoteRendererFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class IngestionController {
	final Logger logger = new Logger(Log.Level.WARNING);

	private final Build build;
	private final List<String> candidateFilePaths;

	private final Transaction transaction = new Transaction();
	private final NoteAccessor noteAccessor = new NoteAccessor();

	public IngestionController(Build build, List<String> candidateFilePaths) {
		this.build = build;
		this.candidateFilePaths = candidateFilePaths;

		new BuildAccessor().create(build);

		this.transaction.addAccessor(noteAccessor);
	}

	public void ingestAll() {
		/*
			 Ingestion process:
			 - render note
			 - save rendered output to database
			 - get note IDs to be used as document IDs
			 - build search index
			 - save search index
		*/

		Note[] notes = new Note[candidateFilePaths.size()];

		for (int i = 0; i < candidateFilePaths.size(); i++) {
			String currentPath = candidateFilePaths.get(i);

			try {
				notes[i] = processNote(readNoteFromDisk(currentPath), currentPath);
				noteAccessor.create(notes[i]);
			} catch (IOException e) {
				logger.logWarning("Error while reading file: " + currentPath);
			} catch (IllegalArgumentException e) {
				logger.logError(e.getMessage());
			}
		}

		try {
			transaction.commit();
		} catch (SQLException e) {
			logger.logError("Failed to save built notes.");
			e.printStackTrace();
		}
	}

	private Note processNote(String noteSource, String filePath) {
		try {
			NoteRenderer renderer = NoteRendererFactory.get(noteSource, filePath, this);
			return new Note(UUID.randomUUID(), build.getBuildID(), noteSource, renderer.render(), renderer.getConfig());
		} catch (IllegalArgumentException e) {
			logger.logWarning(e.getMessage());
			throw new IllegalArgumentException("Could not process note: " + filePath);
		}
	}

	private String readNoteFromDisk(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		return new String(Files.readAllBytes(path));
	}

	public Logger getLogger() {
		return logger;
	}
}