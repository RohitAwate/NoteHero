/*
 * Copyright 2019 Rohit Awate.
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

/**
 * NoteRenderer defines the interface for
 * classes that render notes from any source format
 * to HTML.
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
     * Implementations should utilize a ConfigProcessor
     * for stripping and parsing configuration data that may
     * be part of the note's source. A NoteConfig object
     * built out of this data should be returned.
     *
     * @return Parsed note configuration
     */
    NoteConfig getConfig();
}
