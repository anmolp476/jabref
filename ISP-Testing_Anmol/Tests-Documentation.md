# ISP TEST DESIGN - BibTeX/BibLaTeX Parsing

**INFO**

Here are the main steps to ISP,

1. Identify Testable Functions

    1. BibtexParser.parse(), 

    2. BibtexParser.parseEntry(), 

    3. BibtexParser.parseField(),

    4. BibtexParser.parseString()

2. Identify ALL parameters to the functions

3. Model the input domain in terms of characteristics, each of which give certain partitions

4. Choose particular partitions, and values from within those partitions

5. Refine into test values



## Method 1 - `BibtexParser.parse(Reader in)`

**Description**: This is the main public method thats responsible for the entire parsing process. It takes in a `.bib` file(the `Reader` object), initializes the database, parses any database IDs, and loops through the entire file to extract all preambles, strings, comments, and entries



**Step 2: Identify all the parameters of this method**

    1. There's the explicit parameter being the `Reader in` object. 



**Step 3: Modeling the input domain in terms of characteristics and partitions**

Since this method handles the entire file, we will partitoin based on the contents of the file. 



* **Characteristic A: Overall File Emptiness/Validity**

    * *Block A1:* The file contains valid, parsable BibTeX data.

    * *Block A2:* The file is completely empty.

* **Characteristic B: Types of BibTeX Declarations Present**

    * *Block B1:* File contains standard bibliography entries (e.g., `@article`, `@book`).

    * *Block B2:* File contains special declarations (`@string`, `@preamble`, `@comment`).

    * *Block B3:* File contains a mixture of both standard entries and special declarations.

* **Characteristic C: Presence of Non-BibTeX "Junk" Text**

    * BibTeX standard dictates that any text outside of an `@` declaration should be ignored.

    * *Block C1:* Clean file; contains only valid `@` declarations.

    * *Block C2:* File contains arbitrary "junk" text before, between, or after the `@` entries.



**Step 4: Choose particular partitions and values from within those partitions**

Using BCC, we establish a primary base test simulating a standard, which is the clean good `.bib` file (A1, B1, C1). We then generate subsequent tests by changing one characteristic at a time.



* **Base Test (A1, B1, C1):** Populated file, standard entries only, completely clean text.

* **Varying A:**

    * (A2, no characteristic, no characteristic): Completely empty file.

* **Varying B:**

    * (A1, B2, C1): Populated file, only contains `@string` or `@preamble` declarations, clean text.

    * (A1, B3, C1): Populated file, mixture of `@article` and `@string` declarations, clean text.

* **Varying C:**

    * (A1, B1, C2): Populated file, standard entries, but contains random junk text outside the tags.



**Step 5: Refine into test values**

These combinations are refined into concrete Java `StringReader` inputs to feed into the `parse()` method.



* **Test 1 (Base testA1, B1, C1):** * *Input:* `"@article{key1, author={Smith}} \n @book{key2, title={Testing}}"`

    * *Expected Output:* Returns a `ParserResult` containing a database with exactly 2 parsed entries.

* **Test 2 (A2, N/A, N/A):** * *Input:* `""`

    * *Expected Output:* Returns a `ParserResult` with an empty database, without throwing any `NullPointerException`s.

* **Test 3 (A1, B2, C1):** * *Input:* `"@string{foo = {bar}} \n @preamble{This is a preamble}"`

    * *Expected Output:* Returns a `ParserResult` where the database contains the string and preamble, but 0 standard entries.

* **Test 4 (A1, B3, C1):** * *Input:* `"@string{foo = {bar}} \n @article{key1, author=foo}"`

    * *Expected Output:* Returns a `ParserResult` containing both the string variable and the standard entry that references it.

* **Test 5 (A1, B1, C2):** * *Input:* `"Here is some introduction text. @article{key1, author={Smith}} And here is some concluding text."`

    * *Expected Output:* The parser successfully ignores the outer junk text and successfully returns a database with exactly 1 entry.





## Method 2 - `BibtexParser.parseEntry(String entryType)`



**Description**: This method is responsible for parsing the body of a single BibTeX entry. After the parser has checked for the `@` symbol and the entry type (ex., `article`), this method takes in the opening bracket, extracts the citation key, iteratively parses all internal fields by calling `parseField()`, and verifies the closing bracket.



**Step 2: Identify all the parameters to the functions**

* The explicit parameter is `String entryType` 

* The implicit parameter is The internal `PushbackReader` stream (just want to explain that the `this` object holds several state variables such as, importFormatPreferences, database, eof, and the pushbackReader mentioned here).



**Step 3: Model the input domain in terms of characteristics and partitions**

We will use functionality-based IDM here.



* **Characteristic A: Entry Type Validity (`entryType`)**

    * *Block A1:* Standard normal entry type (e.g., `"article"` or `"book"`).

    * *Block A2:* Custom or never-seen-before entry type (e.g., `"mycustomtype"`).

* **Characteristic B: Enclosure Syntax**

    * *Block B1:* Entry is correctly enclosed in curly braces `{ ... }`.

    * *Block B2:* Entry is correctly enclosed in parentheses `( ... )`.

    * *Block B3:* Missing or mismatched opening bracket.

* **Characteristic C: Citation Key Presence**

    * *Block C1:* A valid, populated citation key exists before the first comma.

    * *Block C2:* The citation key is completely absent (the opening bracket is immediately followed by a comma or closing bracket).

* **Characteristic D: Internal Field Count**

    * *Block D1:* Multiple fields exist, properly separated by commas.

    * *Block D2:* Exactly one field exists.

    * *Block D3:* Zero fields exist (the entry contains only a citation key).



**Step 4: Choose particular partitions and values from within those partitions**

Applying Each Choice Coverage (ECC) to minimize the test suite. Our largest characteristics (B and D) have 3 blocks. Therefore, we only need 3 tests to ensure every partition is executed at least once.



* **Test 1 (A1, B1, C1, D1):** Standard type, braced, key present, multiple fields.

* **Test 2 (A2, B2, C2, D2):** Custom type, parenthesized, no key, single field.

* **Test 3 (A1, B3, C1, D3):** Standard type, invalid bracket, key present, zero fields.



**Step 5: Refine into test values**

For JUnit execution, these strings can be passed into the public `parseSingleEntry(String)` method to start the core `parseEntry()` logic.



* **Test 1 (A1, B1, C1, D1):** * *Input:* `"@article{smith2026, author={Smith}, year=2026}"`

    * *Expected Output:* Returns a `BibEntry` of type `article` with citation key `"smith2026"` containing 2 fields.

* **Test 2 (A2, B2, C2, D2):** * *Input:* `"@software(, title={JabRef})"`

    * *Expected Output:* Returns a `BibEntry` successfully mapping to a custom or fallback `"software"` type with an empty key `""` and 1 field.

* **Test 3 (A1, B3, C1, D3):** * *Input:* `"@article smith2026}"`

    * *Expected Output:* Throws a `ParseException` or `IOException` because the opening bracket is missing.





## Method 3 - `BibtexParser.parseField(BibEntry entry)`



**Description**: This method reads from the parser's internal character stream to extract a single field name and its corresponding content (they're both treated as a key-value pair), and then injects that data into the provided `BibEntry` object. I'll use a functionality-based approach here too because the source code contains branching logic based on the semantic meaning of the field being parsed.



**Step 2: Identify all the parameters to the functions**

* The explicit parameter is the `BibEntry entry` (The data structure into which the parsed field is stored).

* The implicit parameter is the `PushbackReader` stream.



**Step 3: Model the input domain in terms of characteristics and partitions**

* **Characteristic A: Field Semantic Type (The Parsed Key)**

    * *Block A1:* Standard textual field (e.g., `title`, `journal`).

    * *Block A2:* Person Name field requiring concatenation logic (e.g., `author`, `editor`).

    * *Block A3:* Keyword field requiring separator logic (`StandardField.KEYWORDS`).

    * *Block A4:* MacOS BibDesk file attachment (field name starts with `bdsk-file-`).

* **Characteristic B: Prior State of `BibEntry` (The `entry.hasField` branch)**

    * *Block B1:* The `BibEntry` does *not* already contain a field with this name.

    * *Block B2:* The `BibEntry` *already contains* a field with this name.

* **Characteristic C: Content Enclosure Format (The Parsed Value)**

    * *Block C1:* Value is enclosed in curly brackets `{content}`.

    * *Block C2:* Value is enclosed in double quotes `"content"`.

    * *Block C3:* Value is an unquoted numeric digit (e.g., `2026`).

    * *Block C4:* Value utilizes string concatenation with the `#` operator (e.g., `Kopp # Kolb`).



**Step 4: Choose particular partitions and values from within those partitions**

Applying Each Choice Coverage (ECC) because it's simple and does the job.



* **Test 1 (A1, B1, C1):** Standard field, new field, bracketed value.

* **Test 2 (A2, B2, C2):** Person name, field already exists, quoted value.

* **Test 3 (A3, B1, C3):** Keyword field, new field, numeric value.

* **Test 4 (A4, B2, C4):** BibDesk file, field already exists, concatenated value.



**Step 5: Refine into test values**

Since `parseField` is private, these strings are passed into the public `parseSingleEntry(String)` method to trigger the target logic during JUnit execution.



* **Test 1 (A1, B1, C1):** `@article{key, title={Software Testing}}`

    * **Expected Output:** The `title` field is cleanly parsed and set to `"Software Testing"`.

* **Test 2 (A2, B2, C2):** `@article{key, author="Smith", author="Doe"}`

    * **Expected Output:** The parser catches the duplicate `PERSON_NAMES` field and concatenates them, resulting in the `author` field equaling `"Smith and Doe"`.

* **Test 3 (A3, B1, C3):** `@article{key, keywords=2026}`

    * **Expected Output:** Parses the unquoted number into the keywords field successfully as `"2026"`.

* **Test 4 (A4, B2, C4):** `@article{key, bdsk-file-1={dummy}, bdsk-file-1=YmFzZTY0 # dGVzdA==}`

    * **Expected Output:** The parser enters the Base64 branch, concatenates the tokens via the `#` operator, and attempts to decode the resulting file attachment.



## Method 4 - `AuthorListParser.parse(String listOfNames)`

**Description**: This method takes a raw string representing an author or list of authors from a BibTeX file and parses it into an `AuthorList` object containing individual `Author` objects. It handles various string normalizations, specific separators (like "and", commas, and semicolons), special suffixes ("and others"), and complex name affixes ("jr", "von").

**Step 2: Identify all the parameters to the functions**
* The explicit parameter is the `String listOfNames`.
* The implicit parameters include the class constants used for parsing rules, such as `AVOID_TERMS_IN_LOWER_CASE`.

**Step 3: Model the input domain in terms of characteristics and partitions**
We will model this based on the control-flow branches present in the source code for separations and special cases.

* **Characteristic A: Author Separation Format**
    * *Block A1:* Authors are explicitly separated by the word `" and "`.
    * *Block A2:* Authors are separated purely by commas.
    * *Block A3:* Single author (no separators present).
* **Characteristic B: Presence of the "and others" Suffix**
    * *Block B1:* The string ends with `" and others"`.
    * *Block B2:* The string does not end with `" and others"`.
* **Characteristic C: Presence of Name Affixes (e.g., jr, sr, von)**
    * *Block C1:* The string contains a recognized affix from the `AVOID_TERMS_IN_LOWER_CASE` set (e.g., `"jr"`).
    * *Block C2:* The string contains standard names without special affixes.

**Step 4: Choose particular partitions and values from within those partitions**
Applying Each Choice Coverage (ECC) to minimize the test suite. Our largest characteristic (A) has 3 blocks, meaning we only need 3 tests to ensure every single partition is executed at least once.

* **Test 1 (A1, B1, C1):** Separated by "and", contains "and others", contains an affix.
* **Test 2 (A2, B2, C2):** Separated by commas, no "and others", no affix.
* **Test 3 (A3, B2, C2):** Single author, no "and others", no affix.

**Step 5: Refine into test values**
These combinations are refined into concrete Java strings to feed into the `parse()` method.

* **Test 1 (A1, B1, C1):** * *Input:* `"Smith, jr, John and Doe, Jane and others"`
    * *Expected Output:* Triggers the affix logic, splits by "and", and appends the `Author.OTHERS` object. Returns an `AuthorList` with 3 items (Smith, Doe, and OTHERS).
* **Test 2 (A2, B2, C2):** * *Input:* `"Smith, John, Doe, Jane"`
    * *Expected Output:* Hits the comma-separation branch and successfully splits the string, returning an `AuthorList` with 2 distinct `Author` objects.
* **Test 3 (A3, B2, C2):** * *Input:* `"Smith, John"`
    * *Expected Output:* Returns an `AuthorList` containing 1 `Author` object with family name "Smith" and given name "John".
