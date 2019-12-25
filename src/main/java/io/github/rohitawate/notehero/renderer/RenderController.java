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

public class RenderController {
    private String buildLog = "";

    void appendInfo(final String msg) {
        appendLog(msg, LogColors.BLUE);
    }

    void appendWarning(final String msg) {
        appendLog(msg, LogColors.YELLOW);
    }

    void appendError(final String msg) {
        appendLog(msg, LogColors.RED);
    }

    private void appendLog(final String msg, final String color) {
        buildLog += color + msg + LogColors.RESET + "\n";
    }

    public String getBuildLog() {
        return this.buildLog;
    }

    interface LogColors {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        String BLUE = "\u001B[34m";
    }
}
