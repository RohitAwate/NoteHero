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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFIndexBuilder implements IndexBuilder {
	private Map<Integer, String> docs;
	private int line;
	private int start;
	private int current;

	private final List<TermFrequencyMap<Token, Integer>> termFreqMaps = new ArrayList<>();

	public TFIDFIndexBuilder(Map<Integer, String> docs) {
		this.docs = docs;
	}

	private void buildTermFreqMaps() {
		for (Map.Entry<Integer, String> pair : docs.entrySet()) {
			resetParser();
			termFreqMaps.add(getTermFreqMap(pair.getKey(), pair.getValue()));
		}
	}

	static class TermFrequencyMap<K, V> extends HashMap<K, V> {
		private int docID;

		private TermFrequencyMap(int docID) {
			this.docID = docID;
		}
	}

	private TermFrequencyMap<Token, Integer> getTermFreqMap(int docID, String contents) {
		contents = contents.trim();
		TermFrequencyMap<Token, Integer> termFrequencies = new TermFrequencyMap<>(docID);

		for (; current < contents.length(); current++) {
			if (start != current && Character.isWhitespace(contents.codePointAt(current))) {
				addToFreqMap(termFrequencies, getToken(contents));
				skipWhitespace(contents);
			}
		}

		// Add last token
		addToFreqMap(termFrequencies, getToken(contents));

		return termFrequencies;
	}

	private void addToFreqMap(Map<Token, Integer> map, Token token) {
		/*
			TODO: Perform text pre-processing
			- remove punctuation
			- convert to lowercase
			- stemming/lemmatization
			- stop-word removal (TODO: See if this is necessary if we discard the
			                      lower score entries from the index later)
		 */
		token.lexeme = token.lexeme.replaceAll("[^a-zA-Z0-9]+", "");
		token.lexeme = token.lexeme.toLowerCase();

		if (token.lexeme.isEmpty()) return;

		if (map.containsKey(token)) {
			map.replace(token, map.get(token) + 1);
		} else {
			map.put(token, 1);
		}
	}

	private void resetParser() {
		line = 1;
		start = 0;
		current = 0;
	}

	private Token getToken(String input) {
		return new Token(input.substring(start, current), new Token.Location(line, start, current));
	}

	private void checkNewLine(String input) {
		if (input.codePointAt(current) == '\n') {
			line++;
		}
	}

	private void skipWhitespace(String input) {
		while (Character.isWhitespace(input.codePointAt(current))) {
			checkNewLine(input);
			current++;
		}

		start = current;
	}

	private int getDocFreq(Token token) {
		int df = 0;

		for (Map<Token, Integer> tfMap : termFreqMaps)
			if (tfMap.containsKey(token)) df++;

		return df;
	}

	@Override
	public Map<String, IndexData> build() {
		/*
			- iterate over all documents, tokenize them, and get term frequency maps
			- iterate over each map, and over each token and calculate TF-IDF
			- find document frequency from other maps
			- TF-IDF score = tf * log(number of documents/df)
			- add the token to the index
		 */

		buildTermFreqMaps();
		Map<String, IndexData> index = new HashMap<>();

		for (TermFrequencyMap<Token, Integer> tfMap : termFreqMaps) {
			for (Map.Entry<Token, Integer> entry : tfMap.entrySet()) {
				int df = getDocFreq(entry.getKey());
				double score = ((double) entry.getValue()) * Math.log((double) termFreqMaps.size() / df);
				if (score == 0.0) continue;
				addToIndex(index, tfMap.docID, entry.getKey(), score);
			}
		}

		return index;
	}

	/**
	 * Adds the token to index.
	 * <p>
	 * Please refer to DESIGN.md for a diagram of the index structure.
	 * <p>
	 * First, we check if the token's lexeme is already part of the map.
	 * If not, we add a new key and a corresponding IndexData object as value.
	 * <p>
	 * If yes, we must check if the corresponding document has been added to the
	 * Index object. If not, we add a key with the corresponding DocumentData object.
	 * <p>
	 * If yes, we add the token's location data to the list of occurrences.
	 *
	 * @param index - the core map of the index
	 * @param docID - the unique key of the document to which the token belongs
	 * @param token - the token to be added
	 * @param score - the token's TF-IDF score for that document
	 */
	private void addToIndex(Map<String, IndexData> index, int docID, Token token, double score) {
		IndexData indexData;
		if ((indexData = index.getOrDefault(token.lexeme, null)) != null) {
			IndexData.DocumentData documentData;
			if ((documentData = indexData.index.getOrDefault(docID, null)) != null) {
				documentData.occurrences.add(token.location);
			} else {
				documentData = new IndexData.DocumentData(score);
				documentData.occurrences.add(token.location);
				indexData.index.put(docID, documentData);
			}
		} else {
			IndexData newIndexData = new IndexData();
			IndexData.DocumentData newDocumentData = new IndexData.DocumentData(score);
			newDocumentData.occurrences.add(token.location);
			newIndexData.index.put(docID, newDocumentData);
			index.put(token.lexeme, newIndexData);
		}
	}
}
