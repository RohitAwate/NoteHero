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

public class User {
	private String username;
	private String email;
	private String password;
	private Tier tier;

	public User(String username, String email, String password, Tier tier) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.tier = tier;
	}

	public User(String username, String email, Tier tier) {
		this.username = username;
		this.email = email;
		this.password = null;
		this.tier = tier;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Tier getTier() {
		return tier;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", tier=" + tier +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(username, user.username) &&
				Objects.equals(email, user.email) &&
				Objects.equals(password, user.password) &&
				tier == user.tier;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, email, password, tier);
	}
}
