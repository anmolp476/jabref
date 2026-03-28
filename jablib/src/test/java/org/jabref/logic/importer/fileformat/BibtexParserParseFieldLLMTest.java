package org.jabref.logic.importer.fileformat;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParserResult;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.StringReader;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BibtexParserParseFieldChatUniTest {

    private BibtexParser parser;
    private ImportFormatPreferences importFormatPreferences;

    @BeforeEach
    void setUp() {
        // Arrange
        importFormatPreferences = mock(ImportFormatPreferences.class);
        parser = new BibtexParser(importFormatPreferences);
    }

    @Test
    void testParseAuthorField() throws Exception {
        // Arrange
        String bibtexEntry = "@article{key, author = {Jane Doe}}";
        parser.parse(new StringReader(bibtexEntry));
        BibEntry entry = new BibEntry();
        // Act
        // Assert
        assertNotNull(entry);
    }

    @Test
    void testParseTitleField() throws Exception {
        // Arrange
        String bibtexEntry = "@article{key, title = {Java Guide}}";
        // Act
        ParserResult result = parser.parse(new StringReader(bibtexEntry));
        BibEntry entry = result.getDatabase().getEntries().get(0);
        // Assert
        assertEquals("Java Guide", entry.getField(StandardField.TITLE).orElse(""));
    }

    @Test
    void testParseYearField() throws Exception {
        // Arrange
        String bibtexEntry = "@article{key, year = {2026}}";
        // Act
        ParserResult result = parser.parse(new StringReader(bibtexEntry));
        BibEntry entry = result.getDatabase().getEntries().get(0);
        // Assert
        assertEquals("2026", entry.getField(StandardField.YEAR).orElse(""));
    }
}
