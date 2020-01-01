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

package io.github.rohitawate.notehero.ingestion;

import io.github.rohitawate.notehero.logging.Log;
import io.github.rohitawate.notehero.logging.Logger;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IngestionController {
	final Logger logger = new Logger(Log.Level.WARNING);

	private final ArrayList<String> candidateFiles;

	public IngestionController(ArrayList<String> candidateFiles) {
		this.candidateFiles = candidateFiles;
	}

	public void start() {
		CountDownLatch latch = new CountDownLatch(candidateFiles.size());
		ExecutorService executor = Executors.newCachedThreadPool();
		candidateFiles.
				forEach(candidate -> executor.execute(new IngestionThread(this, candidate, latch)));

		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.logError("Ingestion process was interrupted.");
		}
	}
}