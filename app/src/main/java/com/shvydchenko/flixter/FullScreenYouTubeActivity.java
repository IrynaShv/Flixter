package com.shvydchenko.flixter;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class FullScreenYouTubeActivity extends YouTubeBaseActivity {

    private String YOUTUBE_API_KEY = "AIzaSyABoZ1PBsQIYdAyxGQcBoaboKOgPxKnrs0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_you_tube);
        final String videoPath = getIntent().getStringExtra("video_source");

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.movie_video);

        if (videoPath != null){
            youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(videoPath);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });
        }

    }

}
