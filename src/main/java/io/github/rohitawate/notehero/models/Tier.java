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

public enum Tier {
	/*
		┌──────────────────┬─────────────────────────────────────┬────────────────────────────┐
		│       FREE       │               PREMIUM               │          ULTIMATE          │
		├──────────────────┼─────────────────────────────────────┼────────────────────────────┤
		│ $0/forever       │ $2/month                            │ $4/month                   │
		│ Sequential build │ Parallel build                      │ Parallel build             │
		│ 1 Git repository │ 5 Git repositories                  │ Unlimited Git repositories │
		│ Public notes     │ Private notes                       │ Private notes              │
		│ No history       │ History: Head commits during builds │ History: All commits       │
		└──────────────────┴─────────────────────────────────────┴────────────────────────────┘
	 */

	FREE, PREMIUM, ULTIMATE
}
