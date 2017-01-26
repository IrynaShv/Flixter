package com.shvydchenko.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shvydchenko on 1/23/17.
 */

public class Movie {

    private String posterPath;
    private String originalTitle;
    private String overview;
    private String backdropPath;
    private Double voteAverage;
    private ViewType viewType;
    private String releaseDate;
    private String id;
    private String videoSource;

    public enum ViewType {
        POPULAR, UNPOPULAR
    }
    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.viewType = this.getViewType();
        this.id = jsonObject.getString("id");
        this.videoSource = null;
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s" , backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s" , posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Boolean isPopular() {
        return (voteAverage > 5.0);
    }

    public ViewType getViewType() {
        return isPopular() ? ViewType.POPULAR : ViewType.UNPOPULAR;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVideoApiCallUrl() {
        return String.format("https://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", id);
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String src) {
        this.videoSource = src;
    }
}

