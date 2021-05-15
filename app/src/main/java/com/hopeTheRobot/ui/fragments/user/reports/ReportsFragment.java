package com.hopeTheRobot.ui.fragments.user.reports;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.activities.MainActivity;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

public class ReportsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_reports);
        if (UserMainFragment.userToolbar.getVisibility() != View.VISIBLE)
            UserMainFragment.userToolbar.setVisibility(View.VISIBLE);
        UserMainFragment.userToolbar.setTitle(requireContext().getString(R.string.reports));

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
