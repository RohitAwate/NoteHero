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

package io.github.rohitawate.notehero.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogTest {
	@Test
	void greaterThanEqualTo() {
		assertTrue(Log.Level.ERROR.greaterThanEqualTo(Log.Level.WARNING));
		assertTrue(Log.Level.ERROR.greaterThanEqualTo(Log.Level.INFO));
		assertTrue(Log.Level.WARNING.greaterThanEqualTo(Log.Level.INFO));

		assertTrue(Log.Level.ERROR.greaterThanEqualTo(Log.Level.ERROR));
		assertTrue(Log.Level.WARNING.greaterThanEqualTo(Log.Level.WARNING));
		assertTrue(Log.Level.INFO.greaterThanEqualTo(Log.Level.INFO));

		assertFalse(Log.Level.INFO.greaterThanEqualTo(Log.Level.ERROR));
		assertFalse(Log.Level.INFO.greaterThanEqualTo(Log.Level.WARNING));
		assertFalse(Log.Level.WARNING.greaterThanEqualTo(Log.Level.ERROR));
	}
}