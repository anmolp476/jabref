package org.jabref.logic.importer.fileformat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParserResult;
import org.jabref.model.database.BibDatabase;
import org.jabref.model.util.DummyFileUpdateMonitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class BibtexParserParseMethodTest {

    private ImportFormatPreferences importFormatPreferences;

    @BeforeEach
    public void setUp() {
        importFormatPreferences = mock(ImportFormatPreferences.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
    }

    @Test
    @DisplayName("Test 1 (A1, B1, C1): Valid file, standard entries, clean text")
    public void testParseStandardEntriesCleanText() throws IOException {
        String input = "@article{key1, author={Smith}}";
        Reader reader = new StringReader(input);
        BibtexParser parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());

        ParserResult result = parser.parse(reader);
        BibDatabase database = result.getDatabase();

        assertNotNull(database, "The database should be successfully initialized.");
        assertEquals(1, database.getEntryCount(), "The parser should extract exactly one standard entry.");
        assertTrue(database.getEntryByCitationKey("key1").isPresent(), "The specific entry 'key1' should be retrievable.");
    }

    @Test
    @DisplayName("Test 2 (A1, B2, C2): Valid file, special declarations, junk text present")
    public void testParseSpecialDeclarationsWithJunkText() throws IOException {
        String input = "Junk text before. @string{foo = {bar}} Junk text after.";
        Reader reader = new StringReader(input);
        BibtexParser parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());

        ParserResult result = parser.parse(reader);
        BibDatabase database = result.getDatabase();

        assertEquals(0, database.getEntryCount(), "Junk text should not be mistakenly parsed as a standard entry.");
        assertTrue(database.hasStringByName("foo"), "The parser should extract the string macro 'foo'.");
        assertFalse(result.hasWarnings(), "Junk text should be ignored silently without throwing exceptions.");
    }

    @Test
    @DisplayName("Test 3 (A1, B3, C1): Valid file, mixed declarations, clean text")
    public void testParseMixedDeclarationsCleanText() throws IOException {
        String input = "@string{foo = {bar}}\n@article{key1, author=foo}";
        Reader reader = new StringReader(input);
        BibtexParser parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());

        ParserResult result = parser.parse(reader);
        BibDatabase database = result.getDatabase();

        assertEquals(1, database.getEntryCount(), "The parser should extract exactly one standard entry.");
        assertTrue(database.hasStringByName("foo"), "The parser should also extract the string macro.");

        var entry = database.getEntryByCitationKey("key1").get();
        assertEquals("#foo#", entry.getField(org.jabref.model.entry.field.StandardField.AUTHOR).get(),
            "The author field should hold the macro reference formatted as '#foo#'.");
    }

    @Test
    @DisplayName("Test 4 (A2, N/A, N/A): Empty file")
    public void testParseEmptyFile() throws IOException {
        String input = "";
        Reader reader = new StringReader(input);
        BibtexParser parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());

        ParserResult result = parser.parse(reader);
        BibDatabase database = result.getDatabase();

        assertNotNull(database, "Parsing an empty file should return a valid, empty database, not null.");
        assertEquals(0, database.getEntryCount(), "An empty file must yield zero entries.");
        assertFalse(result.hasWarnings(), "An empty file is an acceptable state and should not throw exceptions.");
    }

}
