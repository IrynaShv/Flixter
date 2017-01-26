package com.shvydchenko.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.shvydchenko.flixter.adapters.MovieArrayAdapter;
import com.shvydchenko.flixter.models.Movie;

import org.json.JSONArray;

import java.util.ArrayList;

public class MovieActivity extends YouTubeBaseActivity implements NetworkInterface {

    private ArrayList<Movie> movies;
    private MovieArrayAdapter movieArrayAdapter;
    private ListView lvItems;
    private SwipeRefreshLayout swipeContainer;
    private static Parcelable mListState;
    private static final String LIST_STATE = "listState";
    private NetworkManager networkManager;
    private String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkManager = new NetworkManager(this);
        setContentView(R.layout.activity_movie);
        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieArrayAdapter);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                networkManager.fetchAsyncData(url);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        networkManager.fetchAsyncData(url);

        lvItems.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Movie movie = movies.get(position);
                if (movie.getViewType() == Movie.ViewType.POPULAR) {
                    if (movie.getVideoSource() != null) {
                        handleMovieVideoCallback(movie.getVideoSource(), position, true);
                    } else {
                        networkManager.fetchMovieVideoAsyncData(movie.getVideoApiCallUrl(), position, false);
                    }
                }
            }
        });
        lvItems.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long arg3){
                Movie movie = movies.get(position);
                if (movie.getVideoSource() != null) {
                    handleMovieVideoCallback(movie.getVideoSource(), position, true);
                } else {
                    networkManager.fetchMovieVideoAsyncData(movie.getVideoApiCallUrl(), position, true);
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = lvItems.onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    public void handleMovieDataCallback(JSONArray movieJsonResults) {
        movieArrayAdapter.clear();
        movies.addAll(Movie.fromJsonArray(movieJsonResults));

        swipeContainer.setRefreshing(false);
        movieArrayAdapter.notifyDataSetChanged();

        if (mListState != null) {
            lvItems.onRestoreInstanceState(mListState);
            mListState = null;
        }
    }
    public void handleMovieVideoCallback(String videoSource, int position, Boolean loadDetails){
       try {
           Movie movie = movies.get(position);
           if (movie.getVideoSource() == null || movie.getVideoSource() != videoSource) {
               movies.get(position).setVideoSource(videoSource);
           }
           if (loadDetails) {
               Intent intent = new Intent(MovieActivity.this, DetailsActivity.class);
               intent.putExtra("title", movie.getOriginalTitle());
               intent.putExtra("backdrop_path", movie.getBackdropPath());
               intent.putExtra("overview", movie.getOverview());
               intent.putExtra("vote_average", movie.getVoteAverage());
               intent.putExtra("release_date", movie.getReleaseDate());
               intent.putExtra("video_source", movie.getVideoSource());
               startActivity(intent);
           } else {
               Intent intent = new Intent(MovieActivity.this, FullScreenYouTubeActivity.class);
               intent.putExtra("video_source", movie.getVideoSource());
               startActivity(intent);
           }
       }catch (Exception e) {
           e.printStackTrace();
       }
    }
//
//    public void addVideosToMovies() {
//        for (int i = 0; i < movies.size(); i++) {
//            if(movieArrayAdapter.getItem(i).getVideoSource() == null) {
//                networkManager.fetchMovieVideoAsyncData(movies.get(i).getVideoApiCallUrl(), i, false);
//            }
//        }
//    }
}
