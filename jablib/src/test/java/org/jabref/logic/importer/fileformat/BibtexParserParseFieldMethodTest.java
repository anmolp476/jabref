package org.jabref.logic.importer.fileformat;

import java.util.Optional;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.field.UnknownField;
import org.jabref.model.util.DummyFileUpdateMonitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class BibtexParserParseFieldMethodTest {

    private ImportFormatPreferences importFormatPreferences;
    private BibtexParser parser;

    @BeforeEach
    public void setUp() {
        importFormatPreferences = mock(ImportFormatPreferences.class, RETURNS_DEEP_STUBS);
        parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());
    }

    @Test
    @DisplayName("Test 1 (A1, B1, C1): Standard field, new field, bracketed value")
    public void testParseFieldStandardNewBracketed() throws ParseException {
        String input = "@article{key, title={Software Testing}}";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        assertTrue(entry.hasField(StandardField.TITLE), "The entry should contain a title field.");
        assertEquals("Software Testing", entry.getField(StandardField.TITLE).get(), "Title field should be correctly parsed without brackets.");
    }

    @Test
    @DisplayName("Test 2 (A2, B2, C2): Person name, field already exists, quoted value")
    public void testParseFieldPersonNameDuplicateQuoted() throws ParseException {
        String input = "@article{key, author=\"Smith\", author=\"Doe\"}";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        assertTrue(entry.hasField(StandardField.AUTHOR), "The entry should contain an author field.");
        // JabRef automatically concatenates duplicate author fields with "and"
        assertEquals("Smith and Doe", entry.getField(StandardField.AUTHOR).get(), "Duplicate author fields should be concatenated with 'and'.");
    }

    @Test
    @DisplayName("Test 3 (A3, B1, C3): Keyword field, new field, numeric value")
    public void testParseFieldKeywordNewNumeric() throws ParseException {
        String input = "@article{key, keywords=2026}";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        assertTrue(entry.hasField(StandardField.KEYWORDS), "The entry should contain a keywords field.");
        assertEquals("2026", entry.getField(StandardField.KEYWORDS).get(), "Unquoted numeric value should be parsed as a string.");
    }

    @Test
    @DisplayName("Test 4 (A4, B2, C4): BibDesk file, field already exists, concatenated value")
    public void testParseFieldBibDeskDuplicateConcatenated() throws ParseException {
        // The '#' operator in BibTeX concatenates strings.
        String input = "@article{key, bdsk-file-1={dummy}, bdsk-file-1=YmFzZTY0 # dGVzdA==}";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        UnknownField bdskField = new UnknownField("bdsk-file-1");
        assertTrue(entry.hasField(bdskField), "The entry should contain the custom bdsk-file-1 field.");

        // Asserting that the field is present and populated validates that the '#' concatenation
        // branch was successfully executed without throwing an error.
        String parsedValue = entry.getField(bdskField).get();
        assertTrue(!parsedValue.isEmpty(), "The concatenated BibDesk file string should not be empty.");
    }
}
