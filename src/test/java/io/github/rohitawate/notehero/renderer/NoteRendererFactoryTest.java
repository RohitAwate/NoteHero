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

package io.github.rohitawate.notehero.renderer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteRendererFactoryTest {

	@Test
	void get() {
		assertNotNull(NoteRendererFactory.get("", ".md", null));
		assertEquals(MarkdownRenderer.class, NoteRendererFactory.get("", ".md", null).getClass());

		assertNotNull(NoteRendererFactory.get("", ".markdown", null));
		assertEquals(MarkdownRenderer.class, NoteRendererFactory.get("", ".markdown", null).getClass());

		assertNotNull(NoteRendererFactory.get("", ".mdown", null));
		assertEquals(MarkdownRenderer.class, NoteRendererFactory.get("", ".mdown", null).getClass());

		assertNotNull(NoteRendererFactory.get("", ".mkd", null));
		assertEquals(MarkdownRenderer.class, NoteRendererFactory.get("", ".mkd", null).getClass());

		assertThrows(IllegalArgumentException.class, () -> NoteRendererFactory.get("", ".txt", null));
		assertThrows(IllegalArgumentException.class, () -> NoteRendererFactory.get("", "", null));
	}
}