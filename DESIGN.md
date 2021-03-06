# NoteHero: Design Specification

## Architecture
![Architecture Diagram](assets/ArchitectureDiagram.png)
1. Users push their code to their Git service of choice.
2. The service invokes a webhook to the NodeHero server with the information about the push.
3. NoteHero checks the webhook payload if new commits are present. If no, it skips further steps.
4. If new commits are found, NoteHero pulls the latest changes from the Git service.
5. NoteHero renders the modified/new notes to HTML and builds the search index.
6. On an HTTP request, the fully rendered page is returned in the response. NoteHero also establishes a WebSocket connection with the browser. This is used to push update notifications in case a new commit is pushed whilst NoteHero is already running in the browser.

---

## Server Endpoints
####  `POST /rebuild`
- This is the endpoint invoked by the webhook registered on the Git service. It is invoked when there is a new push.
- Only accepts requests from the Git service.
- Requires a mutex lock to prevent data races while rendering HTML and building the search index.
- Should only re-render HTML and rebuild search index for modified/new notes.
- Re-organizes the comments based on the changes in the file.

#### `GET /cat1/cat2/.../catn/note-slug`
- Delivers the note nested in these categories.
- If the note has `sudo` enabled in the YAML front matter _(check specification below)_, the session cookie is checked. If not authorized, HTTP 401 (Unauthorized) status code is returned with a webpage with an option to sign in.
- The slug should either be specified in the YAML frontmatter of the Markdown source or be generated from the filename. For the latter option, skip the file extension.
- Check mockup below.  

#### `GET /comments?note=/cat1/cat2/.../catn/note-slug`
- **<span style="color:red">Future scope.</span>**
- API endpoint, returns JSON array of comments for that note.
- If the note has `sudo` enabled in the YAML front matter, the session cookie is checked. If not authorized, HTTP 401 (Unauthorized) status code is returned.
- Each line in the note can have multiple comments.
- Sample response:
```json
[
    {
        "rangeStart": 10,
        "rangeEnd": 15, 
        "index": 1,
        "timestamp": "2019-01-19 03:14:07",
        "body": "Check Wikipedia for this"
    },
    {
        "rangeStart": 10,
        "rangeEnd": 15,
        "index": 2,
        "timestamp": "2018-05-06 05:45:12",
        "body": "Add citation"
    }
]
```
- [`rangeStart`, `rangeEnd`]: inclusive limits of the range of data over which the comment is applied
- `index`: the index of the comment among the list of all comments for that line
- `timestamp`: the time instance when the comment was made
- `body`: the actual textual content of the comment 

#### `POST /comments`
- **<span style="color:red">Future scope.</span>**
- API endpoint, accepts a JSON object containing the note's slug and the comments to be added.
- Sample payload:
```json
{
    "note": "/cat1/cat2/.../catn/note-slug",
    "comments": [
        {
            "rangeStart": 10,
            "rangeEnd": 15, 
            "timestamp": "2019-01-19 03:14:07",
            "body": "Check Wikipedia for this"
        },
        {
            "rangeStart": 10,
            "rangeEnd": 15,
            "timestamp": "2018-05-06 05:45:12",
            "body": "Add citation"
        }
    ]
}
```
- `note`: specifies which note the comments belong to
- Note the lack of an `index` field. It is added by the server/database after checking if there are other comments for this range.

#### `POST /login`
- Allows user to login.
- Accepts `application/x-www-formurlencoded` payload.
- Expected fields:
    - `username`: username in plaintext
    - `password`: hashed password _(refer: https://stackoverflow.com/a/48161723)_
    - `csrf_token`: CSRF token injected by server into webpage
- Response places session cookie in the browser.

#### `GET /logout`
- Clears the session on the server and its corresponding cookie in the browser.

#### `GET /`
- Delivers the homepage.
- Check mockup below.

---

## Mockups

#### Homepage
![Homepage Mockup](assets/mockups/Homepage.svg)

---

#### Notes View
![Notes Mockup](assets/mockups/NotesView.svg)

---

#### Git Build Banners
![GitHub Banners](assets/mockups/GitHubBanners.svg)
_TODO: Add a banner for when the user is notified of a new build whilst they're on NoteHero already._

---

#### TODO: Navigation Bar Options

---

## Front Matter Specification
```yaml
---
notehero:
    title: 'Convolutional Neural Networks'
    sudo: false
    slug: 'cnn'
    categories: 'CS > Deep Learning'
---
```

NoteHero's rendering and search indexing can be configured by adding the above block of YAML Front Matter at the beginning of your note. All attributes are **_optional_**.

- `title` (String): Applied to the `<title>` tag in the rendered HTML.
\
**Default behaviour is as follows:** \
**First, remove all characters except upper/lower case alphabet, numbers, underscores and spaces.**
    - **For camel-case filenames**:
    Extension is omitted. The individual words are separated and then concatenated by adding single space in between.
    For example, `HelloWorld.md` becomes `Hello World`.
    - **For snake-case filenames**:
    Extension is omitted. Individual words are separated, first letter is capitalized and then they are concatenated by adding single space in between.
    For example, `hello_world.md` becomes `Hello World`.
    - **For filenames with spaces**:
    Extension is omitted. Individual words are separated, first letter is capitalized and then they are concatenated by adding single space in between. For example, `Hello world.md` becomes `Hello World`.
- `sudo` (Boolean): Defines the visibility of the note. If `true`, note is only visible when logged in, else it is publicly visible. **Defaults to `true`**.
- `slug` (String): Used in the URL of the final note. The format of the URL is as follows: `/cat1/cat2/.../catn/slug` where `cat1`, `cat2`, etc. are the `categories` in the YAML Front Matter.
\
**Default behaviour is as follows:** \
**First, remove all characters except upper/lower case alphabet, numbers, underscores and spaces.**
    - **For camel-case filenames**:
    Extension is omitted. The individual words are separated, converted to lower-cased and then concatenated by adding hyphens in between.
    For example, `HelloWorld.md` becomes `hello-world`.
    - **For snake-case filenames**:
    Extension is omitted. Underscores are replaced with hyphens.
    For example, `hello_world.md` becomes `hello-world`.
    - **For filenames with spaces**:
    Extension is omitted. Spaces are replaced with hyphens. Entire string is converted to lowercase.
    For example, `Hello world.md` becomes `hello-world`.
- `categories` (String): Categories should separated by > i.e. closing angle bracket and written left to right in decreasing order of hierarchy. For example, refer the above example. Categories are included in the search index and used to organize the notes in a hierarchy, as shown in the homepage mockup. \
**Default behaviour depends on the location of the Markdown source file:**
    - If it is nested within directories inside the `notes/` directory, the names of the directories are used for the categories. The hierarchy of the directory structure is maintained. For example, if the file is located in `notes/Math/Calculus/Differentiation/`, then the categories are as `Math > Calculus > Differentiation`.
    - If it is located in `notes/`, then no categories are added to the note. This may cause conflicts while generating URLs.

NoteHero parses the Front Matter and returns a `NoteConfig` object initialized with values from the parse tree.

---

## Implementation Guidelines

![Flowchart for Rendering and Search Index Building](assets/RenderSearchIndexFlowchart.svg)

### Rendering Algorithm
1. Within the root directory of the cloned Git repository, look for the `notes/` directory. If not found, append to the build error log and quit.
2. From the Git commit data, find all Markdown files from `notes/` and generate a list containing the candidates for rendering. Each entry in the list must contain the full path to the file from the `notes/` directory. This is useful for resolving issues related to an absent `categories` attribute in the YAML Front Matter.
3. Invoke the renderer for each file on the list.
    1. Parse the YAML Front Matter _(see algorithm below)_ and store it in an associated object.
    2. Retrieve the substring containing just the Markdown, without the YAML Front Matter.
    3. Render the above string of Markdown code to HTML.
    4. Re-organize the comments. _(see algorithm below)_
    4. Add `<title>` tag based on YAML Front Matter.
    5. Save HTML to disk YAML Front Matter to the database.
    6. Add title, text-content of notes (stripped of Markdown syntax), and categories to search index.

---

### YAML Front Matter Parsing Algorithm
1. Find the opening and closing `---` delimiters for the YAML Front Matter. If none or just one of the delimiters are found, return a `NoteConfig` object initialized with default values and append to build info log.
2. Parse the entire block of YAML.
3. Find the `notehero` object in the parse tree. If not found, return a `NoteConfig` object initialized with default values and append to build info log.
4. If found, return a `NoteConfig` object initialized with values from this YAML object.

---

### Search Engine Implementation
The search engine needs to perform two jobs: **index building** and **query processing**.

#### Index Building Algorithm
1. The modified/new Markdown source files should be converted to plain text (refer [StackOverflow](https://stackoverflow.com/questions/761824/python-how-to-convert-markdown-formatted-text-to-text)). _This approach retains multiline code blocks._
2. Each file is then tokenized. The location of the token in the file is attached to it (line number, start and end of range). Stopword tokens are removed and the remaining are then stemmed. This step should be parallelized for each file.
3. Block until all files complete Step 2.
4. This step calculates the term-frequency, inverse document frequency score (TF-IDF) for each of the file's tokens. The results are stored in a hash table with the key as the token and its value being an object containing the following:
    - The list of documents containing the key.
    - Each document in this list also contains the corresponding TF-IDF score of that token for that document.
    - It also contains a list of all the locations in itself where the token appears.

![Search Index Hash Table Schema](assets/SearchIndexHashTable.svg)

5. A trie data structure is built out of the keys in the hash table. This powers autocomplete in our engine.
6. The hash table and the trie are stored and accessed in memory. Binary representations are also stored to disk that can be used on subsequent server reloads without rebuilding the index.

#### Query Algorithm
1. We first need to generate a set of candidate words for searching in our index. These come from the search query itself and also the autocomplete trie.
2. Tokenize and stem the query and add the resultant tokens to the candidate set.
3. For each of these words, find autocomplete suggestions from the trie. For each of these suggestions, perform tokenization followed by stemming and finally add them to the candidate set.
4. For each word in the candidate list find the corresponding value from the search index hash table. Add it to the pooling list.
5. Each member of the pooling list will contain a TF-IDF score and a set of documents containing the query along with its locations in that query. Thus, we need to find the union of these document sets.
6. Return the union set.

**Alternative for Steps 5 and 6:** \
In case the performance of the above system is poor, we can get rid of the unionization algorithm.
- We iterate over the list of documents that each word in our candidate set appears in. We get these from the search index hash table.
- The document name is set as the key for each entry.
- If a key does not exist in the table, we add it with its value set to the token's TF-IDF score.
- If it does exist, its value is incremented by 1.
- Finally, this hash table is returned.

---

### Disk Storage Schema (DiskHero)
- Storing the notes on the main server's disk would produce many problems:
    - Monolithic architecture
    - Large storage requirements
    - Limited scalibility
    - Poor server performance due to constant disk usage
- Offloading the storage to a separate service (which can run on a separate physical server) solves all of the above problems.
- Go was chosen as the language of choice for this due to its lightweight concurrency model which lends itself perfectly to a simple API server.
- All rendered notes will be stored to the web server's disk in `$HOME/notehero/notes/`.
- The directory structure within the above directory shall be as such:

```
notes/
├── free
│   └── nhusername
│       └── gh
│           └── repo1
│               └── lastestcommithash
│                   └── cat1
│                       └── cat2
│                           └── some-note
├── premium
│   └── nhusername2
│       ├── bb
│       └── gl
│           ├── repo1
│           │   ├── commithash1
│           │   │   └── cat1
│           │   │       └── cat2
│           │   │           └── some-other-note
│           │   └── commithash2
│           │       └── cat1
│           │           └── cat2
│           │               └── some-other-note
│           └── repo2
│               └── commithash1
│                   └── cat1
│                       └── cat2
│                           └── some-other-note
└── ultimate

```
- Each NoteHero plan gets its own directory, which contains directories corresponding to each NoteHero username on that plan.
- Each of these username directories contains directories corresponding to each of the supported Git service providers: `gh` for GitHub, `gl` for GitLab and `bb` for BitBucket.
- The repositories from each of these providers are nested inside this directory.
- Each of these repositories contains directories corresponding to their commit hash. Each of these in turn contains rendered notes from that Git commit nested as per their category hierarchy.
- Further down the road, this monolithic storage architecture can be broken down into a distributed one.

### DiskHero Server Endpoints

- All requests must pass through middleware which checks Origin header and only serves requests from the NoteHero server. Else, returns a 403 (Forbidden).
- DiskHero doesn't concern itself with any of the metadata (`NoteConfig`) related to the notes. This must be handled by NoteHero itself.

#### `GET /nh-username/git-provider/repo-name/cat1/cat2/note-slug`
- Expected query param: **tier=free/premium/ultimate**
- Returns a rendered page.
- Request Content-Type: `text/html`
- 200 if found, 404 if not.

#### `PUT /nh-username/git-provider/repo-name/cat1/cat2/note-slug`
- Request Content Type: `application/json`
- Expected JSON Schema:

```json
{
    "ingestionConfig": {
        "user": {
            "username": "rohit",
            "tier": "free/premium/ultimate"
        },
        "gitHost": "gh/gl/bb",
        "repoName": "myNotes",
        "commitHash": "ee634c967f051f83e1822334d6fb7df03a2276c7"
    },
    "noteConfig": {
        "categories": ["cat1", "cat2", "cat3"],
        "slug": "my-awesome-note"
    },
    "note": "<h1>hello, world!</h1>"
}
```

- `commitHash` may be skipped for `free` tier requests. Required for `premium` and `ultimate`.
- 201 if new note is created, 200 if existing note updated successfully, 409 (Conflict) if there is a conflict in the note path/URL, 400 if any of the required parameters are missing.
- For 201 and 200, return the `Location` header with the URL to the new resource.

#### `DELETE /nh-username/git-provider/repo-name/cat1/cat2/note-slug`
- Expected query param: **tier=free/premium/ultimate**
- Deletes a rendered page.
- 200 if found and deleted, 404 if not.

---

### TODO: TF-IDF Algorithm

### TODO: Server Startup Algorithm

### TODO: Comments Re-Organization Algorithm

---

## API Specification

### Renderer

<!-- 

```mermaid
classDiagram

    class NoteRenderer {
        String source
        NoteConfig config
        NoteRenderer(String)
        void parseConfig()
        NoteConfig getConfig()
        String render()
    }

    class NoteConfig {
        String title
        ArrayList categories
        String slug
        boolean sudo
        NoteConfig(String title)
        NoteConfig(String, ArrayList, String, boolean)
    }

    class YAMLFrontMatterParser {
        String yamlSource
        NoteConfig config
        YAMLFrontMatterParser(String src)
        NoteConfig parse()
    }

    class MarkdownRenderer {
        String markdownSource
        String yamlFrontMatter
        String stripYAMLFrontMatter()
    }

    NoteRenderer "1" *-- "1" NoteConfig
    MarkdownRenderer "1" *-- "1" YAMLFrontMatterParser
    NoteRenderer <|-- MarkdownRenderer
```
 -->

![Renderer Class Diagram](assets/RendererClassDiag.png)

### Search Engine

_Work in progress._

<!--
```mermaid

classDiagram
  
    class Utils {
        String HTMLToPlaintext(String)
    }

    class Token {
        String text
        int line
        int start
        int end
        Token(String, int, int, int)
    }

    class Preprocessor {
        Token[] tokens
        Preprocessor(String)
        void tokenize()
        void removeStopwords()
        void stemTokens()
        Token[] getTokens()
    }

```
-->

![Search Engine Class Diagram](assets/SearchEngineClassDiag.png)

---

## Database Schema

<center>

![Database Schema](assets/DBSchema.png)

</center>
---

## Keybindings

| Action | Keybinding |
| --- | --- |
| Ctrl + Alt + M | Add comment on line |
| <center>/</center> | Focus fuzzy search box |

_Work in progress_
