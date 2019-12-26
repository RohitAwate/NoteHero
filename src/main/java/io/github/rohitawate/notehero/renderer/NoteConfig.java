/*
 * Copyright 2019 Rohit Awate.
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

package io.github.rohitawate.notehero.renderer;

import java.util.Arrays;

/**
 * Used to store the configuration data stored in notes
 * that may be used to affect the build process.
 */
class NoteConfig {
	final String title;
	final String[] categories;
	final String slug;
	final boolean sudo;

	NoteConfig(String title, String[] categories, String slug, boolean sudo) {
		this.title = title;
		this.categories = categories;
		this.slug = slug;
		this.sudo = sudo;
	}

	@Override
	public String toString() {
		return "NoteConfig{" +
				"title='" + title + '\'' +
				", categories=" + Arrays.toString(categories) +
				", slug='" + slug + '\'' +
				", sudo=" + sudo +
				'}';
	}
}
