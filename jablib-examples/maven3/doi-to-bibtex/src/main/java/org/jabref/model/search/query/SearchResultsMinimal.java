package org.jabref.model.search.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jabref.model.entry.BibEntry;

public class SearchResultsMinimal {

    private final Map<String, List<Object>> searchResults = new ConcurrentHashMap<>();

    public void addSingleResult(String entryId, Object result) {
        searchResults.computeIfAbsent(entryId, k -> new ArrayList<>()).add(result);
    }

    public boolean isMatched(BibEntry entry) {
        if (entry == null || entry.getId() == null) {
            return false;
        }
        return searchResults.containsKey(entry.getId());
    }

    public int getMatchCount() {
        return searchResults.size();
    }
}