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

package io.github.rohitawate.notehero.renderer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YAMLFrontMatterProcessorTest {
	private RenderThread thread = new RenderThread("/home/notehero/notes/CS/Deep Learning/CNN.md", new RenderController());

	private YAMLFrontMatterProcessor positiveProc, leadingWhitespace, emptyFrontMatterProc, noOpenDelimProc,
			noCloseDelimProc, noFrontMatterProc, emptySourceProc, singleDelimProc, bothDelimProc;
	private YFMTestCase positive, positiveLeadingSpace, emptyFrontMatter, noOpenDelim, noCloseDelim, noFrontMatter;

	@BeforeEach
	void setUp() throws IOException, URISyntaxException {
		positive = loadTest("Positive.xml");
		positiveProc = new YAMLFrontMatterProcessor(positive.full, thread);

		positiveLeadingSpace = loadTest("LeadingWhitespace.xml");
		leadingWhitespace = new YAMLFrontMatterProcessor(positiveLeadingSpace.full, thread);

		emptyFrontMatter = loadTest("EmptyFrontMatter.xml");
		emptyFrontMatterProc = new YAMLFrontMatterProcessor(emptyFrontMatter.full, thread);

		noFrontMatter = loadTest("NoFrontMatter.xml");
		noFrontMatterProc = new YAMLFrontMatterProcessor(noFrontMatter.full, thread);

		noOpenDelim = loadTest("NoOpenDelim.xml");
		noOpenDelimProc = new YAMLFrontMatterProcessor(noOpenDelim.full, thread);

		noCloseDelim = loadTest("NoCloseDelim.xml");
		noCloseDelimProc = new YAMLFrontMatterProcessor(noCloseDelim.full, thread);

		emptySourceProc = new YAMLFrontMatterProcessor("", thread);
		singleDelimProc = new YAMLFrontMatterProcessor("---", thread);
		bothDelimProc = new YAMLFrontMatterProcessor("---\n---", thread);
	}

	@Test
	void positive() {
		assertEquals(positiveProc.getConfigString(), positive.expectedYFM);
		assertEquals(positiveProc.getStrippedNote(), positive.expectedNote);
	}

	@Test
	void leadingWhitespace() {
		assertEquals(leadingWhitespace.getConfigString(), positiveLeadingSpace.expectedYFM);
		assertEquals(leadingWhitespace.getStrippedNote(), positiveLeadingSpace.expectedNote);
	}

	@Test
	void emptyFrontMatter() {
		assertEquals(emptyFrontMatterProc.getConfigString(), emptyFrontMatter.expectedYFM);
		assertEquals(emptyFrontMatterProc.getStrippedNote(), emptyFrontMatter.expectedNote);
	}

	@Test
	void noFrontMatter() {
		assertEquals(noFrontMatterProc.getConfigString(), noFrontMatter.expectedYFM);
		assertEquals(noFrontMatterProc.getStrippedNote(), noFrontMatter.expectedNote);
	}

	@Test
	void noOpenDelim() {
		assertEquals(noOpenDelimProc.getConfigString(), noOpenDelim.expectedYFM);
		assertEquals(noOpenDelimProc.getStrippedNote(), noOpenDelim.expectedNote);
	}

	@Test
	void noCloseDelim() {
		assertEquals(noCloseDelimProc.getConfigString(), noCloseDelim.expectedYFM);
		assertEquals(noCloseDelimProc.getStrippedNote(), noCloseDelim.expectedNote);
	}

	@Test
	void emptySource() {
		assertEquals(emptySourceProc.getConfigString(), "");
		assertEquals(emptySourceProc.getStrippedNote(), "");
	}

	@Test
	void singleDelim() {
		assertEquals(singleDelimProc.getConfigString(), "");
		assertEquals(singleDelimProc.getStrippedNote(), "---");
	}

	@Test
	void bothDelim() {
		assertEquals(bothDelimProc.getConfigString(), "");
		assertEquals(bothDelimProc.getStrippedNote(), "");
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
		private String full;
		private String expectedYFM;
		private String expectedNote;

		public String getFull() {
			return full;
		}

		public void setFull(String full) {
			this.full = full;
		}

		public String getExpectedYFM() {
			return expectedYFM;
		}

		public void setExpectedYFM(String expectedYFM) {
			this.expectedYFM = expectedYFM;
		}

		public String getExpectedNote() {
			return expectedNote;
		}

		public void setExpectedNote(String expectedNote) {
			this.expectedNote = expectedNote;
		}
	}
}