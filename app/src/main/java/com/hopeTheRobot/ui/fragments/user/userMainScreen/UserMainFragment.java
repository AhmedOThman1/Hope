package com.hopeTheRobot.ui.fragments.user.userMainScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.activities.MainActivity;

public class UserMainFragment extends Fragment {


    View view;
    public static DrawerLayout drawerLayout;
    public static Toolbar userToolbar;
    public static NavigationView navigation_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_main, container, false);
        //        hide action bar
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();

        drawerLayout = view.findViewById(R.id.drawer_layout);
        userToolbar = view.findViewById(R.id.user_toolbar);
        navigation_view = view.findViewById(R.id.nav_view);


        navigation_view.setNavigationItemSelectedListener(item -> {
            int currentFragmentId = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).getCurrentDestination().getId();
            switch (item.getItemId()) {

                case R.id.nav_home:
                    userToolbar.setVisibility(View.GONE);
                    if (currentFragmentId != R.id.userHomeFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.userHomeFragment);
                    break;

                case R.id.nav_swab_requests:
                    if (currentFragmentId != R.id.swabRequestsFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.swabRequestsFragment);
                    break;

                case R.id.nav_high_temp:
                    if (currentFragmentId != R.id.highTempFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.highTempFragment);
                    break;

                case R.id.nav_control_robot:
                    if (currentFragmentId != R.id.controlRobotFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.controlRobotFragment);
                    break;

                case R.id.nav_live_cam:
                    userToolbar.setVisibility(View.GONE);
                    if (currentFragmentId != R.id.liveCamFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.liveCamFragment);
                    break;

                case R.id.nav_reports:
                    if (currentFragmentId != R.id.reportsFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.reportsFragment);
                    break;

                case R.id.nav_edit_profile:
                    if (currentFragmentId != R.id.editProfileFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.editProfileFragment);
                    break;

                case R.id.nav_help:
                    userToolbar.setVisibility(View.GONE);
                    if (currentFragmentId != R.id.helpChatStoreFragment)
                        Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).navigate(R.id.helpChatStoreFragment);
                    break;

                case R.id.logout:
                    requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE).edit()
                            .putBoolean(MainActivity.adminLoggedIn, false)
                            .putBoolean(MainActivity.userLoggedIn, false)
                            .apply();
                    new Handler().postDelayed(() -> restartApp(), 1000);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), drawerLayout, userToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        return view;
    }


    private void restartApp() {
        Intent restartIntent = requireActivity().getBaseContext()
                .getPackageManager()
                .getLaunchIntentForPackage(requireActivity().getBaseContext().getPackageName());
        assert restartIntent != null;
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartIntent);
        requireActivity().finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}