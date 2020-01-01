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

import io.github.rohitawate.notehero.models.NoteConfig;

/**
 * NoteRenderer defines the interface for renderers
 * that produce HTML source from the note's source format.
 */
public interface NoteRenderer {
	/**
	 * Renders the note from the source format to HTML.
	 * Any configuration data must be stripped and parsed before
	 * rendering the note.
	 *
	 * @return HTML source string
	 */
	String render();

	/**
	 * Returns the rendered HTML note.
	 * Calls render if not already rendered.
	 *
	 * @return Rendered HTML string
	 */
	String getRenderedNote();

	/**
	 * Implementations should utilize a ConfigProcessor
	 * for stripping and parsing configuration data that may
	 * be part of the note's source. A NoteConfig object
	 * built out of this data should be returned.
	 *
	 * @return Parsed note configuration
	 */
	NoteConfig getConfig();
}
