package com.twi.awayday2014.services.parse;

import java.util.List;

public interface ParseDataListener<T>{
    void onDataValidation(boolean status);
    void onDataFetched(List<T> data);
    void onDataFetchError(int errorStatus);
    void fetchingFromNetwork();
    void fetchingFromCache();
    void onDataValidationError(int errorStatus);
    void dataIsOutdated();
}
