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

package io.github.rohitawate.notehero.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaseFormatTest {

	@Test
	void identifyCaseFormat() {
		// UpperCamelCase and lowerCamelCase
		assertEquals(CaseFormat.LOWER_CAMEL, CaseFormat.identifyCaseFormat("makingPBJSandwichIn10Minutes"));
		assertEquals(CaseFormat.LOWER_CAMEL, CaseFormat.identifyCaseFormat("aStudyInPink"));
		assertEquals(CaseFormat.LOWER_CAMEL, CaseFormat.identifyCaseFormat("appMarket"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("AStudyInPink    "));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("IIFAMagic"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("IAmBatman"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("BuildingACanoe"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("AmIIFA"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("MP4"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("BigHero6"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("USA12CitiesTour"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("121WebHosting"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("Java8Tutorial"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("PS4Tutorial"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("Java11Tutorial"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("D23"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("Jan15"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("ID3T"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("TC39"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("SLF4J"));
		assertEquals(CaseFormat.UPPER_CAMEL, CaseFormat.identifyCaseFormat("1Direction"));

		// UPPER_SNAKE_CASE and lower_snake_case
		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("building_a_canoe"));
		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("something__weird"));
		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("_______classic_123_"));
		assertEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("KASA_KAY_PUNE   "));
		assertEquals(CaseFormat.MIXED_SNAKE, CaseFormat.identifyCaseFormat("__AVENGERS_2_AgeOfUltron_"));
		assertEquals(CaseFormat.MIXED_SNAKE, CaseFormat.identifyCaseFormat("--AVENGERS-2-Age-OF_ULTRON-"));

		// UPPER_HYPHENATED_CASE and lower_hyphen_case
		assertEquals(CaseFormat.LOWER_HYPHENATED, CaseFormat.identifyCaseFormat("building-a-canoe"));
		assertEquals(CaseFormat.LOWER_HYPHENATED, CaseFormat.identifyCaseFormat("something--weird"));
		assertEquals(CaseFormat.LOWER_HYPHENATED, CaseFormat.identifyCaseFormat("-------classic-123-"));
		assertEquals(CaseFormat.UPPER_HYPHENATED, CaseFormat.identifyCaseFormat("KASA-KAY-PUNE  "));

		// Title Case
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("Building     a Canoe"));
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("  Hello world"));
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("  leading and trailing spaces   "));

		// TODO: Non-alphabetic (just numbers, special characters or a mixture of both)
	}

	@Test
	void tokenize() {
		// UpperCamelCase and lowerCamelCase
		assertArrayEquals(new String[]{"making", "PBJ", "Sandwich", "In", "10", "Minutes"}, CaseFormat.tokenize("makingPBJSandwichIn10Minutes", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"a", "Study", "In", "Pink"}, CaseFormat.tokenize("aStudyInPink", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"app", "Market"}, CaseFormat.tokenize("appMarket", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"A", "Study", "In", "Pink"}, CaseFormat.tokenize("AStudyInPink    ", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"IIFA", "Magic"}, CaseFormat.tokenize("IIFAMagic", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"I", "Am", "Batman"}, CaseFormat.tokenize("IAmBatman", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Building", "A", "Canoe"}, CaseFormat.tokenize("BuildingACanoe", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Am", "IIFA"}, CaseFormat.tokenize("AmIIFA", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"MP4"}, CaseFormat.tokenize("MP4", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Big", "Hero", "6"}, CaseFormat.tokenize("BigHero6", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"USA", "12", "Cities", "Tour"}, CaseFormat.tokenize("USA12CitiesTour", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"121", "Web", "Hosting"}, CaseFormat.tokenize("121WebHosting", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Java", "8", "Tutorial"}, CaseFormat.tokenize("Java8Tutorial", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"PS", "4", "Tutorial"}, CaseFormat.tokenize("PS4Tutorial", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Java", "11", "Tutorial"}, CaseFormat.tokenize("Java11Tutorial", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"D23"}, CaseFormat.tokenize("D23", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"Jan", "15"}, CaseFormat.tokenize("Jan15", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"ID3T"}, CaseFormat.tokenize("ID3T", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"TC39"}, CaseFormat.tokenize("TC39", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"SLF4J"}, CaseFormat.tokenize("SLF4J", CaseFormat.UPPER_CAMEL));
		assertArrayEquals(new String[]{"1", "Direction"}, CaseFormat.tokenize("1Direction", CaseFormat.UPPER_CAMEL));

		// UPPER_SNAKE_CASE and lower_snake_case
		assertArrayEquals(new String[]{"building", "a", "canoe"}, CaseFormat.tokenize("building_a_canoe", CaseFormat.LOWER_SNAKE));
		assertArrayEquals(new String[]{"something", "weird"}, CaseFormat.tokenize("something__weird", CaseFormat.LOWER_SNAKE));
		assertArrayEquals(new String[]{"classic", "123"}, CaseFormat.tokenize("_______classic_123_", CaseFormat.LOWER_SNAKE));
		assertArrayEquals(new String[]{"KASA", "KAY", "PUNE"}, CaseFormat.tokenize("KASA_KAY_PUNE   ", CaseFormat.UPPER_SNAKE));
		assertArrayEquals(new String[]{"AVENGERS", "2", "AgeOfUltron"}, CaseFormat.tokenize("__AVENGERS_2_AgeOfUltron_", CaseFormat.MIXED_SNAKE));

		// UPPER_HYPHENATED_CASE and lower_hyphen_case
		assertArrayEquals(new String[]{"building", "a", "canoe"}, CaseFormat.tokenize("building-a-canoe", CaseFormat.LOWER_HYPHENATED));
		assertArrayEquals(new String[]{"something", "weird"}, CaseFormat.tokenize("something--weird", CaseFormat.LOWER_HYPHENATED));
		assertArrayEquals(new String[]{"classic", "123"}, CaseFormat.tokenize("-------classic-123-", CaseFormat.LOWER_HYPHENATED));
		assertArrayEquals(new String[]{"KASA", "KAY", "PUNE"}, CaseFormat.tokenize("KASA-KAY-PUNE  ", CaseFormat.UPPER_HYPHENATED));

		// Title Case
		assertArrayEquals(new String[]{"Building", "a", "Canoe"}, CaseFormat.tokenize("Building     a Canoe", CaseFormat.TITLE));
		assertArrayEquals(new String[]{"Hello", "world"}, CaseFormat.tokenize("  Hello world", CaseFormat.TITLE));
		assertArrayEquals(new String[]{"leading", "and", "trailing", "spaces"}, CaseFormat.tokenize("  leading and trailing spaces   ", CaseFormat.TITLE));

		// Mixed cases to check precedence
		assertArrayEquals(new String[]{"AVENGERS", "2", "Age", "OF_ULTRON"}, CaseFormat.tokenize("--AVENGERS-2-Age-OF_ULTRON-", CaseFormat.MIXED_HYPHENATED));
	}

	@Test
	void convertTo() {
		assertEquals("Hello World", CaseFormat.convertTo("hello_world", CaseFormat.TITLE));
		assertEquals("HelloWorld", CaseFormat.convertTo("hello_world", CaseFormat.UPPER_CAMEL));
		assertEquals("helloWorld", CaseFormat.convertTo("hello_world", CaseFormat.LOWER_CAMEL));
		assertEquals("HELLO_WORLD", CaseFormat.convertTo("hello_world", CaseFormat.UPPER_SNAKE));
		assertEquals("hello_world", CaseFormat.convertTo("hello_world", CaseFormat.LOWER_SNAKE));
		assertEquals("HeLLo_WOrld", CaseFormat.convertTo("HeLLo-WOrld", CaseFormat.MIXED_SNAKE));
		assertEquals("HELLO-WORLD", CaseFormat.convertTo("hello_world", CaseFormat.UPPER_HYPHENATED));
		assertEquals("hello-world", CaseFormat.convertTo("hello_world", CaseFormat.LOWER_HYPHENATED));
		assertEquals("HeLLo-WOrld", CaseFormat.convertTo("HeLLo WOrld", CaseFormat.MIXED_HYPHENATED));

		assertEquals("Building a Canoe", CaseFormat.convertTo("BuildingACanoe", CaseFormat.TITLE));
		assertEquals("BuildingACanoe", CaseFormat.convertTo("BuildingACanoe", CaseFormat.UPPER_CAMEL));
		assertEquals("buildingACanoe", CaseFormat.convertTo("BuildingACanoe", CaseFormat.LOWER_CAMEL));
		assertEquals("BUILDING_A_CANOE", CaseFormat.convertTo("BuildingACanoe", CaseFormat.UPPER_SNAKE));
		assertEquals("building_a_canoe", CaseFormat.convertTo("BuildingACanoe", CaseFormat.LOWER_SNAKE));
		assertEquals("builDing_A_cANOe", CaseFormat.convertTo("builDing A cANOe", CaseFormat.MIXED_SNAKE));
		assertEquals("building-a-canoe", CaseFormat.convertTo("BuildingACanoe", CaseFormat.LOWER_HYPHENATED));
		assertEquals("BUILDING-A-CANOE", CaseFormat.convertTo("BuildingACanoe", CaseFormat.UPPER_HYPHENATED));
		assertEquals("builDing-A-cANOe", CaseFormat.convertTo("builDing A cANOe", CaseFormat.MIXED_HYPHENATED));

		assertEquals("Web Development with Django", CaseFormat.convertTo("web_development_with_django", CaseFormat.TITLE));
		assertEquals("WebDevelopmentWithDjango", CaseFormat.convertTo("web_development_with_django", CaseFormat.UPPER_CAMEL));
		assertEquals("webDevelopmentWithDjango", CaseFormat.convertTo("web_development_with_django", CaseFormat.LOWER_CAMEL));
		assertEquals("WEB_DEVELOPMENT_WITH_DJANGO", CaseFormat.convertTo("WebDevelopmentWithDjango", CaseFormat.UPPER_SNAKE));
		assertEquals("web_development_with_django", CaseFormat.convertTo("webDevelopmentWithDjango", CaseFormat.LOWER_SNAKE));
		assertEquals("wEb_DeveLOPment_wiTH_DjangO", CaseFormat.convertTo("wEb_DeveLOPment_wiTH_DjangO", CaseFormat.MIXED_SNAKE));
		assertEquals("WEB-DEVELOPMENT-WITH-DJANGO", CaseFormat.convertTo("web_development_with_django", CaseFormat.UPPER_HYPHENATED));
		assertEquals("web-development-with-django", CaseFormat.convertTo("web deveLopment With django", CaseFormat.LOWER_HYPHENATED));
		assertEquals("wEb-DeveLOPment-wiTH-DjangO", CaseFormat.convertTo("wEb_DeveLOPment_wiTH_DjangO", CaseFormat.MIXED_HYPHENATED));

		assertEquals("I'm Going to D23", CaseFormat.convertTo("I'm going to D23", CaseFormat.TITLE));
		assertEquals("IAmGoingToD23", CaseFormat.convertTo("i_am_going_to_d23", CaseFormat.UPPER_CAMEL));
		assertEquals("iAmGoingToD23", CaseFormat.convertTo("IAmGoingToD23", CaseFormat.LOWER_CAMEL));
		assertEquals("I_AM_GOING_TO_D23", CaseFormat.convertTo("i-am-going-to-d23", CaseFormat.UPPER_SNAKE));
		assertEquals("i_am_going_to_d23", CaseFormat.convertTo("iAmGoingToD23", CaseFormat.LOWER_SNAKE));
		assertEquals("i_aM_goIng_tO_d23", CaseFormat.convertTo("i aM goIng tO d23", CaseFormat.MIXED_SNAKE));
		assertEquals("I-AM-GOING-TO-D23", CaseFormat.convertTo("i_aM_goIng_tO_d23", CaseFormat.UPPER_HYPHENATED));
		assertEquals("i-am-going-to-d23", CaseFormat.convertTo("i am going to d23", CaseFormat.LOWER_HYPHENATED));
		assertEquals("i-am-going-to-d23", CaseFormat.convertTo("i-am-going-to-d23", CaseFormat.MIXED_HYPHENATED));
	}

	@Test
	void equalAcrossFormats() {
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "linear regression"));
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "linear-regression"));
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "linear_regression"));
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "LinearRegression"));
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "linearRegression"));
		assertTrue(CaseFormat.equalAcrossFormats("Linear Regression", "linear regression"));
	}
}