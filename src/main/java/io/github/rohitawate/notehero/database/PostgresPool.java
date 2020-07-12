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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.github.rohitawate.notehero.logging.Log;
import io.github.rohitawate.notehero.logging.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresPool {
	private static Logger logger = new Logger(Log.Level.WARNING);

	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource dataSource;

	static {
		String pgURL = System.getenv("POSTGRES_URL");
		if (pgURL == null) {
			pgURL = "postgresql://localhost:5432/";
		}

		String pgUsername = System.getenv("POSTGRES_USER");
		if (pgUsername == null) {
			pgUsername = "";
		}

		String pgPassword = System.getenv("POSTGRES_PASSWORD");
		if (pgPassword == null) {
			pgPassword = "";
		}

		config.setJdbcUrl("jdbc:" + pgURL);
		config.setUsername(pgUsername);
		config.setPassword(pgPassword);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		dataSource = new HikariDataSource(config);
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	private static void printStats() {
		HikariPoolMXBean bean = dataSource.getHikariPoolMXBean();
		System.out.printf("idle: %d; active: %d; total: %d\n", bean.getIdleConnections(),
				bean.getActiveConnections(), bean.getTotalConnections());
	}

	public static void returnConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.logError("Error while returning connection to Hikari: ");
				e.printStackTrace();
			}
		}
	}

	public static void close() {
		dataSource.close();
	}
}
