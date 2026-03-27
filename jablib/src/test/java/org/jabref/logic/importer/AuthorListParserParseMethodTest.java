package org.jabref.logic.importer;

import org.jabref.model.entry.Author;
import org.jabref.model.entry.AuthorList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorListParserParseMethodTest {

    @Test
    @DisplayName("Test 1 (A1, B1, C1): Separated by 'and', contains 'and others', contains an affix")
    public void testParseAndSeparationWithOthersAndAffix() {
        String input = "Smith, jr, John and Doe, Jane and others";
        AuthorListParser parser = new AuthorListParser();

        AuthorList result = parser.parse(input);

        assertEquals(3, result.getAuthors().size(), "Should parse 3 elements: 2 distinct authors and the OTHERS object.");

        Author firstAuthor = result.getAuthors().get(0);
        assertEquals("Smith", firstAuthor.getFamilyName().orElse(""), "First author family name should be Smith.");

        assertEquals("jr", firstAuthor.getNameSuffix().orElse(""), "First author jr/affix should be parsed as jr.");

        Author secondAuthor = result.getAuthors().get(1);
        assertEquals("Doe", secondAuthor.getFamilyName().orElse(""), "Second author family name should be Doe.");

        assertTrue(result.getAuthors().contains(Author.OTHERS), "The list should contain the Author.OTHERS object.");
    }

    @Test
    @DisplayName("Test 2 (A2, B2, C2): Separated by commas, no 'and others', no affix")
    public void testParseCommaSeparationNoOthersNoAffix() {
        String input = "Smith, John, Doe, Jane";
        AuthorListParser parser = new AuthorListParser();

        AuthorList result = parser.parse(input);

        assertEquals(2, result.getAuthors().size(), "Should parse exactly 2 authors due to comma separation.");

        assertEquals("Smith", result.getAuthors().get(0).getFamilyName().orElse(""), "First author family name should be Smith.");
        assertEquals("John", result.getAuthors().get(0).getGivenName().orElse(""), "First author given name should be John.");

        assertEquals("Doe", result.getAuthors().get(1).getFamilyName().orElse(""), "Second author family name should be Doe.");
        assertEquals("Jane", result.getAuthors().get(1).getGivenName().orElse(""), "Second author given name should be Jane.");
    }

    @Test
    @DisplayName("Test 3 (A3, B2, C2): Single author, no 'and others', no affix")
    public void testParseSingleAuthorNoOthersNoAffix() {
        String input = "Smith, John";
        AuthorListParser parser = new AuthorListParser();

        AuthorList result = parser.parse(input);

        assertEquals(1, result.getAuthors().size(), "Should parse exactly 1 author.");

        assertEquals("Smith", result.getAuthors().get(0).getFamilyName().orElse(""), "Author family name should be Smith.");
        assertEquals("John", result.getAuthors().get(0).getGivenName().orElse(""), "Author given name should be John.");
    }
}
