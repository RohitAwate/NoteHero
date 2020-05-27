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

import io.github.rohitawate.notehero.language.English;

import java.util.Map;

public class TFIDFIndexBuilder implements IndexBuilder {
	private Map<Integer, String> candidateDocs;

	// parser variables
	private int line;
	private int start;
	private int current;

	private final Index index = new Index();

	/**
	 * @param candidateDocs - map with a unique integer identifying the document for the key,
	 *                      and the document's contents as the value
	 */
	public TFIDFIndexBuilder(Map<Integer, String> candidateDocs) {
		this.candidateDocs = candidateDocs;
	}

	@Override
	public Index build() {
		/*
			- scan all documents, tokenize them, and generate document maps
			- iterate over document maps and their tokens and calculate their TF-IDF
				- find document frequency from other maps
			- TF-IDF score = tf * log(number of documents/df)
		 */

		buildDocuments();

		for (Document document : index) {
			for (Map.Entry<String, TokenData> pair : document.entrySet()) {
				String token = pair.getKey();
				TokenData tokenData = pair.getValue();

				int documentFreq = getDocFreq(token);
				tokenData.score = tokenData.score * (float) Math.log((double) index.size() / documentFreq);
			}
		}

		return index;
	}

	private void buildDocuments() {
		for (Map.Entry<Integer, String> pair : candidateDocs.entrySet()) {
			resetParser();

			String contents = pair.getValue();
			contents = contents.trim();
			Document document = new Document(pair.getKey());

			// iterate over document and generate tokens
			for (; current < contents.length(); current++) {
				if (start != current && Character.isWhitespace(contents.codePointAt(current))) {
					// add token to document
					addToDocument(document, getToken(contents), new Location(line, start, current));
					skipWhitespace(contents);
				}
			}

			// Add last token
			addToDocument(document, getToken(contents), new Location(line, start, current));

			// add document to main list
			index.add(document);
		}
	}

	private void addToDocument(Document document, String token, Location location) {
		/*
			TODO: Perform text pre-processing
			- remove punctuation
			- convert to lowercase
			- stemming/lemmatization
			- stop-word removal (TODO: See if this is necessary if we discard the
			                      lower score entries from the index later)
		 */
		token = token.replaceAll("[^a-zA-Z0-9]+", "");
		token = token.toLowerCase();

		// To avoid empty strings, single letters, numbers, or stop words
		if (token.length() < 2 || token.matches("[0-9]+") || English.isStopWord(token)) return;

		TokenData data;
		if (document.containsKey(token)) {
			data = document.get(token);
			data.score++;
		} else {
			data = new TokenData();
			data.score = 1;
		}

		data.occurrences.add(location);
		document.put(token, data);
	}

	private void resetParser() {
		line = 1;
		start = 0;
		current = 0;
	}

	private String getToken(String input) {
		return input.substring(start, current);
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

	private int getDocFreq(String token) {
		int df = 0;

		for (Document document : index)
			if (document.containsKey(token)) df++;

		return df;
	}
}
