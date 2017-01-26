package com.shvydchenko.flixter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by shvydchenko on 1/25/17.
 */

public class NetworkManager {

    private AsyncHttpClient client;
    private NetworkInterface networkInterface;

    public NetworkManager(NetworkInterface networkInterface) {
        client = new AsyncHttpClient();
        this.networkInterface = networkInterface;
    }

    void fetchAsyncData(String url) {
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    networkInterface.handleMovieDataCallback(movieJsonResults);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, t, errorResponse);
            }
        });
    }
    void fetchMovieVideoAsyncData(String url, final int position, final Boolean loadDetails) {
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    if (response != null) {
                        movieJsonResults = response.getJSONArray("youtube");
                        JSONObject videoJson = movieJsonResults.getJSONObject(0);
                        networkInterface.handleMovieVideoCallback(videoJson.getString("source"), position, loadDetails);
                    } else {
                        networkInterface.handleMovieVideoCallback(null, position, loadDetails);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, t, errorResponse);
            }
        });
    }


}
