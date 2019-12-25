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

import com.google.gson.Gson;
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
    private RenderController controller = new RenderController();

    private YAMLFrontMatterProcessor positiveProc, positiveLeadingSpaceProc, emptyFrontMatterProc, noOpenDelimProc,
            noCloseDelimProc, noFrontMatterProc, emptySourceProc, singleDelimProc, bothDelimProc;
    private YFMTestCase positive, positiveLeadingSpace, emptyFrontMatter, noOpenDelim, noCloseDelim, noFrontMatter;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        positive = loadTest("Positive.json");
        positiveProc = new YAMLFrontMatterProcessor(positive.full, controller);

        positiveLeadingSpace = loadTest("LeadingWhitespace.json");
        positiveLeadingSpaceProc = new YAMLFrontMatterProcessor(positiveLeadingSpace.full, controller);

        emptyFrontMatter = loadTest("EmptyFrontMatter.json");
        emptyFrontMatterProc = new YAMLFrontMatterProcessor(emptyFrontMatter.full, controller);

        noFrontMatter = loadTest("NoFrontMatter.json");
        noFrontMatterProc = new YAMLFrontMatterProcessor(noFrontMatter.full, controller);

        noOpenDelim = loadTest("NoOpenDelim.json");
        noOpenDelimProc = new YAMLFrontMatterProcessor(noOpenDelim.full, controller);

        noCloseDelim = loadTest("NoCloseDelim.json");
        noCloseDelimProc = new YAMLFrontMatterProcessor(noCloseDelim.full, controller);

        emptySourceProc = new YAMLFrontMatterProcessor("", controller);
        singleDelimProc = new YAMLFrontMatterProcessor("---", controller);
        bothDelimProc = new YAMLFrontMatterProcessor("---\n---", controller);
    }

    @Test
    void positive() {
        assertEquals(positiveProc.getStrippedConfig(), positive.expectedYFM);
        assertEquals(positiveProc.getStrippedNote(), positive.expectedNote);
    }

    @Test
    void positiveLeadingSpace() {
        assertEquals(positiveLeadingSpaceProc.getStrippedConfig(), positiveLeadingSpace.expectedYFM);
        assertEquals(positiveLeadingSpaceProc.getStrippedNote(), positiveLeadingSpace.expectedNote);
    }

    @Test
    void emptyFrontMatter() {
        assertEquals(emptyFrontMatterProc.getStrippedConfig(), emptyFrontMatter.expectedYFM);
        assertEquals(emptyFrontMatterProc.getStrippedNote(), emptyFrontMatter.expectedNote);
    }

    @Test
    void noFrontMatter() {
        assertEquals(noFrontMatterProc.getStrippedConfig(), noFrontMatter.expectedYFM);
        assertEquals(noFrontMatterProc.getStrippedNote(), noFrontMatter.expectedNote);
    }

    @Test
    void noOpenDelim() {
        assertEquals(noOpenDelimProc.getStrippedConfig(), noOpenDelim.expectedYFM);
        assertEquals(noOpenDelimProc.getStrippedNote(), noOpenDelim.expectedNote);
    }

    @Test
    void noCloseDelim() {
        assertEquals(noCloseDelimProc.getStrippedConfig(), noCloseDelim.expectedYFM);
        assertEquals(noCloseDelimProc.getStrippedNote(), noCloseDelim.expectedNote);
    }

    @Test
    void emptySource() {
        assertEquals(emptySourceProc.getStrippedConfig(), "");
        assertEquals(emptySourceProc.getStrippedNote(), "");
    }

    @Test
    void singleDelim() {
        assertEquals(singleDelimProc.getStrippedConfig(), "");
        assertEquals(singleDelimProc.getStrippedNote(), "---");
    }

    @Test
    void bothDelim() {
        assertEquals(bothDelimProc.getStrippedConfig(), "");
        assertEquals(bothDelimProc.getStrippedNote(), "");
    }

    YFMTestCase loadTest(String testCasePath) throws IOException, URISyntaxException {
        URL url = getClass().getResource("YAMLFrontMatterProcessor/" + testCasePath);
        Path path = Paths.get(url.toURI());
        String fileContents = new String(Files.readAllBytes(path));

        Gson gson = new Gson();
        return gson.fromJson(fileContents, YFMTestCase.class);
    }

    static class YFMTestCase {
        final String full;
        final String expectedYFM;
        final String expectedNote;

        public YFMTestCase(String full, String expectedYFM, String expectedNote) {
            this.full = full;
            this.expectedYFM = expectedYFM;
            this.expectedNote = expectedNote;
        }
    }
}