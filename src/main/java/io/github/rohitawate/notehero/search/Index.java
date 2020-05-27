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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Index is the core data structure here.
 * It stores a list of documents, which contain
 * the tokens and their respective scores.
 */
class Index extends ArrayList<Document> {
	Set<TokenData> search(String query) {
		String[] keywords = query.split("\\s+");

		Set<TokenData> results = new HashSet<>();

		for (String keyword : keywords) {
			for (Document document : this) {
				if (document.containsKey(keyword)) {
					results.add(document.get(keyword));
				}
			}
		}

		return results;
	}
}

/**
 * Document is a map of tokens (strings) and their data.
 */
class Document extends HashMap<String, TokenData> {
	final int id;

	public Document(int id) {
		this.id = id;
	}
}

/**
 * TokenData stores the score of a token for a certain document
 * and a list of its occurrences within it.
 */
class TokenData {
	float score;
	final ArrayList<Location> occurrences = new ArrayList<>();
}

/**
 * Location stores:
 * - line: the line on which the lexeme is found
 * - [start, end): the character bounds of the lexeme within the document
 */
class Location {
	final int line;
	final int start, end;

	Location(@JsonProperty("line") int line,
			 @JsonProperty("start") int start,
			 @JsonProperty("end") int end) {
		this.line = line;
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return line == location.line &&
				start == location.start &&
				end == location.end;
	}
}
