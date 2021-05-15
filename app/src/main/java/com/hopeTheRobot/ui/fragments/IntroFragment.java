package com.hopeTheRobot.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hopeTheRobot.R;
import com.hopeTheRobot.adapters.IntroPagerAdapter;
import com.hopeTheRobot.pojo.IntroItem;
import com.hopeTheRobot.ui.activities.MainActivity;

import java.util.ArrayList;

public class IntroFragment extends Fragment {

    TabLayout intro_tab_layout;
    ViewPager introViewPager;
    TextView get_started, skip;
    int position = 0;

    SharedPreferences sharedPreferences;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_intro, container, false);

        intro_tab_layout = view.findViewById(R.id.intro_tab_layout);
        introViewPager = view.findViewById(R.id.introViewPager);
        skip = view.findViewById(R.id.skip);
        get_started = view.findViewById(R.id.get_started);

        ArrayList<IntroItem> introItems = new ArrayList<>();
        introItems.add(new IntroItem(R.drawable.intro_img1, requireActivity().getResources().getStringArray(R.array.intro_titles)[0], requireActivity().getResources().getStringArray(R.array.intro_bodies)[0]));
        introItems.add(new IntroItem(R.drawable.intro_img2, requireActivity().getResources().getStringArray(R.array.intro_titles)[1], requireActivity().getResources().getStringArray(R.array.intro_bodies)[1]));
        introItems.add(new IntroItem(R.drawable.intro_img3, requireActivity().getResources().getStringArray(R.array.intro_titles)[2], requireActivity().getResources().getStringArray(R.array.intro_bodies)[2]));
        introItems.add(new IntroItem(R.drawable.intro_img4, requireActivity().getResources().getStringArray(R.array.intro_titles)[3], requireActivity().getResources().getStringArray(R.array.intro_bodies)[3]));


        IntroPagerAdapter introPagerAdapter = new IntroPagerAdapter(requireContext());
        introPagerAdapter.setIntroItems(introItems);
        introViewPager.setAdapter(introPagerAdapter);
        intro_tab_layout.setupWithViewPager(introViewPager);

        intro_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == introItems.size() - 1) {
                    skip.setVisibility(View.INVISIBLE);
                    get_started.setVisibility(View.VISIBLE);
                    get_started.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.intro_button_animation));
                } else {
                    skip.setVisibility(View.VISIBLE);
                    get_started.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sharedPreferences = requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);

        skip.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean(MainActivity.showIntro, false).apply();
            goToNext();
        });

        get_started.setOnClickListener(v -> goToNext());

        return view;
    }

    private void goToNext() {
//        if (sharedPreferences.getBoolean(MainActivity.adminLoggedIn, false))
//            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_connectToRobotFragment);
//        else
        if (sharedPreferences.getBoolean(MainActivity.userLoggedIn, false) && sharedPreferences.getBoolean(MainActivity.profileNotCompleted, true)) {
            Toast.makeText(requireContext(), "Please complete profile first!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_completeProfileFragment);
        } else if (sharedPreferences.getBoolean(MainActivity.userLoggedIn, false))
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_userMainFragment);
        else
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_connectToRobotFragment);
    }

}
