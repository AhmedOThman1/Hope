package com.hopeTheRobot.ui.fragments.user.liveCam;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

public class LiveCamFragment extends Fragment {

    ProgressBar exo_progress_bar;
    PlayerView live_video;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_cam, container, false);

        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_live_cam);
        if (UserMainFragment.userToolbar.getVisibility() != View.VISIBLE)
            UserMainFragment.userToolbar.setVisibility(View.VISIBLE);
        UserMainFragment.userToolbar.setTitle(requireContext().getString(R.string.live_cam));


        live_video = view.findViewById(R.id.live_video);
        exo_progress_bar = view.findViewById(R.id.exo_progress_bar);
        ImageView live_img = view.findViewById(R.id.live_img);
// setup animation
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                live_img,
                PropertyValuesHolder.ofFloat("scaleX", 0.85f),
                PropertyValuesHolder.ofFloat("scaleY", 0.85f)
        );
        objectAnimator.setDuration(700);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        String liveUrl = "http://192.168.1.3:5000/";


        if (URLUtil.isValidUrl(liveUrl)) {

           SimpleExoPlayer exoPlayersVideo = ExoPlayerFactory.newSimpleInstance(requireContext(), new DefaultTrackSelector(
                    requireContext(),
                    new AdaptiveTrackSelection.Factory()
            ), new DefaultLoadControl());

            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(liveUrl),
                    new DefaultHttpDataSourceFactory("exoplayer_video"),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            exoPlayersVideo.setMediaSource(mediaSource);
            live_video.setPlayer(exoPlayersVideo);
            live_video.setKeepScreenOn(true);
            exoPlayersVideo.prepare();
            exoPlayersVideo.setPlayWhenReady(false);

            exoPlayersVideo.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_BUFFERING)
                        exo_progress_bar.setVisibility(View.VISIBLE);
                    else if (playbackState == Player.STATE_READY)
                        exo_progress_bar.setVisibility(View.GONE);
                }
            });
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).popBackStack(R.id.userHomeFragment,false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
