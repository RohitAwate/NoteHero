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

import io.github.rohitawate.notehero.models.GitRepo;
import io.github.rohitawate.notehero.models.Tier;
import io.github.rohitawate.notehero.models.User;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GitRepoAccessorTest {

	private static final UserAccessor USER_ACCESSOR = new UserAccessor();
	private static final GitRepoAccessor REPO_ACCESSOR = new GitRepoAccessor();

	private static final User TEST_USER = new User("gitrepotest", "test@test.com", "pass123", Tier.FREE);
	private static final GitRepo TEST_REPO = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.GITHUB, "RohitAwate",
			"NoteHero", "master", UUID.randomUUID());

	@BeforeAll
	static void setUp() {
		assertTrue(USER_ACCESSOR.create(TEST_USER));
	}

	@Test
	@Order(1)
	void create() {
		assertTrue(REPO_ACCESSOR.create(TEST_REPO));
	}

	@Test
	@Order(2)
	void read() {
		Optional<GitRepo> repoWrapper = REPO_ACCESSOR.read(TEST_REPO.getRepoID());
		repoWrapper.ifPresent(gitRepo -> assertEquals(TEST_REPO, gitRepo));
	}

	@Test
	@Order(3)
	void update() {
		GitRepo updatedRepo = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.BITBUCKET, "JonSnow",
				"Everest", "feature-x", UUID.randomUUID());
		assertTrue(REPO_ACCESSOR.update(TEST_REPO.getRepoID(), updatedRepo));
	}

	@Test
	@Order(4)
	void delete() {
		assertTrue(REPO_ACCESSOR.delete(TEST_REPO.getRepoID()));
	}

	@AfterAll
	static void cleanup() {
		assertTrue(USER_ACCESSOR.delete(TEST_USER.getUsername()));
	}
}