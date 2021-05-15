package com.hopeTheRobot.ui.fragments.user.controlRobot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControlRobotFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_control_robot, container, false);

        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_control_robot);
        if (UserMainFragment.userToolbar.getVisibility() != View.VISIBLE)
            UserMainFragment.userToolbar.setVisibility(View.VISIBLE);
        UserMainFragment.userToolbar.setTitle(requireContext().getString(R.string.control_robot));

        TextView conText = view.findViewById(R.id.con_text);
        JoystickView joystick = view.findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want
                conText.setText("Control robot\nAngle: " + angle + "\nStrength: " + strength);
            }
        });

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
                Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).popBackStack(R.id.userHomeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}
