package com.shvydchenko.flixter;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class DetailsActivity extends YouTubeBaseActivity {

    private String YOUTUBE_API_KEY = "AIzaSyABoZ1PBsQIYdAyxGQcBoaboKOgPxKnrs0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        loadData();
    }

    private void loadData()
    {
        String title = getIntent().getStringExtra("title");
        final String videoPath = getIntent().getStringExtra("video_source");
        String overview = getIntent().getStringExtra("overview");
        String releaseDate = getIntent().getStringExtra("release_date");
        float voteAverage = (float) getIntent().getDoubleExtra("vote_average",0);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.details_movie_video);
        TextView tvTitle = (TextView) findViewById(R.id.details_movie_title);
        TextView tvOverview = (TextView) findViewById(R.id.details_movie_overview);
        TextView tvReleaseDate = (TextView) findViewById(R.id.details_release_date);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.details_rating_bar);

        if (videoPath != null){
            youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoPath);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
        }

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvReleaseDate.append(releaseDate);
        ratingBar.setRating(voteAverage);
    }

}
