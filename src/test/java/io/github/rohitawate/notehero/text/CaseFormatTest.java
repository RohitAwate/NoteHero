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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaseFormatTest {

	@Test
	void tokenize() {
		assertArrayEquals(new String[]{"making", "PBJ", "Sandwich", "In", "10", "Minutes"}, CaseFormat.tokenize("makingPBJSandwichIn10Minutes", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"a", "Study", "In", "Pink"}, CaseFormat.tokenize("aStudyInPink", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"app", "Market"}, CaseFormat.tokenize("appMarket", CaseFormat.LOWER_CAMEL));
		assertArrayEquals(new String[]{"A", "Study", "In", "Pink"}, CaseFormat.tokenize("AStudyInPink", CaseFormat.UPPER_CAMEL));
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
	}

	@Test
	void convertTo() {
	}

	@Test
	void identifyCaseFormat() {
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("hello world_0"));
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("काहीही हा श्री"));
		assertEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("hello world!"));
		assertNotEquals(CaseFormat.TITLE, CaseFormat.identifyCaseFormat("helloWorld"));

		assertNotEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("__"));
		assertEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("HELLO_WORLD!"));
		assertEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("123_123H"));
		assertEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("{H}_H"));
		assertNotEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("{H}__h"));

		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("hello_world!"));
		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("123_123h"));
		assertEquals(CaseFormat.LOWER_SNAKE, CaseFormat.identifyCaseFormat("{h}_h"));
		assertNotEquals(CaseFormat.UPPER_SNAKE, CaseFormat.identifyCaseFormat("{h}__H"));
	}

	@Test
	void identifyCase() {
	}
}