package com.twi.awayday2014.services;

import android.app.Application;
import android.util.Log;

import com.parse.*;
import com.twi.awayday2014.models.Feedback;
import com.twi.awayday2014.models.Theme;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.twi.awayday2014.utils.Constants.Parse.*;

public class ParseDataService {
    private static final String TAG = "ParseDataService";

    private static final String YOUR_APPLICATION_ID = "cRkqJtkVvjyuC5pvKRzLNz8CFm6WgrbPqX0uKX7a";
    private static final String YOUR_CLIENT_KEY = "o4Dr0m1oV8PBWw0DQSeaYmd9T3LSayKIPJCkbIxd";

    private List<ParseDataListener> parseDataListener;

    public ParseDataService(Application application) {
        setupParse(application);
        parseDataListener = new ArrayList<ParseDataListener>();
    }

    private void setupParse(Application application) {
        Parse.initialize(application, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void fetchThemeInBackground() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_THEME);
        query.whereEqualTo(COL_ACTIVE, true);
        Log.d(TAG, "Fetching theme in background");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject theme, ParseException e) {
                if (e == null) {
                    if (theme != null) {
                        String headerText = theme.getString(COL_HEADER_TEXT);
                        String mainText = theme.getString(COL_MAIN_TEXT);
                        String footerText = theme.getString(COL_FOOTER_TEXT);
                        JSONArray imagesArray = theme.getJSONArray(COL_IMAGES);
                        List<String> imageObjects = new ArrayList<String>();
                        try {
                            for (int i = 0; i < imagesArray.length(); i++) {
                                imageObjects.add(imagesArray.getString(i));
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        Log.d(TAG, "Theme: " +
                                "\nHeader: " + headerText +
                                "\nMainText: " + mainText +
                                "\nFooterText: " + footerText +
                                "\nImages: " + imageObjects.toString());


                        fetchImagesAndNotify(headerText, mainText, footerText, imageObjects);

                    } else {
                        Log.e(TAG, "No active theme found");
                        for (ParseDataListener dataListener : parseDataListener) {
                            dataListener.onThemeFetchedError(ERROR_NO_DATA_FOUND);
                        }
                    }

                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                    for (ParseDataListener dataListener : parseDataListener) {
                        dataListener.onThemeFetchedError(ERROR_EXCEPTION_THROWN);
                    }
                }
            }

            private void fetchImagesAndNotify(final String headerText, final String mainText, final String footerText, List<String> imageObjects) {
                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                for (String imageObject : imageObjects) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_IMAGES);
                    query.whereEqualTo(COL_OBJECT_ID, imageObject);
                    queries.add(query);
                }
                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                mainQuery.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> results, ParseException e) {
                        if (e == null) {
                            List<String> images = new ArrayList<String>();
                            for (ParseObject result : results) {
                                ParseFile parseFile = result.getParseFile(COL_IMAGE);
                                images.add(parseFile.getUrl());
                                Log.d(TAG, parseFile.getUrl());
                            }

                            for (ParseDataListener dataListener : parseDataListener) {
                                dataListener.onThemeFetched(new Theme(mainText, headerText,
                                        footerText, images));
                            }
                        }else{
                            Log.e(TAG, "Error: " + e.getMessage());
                            for (ParseDataListener dataListener : parseDataListener) {
                                dataListener.onThemeFetchedError(ERROR_EXCEPTION_THROWN);
                            }
                        }
                    }
                });
            }
        });
    }

    public void addListener(ParseDataListener listener){
        if(listener != null){
            parseDataListener.add(listener);
        }
    }

    public void removeListener(ParseDataListener listener){
        parseDataListener.remove(listener);
    }

    public boolean post(Feedback feedback) {
        ParseObject pObject = new ParseObject("Feedback");
        pObject.put("sessionId", feedback.getSessionId());
        pObject.put("sessionTitle", feedback.getSessionTitle());
        pObject.put("overallRating", feedback.getOverallRating());
        pObject.put("relevantContentRating", feedback.getRelevantContentRating());
        pObject.put("speakerQuality", feedback.getSpeakerQuality());
        pObject.put("anythingElse", feedback.getAnythingElse());
        pObject.saveInBackground();
        return true;
    }

    public interface ParseDataListener {
        void onThemeFetched(Theme theme);
        void onThemeFetchedError(int status);
    }
}
