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

/**
 * A Note object contains the rendered
 * note and its configuration.
 */
public class Note {
	public final String renderedNote;
	public final NoteConfig config;

	public Note(String renderedNote, NoteConfig config) {
		this.renderedNote = renderedNote;
		this.config = config;
	}

	@Override
	public String toString() {
		return "Note{" +
				"renderedNote='" + renderedNote + '\'' +
				", config=" + config +
				'}';
	}
}