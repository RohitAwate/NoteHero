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

import io.github.rohitawate.notehero.models.Note;
import io.github.rohitawate.notehero.models.NoteConfig;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class NoteAccessor extends TransactionalDataAccessor<Note, UUID> {
	@Override
	public boolean create(Note note) {
		Connection conn = null;

		try {
			conn = getConnection();

			PreparedStatement statement = conn.prepareStatement("INSERT INTO Notes (NoteID, BuildID, HTML, Markdown, Title, Private, Slug, Categories) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			statement.setObject(1, note.noteID, Types.OTHER);
			statement.setObject(2, note.buildID, Types.OTHER);
			statement.setString(3, note.html);
			statement.setString(4, note.markdown);
			statement.setString(5, note.config.title);
			statement.setBoolean(6, note.config.sudo);
			statement.setString(7, note.config.slug);
			statement.setArray(8, conn.createArrayOf("VARCHAR", note.config.categories));
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while creating new note: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public Optional<Note> read(UUID noteID) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Notes WHERE NoteID=?");
			statement.setObject(1, noteID, Types.OTHER);
			ResultSet result = statement.executeQuery();

			if (!result.next()) {
				return Optional.empty();
			}

			Note note = new Note(
					noteID,
					result.getObject("BuildID", UUID.class),
					result.getString("Markdown"),
					result.getString("HTML"),
					new NoteConfig(
							result.getString("Title"),
							(String[]) result.getArray("Categories").getArray(),
							result.getString("Slug"),
							result.getBoolean("Private")
					)
			);

			return Optional.of(note);
		} catch (SQLException e) {
			logger.logError("Error while reading note: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return Optional.empty();
	}

	@Override
	public boolean update(UUID noteID, Note note) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement;

			statement = conn.prepareStatement("UPDATE Notes SET Title=?, Private=?, Categories=? WHERE NoteID=?");
			statement.setString(1, note.config.title);
			statement.setBoolean(2, note.config.sudo);
			statement.setArray(3, conn.createArrayOf("VARCHAR", note.config.categories));
			statement.setObject(4, noteID, Types.OTHER);

			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while updating note: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}

	@Override
	public boolean delete(UUID noteID) {
		Connection conn = null;

		try {
			conn = getConnection();
			PreparedStatement statement = conn.prepareStatement("DELETE FROM Notes WHERE NoteID=?");
			statement.setObject(1, noteID, Types.OTHER);
			return statement.executeUpdate() == 1;
		} catch (SQLException e) {
			logger.logError("Error while deleting note: ");
			e.printStackTrace();
		} finally {
			PostgresPool.returnConnection(conn);
		}

		return false;
	}
}
