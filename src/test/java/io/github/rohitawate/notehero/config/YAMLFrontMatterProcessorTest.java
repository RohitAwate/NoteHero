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

package io.github.rohitawate.notehero.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.github.rohitawate.notehero.database.BuildAccessor;
import io.github.rohitawate.notehero.database.GitRepoAccessor;
import io.github.rohitawate.notehero.database.UserAccessor;
import io.github.rohitawate.notehero.ingestion.IngestionController;
import io.github.rohitawate.notehero.models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YAMLFrontMatterProcessorTest {
	private static IngestionController controller;

	private static final UserAccessor USER_ACCESSOR = new UserAccessor();
	private static final GitRepoAccessor REPO_ACCESSOR = new GitRepoAccessor();

	private static final User TEST_USER = new User("yfmtest", "yfmtest@test.com", "pass123", Tier.FREE);
	private static final GitRepo TEST_REPO = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.GITLAB,
			"Chandler", "Everest", "main", UUID.randomUUID());
	private static final Build TEST_BUILD = new Build(TEST_REPO.getRepoID(), "main",
			"cab3b4d254bfafa74f0641b05761882921e5b435", "JonSnow", OffsetDateTime.now(), Build.BuildStatus.SUCCESS);

	@BeforeAll
	static void setupController() {
		USER_ACCESSOR.create(TEST_USER);
		REPO_ACCESSOR.create(TEST_REPO);

		List<String> candidateFilePaths = new ArrayList<>();

		candidateFilePaths.add("Positive.xml");
		candidateFilePaths.add("LeadingWhitespace.xml");
		candidateFilePaths.add("EmptyFrontMatter.xml");
		candidateFilePaths.add("NoFrontMatter.xml");
		candidateFilePaths.add("NoOpenDelim.xml");
		candidateFilePaths.add("NoCloseDelim.xml");
		candidateFilePaths.add("NoOpenDelim.xml");
		candidateFilePaths.add("EmptySource.xml");
		candidateFilePaths.add("SingleDelim.xml");
		candidateFilePaths.add("BothDelim.xml");

		controller = new IngestionController(TEST_BUILD, candidateFilePaths);
	}

	@Test
	void positive() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("Positive.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void leadingWhitespace() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("LeadingWhitespace.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void emptyFrontMatter() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("EmptyFrontMatter.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void noFrontMatter() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("NoFrontMatter.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void noOpenDelim() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("NoOpenDelim.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void noCloseDelim() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("NoCloseDelim.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void emptySource() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("EmptySource.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void singleDelim() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("SingleDelim.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	@Test
	void bothDelim() throws IOException, URISyntaxException {
		YFMTestCase testCase = loadTest("BothDelim.xml");
		YAMLFrontMatterProcessor processor = new YAMLFrontMatterProcessor(testCase.full, testCase.filePath, controller);

		performChecks(testCase, processor);
	}

	private void performChecks(YFMTestCase positive, YAMLFrontMatterProcessor processor) {
		assertEquals(positive.expectedYFM, processor.getConfigString());
		assertEquals(positive.expectedNote, processor.getStrippedNote());
		assertEquals(positive.expectedConfig, processor.getParsedConfig());
	}

	YFMTestCase loadTest(String testCasePath) throws URISyntaxException, IOException {
		URL url = getClass().getResource("YAMLFrontMatterProcessor/" + testCasePath);
		Path path = Paths.get(url.toURI());
		String fileContents = new String(Files.readAllBytes(path));

		XmlMapper mapper = new XmlMapper();
		YFMTestCase testCase = mapper.readValue(fileContents, YFMTestCase.class);

		testCase.full = testCase.full.trim();
		testCase.expectedYFM = testCase.expectedYFM.trim();
		testCase.expectedNote = testCase.expectedNote.trim();

		return testCase;
	}

	static class YFMTestCase {
		@JacksonXmlProperty
		private String full;

		@JacksonXmlProperty
		private String expectedYFM;

		@JacksonXmlProperty
		private String expectedNote;

		@JacksonXmlProperty
		private String filePath;

		@JacksonXmlProperty
		private NoteConfig expectedConfig;
	}

	@AfterAll
	static void cleanup() {
		new BuildAccessor().delete(TEST_BUILD.getBuildID());
		assertTrue(REPO_ACCESSOR.delete(TEST_REPO.getRepoID()));
		assertTrue(USER_ACCESSOR.delete(TEST_USER.getUsername()));
	}
}