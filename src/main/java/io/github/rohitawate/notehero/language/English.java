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

package io.github.rohitawate.notehero.language;

import io.github.rohitawate.notehero.text.CaseFormat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class English {
	public static final Set<String> STOPWORDS = new HashSet<>();

	public static boolean isStopWord(String word) {
		if (STOPWORDS.isEmpty()) {
			try {
				loadStopwords();
			} catch (IOException | URISyntaxException e) {
				return false;
			}
		}

		return STOPWORDS.contains(word.toLowerCase());
	}

	private static void loadStopwords() throws IOException, URISyntaxException {
		URL url = CaseFormat.class.getResource("EnglishStopWords.txt");
		Path path = Paths.get(url.toURI());
		String[] stopWords = new String(Files.readAllBytes(path)).split("\n+");
		STOPWORDS.addAll(Arrays.asList(stopWords));
	}
}
