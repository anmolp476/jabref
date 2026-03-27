package org.jabref.model.search.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jabref.model.entry.BibEntry;


/* This file was created to isolate the SearchResults.java classes that will be targeted by 
   the CodeQwen LLM testing component of the lab. */
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