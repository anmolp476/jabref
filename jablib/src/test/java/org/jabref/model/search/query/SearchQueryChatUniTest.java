package org.jabref.model.search.query;

import org.jabref.model.search.SearchFlags;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class SearchQueryChatUniTest {

    @Test
    void testToString() {
        // Arrange
        String searchExpression = "author:John Doe";
        SearchQuery query = new SearchQuery(searchExpression);
        // Act
        String result = query.toString();
        // Assert
        assertEquals("author:John Doe", result);
    }

    // --- isValid tests ---

    @Test
    void testIsValidReturnsTrueForValidExpression() {
        // Arrange
        SearchQuery query = new SearchQuery("author:John Doe");
        // Act
        boolean result = query.isValid();
        // Assert
        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalseForInvalidExpression() {
        // Arrange
        SearchQuery query = new SearchQuery("AND == !");
        // Act
        boolean result = query.isValid();
        // Assert
        assertFalse(result);
    }

    // --- equals tests ---

    @Test
    void testEqualsReturnsTrueForSameExpressionAndFlags() {
        // Arrange
        SearchQuery query1 = new SearchQuery("author:John Doe");
        SearchQuery query2 = new SearchQuery("author:John Doe");
        // Act & Assert
        assertEquals(query1, query2);
    }

    @Test
    void testEqualsReturnsFalseForDifferentExpression() {
        // Arrange
        SearchQuery query1 = new SearchQuery("author:John Doe");
        SearchQuery query2 = new SearchQuery("author:Jane Doe");
        // Act & Assert
        assertNotEquals(query1, query2);
    }

    @Test
    void testEqualsReturnsFalseForDifferentFlags() {
        // Arrange
        SearchQuery query1 = new SearchQuery("author:John Doe", EnumSet.noneOf(SearchFlags.class));
        SearchQuery query2 = new SearchQuery("author:John Doe", EnumSet.of(SearchFlags.REGULAR_EXPRESSION));
        // Act & Assert
        assertNotEquals(query1, query2);
    }

    @Test
    void testEqualsReturnsFalseForNonSearchQueryObject() {
        // Arrange
        SearchQuery query = new SearchQuery("author:John Doe");
        // Act & Assert
        assertNotEquals("author:John Doe", query);
    }

    @Test
    void testEqualsReturnsTrueForSameObject() {
        // Arrange
        SearchQuery query = new SearchQuery("author:John Doe");
        // Act & Assert
        assertEquals(query, query);
    }
}
