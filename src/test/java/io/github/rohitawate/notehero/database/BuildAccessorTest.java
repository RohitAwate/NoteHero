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

import io.github.rohitawate.notehero.models.Build;
import io.github.rohitawate.notehero.models.GitRepo;
import io.github.rohitawate.notehero.models.Tier;
import io.github.rohitawate.notehero.models.User;
import org.junit.jupiter.api.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BuildAccessorTest {
	private static final UserAccessor USER_ACCESSOR = new UserAccessor();
	private static final GitRepoAccessor REPO_ACCESSOR = new GitRepoAccessor();
	private static final BuildAccessor BUILD_ACCESSOR = new BuildAccessor();

	private static final User TEST_USER = new User("buildtest", "buildtest@test.com", "pass123", Tier.FREE);
	private static final GitRepo TEST_REPO = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.GITLAB,
			"Doraemon", "Everest", "main", UUID.randomUUID());
	private static final Build TEST_BUILD = new Build(TEST_REPO.getRepoID(), TEST_REPO.getBranch(),
			"cab3b4d254bfafa74f0641b05761882921e5b435", "JonSnow", OffsetDateTime.now(), Build.BuildStatus.SUCCESS);

	@BeforeAll
	static void setUp() {
		assertTrue(USER_ACCESSOR.create(TEST_USER));
		assertTrue(REPO_ACCESSOR.create(TEST_REPO));
	}

	@Test
	@Order(1)
	void create() {
		assertTrue(BUILD_ACCESSOR.create(TEST_BUILD));
	}

	@Test
	@Order(2)
	void read() {
		Optional<Build> buildWrapper = BUILD_ACCESSOR.read(TEST_BUILD.getBuildID());
		if (buildWrapper.isPresent()) assertEquals(TEST_BUILD, buildWrapper.get());
		else fail();
	}

	@Test
	@Order(3)
	void update() {
		Build updatedBuild = new Build(TEST_REPO.getRepoID(), "feature-x",
				"6cbee4d3d40978096a2b8c5f43466842a99e3af0", "DanyTargaryen", OffsetDateTime.now(), Build.BuildStatus.RUNTIME_ERROR);
		assertTrue(BUILD_ACCESSOR.update(TEST_BUILD.getBuildID(), updatedBuild));

		Optional<Build> buildWrapper = BUILD_ACCESSOR.read(TEST_BUILD.getBuildID());
		if (buildWrapper.isPresent()) {
			Build build = buildWrapper.get();
			// Checking the build attributes that should not be updated
			assertEquals(TEST_BUILD.getBuildID(), build.getBuildID());
			assertEquals(TEST_BUILD.getRepoID(), build.getRepoID());
			assertEquals(TEST_BUILD.getBranch(), build.getBranch());
			assertEquals(TEST_BUILD.getCommitHash(), build.getCommitHash());
		} else fail();
	}

	@Test
	@Order(4)
	void delete() {
		assertTrue(BUILD_ACCESSOR.delete(TEST_BUILD.getBuildID()));
	}

	@AfterAll
	static void cleanup() {
		assertTrue(REPO_ACCESSOR.delete(TEST_REPO.getRepoID()));
		assertTrue(USER_ACCESSOR.delete(TEST_USER.getUsername()));
	}
}