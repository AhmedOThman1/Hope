package com.hopeTheRobot.ui.fragments.auth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.activities.MainActivity;

public class ConnectToRobotFragment extends Fragment {

    TextInputLayout serial_number, password;
    ProgressBar loading;
    FirebaseAuth mAuth;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_connect_to_robot, container, false);

        serial_number = view.findViewById(R.id.serial_number);
        password = view.findViewById(R.id.password);
        loading = view.findViewById(R.id.loading);


        mAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.connect).setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            if (serial_number.getEditText().getText().toString().isEmpty()) {
                serial_number.setError(getResources().getString(R.string.empty));
                serial_number.requestFocus();
                openKeyboard(serial_number.getEditText());
                loading.setVisibility(View.GONE);
            } else if (password.getEditText().getText().toString().isEmpty()) {
                serial_number.setError(null);
                password.setError(getResources().getString(R.string.empty));
                password.requestFocus();
                openKeyboard(password.getEditText());
                loading.setVisibility(View.GONE);
            } else {
                serial_number.setError(null);
                password.setError(null);

                login(serial_number.getEditText().getText().toString() + "@hope.robot", password.getEditText().getText().toString());
            }
        });


        view.findViewById(R.id.don_t_have_a_robot).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_connectToRobotFragment_to_howToGetHopeRobotFragment);
        });

        return view;
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    loading.setVisibility(View.GONE);
                                    // Sign in success, update UI with the signed-in user's information
                                    close_keyboard();
                                    Log.w("Login", "signInWithEmail:success");
                                    if (email.equalsIgnoreCase("hope@hope.robot")) {

                                        Navigation.findNavController(view).navigate(R.id.adminChatUsersFragment);
                                        requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE)
                                                .edit().putBoolean(
                                                MainActivity.adminLoggedIn, true
                                        ).apply();

                                    } else {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE)
                                                .edit().putBoolean(MainActivity.userLoggedIn, true).apply();
                                        if (user.getDisplayName() == null) {
                                            Toast.makeText(requireContext(), "Please complete profile first!", Toast.LENGTH_SHORT).show();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("password", password);
                                            Navigation.findNavController(view).navigate(R.id.action_connectToRobotFragment_to_completeProfileFragment, bundle);
                                        } else {
                                            requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE)
                                                    .edit().putBoolean(MainActivity.profileNotCompleted, false).apply();
                                            Navigation.findNavController(view).navigate(R.id.action_connectToRobotFragment_to_userMainFragment);
                                        }

                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    loading.setVisibility(View.GONE);
                                    Log.w("Login", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(
                                            requireContext(), "Email or password is incorrect.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });
    }


    private void openKeyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText

    }


    private void close_keyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
