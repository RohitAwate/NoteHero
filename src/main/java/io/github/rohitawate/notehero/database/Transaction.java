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

import io.github.rohitawate.notehero.logging.Log;
import io.github.rohitawate.notehero.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
	private Connection connection;
	private Logger logger = new Logger(Log.Level.WARNING);    // TODO: Use default global logger

	public Transaction() {
		try {
			connection = PostgresPool.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			logger.logError("Could not initialize a connection for the transaction.");
			e.printStackTrace();
		}
	}

	public void addAccessor(TransactionalDataAccessor<?, ?> accessor) {
		if (connection == null) {
			logger.logError("No connection available for transaction.");
			return;
		}

		accessor.setConnection(connection);
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	private void end() throws SQLException {
		connection.setAutoCommit(true);
		PostgresPool.returnConnection(connection);
		this.connection = null;
	}
}
