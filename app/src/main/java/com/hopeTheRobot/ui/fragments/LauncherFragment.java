package com.hopeTheRobot.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.activities.MainActivity;

public class LauncherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);
        new Handler().postDelayed(() -> {
            if (sharedPreferences.getBoolean(MainActivity.showIntro, true))
                Navigation.findNavController(view).navigate(R.id.action_launcherFragment_to_introFragment);
//            else if (sharedPreferences.getBoolean(MainActivity.adminLoggedIn, false))
//                Navigation.findNavController(view).navigate(R.id.action_launcherFragment_to_adminMainFragment);
            else if (sharedPreferences.getBoolean(MainActivity.userLoggedIn, false) && sharedPreferences.getBoolean(MainActivity.profileNotCompleted, true)) {
                Toast.makeText(requireContext(), "Please complete profile first!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_launcherFragment_to_completeProfileFragment);
            } else if (sharedPreferences.getBoolean(MainActivity.userLoggedIn, false))
                Navigation.findNavController(view).navigate(R.id.action_launcherFragment_to_userMainFragment);
            else
                Navigation.findNavController(view).navigate(R.id.action_launcherFragment_to_connectToRobotFragment);

        }, 4000);

        return view;
    }

}
