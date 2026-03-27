package org.jabref.logic.importer.fileformat;

import java.util.Optional;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.util.DummyFileUpdateMonitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class BibtexParserParseEntryMethodTest {

    private ImportFormatPreferences importFormatPreferences;
    private BibtexParser parser;

    @BeforeEach
    public void setUp() {
        importFormatPreferences = mock(ImportFormatPreferences.class, RETURNS_DEEP_STUBS);
        parser = new BibtexParser(importFormatPreferences, new DummyFileUpdateMonitor());
    }

    @Test
    @DisplayName("Test 1 (A1, B1, C1, D1): Standard type, braced, key present, multiple fields")
    public void testParseEntryStandardBracedWithKeyAndFields() throws ParseException {
        String input = "@article{smith2026, author={Smith}, year=2026}";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        assertEquals("article", entry.getType().getName().toLowerCase(), "Entry type should be article.");
        assertEquals("smith2026", entry.getCitationKey().orElse(""), "Citation key should be smith2026.");

        assertTrue(entry.hasField(StandardField.AUTHOR), "The entry should contain an author field.");
        assertTrue(entry.hasField(StandardField.YEAR), "The entry should contain a year field.");
        assertEquals("Smith", entry.getField(StandardField.AUTHOR).get(), "Author field should be Smith.");
    }

    @Test
    @DisplayName("Test 2 (A2, B2, C2, D2): Custom type, parenthesized, no key, single field")
    public void testParseEntryCustomParenthesizedNoKeySingleField() throws ParseException {
        String input = "@software(, title={JabRef})";

        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);
        assertTrue(optEntry.isPresent(), "The entry should be parsed successfully.");
        BibEntry entry = optEntry.get();

        assertEquals("software", entry.getType().getName().toLowerCase(), "Entry type should map to software.");
        assertTrue(entry.getCitationKey().isEmpty(), "Citation key should be empty/absent.");

        assertTrue(entry.hasField(StandardField.TITLE), "The entry should contain a title field.");
        assertEquals("JabRef", entry.getField(StandardField.TITLE).get(), "Title field should be JabRef.");
    }

    @Test
    @DisplayName("Test 3 (A1, B3, C1, D3): Standard type, invalid bracket, key present, zero fields")
    public void testParseEntryInvalidBracketZeroFields() throws ParseException {
        String input = "@article smith2026}";

        // Call the parser normally
        Optional<BibEntry> optEntry = parser.parseSingleEntry(input);

        // Assert that JabRef swallowed the error and dropped the entry
        assertTrue(optEntry.isEmpty(), "Missing opening bracket should cause the parser to drop the entry and return empty");
    }
}
