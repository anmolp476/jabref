package org.jabref.logic.importer.fileformat;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.junit.jupiter.api.*;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BibtexParserParseFieldLLMTest {

    private BibtexParser parser;
    private PushbackReader pushbackReader;

    @BeforeEach
    void setUp() {
        // 1. Manually create the Spy (No @Spy tag needed)
        pushbackReader = spy(new PushbackReader(new StringReader("")));

        // 2. Manually create the parser (No @InjectMocks tag needed)
        // Note: If BibtexParser requires arguments here (like preferences),
        // this line will turn red in IntelliJ and tell you exactly what is missing! change so I can commitasdfadsfdsaf
        parser = new BibtexParser();
    }

    // --- parseField tests ---

    @Test
    void testParseAuthorField() throws Exception {
        // Arrange
        BibEntry entry = new BibEntry();
        doReturn((int)'a', (int)'u', (int)'t', (int)'h', (int)'o', (int)'r', (int)'=',
                (int)'{', (int)'J', (int)'a', (int)'n', (int)'e', (int)' ', (int)'D', (int)'o', (int)'e', (int)'}', (int)',', -1)
                .when(pushbackReader).read();

        // Act
        parser.parseField(entry);

        // Assert
        assertEquals("Jane Doe", entry.getField(StandardField.AUTHOR).orElse(""));
    }

    @Test
    void testParseTitleField() throws Exception {
        // Arrange
        BibEntry entry = new BibEntry();
        doReturn((int)'t', (int)'i', (int)'t', (int)'l', (int)'e', (int)'=',
                (int)'"', (int)'J', (int)'a', (int)'v', (int)'a', (int)' ', (int)'G', (int)'u', (int)'i', (int)'d', (int)'e', (int)'"', (int)',', -1)
                .when(pushbackReader).read();

        // Act
        parser.parseField(entry);

        // Assert
        assertEquals("Java Guide", entry.getField(StandardField.TITLE).orElse(""));
    }

    @Test
    void testParseYearField() throws Exception {
        // Arrange
        BibEntry entry = new BibEntry();
        doReturn((int)'y', (int)'e', (int)'a', (int)'r', (int)'=',
                (int)'2', (int)'0', (int)'2', (int)'6', (int)',', -1)
                .when(pushbackReader).read();

        // Act
        parser.parseField(entry);

        // Assert
        assertEquals("2026", entry.getField(StandardField.YEAR).orElse(""));
    }
}
