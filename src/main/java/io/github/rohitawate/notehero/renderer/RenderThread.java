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

public class RenderThread implements Runnable {
	final String filePath;
	private final RenderController controller;

	public RenderThread(String filePath, RenderController controller) {
		this.filePath = filePath;
		this.controller = controller;
	}

	@Override
	public void run() {

	}

	void logInfo(String msg) {
		controller.appendLog(msg, RenderController.LogColors.BLUE);
	}

	void logWarning(String msg) {
		controller.appendLog(msg, RenderController.LogColors.YELLOW);
	}

	void logError(String msg) {
		controller.appendLog(msg, RenderController.LogColors.RED);
	}
}
