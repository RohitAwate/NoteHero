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

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class GitRepoAccessor extends TransactionalDataAccessor<GitRepo, UUID> {
	@Override
	public boolean create(GitRepo gitRepo) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO GitRepos VALUES(?, ?, CAST(? AS GitHost), ?, ?, ?, ?)");
			statement.setObject(1, gitRepo.getRepoID(), Types.OTHER);
			statement.setString(2, gitRepo.getUsername());
			statement.setString(3, gitRepo.getGitHost().toString());
			statement.setString(4, gitRepo.getHostUsername());
			statement.setString(5, gitRepo.getRepoName());
			statement.setString(6, gitRepo.getBranch());
			statement.setObject(7, gitRepo.getLatestBuild(), Types.OTHER);
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while creating new repo: ");
			e.printStackTrace();
		} finally {
			returnConnection();
		}

		return false;
	}

	@Override
	public Optional<GitRepo> read(UUID repoID) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM GitRepos WHERE RepoID=?");
			statement.setObject(1, repoID, Types.OTHER);
			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				return Optional.empty();
			}

			GitRepo repo = new GitRepo(
					result.getObject("RepoID", UUID.class),
					result.getString("Username"),
					GitRepo.GitHost.valueOf(result.getString("GitHost")),
					result.getString("HostUsername"),
					result.getString("RepoName"),
					result.getString("Branch"),
					result.getObject("LatestBuildID", UUID.class)
			);

			return Optional.of(repo);
		} catch (SQLException e) {
			logger.logError("Error while reading repo: ");
			e.printStackTrace();
		} finally {
			returnConnection();
		}

		return Optional.empty();
	}

	@Override
	public boolean update(UUID repoID, GitRepo gitRepo) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement("UPDATE GitRepos SET GitHost=CAST(? AS GitHost), HostUsername=?, RepoName=?, Branch=?, LatestBuildID=? WHERE RepoID=?");
			statement.setString(1, gitRepo.getGitHost().toString());
			statement.setString(2, gitRepo.getHostUsername());
			statement.setString(3, gitRepo.getRepoName());
			statement.setString(4, gitRepo.getBranch());
			statement.setObject(5, gitRepo.getLatestBuild(), Types.OTHER);
			statement.setObject(6, repoID, Types.OTHER);

			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while updating repo: ");
			e.printStackTrace();
		} finally {
			returnConnection();
		}

		return false;
	}

	@Override
	public boolean delete(UUID repoID) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("DELETE FROM GitRepos WHERE RepoID=?");
			statement.setObject(1, repoID, Types.OTHER);
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while deleting repo: ");
			e.printStackTrace();
		} finally {
			returnConnection();
		}

		return false;
	}
}
