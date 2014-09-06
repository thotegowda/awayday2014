package com.twi.awayday2014.services.parse;

import com.twi.awayday2014.services.ParseDataService;

import java.util.List;

public interface ParseDataFetcher<T> {
    void checkDataOutdated();
    void fetchData();
    void invalidateAndFetchFreshData();
    boolean isDataFetched();
    List<T> getFetchedData();
    T getDataById(String id);
    void addListener(ParseDataListener parseDataListener);
    void removeListener(ParseDataListener parseDataListener);
}
