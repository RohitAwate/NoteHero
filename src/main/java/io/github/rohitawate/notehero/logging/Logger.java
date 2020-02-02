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
import java.util.ArrayList;

/**
 * Stores a list of logs and provides an API
 * for conveniently and safely appending to it.
 * <p>
 * Logger is thread-safe and thus can be used
 * from any number of threads. The sequence of logs
 * will be predictable and identical to that of the
 * append calls.
 * <p>
 * Logger can interact with LogExporters to export
 * its logs list to any target format.
 */
public class Logger {
	/**
	 * The list of logs.
	 */
	private final ArrayList<Log> logs = new ArrayList<>();

	/**
	 * Denotes the threshold logs level required
	 * to be appended to the logs list.
	 * <p>
	 * Any logs with level greater than or equal to
	 * the threshold will be appended to the logs.
	 */
	private final Log.Level thresholdLevel;

	/**
	 * Checks the level of the incoming log and greater
	 * than or equal to thresholdLevel, appends to the list.
	 *
	 * @param log Incoming log
	 */
	private synchronized void append(Log log) {
		if (log.level.greaterThanEqualTo(thresholdLevel)) {
			logs.add(log);
		}
	}

	/**
	 * Uses a LogExporter to serialize and export the logs
	 * to some target format and location.
	 *
	 * @param exporter The appropriate LogExporter implementation
	 */
	public void export(LogExporter exporter) {
		exporter.serialize(logs);
		exporter.export();
	}

	public Logger(Log.Level thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}

	// Convenience methods to append logs of desired level to the list
	public void logInfo(String msg) {
		append(new Log(Log.Level.INFO, msg, LocalDateTime.now()));
	}

	public void logWarning(String msg) {
		append(new Log(Log.Level.WARNING, msg, LocalDateTime.now()));
	}

	public void logError(String msg) {
		append(new Log(Log.Level.ERROR, msg, LocalDateTime.now()));
	}
}
