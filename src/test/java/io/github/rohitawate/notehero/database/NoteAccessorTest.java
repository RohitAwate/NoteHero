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

package io.github.rohitawate.notehero.database;

import io.github.rohitawate.notehero.models.*;
import org.junit.jupiter.api.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteAccessorTest {
	private static final UserAccessor USER_ACCESSOR = new UserAccessor();
	private static final GitRepoAccessor REPO_ACCESSOR = new GitRepoAccessor();
	private static final BuildAccessor BUILD_ACCESSOR = new BuildAccessor();
	private static final NoteAccessor NOTE_ACCESSOR = new NoteAccessor();

	private static final User TEST_USER = new User("notetest", "notetest@test.com", "pass123", Tier.FREE);
	private static final GitRepo TEST_REPO = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.GITLAB,
			"Nobita", "BambooCopter", "main", UUID.randomUUID());
	private static final Build TEST_BUILD = new Build(TEST_REPO.getRepoID(), TEST_REPO.getBranch(),
			"cc7efb1dfabc2eab67e052d76f54c304e81b28c4", OffsetDateTime.now(), Build.BuildStatus.SUCCESS);
	private static final NoteConfig TEST_NOTE_CONFIG = new NoteConfig("Test Note", new String[]{"random", "test"}, "/random/test", true);
	private static final Note TEST_NOTE = new Note(TEST_BUILD.getBuildID(), "**bold**", "<strong>bold</strong>", TEST_NOTE_CONFIG);

	@BeforeAll
	static void setUp() {
		assertTrue(USER_ACCESSOR.create(TEST_USER));
		assertTrue(REPO_ACCESSOR.create(TEST_REPO));
		assertTrue(BUILD_ACCESSOR.create(TEST_BUILD));
	}

	@Test
	@Order(1)
	void create() {
		assertTrue(NOTE_ACCESSOR.create(TEST_NOTE));
	}

	@Test
	@Order(2)
	void read() {
		Optional<Note> noteWrapper = NOTE_ACCESSOR.read(TEST_NOTE.noteID);
		if (noteWrapper.isPresent()) assertEquals(TEST_NOTE, noteWrapper.get());
		else fail();
	}

	@Test
	@Order(3)
	void update() {
		NoteConfig updatedNoteConfig = new NoteConfig("Updated Note", new String[]{"new", "updated"}, "/random/updated/note", false);
		Note updatedNote = new Note(UUID.randomUUID(), UUID.randomUUID(), "*italic*", "<em>italic</em>", updatedNoteConfig);

		assertTrue(NOTE_ACCESSOR.update(TEST_NOTE.noteID, updatedNote));

		Optional<Note> noteWrapper = NOTE_ACCESSOR.read(TEST_NOTE.noteID);
		if (noteWrapper.isPresent()) {
			Note note = noteWrapper.get();
			// Checking the note attributes that should not be updated
			assertEquals(TEST_NOTE.noteID, note.noteID);
			assertEquals(TEST_NOTE.buildID, note.buildID);
			assertEquals(TEST_NOTE.html, note.html);
			assertEquals(TEST_NOTE.markdown, note.markdown);
			assertEquals(TEST_NOTE.config.slug, note.config.slug);
		} else fail();
	}

	@Test
	@Order(4)
	void delete() {
		assertTrue(NOTE_ACCESSOR.delete(TEST_NOTE.noteID));
	}

	@AfterAll
	static void cleanup() {
		assertTrue(BUILD_ACCESSOR.delete(TEST_BUILD.getBuildID()));
		assertTrue(REPO_ACCESSOR.delete(TEST_REPO.getRepoID()));
		assertTrue(USER_ACCESSOR.delete(TEST_USER.getUsername()));
	}
}