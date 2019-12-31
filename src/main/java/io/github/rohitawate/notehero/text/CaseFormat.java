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

import java.util.ArrayList;
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
		ArrayList<String> buffer = new ArrayList<>();
		int currentCodePoint;
		int start = 0;

		int sourceLen = source.length();
		for (int i = 0; i < sourceLen; i++) {
			currentCodePoint = source.codePointAt(i);

			if (Character.isAlphabetic(currentCodePoint) && Character.isUpperCase(currentCodePoint)) {
				if (canAddPrevious(i, start, source)) {
					buffer.add(source.substring(start, i));
					start = i;
				}

				if (isNextUpper(i, source) && isCurrentSingle(i, start, source)) {
					buffer.add(Character.toString(source.charAt(i)));
					start = i + 1;
				}
			} else if (Character.isDigit(currentCodePoint)) {
				/*
				 * Dealing with numbers in CamelCase requires observation of the
				 * previous and next code points in the string, if available. This is
				 * so because whether or not we separate the number into its own string
				 * depends on those neighbouring code points.
				 */
				int prevCodePoint, nextCodePoint;
				boolean prevIsDigit, nextIsDigit;

				// Checking upper limit of range
				if (i <= sourceLen - 2) {
					nextCodePoint = source.codePointAt(i + 1);
					nextIsDigit = Character.isDigit(nextCodePoint);
				} else continue;

				// Checking lower limit of range
				if (i > 0) {
					prevCodePoint = source.codePointAt(i - 1);
					prevIsDigit = Character.isDigit(prevCodePoint);
				} else {
					/*
						 For a digit at i = 0, we need to check if the next code point is a digit as well. That would
						 imply that there are more digits to the number. In this case, we continue exploring the number.
						 For example: 121WebHosting

						 If that is not the case, and the next code point is something other than a digit,
						 we add the current code point to the buffer. For example: 1Direction
					*/
					if (!nextIsDigit) {
						buffer.add(Character.toString(source.charAt(i)));
						start = i + 1;
					}

					continue;
				}

				// Some useful booleans
				boolean prevIsAlpha = Character.isAlphabetic(prevCodePoint);
				boolean nextIsAlpha = Character.isAlphabetic(nextCodePoint);

				boolean prevIsUpper = Character.isUpperCase(prevCodePoint);
				boolean nextIsUpper = Character.isUpperCase(nextCodePoint);

				/*
					If next code point is a digit and the previous is not, we might want to add the previous
					[start, i) code points to the buffer.
				 */
				if (nextIsDigit) {
					/*
						We also need to check if the number is not part of some acronym.
						For example, Jan15 or D23.

						For this we check if the previous code point is alphabetic and not lowercase.
						We only add the previous [start, i) if this condition fails.
						For example: The first '1' from Java11Tutorial
					 */
					if (!prevIsDigit && prevIsAlpha && !prevIsUpper) {
						buffer.add(source.substring(start, i));
						start = i;
					}
				} else {
					if (prevIsDigit) {
						/*
							If next is not a digit, we can add the substring [start, i] to the
							buffer.

							For example: The second '1' from Java11Tutorial
					 	*/
						buffer.add(source.substring(start, i + 1));
						start = i + 1;
					} else {
						/*
						 	Here, the numeric code point is sandwiched between two non-numeric
						 	code points. For example: ID3T, Java8Tutorial or PS4Tutorial.

						 	The difference in the two examples is that for the first one, the
						 	numeric code point is part of an acronym while in the second one
						 	it is an individual number. We can resolve this by checking the
						 	if the previous and next code points are alphabetic and subsequently,
						 	their cases.

						 	If previous is lowercase and next is uppercase, we add the single number
						 	to the buffer. (Java8Tutorial)

						 	Before that we check if any substring [start, i) is pending to be added
						 	and do so.
						 */
						if (prevIsAlpha && !prevIsUpper && nextIsAlpha && nextIsUpper) {
							if (start != i) {
								buffer.add(source.substring(start, i));
							}

							buffer.add(Character.toString(source.charAt(i)));
							start = i + 1;
						}

						/*
							If both are uppercase, we need to check if the (i + 2) character exists.

							If it does and is lowercase, then we are in the PS4Tutorial case. Here, the substring
							[start, i] must be added to the buffer.

							We concur that the number is part of an acronym if the (i + 2) character does not exist
							or if it exists and is uppercase.
						 */

						else if (i + 2 <= sourceLen - 2 && Character.isLowerCase(source.codePointAt(i + 2))) {
							buffer.add(source.substring(start, i + 1));
							start = i + 1;
						}
					}
				}
			}
		}

		// Add the remaining part of the string
		buffer.add(source.substring(start));

		return buffer.toArray(new String[0]);
	}

	/**
	 * Delegate for tokenizeCamelCase.
	 * <p>
	 * Determines whether there exists a next code point and if
	 * is upper case.
	 *
	 * @param current The current position in the string
	 * @param source  The source string
	 * @return true if exists and is upper, else false
	 */
	private static boolean isNextUpper(int current, String source) {
		// Check if next code point actually exists
		if (current + 1 >= source.length()) return false;

		// If exists, check if it is upper
		return Character.isUpperCase(source.codePointAt(current + 1));
	}

	/**
	 * Delegate for tokenizeCamelCase.
	 * <p>
	 * Determines whether the current alphabetic code point at the current
	 * index is a single letter word i.e. "I" or "A".
	 *
	 * @param current The current position in the string
	 * @param start   The starting index for the next string to be added
	 * @param source  The source string
	 * @return true if single word, else false
	 */
	private static boolean isCurrentSingle(int current, int start, String source) {
		int currentCodePoint = source.codePointAt(current);

		// "I" and "A" are the only single letter words in the English language.
		if (currentCodePoint != 'I' && currentCodePoint != 'A') return false;

		/*
			We have already established that the next is upper.
		 	This means that "I" or "A" could be one of the following:
		 	 - A single word, as mentioned in the comment above
		 	 	For example: BuildingACanoe, IAmBatman, etc.
		 	 - Part of an acronym.
		 	   	For example: IIFAMagic, IBM, AAPL, etc.

		 	We can determine which of the above cases we're looking at by checking if
		 	the second next i.e. (current + 2) character is alphabetic and lowercase.
			For example:
				- BuildingACanoe: When we're at 'A', 'C' i.e. next is uppercase.
				(current + 2) is 'a' which is lowercase and thus this falls in the first case.
				- IBM: When we're at 'I', 'B' i.e. next is uppercase. (current + 2) is 'M'
				which is uppercase and thus falls in the second case.
				- ID3T: When we're at 'I', 'D' i.e. next is uppercase. (current + 2) is '3'
				which is not alphabetic and thus is not a single word.

			In the case of a single word we do nothing. Hence, we only try to detect the second one.
			However, we also need to check if the "I" or "A" is not part of a continuing acronym.
			This can be done by checking if start == current.

			We also need to ensure that we're not violating the string's range. It should have the
			(current + 2) character. If it doesn't, it must be an acronym since "I" and "A" cannot
			appear next to each other in the English language.
		*/

		if (start != current) return false;

		int nextNext = current + 2;
		if (nextNext > source.length() - 2) return false;

		int nextNextCodePoint = source.codePointAt(nextNext);
		return Character.isAlphabetic(nextNextCodePoint) && Character.isLowerCase(nextNextCodePoint);
	}

	/**
	 * Delegate for tokenizeCamelCase.
	 * <p>
	 * Decides whether the previous substring i.e. [start, current)
	 * can be added to the buffer.
	 *
	 * @param current The current position in the string
	 * @param start   The starting index for the next string to be added
	 * @param source  The source string
	 * @return true if can be added, else false
	 */
	private static boolean canAddPrevious(int current, int start, String source) {
		/*
			 We can add the the previous substring i.e. [start, current) to buffer
			 when the following conditions are met:
			 	- 0 < current < sourceLen - 2
			 	 	This means that we're not at the first or last code points of the string.
			 	 	This ensures that we're not violating the range for the second condition.
			 	- either previous or next code points are alphabetic and lower case
			 		This means that we're not part of an acronym.
			 	- start != current
			 		We don't add a single character here since that is isCurrentSingle's responsibility
		*/

		if (0 >= current || current >= source.length() - 2) return false;

		int prev = source.codePointAt(current - 1);
		int next = source.codePointAt(current + 1);

		if (!Character.isAlphabetic(prev) || !Character.isAlphabetic(next))
			return false;

		if (Character.isUpperCase(prev) && Character.isUpperCase(next))
			return false;

		return start != current;
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
