package org.jabref.model.search.query;

import org.jabref.model.search.query.SearchQuery;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumSet;
import java.util.Objects;
import org.jabref.model.search.SearchFlags;
import org.jabref.model.search.ThrowingErrorListener;
import org.jabref.search.SearchLexer;
import org.jabref.search.SearchParser;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SearchQuery_toString_6_0_Test {

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
}
