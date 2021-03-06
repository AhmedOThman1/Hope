package com.hopeTheRobot.ui.fragments.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.UserItem;
import com.hopeTheRobot.ui.activities.MainActivity;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hopeTheRobot.ui.activities.MainActivity.GAL_CODE;
import static com.hopeTheRobot.ui.activities.MainActivity.OPEN_GAL_PERMISSION_CODE;

public class CompleteProfileFragment extends Fragment {


    FirebaseDatabase database;
    DatabaseReference usersRef;
    StorageReference usersStorageRef;
    CircleImageView user_img;
    TextInputLayout name, newPassword;
    String currentPassword;
    TextView complete;
    ProgressBar loading;
    FirebaseAuth mAuth;
    View view;
    Uri ImageUri;
    FirebaseUser currentUser;
    UserItem currentUserItem = new UserItem();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_edit_profile, container, false);


        user_img = view.findViewById(R.id.user_img);
        name = view.findViewById(R.id.name);
        newPassword = view.findViewById(R.id.new_password);
        loading = view.findViewById(R.id.loading);
        complete = view.findViewById(R.id.edit_profile);

        Bundle args = getArguments();
        if (args != null)
            currentPassword = args.getString("password", "");
        else newPassword.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        usersStorageRef = FirebaseStorage.getInstance().getReference("Users");
        getCurrentUserItem();
        ImageUri = currentUser.getPhotoUrl();

        complete.setText(requireContext().getString(R.string.complete_profile));
        view.findViewById(R.id.current_password).setVisibility(View.GONE);

        if (currentUser.getPhotoUrl() == null)
            user_img.setImageResource(R.drawable.robot_img_small);
        else
            Glide.with(requireContext())
                    .load(currentUser.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(user_img);

        name.getEditText().setText(currentUser.getDisplayName());

        complete.setOnClickListener(v -> {
            if (name.getEditText().getText().toString().isEmpty()) {
                name.setError(getResources().getString(R.string.empty));
                name.requestFocus();
                openKeyboard(name.getEditText());
            } else if (!newPassword.getEditText().getText().toString().trim().isEmpty() && newPassword.getEditText().getText().toString().trim().length() < 6) {
                name.setError(null);
                newPassword.setError(getResources().getString(R.string.can_not_be_less_than_6));
                newPassword.requestFocus();
                openKeyboard(newPassword.getEditText());
            } else {
                name.setError(null);
                newPassword.setError(null);
                loading.setVisibility(View.VISIBLE);
                completeProfile(name.getEditText().getText().toString(), currentPassword, newPassword.getEditText().getText().toString().trim());
            }
        });

        user_img.setOnClickListener(v -> checkPermissionAndOpenGal());

        return view;
    }

    private void completeProfile(String name, String currentPassword, String newPassword) {

        if (newPassword.isEmpty()) {
            try {
                changeName(name);
                if (ImageUri != null)
                    changePhoto();
            } finally {
                UserItem userItem = new UserItem();
                userItem.setUserId(currentUser.getUid());
                userItem.setUsername(currentUser.getDisplayName());
                userItem.setEmail(currentUser.getEmail());
                if (currentUser.getPhotoUrl() != null)
                    userItem.setUserImage(currentUser.getPhotoUrl().toString());
                usersRef.child(userItem.getUserId()).setValue(userItem);
                close_keyboard();
                Toast.makeText(requireContext(), "Profile has been completed successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE)
                        .edit().putBoolean(MainActivity.profileNotCompleted, false).apply();
                Navigation.findNavController(view).navigate(R.id.action_completeProfileFragment_to_userMainFragment);
            }

        } else {
            AuthCredential authCredential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

            currentUser.reauthenticate(authCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {

                            try {
                                changeName(name);
                                if (ImageUri != null)
                                    changePhoto();
                            } finally {
                                UserItem userItem = new UserItem();
                                userItem.setUserId(currentUser.getUid());
                                userItem.setUsername(currentUser.getDisplayName());
                                userItem.setEmail(currentUser.getEmail());
                                if (currentUser.getPhotoUrl() != null)
                                    userItem.setUserImage(currentUser.getPhotoUrl().toString());
                                usersRef.child(userItem.getUserId()).setValue(userItem);
                                Toast.makeText(requireContext(), "Profile has been completed successfully", Toast.LENGTH_SHORT).show();
                                close_keyboard();
                                requireActivity().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE)
                                        .edit().putBoolean(MainActivity.profileNotCompleted, false).apply();
                                Navigation.findNavController(view).navigate(R.id.action_completeProfileFragment_to_userMainFragment);
                            }
                        } else {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(requireContext(), "Failed, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Failed, try again!", Toast.LENGTH_SHORT).show();
                    });
                }
            }).addOnFailureListener(e -> {
                loading.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Failed, try again!", Toast.LENGTH_SHORT).show();
            });

        }

    }

    private void changeName(String name) {
        loading.setVisibility(View.GONE);
        UserProfileChangeRequest profileUpdates =
                new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        currentUser.updateProfile(profileUpdates);
    }


    private void changePhoto() {
        usersStorageRef.child(currentUser.getUid()).putFile(ImageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                usersStorageRef.child(currentUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
                            loading.setVisibility(View.GONE);
                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                            currentUser.updateProfile(profileUpdates);
                        }
                ).addOnFailureListener(e -> {
                    Toast.makeText(
                            requireContext(),
                            "Failed to upload ! Try again." + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                    loading.setVisibility(View.GONE);
                });
            }
        }).addOnProgressListener(snapshot -> {
            loading.setVisibility(View.VISIBLE);
        }).continueWith(task -> {
            loading.setVisibility(View.GONE);
            if (!task.isSuccessful()) {
                Toast.makeText(
                        requireContext(),
                        "Failed to upload! Try again.",
                        Toast.LENGTH_LONG
                ).show();
            }
            return task;
        });
    }

    private void checkPermissionAndOpenGal() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    OPEN_GAL_PERMISSION_CODE);
        } else {
            Intent gal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
            startActivityForResult(gal, GAL_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_GAL_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent gal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");
            startActivityForResult(gal, GAL_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAL_CODE && resultCode == Activity.RESULT_OK) {
            // image from gallery
            Uri imageUri = data.getData();
            if (imageUri != null) {
                // one image
                user_img.setImageURI(imageUri);
                user_img.setBorderColor(requireContext().getColor(R.color.colorPrimary));
                user_img.setBorderWidth(2);
                ImageUri = imageUri;
            }
        }
    }

    private void getCurrentUserItem() {
        usersRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserItem = snapshot.getValue(UserItem.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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