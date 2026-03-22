package org.jabref.model.search.query;

import org.jabref.model.entry.BibEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchResultsTest {

    private SearchResults searchResults;
    private BibEntry      bibEntryMatched;
    private BibEntry      bibEntryNotMatched;

    @BeforeEach
    public void setUp() {
        searchResults      = new SearchResults();
        bibEntryMatched    = new BibEntry();
        bibEntryNotMatched = new BibEntry();
    }

    /* Method under test: SearchResults.isMatched()
     *   The predicate (P) is searchResults.containsKey(entry.getId())
     *        Clause 1 (C1) is containsKey(entry.getId())
     *
     *        Test 1: C1 shall determine P equals true  (entry exists)
     *        Test 2: C1 shall determine P equals false (entry does not exist)
     */
    @Test
    public void testIsMatchedTrueEntryExists() {
        /* C1 true, P true */
        searchResults.addSearchResult(bibEntryMatched.getId(), new SearchResult());
        assertTrue(searchResults.isMatched(bibEntryMatched));
    }

    @Test
    public void testIsMatchedFalseEntryDNE() {
        /* C1 false, P false */
        assertFalse(searchResults.isMatched(bibEntryNotMatched));
    }

    /* Method under test: SearchResults.hasFullTextResults()
     *    The predicate (P1) is searchResults.containsKey(entry.getId())
     *    The predicate (P2) is anyMatch(SearchResults::hasFullTextResults)
     *         Clause 1 (C1) is containsKey(entry.getId()) (entry exists)
     *         Clause 2 (C2) is anyMatch(SearchResults::hasFullTextResults)
     *
     *         Test 1 and 2: C1 shall determine P, while C2 is fixed (entry DNE causes C2 to skip)
     *         Test 2 and 3: C2 shall determine P, while C1 is fixed
     */
    @Test
    public void testHasFulltextResultsFalseEntryDNE() {
        /* C1 is false, C2 is false (skipped), P1 is false, P2 is false (skipped) */
        assertFalse(searchResults.hasFulltextResults(bibEntryNotMatched));
    }

    @Test
    public void testHasFulltextResultsFalseEntryExists() {
        /* C1 is true, C2 is false, P1 is true, P2 is false */
        searchResults.addSearchResult(bibEntryMatched.getId(), new SearchResult());
        assertFalse(searchResults.hasFulltextResults(bibEntryMatched));
    }

    @Test
    public void testHasFulltextResultsTrueEntryExists() {
        /* C1 is true, C2 is true, P1 is true, P2 is true */

        /* Create a mock result to force hasFulltextResults() == true */
        SearchResult mockResult = mock(SearchResult.class);
        when(mockResult.hasFulltextResults()).thenReturn(true);

        searchResults.addSearchResult(bibEntryMatched.getId(), mockResult);
        assertTrue(searchResults.hasFulltextResults(bibEntryMatched));
    }
}
