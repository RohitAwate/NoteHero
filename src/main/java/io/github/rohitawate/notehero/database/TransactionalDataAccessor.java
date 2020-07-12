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

import java.sql.Connection;
import java.sql.SQLException;

abstract class TransactionalDataAccessor<T, PK> implements DataAccessor<T, PK> {
	private Connection connection;
	private boolean transactional;

	void initTransactionMode(Connection connection) {
		this.connection = connection;
		this.transactional = true;
	}

	void exitTransactionMode() {
		this.connection = null;
		this.transactional = true;
	}

	void returnConnection() {
		if (transactional) return;

		PostgresPool.returnConnection(this.connection);
		this.connection = null;
	}

	Connection getConnection() throws SQLException {
		if (this.connection == null) {
			this.connection = PostgresPool.getConnection();
		}

		return connection;
	}
}
