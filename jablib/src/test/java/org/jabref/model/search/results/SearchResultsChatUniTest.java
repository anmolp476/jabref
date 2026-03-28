package org.jabref.model.search.query;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.types.StandardEntryType;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchResultsChatUniTest {

    // --- isMatched tests ---

    @Test
    void testIsMatchedReturnsTrueWhenEntryExists() {
        // Arrange
        SearchResults searchResults = new SearchResults();
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        SearchResult mockResult = mock(SearchResult.class);
        searchResults.addSearchResult(entry.getId(), mockResult);
        // Act
        boolean result = searchResults.isMatched(entry);
        // Assert
        assertTrue(result);
    }

    @Test
    void testIsMatchedReturnsFalseWhenEntryDoesNotExist() {
        // Arrange
        SearchResults searchResults = new SearchResults();
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        // Act
        boolean result = searchResults.isMatched(entry);
        // Assert
        assertFalse(result);
    }

    // --- hasFulltextResults tests ---

    @Test
    void testHasFulltextResultsReturnsTrueWhenFulltextExists() {
        // Arrange
        SearchResults searchResults = new SearchResults();
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        SearchResult mockResult = mock(SearchResult.class);
        when(mockResult.hasFulltextResults()).thenReturn(true);
        searchResults.addSearchResult(entry.getId(), mockResult);
        // Act
        boolean result = searchResults.hasFulltextResults(entry);
        // Assert
        assertTrue(result);
    }

    @Test
    void testHasFulltextResultsReturnsFalseWhenEntryNotMatched() {
        // Arrange
        SearchResults searchResults = new SearchResults();
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        // Act
        boolean result = searchResults.hasFulltextResults(entry);
        // Assert
        assertFalse(result);
    }
}
