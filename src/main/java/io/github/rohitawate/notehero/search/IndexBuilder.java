package io.github.rohitawate.notehero.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	 */
	class Token {
		static class Location {
			final int line;
			final int start, end;

			Location(int line, int start, int end) {
				this.line = line;
				this.start = start;
				this.end = end;
			}
		}

		final String lexeme;
		final Location location;

		public Token(String lexeme, Location location) {
			this.lexeme = lexeme;
			this.location = location;
		}
	}

	/**
	 * Produces a set of tokens from the input note string.
	 *
	 * @return List of tokens
	 */
	List<Token> tokenize();

	/**
	 * Index stores the actual information about a token
	 * and its occurrences in the notes.
	 * <p>
	 * The core data structure is an associated array
	 * with the token's lexeme for a key and a list of documents
	 * in which it appears as a value.
	 * <p>
	 * Refer DESIGN.md for more details about the index.
	 */
	class Index {
		/**
		 * Document stores the token's score and occurrences
		 * within itself.
		 */
		static class Document {
			final int score;
			final ArrayList<Token.Location> occurrences;

			Document(int score) {
				this.score = score;
				this.occurrences = new ArrayList<>();
			}
		}

		Map<String, List<Document>> index;
	}

	/**
	 * Builds the search index and returns a map containing
	 * the tokens for keys and an Index object.
	 *
	 * @return Map representing the search index
	 */
	Map<String, Index> build();
}
