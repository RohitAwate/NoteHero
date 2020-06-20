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

import io.github.rohitawate.notehero.models.Tier;
import io.github.rohitawate.notehero.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserAccessor extends TransactionalDataAccessor<User, String> {

	@Override
	public boolean create(User user) {
		// User can be instantiated without a password
		if (user.getPassword() == null) {
			return false;
		}

		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO Users (Username, Email, Password, Tier) VALUES(?, ?, crypt(?, gen_salt('bf', 8)), CAST(? AS UserTier))");
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getTier().toString());
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while creating new user: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public Optional<User> read(String username) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT Username, Email, Tier FROM Users WHERE Username=?");
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				return Optional.empty();
			}

			User user = new User(
					result.getString("Username"),
					result.getString("Email"),
					Tier.valueOf(result.getString("Tier"))
			);

			return Optional.of(user);
		} catch (SQLException e) {
			logger.logError("Error while reading user: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return Optional.empty();
	}

	public Optional<User> login(String username, String password) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT Username, Email, Tier FROM Users WHERE Username=? AND Password=crypt(?, Password)");
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				return Optional.empty();
			}

			User user = new User(
					result.getString("Username"),
					result.getString("Email"),
					Tier.valueOf(result.getString("Tier"))
			);

			return Optional.of(user);
		} catch (SQLException e) {
			logger.logError("Error while logging in user: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return Optional.empty();
	}

	@Override
	public boolean update(String username, User user) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement;

			// Since a User object can be instantiated with/without a password
			if (user.getPassword() == null) {
				statement = conn.prepareStatement("UPDATE USERS SET Username=?, Email=?, Tier=CAST(? AS UserTier) WHERE Username=?");
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getEmail());
				statement.setString(3, user.getTier().toString());
				statement.setString(4, username);
			} else {
				statement = conn.prepareStatement("UPDATE USERS SET Username=?, Email=?, Password=crypt(?, gen_salt('bf', 8)), Tier=CAST(? AS UserTier) WHERE Username=?");
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getEmail());
				statement.setString(3, user.getPassword());
				statement.setString(4, user.getTier().toString());
				statement.setString(5, username);
			}

			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while updating user: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public boolean delete(String username) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("DELETE FROM Users WHERE Username=?");
			statement.setString(1, username);
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while deleting user: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}
}
