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
import java.util.Map;
import java.util.Objects;

/**
 * Uses an indexing algorithm and builds a search index.
 * The search index gives a score for every token
 */
public interface IndexBuilder {
	/**
	 * A token is the result of the tokenization
	 * process on the textual representation of the
	 * note. It stores the lexeme itself and the
	 * location information about it.
	 * <p>
	 * Class parameters:
	 * - line: the line on which the lexeme is found
	 * - [start, end): the character bounds of the lexeme
	 * within the string
	 */
	class Token {
		static class Location {
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

		final String lexeme;
		final Location location;

		public Token(@JsonProperty("lexeme") String lexeme,
					 @JsonProperty("location") Location location) {
			this.lexeme = lexeme;
			this.location = location;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Token token = (Token) o;
			return Objects.equals(lexeme, token.lexeme);    // TODO: check location as well
		}
	}

	/**
	 * IndexData stores the actual information about a token i.e. the
	 * documents it appears in, its respective score and locations.
	 * <p>
	 * The core data structure is an associated array with the
	 * document's path for a key and the score and location as values.
	 * <p>
	 * Refer DESIGN.md for more details about the search index.
	 */
	class IndexData {
		/**
		 * DocumentData stores the token's score and occurrences within itself.
		 */
		static class DocumentData {
			final int score;
			final ArrayList<Token.Location> occurrences;

			DocumentData(int score) {
				this.score = score;
				this.occurrences = new ArrayList<>();
			}
		}

		/*
			 Key: Document's unique ID
			 Value: Score and list of occurrences (IndexData)
		*/
		Map<Integer, DocumentData> index = new HashMap<>();
	}

	/**
	 * Builds the search index and returns a map containing
	 * the tokens for keys and an Index object.
	 *
	 * @return Map representing the search index
	 */
	Map<String, IndexData> build();
}
