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
import java.util.List;
import java.util.Map;

public class TFIDFIndexBuilder implements IndexBuilder {
	private String input;
	private int line = 1;
	private int start = 0;
	private int current = 0;

	public TFIDFIndexBuilder(String input) {
		this.input = input;
	}

	@Override
	public List<Token> tokenize() {
		input = input.trim();

		List<Token> tokens = new ArrayList<>();
		for (; current < input.length(); current++) {
			if (start != current && Character.isWhitespace(input.codePointAt(current))) {
				tokens.add(getToken());
				skipWhitespace();
			}
		}

		// Add last token
		tokens.add(getToken());

		return tokens;
	}

	@Override
	public Map<String, Index> build() {
		return null;
	}

	private Token getToken() {
		return new Token(input.substring(start, current), new Token.Location(line, start, current));
	}

	private void checkNewLine() {
		if (input.codePointAt(current) == '\n') {
			line++;
		}
	}

	private void skipWhitespace() {
		while (Character.isWhitespace(input.codePointAt(current))) {
			checkNewLine();
			current++;
		}

		start = current;
	}
}
