package com.shvydchenko.flixter;

import org.json.JSONArray;

/**
 * Created by shvydchenko on 1/25/17.
 */

public interface NetworkInterface {
    void handleMovieDataCallback(JSONArray movieArrayData);
    void handleMovieVideoCallback(String videoSource, int position, Boolean loadDetails);
}
