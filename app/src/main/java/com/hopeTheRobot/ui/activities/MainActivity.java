package com.hopeTheRobot.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hopeTheRobot.R;

public class MainActivity extends AppCompatActivity {

    public final static String shared_pref = "shared_pref";
    public final static String showIntro = "show_intro";
    public final static String profileNotCompleted = "profileNotCompleted";
    public final static String adminLoggedIn = "adminLoggedIn";
    public final static String userLoggedIn = "userLoggedIn";
    public final static int GAL_CODE = 0;
    public final static int OPEN_GAL_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for offline mode
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);

        getSupportActionBar().hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {

            getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                    .putBoolean(adminLoggedIn, false)
                    .putBoolean(userLoggedIn, false)
                    .apply();
            new Handler().postDelayed(() -> restartApp(), 1000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartApp() {
        Intent restartIntent = getBaseContext()
                .getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert restartIntent != null;
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartIntent);
        finish();
    }
}