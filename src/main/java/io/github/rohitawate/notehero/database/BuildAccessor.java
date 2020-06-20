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

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public class BuildAccessor extends TransactionalDataAccessor<Build, UUID> {
	@Override
	public boolean create(Build build) {
		Connection conn = null;

		try {
			conn = getConnection();

			PreparedStatement statement = conn.prepareStatement("INSERT INTO Builds (BuildID, RepoID, GitBranch, CommitHash, Committer, StartTime, Status) VALUES(?, ?, ?, ?, ?, ?, CAST(? AS BuildStatus))");
			statement.setObject(1, build.getBuildID(), Types.OTHER);
			statement.setObject(2, build.getRepoID(), Types.OTHER);
			statement.setString(3, build.getBranch());
			statement.setString(4, build.getCommitHash());
			statement.setString(5, build.getCommitter());
			statement.setObject(6, build.getStartTime());
			statement.setString(7, build.getStatus().toString());
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while creating new build: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public Optional<Build> read(UUID buildID) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Builds WHERE BuildID=?");
			statement.setObject(1, buildID, Types.OTHER);
			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				return Optional.empty();
			}

			Build build = new Build(
					buildID,
					result.getObject("RepoID", UUID.class),
					result.getString("GitBranch"),
					result.getString("CommitHash"),
					result.getString("Committer"),
					result.getObject("StartTime", OffsetDateTime.class),
					Build.BuildStatus.valueOf(result.getString("Status"))
			);

			return Optional.of(build);
		} catch (SQLException e) {
			logger.logError("Error while reading build: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return Optional.empty();
	}

	@Override
	public boolean update(UUID buildID, Build build) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement;

			statement = conn.prepareStatement("UPDATE Builds SET StartTime=?, Status=CAST(? AS BuildStatus) WHERE BuildID=?");
			statement.setObject(1, build.getStartTime());
			statement.setString(2, build.getStatus().toString());
			statement.setObject(3, buildID, Types.OTHER);

			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while updating build: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public boolean delete(UUID buildID) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("DELETE FROM Builds WHERE BuildID=?");
			statement.setObject(1, buildID, Types.OTHER);
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while deleting build: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}
}
