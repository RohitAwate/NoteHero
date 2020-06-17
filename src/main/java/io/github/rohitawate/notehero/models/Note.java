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

package io.github.rohitawate.notehero.models;

import java.util.Objects;
import java.util.UUID;

/**
 * A Note object contains the rendered
 * note and its configuration.
 */
public class Note {
	public final UUID noteID;
	public final UUID buildID;
	public final String markdown;
	public final String html;
	public final NoteConfig config;

	public Note(UUID buildID, String markdown, String html, NoteConfig config) {
		this.noteID = UUID.randomUUID();
		this.buildID = buildID;
		this.markdown = markdown;
		this.html = html;
		this.config = config;
	}

	public Note(UUID noteID, UUID buildID, String markdown, String html, NoteConfig config) {
		this.noteID = noteID;
		this.buildID = buildID;
		this.markdown = markdown;
		this.html = html;
		this.config = config;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Note note = (Note) o;
		return Objects.equals(noteID, note.noteID) &&
				Objects.equals(buildID, note.buildID) &&
				Objects.equals(markdown, note.markdown) &&
				Objects.equals(html, note.html) &&
				Objects.equals(config, note.config);
	}

	@Override
	public int hashCode() {
		return Objects.hash(noteID, buildID, markdown, html, config);
	}

	@Override
	public String toString() {
		return "Note{" +
				"noteID=" + noteID +
				", repoID=" + buildID +
				", markdown='" + markdown + '\'' +
				", html='" + html + '\'' +
				", config=" + config +
				'}';
	}
}
