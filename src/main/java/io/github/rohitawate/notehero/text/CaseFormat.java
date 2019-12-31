/*
 * Copyright 2019 Rohit Awate.
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

package io.github.rohitawate.notehero.text;

import java.util.logging.Logger;

/**
 * Denotes the case format of a string.
 */
public enum CaseFormat {
	UPPER_CAMEL,        // FooBar
	LOWER_CAMEL,        // fooBar
	UPPER_SNAKE,        // FOO_BAR
	LOWER_SNAKE,        // foo_bar
	MIXED_SNAKE,        // fOo_BaR
	UPPER_HYPHENATED,   // FOO-BAR,
	LOWER_HYPHENATED,   // foo-bar
	MIXED_HYPHENATED,   // fOo-BaR
	TITLE;              // foo bar

	public static String convertTo(CaseFormat targetCaseFormat, String source) {
		// identify source's case
		CaseFormat sourceCaseFormat = identifyCaseFormat(source);

		// tokenize
		String[] tokens = tokenize(source, sourceCaseFormat);

		// build

		return "";
	}

	/**
	 * Identifies the case format of the string.
	 *
	 * @param source Candidate string
	 * @return The case format of the string
	 */
	public static CaseFormat identifyCaseFormat(String source) {
		source = source.trim();

		if (source.matches("([^\\s]+\\s+)+[^\\s]+")) {
			/*
				 This can mean that other cases exist in the string as well.
				 For example, "Making a cam_shaft" contains both spaces as
				 well as lower snake case.

				 However, we let spaces take precedence over others.
			*/

			return CaseFormat.TITLE;
		} else if (source.matches("([^\\s]+_+)+[^\\s]+")) {
			final Case sourceCase = Case.identifyCase(source);
			switch (sourceCase) {
				case LOWER:
					return CaseFormat.LOWER_SNAKE;
				case MIXED:
					return CaseFormat.MIXED_SNAKE;
				case UPPER:
					return CaseFormat.UPPER_SNAKE;
			}
		} else if (source.matches("([^\\s]+-+)+[^\\s]+")) {
			final Case sourceCase = Case.identifyCase(source);
			switch (sourceCase) {
				case LOWER:
					return CaseFormat.LOWER_HYPHENATED;
				case MIXED:
					return CaseFormat.MIXED_HYPHENATED;
				case UPPER:
					return CaseFormat.UPPER_HYPHENATED;
			}
		} else {
			boolean isFirstUpper = Character.isUpperCase(source.codePointAt(0));
			return isFirstUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL;
		}

		// assumption
		Logger.getGlobal().warning("Unknown CaseFormat: " + source);
		return LOWER_CAMEL;
	}

	/**
	 * Tokenizes the source string into an array of tokens.
	 * Utilizes the knowledge of the source's case format
	 * while performing the tokenization operation.
	 *
	 * @param source           The source string
	 * @param sourceCaseFormat The case format of the source string
	 * @return String array of tokens
	 */
	public static String[] tokenize(String source, CaseFormat sourceCaseFormat) {
		// All but CamelCase since they have clear delimiters
		// which can be used to tokenize the string.
		if (sourceCaseFormat != LOWER_CAMEL && sourceCaseFormat != UPPER_CAMEL) {
			String delimRegex;

			switch (sourceCaseFormat) {
				case UPPER_SNAKE:
				case LOWER_SNAKE:
				case MIXED_SNAKE:
					delimRegex = "_+";
					break;
				case UPPER_HYPHENATED:
				case LOWER_HYPHENATED:
				case MIXED_HYPHENATED:
					delimRegex = "-+";
					break;
				case TITLE:
					delimRegex = "\\s+";
					break;
				default:
					delimRegex = "";
			}

			return source.split(delimRegex);
		}

		// CamelCase
		return tokenizeCamelCase(source);
	}


	/**
	 * Delegate for tokenize.
	 * <p>
	 * Tokenizes a string in upper or lower camel case
	 * into an array of tokens.
	 *
	 * @param source The source string
	 * @return String array of tokens
	 */
	private static String[] tokenizeCamelCase(String source) {
		/*
			 This regex powered implementation is probably not the fastest
			 or most efficient. What it is, is readable and concise.
			 This is inspired by the StackOverflow answer here:
			 https://stackoverflow.com/questions/7225407/convert-camelcasetext-to-sentence-case-text

			 For the previous parser-style implementation, check this commit:
			 https://github.com/RohitAwate/NoteHero/commit/aec96d38a9cbfbfdb4cc018393e52fb0ae63d900
		*/

		source = source
				// aStudyInPink -> a Study InPink
				.replaceAll("([a-z])([A-Z][a-z])", "$1 $2")

				// makingPBJQuickly -> making PBJ Quickly
				.replaceAll("([a-z])([A-Z]+)([A-Z][a-z])", "$1 $2 $3")

				// InBengal -> In Bengal
				.replaceAll("([A-Z][a-z])([A-Z])", "$1 $2")

				// BuildingACanoe -> Building A Canoe
				.replaceAll("([a-z])([AI])([A-Z][a-z])", "$1 $2 $3")

				// AStudy -> A Study
				.replaceAll("([AI])([A-Z][a-z])", "$1 $2")

				// BigHero6 -> Big Hero 6
				.replaceAll("([a-z])([0-9])", "$1 $2")

				// USA12CitiesTour -> USA 12 Cities Tour
				.replaceAll("([A-Z]+)([0-9]+)([A-Z][a-z])", "$1 $2 $3")

				// 121WebHosting -> 121 Web Hosting
				.replaceAll("([0-9]+)([A-Z][a-z])", "$1 $2")

				.trim();

		return source.split("\\s+");
	}

	/**
	 * Denotes the case of the string.
	 */
	public enum Case {
		LOWER,   // foobar
		MIXED,   // fooBar
		UPPER;   // FOOBAR

		/**
		 * Checks if the same case (upper/lower) is maintained across the string.
		 * <p>
		 * Starts by checking the case of the first alphabetic character. The same should
		 * be maintained for all alphabetic characters across the string. Else, it is mixed.
		 *
		 * @param source The source string
		 * @return 0 - lower, 1 - mixed, 2 - upper
		 */
		public static Case identifyCase(String source) {
			int codePoint, i = 0;

			do {
				codePoint = source.codePointAt(i++);
			} while (!Character.isAlphabetic(codePoint));

			final boolean isFirstUpper = Character.isUpperCase(codePoint);

			for (; i < source.length(); i++) {
				codePoint = source.codePointAt(i);
				if (Character.isAlphabetic(codePoint) && (Character.isUpperCase(codePoint) != isFirstUpper)) {
					return Case.MIXED;
				}
			}

			return isFirstUpper ? Case.UPPER : Case.LOWER;
		}
	}
}
