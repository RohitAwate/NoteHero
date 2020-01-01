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

/**
 * Delegate to NoteRenderer for processing
 * any configuration data that might be part
 * of the note's source.
 */
interface ConfigProcessor {
	/**
	 * The stripped configuration data is returned
	 * here. If stripConfig() has not been called
	 * before, it should be called here.
	 *
	 * @return Stripped configuration substring
	 */
	String getConfigString();

	/**
	 * After stripping the configuration data, only
	 * the relevant markup source remains. This is
	 * returned here. If stripConfig() has not been
	 * called before, it should be called here.
	 *
	 * @return Markup source without configuration data
	 */
	String getStrippedNote();

	/**
	 * Parses the configuration substring and
	 * returns a NoteConfig object.
	 *
	 * @return Parsed note configuration
	 */
	NoteConfig getParsedConfig();
}
