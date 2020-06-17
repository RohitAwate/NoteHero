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
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class UserAccessorTest {

	private final UserAccessor accessor = new UserAccessor();
	private final User testUser = new User("rohitawate", "rohit@mailinator.com", "rohit123", Tier.ULTIMATE);

	@Test
	@Order(1)
	void create() {
		assertTrue(accessor.create(testUser));
	}

	@Test
	@Order(2)
	void read() {
		Optional<User> userWrapper = accessor.read("rohitawate");

		if (userWrapper.isPresent()) {
			User user = userWrapper.get();
			assertEquals(testUser.getUsername(), user.getUsername());
			assertEquals(testUser.getEmail(), user.getEmail());
			assertEquals(testUser.getTier(), user.getTier());
		} else fail();
	}

	@Test
	@Order(3)
	void login() {
		Optional<User> userWrapper = accessor.login("rohitawate", "rohit123");

		if (userWrapper.isPresent()) {
			User user = userWrapper.get();
			assertEquals(testUser.getUsername(), user.getUsername());
			assertEquals(testUser.getEmail(), user.getEmail());
			assertEquals(testUser.getTier(), user.getTier());
		} else fail();
	}

	@Test
	@Order(4)
	void update() {
		User updatedUserWithPassword = new User("jonsnow", "jon@mailinator.com", "dany", Tier.ULTIMATE);
		assertTrue(accessor.update("rohitawate", updatedUserWithPassword));

		User updatedUserWithoutPassword = new User("batman", "bruce@wayne.com", Tier.ULTIMATE);
		assertTrue(accessor.update("jonsnow", updatedUserWithoutPassword));
	}

	@Test
	@Order(5)
	void delete() {
		assertTrue(accessor.delete("batman"));
	}
}