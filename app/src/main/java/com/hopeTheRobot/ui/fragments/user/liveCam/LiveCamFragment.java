package com.hopeTheRobot.ui.fragments.user.liveCam;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

public class LiveCamFragment extends Fragment {

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
