package org.jabref.model.search.query;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

import org.jabref.model.search.SearchFlags;
import static org.junit.jupiter.api.Assertions.*;

public class SearchQueryTest {
    /* Helper function to create a query with additional search flags */
    private SearchQuery makeQueryWithFlags(String query, EnumSet<SearchFlags> flags) {
        return new SearchQuery(query, flags);
    }

    /* Helper function to create a query without additional search flags */
    private SearchQuery makeQueryWithoutFlags(String query) {
        return new SearchQuery(query, EnumSet.noneOf(SearchFlags.class));
    }

    /* Method under test: SearchQuery.isValid()
     *   The predicate (P) is isValidExpression
     *          Clause 1 (C1) is query
     *
     *          Test 1. C1 shall determine P equals true
     *          Test 2. C2 shall determine P equals false
     */
    @Test
    public void testIsSimpleQueryWithoutFlagsValid() {
        /* C1 is true, P is true */
        SearchQuery query = makeQueryWithoutFlags("torvalds");
        assertTrue(query.isValid());
    }

    @Test
    public void testIsSimpleQueryEmptyValid() {
        /* C1 is false, P is false */
        SearchQuery query = makeQueryWithoutFlags("AND == !");
        assertFalse(query.isValid());
    }

    /* Method under test: SearchQuery.equals()
     *   The predicate (P) is ObjectsEquals(searchEx, that.searchEx) && Objects.equals(searchFl, that.searchFl)
     *        Clause 1 (C1) is Objects.equals(searchEx, that.searchEx)
     *        Clause 2 (C2) is Objects.equals(searchFl, that.searchFl)
     *
     *        Test 1 and 2. C1 shall determine the predicate, while C2 remains fixed.
     *        Test 3 and 4. C2 shall determine the predicate, while C1 remains fixed.
     */
    @Test public void testEqualsTrueClause2Fixed() {
        /* C1 is True, C2 is True, P is true */
        SearchQuery query1 = makeQueryWithoutFlags("torvalds");
        SearchQuery query2 = makeQueryWithoutFlags("torvalds");

        assertEquals(query1, query2);
    }

    @Test public void testEqualsFalseClause2Fixed() {
        /* C1 is false, C2 is false, P is false */
        SearchQuery query1 = makeQueryWithoutFlags("torvalds");
        SearchQuery query2 = makeQueryWithoutFlags("smith");

        assertNotEquals(query1, query2);
    }

    @Test
    public void testEqualsTrueClause1Fixed() {
        /* C1 is true, C2 is true, P is true */
        SearchQuery query1 = makeQueryWithoutFlags("torvalds");
        SearchQuery query2 = makeQueryWithoutFlags("torvalds");

        assertEquals(query1, query2);
    }

    @Test
    public void testEqualsFalseClause1Fixed() {
        /* C1 is true, C2 is false, P is true */
        SearchQuery query1 = makeQueryWithoutFlags("torvalds");
        SearchQuery query2 = makeQueryWithFlags("torvalds", EnumSet.of(SearchFlags.REGULAR_EXPRESSION));

        assertNotEquals(query1, query2);
    }
}
