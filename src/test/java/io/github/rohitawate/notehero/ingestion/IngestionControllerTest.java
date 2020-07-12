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

package io.github.rohitawate.notehero.ingestion;

import io.github.rohitawate.notehero.database.GitRepoAccessor;
import io.github.rohitawate.notehero.database.UserAccessor;
import io.github.rohitawate.notehero.models.Build;
import io.github.rohitawate.notehero.models.GitRepo;
import io.github.rohitawate.notehero.models.Tier;
import io.github.rohitawate.notehero.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IngestionControllerTest {
	private static final UserAccessor USER_ACCESSOR = new UserAccessor();
	private static final GitRepoAccessor REPO_ACCESSOR = new GitRepoAccessor();

	private static final User TEST_USER = new User("yfmtest", "yfmtest@test.com", "pass123", Tier.FREE);
	private static final GitRepo TEST_REPO = new GitRepo(TEST_USER.getUsername(), GitRepo.GitHost.GITLAB,
			"Chandler", "Everest", "main", UUID.randomUUID());
	private static final Build TEST_BUILD = new Build(TEST_REPO.getRepoID(), "main",
			"cab3b4d254bfafa74f0641b05761882921e5b435", "JonSnow", OffsetDateTime.now(), Build.BuildStatus.SUCCESS);

	@BeforeAll
	static void setUp() {
		assertTrue(USER_ACCESSOR.create(TEST_USER));
		assertTrue(REPO_ACCESSOR.create(TEST_REPO));
	}

	@Test
	void ingestAll() {
		List<String> candidates = getCandidates("IngestionController/notes");
		IngestionController controller = new IngestionController(TEST_BUILD, candidates);
		controller.ingestAll();
	}

	// Taken from: https://stackoverflow.com/a/59071217/6948907
	public List<String> getCandidates(String dir) {
		URL url = IngestionControllerTest.class.getResource(dir);
		Path path = null;
		try {
			path = Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		List<String> fileWithName = null;
		try {
			fileWithName = Files.walk(path)
					.filter(s -> s.toString().endsWith(".md") || s.toString().endsWith(".mkd") || s.toString().endsWith(".markdown") || s.toString().endsWith(".mdown"))
					.map(Path::toString)
					.sorted()
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileWithName;
	}
}