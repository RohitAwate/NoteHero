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

package io.github.rohitawate.notehero.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigProcessorFactoryTest {

	@Test
	void get() {
		assertNotNull(ConfigProcessorFactory.get("yaml", "", null, null));
		assertEquals(YAMLFrontMatterProcessor.class, ConfigProcessorFactory.get("yaml", "", null, null).getClass());

		assertNotNull(ConfigProcessorFactory.get("yml", "", null, null));
		assertEquals(YAMLFrontMatterProcessor.class, ConfigProcessorFactory.get("yml", "", null, null).getClass());

		assertNull(ConfigProcessorFactory.get("unknown", "", null, null));
		assertNull(ConfigProcessorFactory.get("", "", null, null));
	}
}