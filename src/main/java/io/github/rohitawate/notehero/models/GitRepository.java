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

package io.github.rohitawate.notehero.models;

import java.util.Objects;
import java.util.UUID;

public class GitRepository {

	public enum GitHost {
		GITHUB, GITLAB, BITBUCKET
	}

	private final UUID repoID;
	private final String username;
	private final GitHost gitHost;
	private final String hostUsername;
	private final String repoName;
	private final String branch;
	private final UUID latestBuild;

	public GitRepository(UUID repoID, String username, GitHost gitHost, String hostUsername, String repoName, String branch, UUID latestBuild) {
		this.repoID = repoID;
		this.username = username;
		this.gitHost = gitHost;
		this.hostUsername = hostUsername;
		this.repoName = repoName;
		this.branch = branch;
		this.latestBuild = latestBuild;
	}

	public GitRepository(String username, GitHost gitHost, String hostUsername, String repoName, String branch, UUID latestBuild) {
		this.repoID = UUID.randomUUID();
		this.username = username;
		this.gitHost = gitHost;
		this.hostUsername = hostUsername;
		this.repoName = repoName;
		this.branch = branch;
		this.latestBuild = latestBuild;
	}

	public UUID getRepoID() {
		return repoID;
	}

	public String getUsername() {
		return username;
	}

	public GitHost getGitHost() {
		return gitHost;
	}

	public String getHostUsername() {
		return hostUsername;
	}

	public String getRepoName() {
		return repoName;
	}

	public String getBranch() {
		return branch;
	}

	public UUID getLatestBuild() {
		return latestBuild;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GitRepository that = (GitRepository) o;
		return Objects.equals(repoID, that.repoID) &&
				Objects.equals(username, that.username) &&
				gitHost == that.gitHost &&
				Objects.equals(hostUsername, that.hostUsername) &&
				Objects.equals(repoName, that.repoName) &&
				Objects.equals(branch, that.branch) &&
				Objects.equals(latestBuild, that.latestBuild);
	}

	@Override
	public int hashCode() {
		return Objects.hash(repoID, username, gitHost, hostUsername, repoName, branch, latestBuild);
	}

	@Override
	public String toString() {
		return "Repository{" +
				"repoID=" + repoID +
				", username='" + username + '\'' +
				", repoHost=" + gitHost +
				", hostUsername='" + hostUsername + '\'' +
				", repoName='" + repoName + '\'' +
				", branch='" + branch + '\'' +
				", latestBuild=" + latestBuild +
				'}';
	}
}