package com.hopeTheRobot.ui.fragments.user.controlRobot;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.ControlItem;
import com.hopeTheRobot.pojo.MotorsPositionsItem;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

public class ControlRobotFragment extends Fragment {

    CardView warning_card;
    View view;
    FirebaseDatabase database;
    DatabaseReference controlRef, movementRef, liveLinkRef;
    Slider base_motor, shoulder_motor, elbow_motor, wrist_roll_motor, wrist_pitch_motor, gripper_motor;
    MotorsPositionsItem motorsPositionsItem = new MotorsPositionsItem();
    String liveUrl = "";
    WebView web_view_live;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_control_robot, container, false);

        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_control_robot);
        if (UserMainFragment.userToolbar.getVisibility() != View.VISIBLE)
            UserMainFragment.userToolbar.setVisibility(View.VISIBLE);
        UserMainFragment.userToolbar.setTitle(requireContext().getString(R.string.control_robot));

//        TextView conText = view.findViewById(R.id.con_text);
//        JoystickView joystick = view.findViewById(R.id.joystickView);
//        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
//            @Override
//            public void onMove(int angle, int strength) {
//                // do whatever you want
//                conText.setText("Control robot\nAngle: " + angle + "\nStrength: " + strength);
//            }
//        });

//        String liveUrl = "http://192.168.1.3:5000/";

        web_view_live = view.findViewById(R.id.web_view_live);
//        web_view_live.loadUrl(liveUrl);


        base_motor = view.findViewById(R.id.base_motor);
        shoulder_motor = view.findViewById(R.id.shoulder_motor);
        elbow_motor = view.findViewById(R.id.elbow_motor);
        wrist_roll_motor = view.findViewById(R.id.wrist_roll_motor);
        wrist_pitch_motor = view.findViewById(R.id.wrist_pitch_motor);
        gripper_motor = view.findViewById(R.id.gripper_motor);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        liveLinkRef = database.getReference("Live").child(currentUser.getUid());
        getLiveLink();
        movementRef = database.getReference("Movement").child(currentUser.getUid());
        getMovementMotorsPositions();

        base_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "base_motor: " + slider.getValue());
                motorsPositionsItem.setBaseMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        shoulder_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "shoulder_motor: " + slider.getValue());
                motorsPositionsItem.setShoulderMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        elbow_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "elbow_motor: " + slider.getValue());
                motorsPositionsItem.setElbowMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        wrist_roll_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "wrist_roll_motor: " + slider.getValue());
                motorsPositionsItem.setWristRollMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        wrist_pitch_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "wrist_pitch_motor: " + slider.getValue());
                motorsPositionsItem.setWristPitchMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        gripper_motor.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Log.w("ControlRobot", "gripper_motor: " + slider.getValue());
                motorsPositionsItem.setGripperMotorPosition(Math.round(slider.getValue()));
                movementRef.setValue(motorsPositionsItem);
            }
        });
        //warrniing setup
        TextView warning_title = view.findViewById(R.id.warning_title);
        warning_card = view.findViewById(R.id.warning_card);
        controlRef = database.getReference("Control").child(currentUser.getUid());

        getControlInfo();
        // setup animation
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                warning_title,
                PropertyValuesHolder.ofFloat("scaleX", 0.85f),
                PropertyValuesHolder.ofFloat("scaleY", 0.85f)
        );
        objectAnimator.setDuration(700);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        view.findViewById(R.id.warning_turn_on).setOnClickListener(v -> {
            controlItem.setMovementControl(!controlItem.getMovementControl());
            controlRef.setValue(controlItem);
            warning_card.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void getLiveLink() {
        liveLinkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                liveUrl = snapshot.getValue(String.class);
                if (liveUrl != null)
                    web_view_live.loadUrl(liveUrl);
                else
                    Toast.makeText(requireContext(), "Live Link is incorrect", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ControlItem controlItem;

    private void getControlInfo() {
        controlRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                controlItem = snapshot.getValue(ControlItem.class);
                if (controlItem == null)
                    controlItem = new ControlItem();
                warning_card.setVisibility(controlItem.getMovementControl() ? View.GONE : View.VISIBLE);
                view.findViewById(R.id.hidden_view).setVisibility(controlItem.getMovementControl() ? View.GONE : View.VISIBLE);
                base_motor.setEnabled(controlItem.getMovementControl());
                shoulder_motor.setEnabled(controlItem.getMovementControl());
                elbow_motor.setEnabled(controlItem.getMovementControl());
                wrist_roll_motor.setEnabled(controlItem.getMovementControl());
                wrist_pitch_motor.setEnabled(controlItem.getMovementControl());
                gripper_motor.setEnabled(controlItem.getMovementControl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMovementMotorsPositions() {
        movementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                motorsPositionsItem = snapshot.getValue(MotorsPositionsItem.class);
                if (motorsPositionsItem == null)
                    motorsPositionsItem = new MotorsPositionsItem();

                base_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getBaseMotorPosition()));
                shoulder_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getShoulderMotorPosition()));
                elbow_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getElbowMotorPosition()));
                wrist_roll_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getWristRollMotorPosition()));
                wrist_pitch_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getWristPitchMotorPosition()));
                gripper_motor.setValue(Float.parseFloat("" + motorsPositionsItem.getGripperMotorPosition()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
