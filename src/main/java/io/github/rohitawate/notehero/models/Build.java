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
import java.util.Objects;
import java.util.UUID;

public class Build {

	public enum BuildStatus {
		SUCCESS, FAILED, TIMED_OUT, RUNTIME_ERROR
	}

	private final UUID buildID;
	private final UUID repoID;
	private final String branch;
	private final String commitHash;
	private final OffsetDateTime dateTime;
	private final BuildStatus buildStatus;

	public Build(UUID buildID, UUID repoID, String branch, String commitHash, OffsetDateTime dateTime, BuildStatus buildStatus) {
		this.buildID = buildID;
		this.repoID = repoID;
		this.branch = branch;
		this.commitHash = commitHash;
		this.dateTime = dateTime;
		this.buildStatus = buildStatus;
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

	public OffsetDateTime getDateTime() {
		return dateTime;
	}

	public BuildStatus getBuildStatus() {
		return buildStatus;
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
				Objects.equals(dateTime, build.dateTime) &&
				buildStatus == build.buildStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(buildID, repoID, branch, commitHash, dateTime, buildStatus);
	}

	@Override
	public String toString() {
		return "Build{" +
				"buildID=" + buildID +
				", repoID=" + repoID +
				", branch='" + branch + '\'' +
				", commitHash='" + commitHash + '\'' +
				", dateTime=" + dateTime +
				", buildStatus=" + buildStatus +
				'}';
	}
}
