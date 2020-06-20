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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class Build {

	public String getCommitter() {
		return committer;
	}

	public enum BuildStatus {
		SUCCESS, FAILED, TIMED_OUT, RUNTIME_ERROR
	}

	private final UUID buildID;
	private final UUID repoID;
	private final String branch;
	private final String commitHash;
	private final String committer;
	private OffsetDateTime startTime;
	private final BuildStatus status;

	public Build(UUID repoID, String branch, String commitHash, String committer, OffsetDateTime startTime, BuildStatus status) {
		this.buildID = UUID.randomUUID();
		this.repoID = repoID;
		this.branch = branch;
		this.committer = committer;
		this.commitHash = commitHash;
		this.startTime = startTime.withOffsetSameInstant(ZoneOffset.UTC);
		this.status = status;
	}

	public Build(UUID buildID, UUID repoID, String branch, String commitHash, String committer, OffsetDateTime startTime, BuildStatus status) {
		this.buildID = buildID;
		this.repoID = repoID;
		this.branch = branch;
		this.committer = committer;
		this.commitHash = commitHash;
		this.startTime = startTime.withOffsetSameInstant(ZoneOffset.UTC);
		this.status = status;
	}

	public UUID getBuildID() {
		return buildID;
	}

	public UUID getRepoID() {
		return repoID;
	}

	public String getBranch() {
		return branch;
	}

	public String getCommitHash() {
		return commitHash;
	}

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public BuildStatus getStatus() {
		return status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Build build = (Build) o;
		return Objects.equals(buildID, build.buildID) &&
				Objects.equals(repoID, build.repoID) &&
				Objects.equals(branch, build.branch) &&
				Objects.equals(commitHash, build.commitHash) &&
				Objects.equals(committer, build.committer) &&
				Objects.equals(startTime, build.startTime) &&
				status == build.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(buildID, repoID, branch, commitHash, committer, startTime, status);
	}

	@Override
	public String toString() {
		return "Build{" +
				"buildID=" + buildID +
				", repoID=" + repoID +
				", branch='" + branch + '\'' +
				", commitHash='" + commitHash + '\'' +
				", committer='" + committer + '\'' +
				", dateTime=" + startTime +
				", buildStatus=" + status +
				'}';
	}
}
