package net.crescenthikari.bakingapp.features.step;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import net.crescenthikari.bakingapp.R;
import net.crescenthikari.bakingapp.data.model.Recipe;
import net.crescenthikari.bakingapp.data.model.StepsItem;
import net.crescenthikari.bakingapp.data.repository.RecipeRepository;
import net.crescenthikari.bakingapp.features.step.model.step.RecipeStepListData;
import net.crescenthikari.bakingapp.features.step.model.step.StepData;
import net.crescenthikari.bakingapp.utils.ActivityUtils;
import net.crescenthikari.bakingapp.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String ARG_STEP_ID = "step_id";
    public static final String KEY_CURRENT_WINDOW_IDX = "KEY_CURRENT_WINDOW_IDX";
    public static final String KEY_PLAY_WHEN_READY = "KEY_PLAY_WHEN_READY";
    public static final String KEY_PLAYBACK_POSITION = "KEY_PLAYBACK_POSITION";
    private static final String TAG = "RecipeStepDetailFragmen";
    @BindView(R.id.recipe_detail_video_player)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.recipe_detail_image_view)
    ImageView recipeImage;

    @BindView(R.id.recipe_detail_full_descriptions)
    TextView recipeFullDescriptionField;

    @Inject
    RecipeRepository recipeRepository;

    private ViewGroup container;
    private long recipeId;
    private long stepId;
    private Unbinder unbinder;
    private SimpleExoPlayer player;
    private AppBarLayout appBarLayout;
    private int currentWindowIndex;
    private boolean playWhenReady = true;
    private long playbackPosition;
    private boolean isTwoPane;
    private MediaSessionCompat mediaSession;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(long recipeId, long stepId) {
        RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putLong(ARG_STEP_ID, stepId);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeId = getArguments().getLong(ARG_RECIPE_ID, 0);
            stepId = getArguments().getLong(ARG_STEP_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.two_pane_mode);

        if (savedInstanceState != null) {
            currentWindowIndex = savedInstanceState.getInt(KEY_CURRENT_WINDOW_IDX);
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
        }

        Activity activity = this.getActivity();
        appBarLayout = activity.findViewById(R.id.app_bar);
        container = activity.findViewById(R.id.recipe_step_detail_container);
        getRecipeDetail();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CURRENT_WINDOW_IDX, currentWindowIndex);
        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
        outState.putLong(KEY_PLAYBACK_POSITION, playbackPosition);
        super.onSaveInstanceState(outState);
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                );
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                player.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                player.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void getRecipeDetail() {
        recipeRepository.getRecipe(recipeId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Recipe, ObservableSource<Map<String, RecipeStepListData>>>() {
                    @Override
                    public ObservableSource<Map<String, RecipeStepListData>> apply(@NonNull Recipe recipe) throws Exception {
                        Map<String, RecipeStepListData> data = new HashMap<>();
                        for (StepsItem item : recipe.getSteps()) {
                            data.put(String.valueOf(item.getId()), new StepData(item));
                        }
                        return Observable.just(data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, RecipeStepListData>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // do nothing
                    }

                    @Override
                    public void onNext(@NonNull Map<String, RecipeStepListData> recipeStepMap) {
                        StepData stepData = ((StepData) recipeStepMap.get(String.valueOf(stepId)));
                        if (stepData != null) {
                            if (stepData.haveVideo()) {
                                setupVideoPlayer(stepData);
                                recipeImage.setVisibility(View.GONE);
                            } else if (stepData.haveImage()) {
                                setupRecipeImage(stepData);
                                exoPlayerView.setVisibility(View.GONE);
                            } else {
                                exoPlayerView.setVisibility(View.GONE);
                                recipeImage.setVisibility(View.GONE);
                            }
                            recipeFullDescriptionField.setText(stepData.getStepFullDesc());
                        } else {
                            Log.d(TAG, "onNext: step data is null");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // do nothing
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }

    private void setupRecipeImage(StepData stepData) {
        recipeImage.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(stepData.getImageURL())
                .into(recipeImage);
    }

    private void setupVideoPlayer(StepData stepData) {
        final int orientation = getResources().getConfiguration().orientation;
        Log.d(TAG, "setupVideoPlayer: " + orientation);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {
            Log.d(TAG, "setupVideoPlayer: setup fullscreen");
            recipeFullDescriptionField.setVisibility(View.GONE);
            if (appBarLayout != null) {
                appBarLayout.setVisibility(View.GONE);
            }
            ViewUtils.changeHeightAndWidthToMatchParent(exoPlayerView);
            ActivityUtils.hideSystemUI(getActivity());
        } else {
            Log.d(TAG, "setupVideoPlayer: setup non-fullscreen");
            recipeFullDescriptionField.setVisibility(View.VISIBLE);
            if (appBarLayout != null) {
                appBarLayout.setVisibility(View.VISIBLE);
            }
        }
        exoPlayerView.setVisibility(View.VISIBLE);
        initializeMediaSession();
        setupExoPlayer();
        Uri mp4VideoUri = Uri.parse(stepData.getVideoURL());
        playVideo(mp4VideoUri);
    }

    private void playVideo(Uri mp4VideoUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this.getContext(),
                Util.getUserAgent(getContext(), "BakingApp"),
                bandwidthMeter
        );
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(
                mp4VideoUri,
                dataSourceFactory,
                extractorsFactory,
                null,
                null
        );
        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    private void setupExoPlayer() {
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector);

        // 3. Setup view player
        exoPlayerView.setPlayer(player);
        player.seekTo(playbackPosition);
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    private void releasePlayer() {
        if (player != null) {
            currentWindowIndex = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

}
