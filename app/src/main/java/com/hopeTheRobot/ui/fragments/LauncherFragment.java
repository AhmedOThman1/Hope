package com.hopeTheRobot.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.hopeTheRobot.R;
import com.hopeTheRobot.ui.activities.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class LauncherFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_launcher, container, false);

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

//        if (ActivityCompat.checkSelfPermission(requireContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        else
//            saveCardToDevice();
        return view;
    }

    CardView CardItem;

    private void saveCardToDevice() {
        CardItem = view.findViewById(R.id.card_item);
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String PATH_NAME;

            Calendar c = Calendar.getInstance();
            String FileTimeName = "" +
                    (c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)) +
                    (c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)) +
                    (c.get(Calendar.SECOND) < 10 ? "0" + c.get(Calendar.SECOND) : c.get(Calendar.SECOND));

            File AppFolder = new File(Environment.getExternalStorageDirectory() + "/" +
                    requireContext().getResources().getString(R.string.app_name) + "/");

            if (!AppFolder.exists()) {

                if (AppFolder.mkdirs())
                    PATH_NAME = AppFolder.getAbsolutePath() + "/" + FileTimeName + ".png";
                else
                    PATH_NAME = Environment.getExternalStorageDirectory() + "/" + FileTimeName + ".png";
            } else
                PATH_NAME = AppFolder.getAbsolutePath() + "/" + FileTimeName + ".png";


            // create bitmap screen capture

            CardItem.setDrawingCacheEnabled(true);
//            CardItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            CardItem.layout(0, 0, CardItem.getMeasuredWidth(), CardItem.getMeasuredHeight());
            new Handler().postDelayed(() -> {
                CardItem.buildDrawingCache(true);
                Bitmap bitmap = Bitmap.createBitmap(CardItem.getDrawingCache());
                CardItem.setDrawingCacheEnabled(false);

                File imageFile = new File(PATH_NAME);
        try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(requireContext(), "تم", Toast.LENGTH_SHORT).show();
            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
                Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            }, 1000);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
