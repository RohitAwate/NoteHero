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

package io.github.rohitawate.notehero.logging;

import java.util.List;

/**
 * Serializes and exports logs to a target format.
 * Used by Logger to export its logs externally.
 */
public interface LogExporter {
	/**
	 * Converts the list of logs to an internal representation
	 * fit for export.
	 *
	 * @param logs The list of logs to serialize
	 */
	void serialize(List<Log> logs);

	/**
	 * Exports the internal representation to its target location.
	 */
	void export();
}
