/*
 * Copyright 2020 Rohit Awate.
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

package io.github.rohitawate.notehero.ingestion.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.Arrays;
import java.util.Objects;

/**
 * Used to store the configuration data stored in notes
 * that may be used to affect the build process.
 */
class NoteConfig {
	final String title;
	final String[] categories;
	final String slug;
	final boolean sudo;

	@JsonCreator
	NoteConfig(@JsonProperty("title") String title,
			   @JsonProperty("categories") @JsonSetter(nulls = Nulls.AS_EMPTY) String[] categories,
			   @JsonProperty("slug") String slug,
			   @JsonProperty("sudo") boolean sudo) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NoteConfig that = (NoteConfig) o;
		return sudo == that.sudo &&
				Objects.equals(title, that.title) &&
				Arrays.equals(categories, that.categories) &&
				Objects.equals(slug, that.slug);
	}
}
