/*
 * Copyright 2020 Rohit Awate.
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

package io.github.rohitawate.notehero.ingestion.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.github.rohitawate.notehero.ingestion.IngestionController;
import io.github.rohitawate.notehero.ingestion.IngestionThread;
import io.github.rohitawate.notehero.models.NoteConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YAMLFrontMatterProcessorTest {
	private IngestionController controller = new IngestionController();

	@Test
	void positive() throws IOException, URISyntaxException {
		YFMTestCase positive = loadTest("Positive.xml");
		YAMLFrontMatterProcessor positiveProc = new YAMLFrontMatterProcessor(positive.full, new IngestionThread(controller, positive.filePath));

		assertEquals(positiveProc.getConfigString(), positive.expectedYFM);
		assertEquals(positiveProc.getStrippedNote(), positive.expectedNote);
		assertEquals(positiveProc.getParsedConfig(), positive.expectedConfig);
	}

	@Test
	void leadingWhitespace() throws IOException, URISyntaxException {
		YFMTestCase leadingWhitespace = loadTest("LeadingWhitespace.xml");
		YAMLFrontMatterProcessor leadingWhitespaceProc = new YAMLFrontMatterProcessor(leadingWhitespace.full, new IngestionThread(controller, leadingWhitespace.filePath));

		assertEquals(leadingWhitespaceProc.getConfigString(), leadingWhitespace.expectedYFM);
		assertEquals(leadingWhitespaceProc.getStrippedNote(), leadingWhitespace.expectedNote);
		assertEquals(leadingWhitespaceProc.getParsedConfig(), leadingWhitespace.expectedConfig);
	}

	@Test
	void emptyFrontMatter() throws IOException, URISyntaxException {
		YFMTestCase emptyFrontMatter = loadTest("EmptyFrontMatter.xml");
		YAMLFrontMatterProcessor emptyFrontMatterProc = new YAMLFrontMatterProcessor(emptyFrontMatter.full, new IngestionThread(controller, emptyFrontMatter.filePath));

		assertEquals(emptyFrontMatterProc.getConfigString(), emptyFrontMatter.expectedYFM);
		assertEquals(emptyFrontMatterProc.getStrippedNote(), emptyFrontMatter.expectedNote);
		assertEquals(emptyFrontMatterProc.getParsedConfig(), emptyFrontMatter.expectedConfig);
	}

	@Test
	void noFrontMatter() throws IOException, URISyntaxException {
		YFMTestCase noFrontMatter = loadTest("NoFrontMatter.xml");
		YAMLFrontMatterProcessor noFrontMatterProc = new YAMLFrontMatterProcessor(noFrontMatter.full, new IngestionThread(controller, noFrontMatter.filePath));

		assertEquals(noFrontMatterProc.getConfigString(), noFrontMatter.expectedYFM);
		assertEquals(noFrontMatterProc.getStrippedNote(), noFrontMatter.expectedNote);
		assertEquals(noFrontMatterProc.getParsedConfig(), noFrontMatter.expectedConfig);
	}

	@Test
	void noOpenDelim() throws IOException, URISyntaxException {
		YFMTestCase noOpenDelim = loadTest("NoOpenDelim.xml");
		YAMLFrontMatterProcessor noOpenDelimProc = new YAMLFrontMatterProcessor(noOpenDelim.full, new IngestionThread(controller, noOpenDelim.filePath));

		assertEquals(noOpenDelimProc.getConfigString(), noOpenDelim.expectedYFM);
		assertEquals(noOpenDelimProc.getStrippedNote(), noOpenDelim.expectedNote);
		assertEquals(noOpenDelimProc.getParsedConfig(), noOpenDelim.expectedConfig);
	}

	@Test
	void noCloseDelim() throws IOException, URISyntaxException {
		YFMTestCase noCloseDelim = loadTest("NoCloseDelim.xml");
		YAMLFrontMatterProcessor noCloseDelimProc = new YAMLFrontMatterProcessor(noCloseDelim.full, new IngestionThread(controller, noCloseDelim.filePath));

		assertEquals(noCloseDelimProc.getConfigString(), noCloseDelim.expectedYFM);
		assertEquals(noCloseDelimProc.getStrippedNote(), noCloseDelim.expectedNote);
		assertEquals(noCloseDelimProc.getParsedConfig(), noCloseDelim.expectedConfig);
	}

	@Test
	void emptySource() throws IOException, URISyntaxException {
		YFMTestCase emptySource = loadTest("EmptySource.xml");
		YAMLFrontMatterProcessor emptySourceProc = new YAMLFrontMatterProcessor(emptySource.full, new IngestionThread(controller, emptySource.filePath));

		assertEquals(emptySourceProc.getConfigString(), emptySource.expectedYFM);
		assertEquals(emptySourceProc.getStrippedNote(), emptySource.expectedNote);
		assertEquals(emptySourceProc.getParsedConfig(), emptySource.expectedConfig);
	}

	@Test
	void singleDelim() throws IOException, URISyntaxException {
		YFMTestCase singleDelim = loadTest("SingleDelim.xml");
		YAMLFrontMatterProcessor singleDelimProc = new YAMLFrontMatterProcessor(singleDelim.full, new IngestionThread(controller, singleDelim.filePath));

		assertEquals(singleDelimProc.getConfigString(), singleDelim.expectedYFM);
		assertEquals(singleDelimProc.getStrippedNote(), singleDelim.expectedNote);
		assertEquals(singleDelimProc.getParsedConfig(), singleDelim.expectedConfig);
	}

	@Test
	void bothDelim() throws IOException, URISyntaxException {
		YFMTestCase bothDelim = loadTest("BothDelim.xml");
		YAMLFrontMatterProcessor bothDelimProc = new YAMLFrontMatterProcessor(bothDelim.full, new IngestionThread(controller, bothDelim.filePath));

		assertEquals(bothDelimProc.getConfigString(), bothDelim.expectedYFM);
		assertEquals(bothDelimProc.getStrippedNote(), bothDelim.expectedNote);
		assertEquals(bothDelimProc.getParsedConfig(), bothDelim.expectedConfig);
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

}