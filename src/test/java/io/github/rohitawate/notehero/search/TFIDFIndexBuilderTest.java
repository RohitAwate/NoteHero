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

package io.github.rohitawate.notehero.search;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class TFIDFIndexBuilderTest {
	@Test
	void build() throws IOException, URISyntaxException {
		Map<Integer, String> docs = new HashMap<>();
		docs.put(1, readFile("TheDarkKnight"));
		docs.put(2, readFile("Java"));
		docs.put(3, readFile("DNA"));

		IndexBuilder builder = new TFIDFIndexBuilder(docs);
		Map<String, IndexBuilder.IndexData> index = builder.build();
	}

	private static String readFile(String name) throws URISyntaxException, IOException {
		URL url = TFIDFIndexBuilderTest.class.getResource("TFIDFIndexBuilder/" + name + ".txt");
		Path path = Paths.get(url.toURI());
		return new String(Files.readAllBytes(path));
	}
}