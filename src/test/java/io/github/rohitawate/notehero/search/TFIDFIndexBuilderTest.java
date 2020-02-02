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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TFIDFIndexBuilderTest {
	@Test
	void tokenize() throws IOException, URISyntaxException {
		String s = "\n\nHello world this \n\tis NoteHero\n\n I am \n\namazing\n\n";
		IndexBuilder builder = new TFIDFIndexBuilder(s);
		List<IndexBuilder.Token> tokens = builder.tokenize();

		// Load test case
		URL url = getClass().getResource("TFIDFIndexBuilder/TestCase.json");
		Path path = Paths.get(url.toURI());
		String fileContents = new String(Files.readAllBytes(path));

		ObjectMapper mapper = new JsonMapper();
		List<IndexBuilder.Token> expectedTokens =
				mapper.readValue(fileContents, mapper.getTypeFactory().constructCollectionType(List.class, IndexBuilder.Token.class));

		assertArrayEquals(expectedTokens.toArray(), tokens.toArray());
	}
}