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

import java.time.LocalDateTime;

/**
 * Denotes a single entry in the logs.
 * Entries contain information about events.
 */
public class Log {
	public final Level level;
	public final String msg;
	public final LocalDateTime dateTime;

	public Log(Level level, String msg, LocalDateTime dateTime) {
		this.level = level;
		this.msg = msg;
		this.dateTime = dateTime;
	}

	/**
	 * Denotes the severity level of the log.
	 */
	public enum Level {
		// General-purpose level for anything but warnings and errors.
		INFO,

		// Indicates that something unexpected was encountered, but has been handled
		// and will not result in failure.
		WARNING,

		// Catastrophic error which leads to the failure of a process.
		ERROR;

		/**
		 * @return Numeric value for level
		 */
		private int getValue() {
			switch (this) {
				case ERROR:
					return 3;
				case WARNING:
					return 2;
				default:
					return 1;
			}
		}

		/**
		 * Compares if the current level is greater than or
		 * equal to that of some other.
		 *
		 * @param level The level against which the comparison is to be made
		 * @return true if >=, else false
		 */
		public boolean greaterThanEqualTo(Level level) {
			return this.getValue() >= level.getValue();
		}
	}
}
