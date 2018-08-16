package com.udacity.bakingtime.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingtime.R;
import com.udacity.bakingtime.model.Step;

/**
 *
 */
public class StepDetailFragment extends Fragment {

    private static final String TAG = "StepDetailFragment";
    private static final String STEP = "step";
    private static final String POSITION = "position";
    private static final String AUTOPLAY = "autoplay";

    private boolean fullScreen;
    private Step step;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private long position;
    private boolean autoPlay;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepDetailFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP);
            position = savedInstanceState.getLong(POSITION);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY);
        } else {
            position = 0;
            autoPlay = true;
        }

        View rootView;
        if (fullScreen) {
            rootView = inflater.inflate(R.layout.fragment_recipe_step_fullscreen, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        }

        TextView descriptionView = rootView.findViewById(R.id.recipe_step_description);
        if (descriptionView != null) {
            descriptionView.setText(step.description);
        }

        playerView = rootView.findViewById(R.id.player_view);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initializePlayer();
        }
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        if (TextUtils.isEmpty(step.videoURL)) {
            playerView.setVisibility(View.GONE);
        } else if (exoPlayer == null) {
            Uri mediaUri = Uri.parse(step.videoURL);
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            //exoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.seekTo(position);
            exoPlayer.setPlayWhenReady(autoPlay);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            if (exoPlayer.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                // avoid seek to end of stream
                this.position = 0;
                this.autoPlay = false;
            } else {
                this.position = exoPlayer.getCurrentPosition();
                this.autoPlay = exoPlayer.getPlayWhenReady();
            }
        }
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(STEP, step);
        currentState.putLong(POSITION, position);
        currentState.putBoolean(AUTOPLAY, autoPlay);
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

}
