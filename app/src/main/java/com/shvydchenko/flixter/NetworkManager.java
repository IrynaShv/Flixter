package com.shvydchenko.flixter;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shvydchenko on 1/25/17.
 */

public class NetworkManager {

    private NetworkInterface networkInterface;
    private OkHttpClient okHttpClient;
    private Handler mHandler;

    public NetworkManager(NetworkInterface networkInterface) {
        okHttpClient = new OkHttpClient();
        this.networkInterface = networkInterface;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchAsyncData(String url) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    try {
                        final String responseData = response.body().string();
                        final JSONArray movieJsonResults = new JSONObject(responseData).getJSONArray("results");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                networkInterface.handleMovieDataCallback(movieJsonResults);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void fetchMovieVideoAsyncData(String url, final int position, final Boolean loadDetails) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    try {
                        final String responseData = response.body().string();
                        final JSONArray movieJsonResults = new JSONObject(responseData).getJSONArray("youtube");
                        final String videoSource = movieJsonResults.getJSONObject(0).getString("source");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (videoSource != null) {
                                    networkInterface.handleMovieVideoCallback(videoSource, position, loadDetails);
                                } else {
                                    networkInterface.handleMovieVideoCallback(null, position, loadDetails);
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}