package com.hopeTheRobot.ui.fragments.user.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hopeTheRobot.R;
import com.hopeTheRobot.network.ApiClient;
import com.hopeTheRobot.pojo.ControlItem;
import com.hopeTheRobot.pojo.Covid19ReportItem;
import com.hopeTheRobot.ui.activities.MainActivity;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import java.text.DecimalFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment {

    ControlItem controlItem = new ControlItem();
    View view;
    CardView first_card, second_card, third_card, fourth_card;
    TextView first_card_on_off_text, second_card_on_off_text, third_card_on_off_text, fourth_card_on_off_text;
    SwitchMaterial first_card_on_off, second_card_on_off, third_card_on_off, fourth_card_on_off;
    TextView first_card_title, second_card_title, third_card_title, fourth_card_title;
    ImageView first_card_img, second_card_img, third_card_img, fourth_card_img;
    CircleImageView user_img, user_img_inside_toolbar;
    TextView user_name_inside_toolbar, welcome_text;
    TextView today_cases, today_recovered, today_deaths, total_cases, total_recovered, total_deaths, population, last_update_date, last_update_time, egypt_live;
    ImageView egypt_live_img;
    FirebaseDatabase database;
    DatabaseReference controlRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_home, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            logoutAndGoToLogin();
        } else {
            database = FirebaseDatabase.getInstance();
            controlRef = database.getReference("Control").child(currentUser.getUid());

            UserMainFragment.navigation_view.setCheckedItem(R.id.nav_home);
            if (UserMainFragment.userToolbar != null)
                UserMainFragment.userToolbar.setVisibility(View.GONE);

            Toolbar toolbar = view.findViewById(R.id.toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), UserMainFragment.drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            UserMainFragment.drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            user_img = view.findViewById(R.id.user_img);
            user_img_inside_toolbar = view.findViewById(R.id.user_img_inside_toolbar);

            Glide.with(requireActivity())
                    .load(currentUser.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA.DATA)
                    .placeholder(R.drawable.robot_img_small)
                    .into(user_img);

            Glide.with(requireActivity())
                    .load(currentUser.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.robot_img_small)
                    .into(user_img_inside_toolbar);


            welcome_text = view.findViewById(R.id.welcome_text);
            user_name_inside_toolbar = view.findViewById(R.id.user_name_inside_toolbar);

            String[] name = currentUser.getDisplayName().split(" ");
            welcome_text.setText(getString(R.string.hello) + name[0]);
            user_name_inside_toolbar.setText(currentUser.getDisplayName());

            first_card = view.findViewById(R.id.first_card);
            second_card = view.findViewById(R.id.second_card);
            third_card = view.findViewById(R.id.third_card);
            fourth_card = view.findViewById(R.id.fourth_card);

            first_card_on_off_text = view.findViewById(R.id.first_card_on_off_text);
            second_card_on_off_text = view.findViewById(R.id.second_card_on_off_text);
            third_card_on_off_text = view.findViewById(R.id.third_card_on_off_text);
            fourth_card_on_off_text = view.findViewById(R.id.fourth_card_on_off_text);

            first_card_on_off = view.findViewById(R.id.first_card_on_off);
            second_card_on_off = view.findViewById(R.id.second_card_on_off);
            third_card_on_off = view.findViewById(R.id.third_card_on_off);
            fourth_card_on_off = view.findViewById(R.id.fourth_card_on_off);

            first_card_title = view.findViewById(R.id.first_card_title);
            second_card_title = view.findViewById(R.id.second_card_title);
            third_card_title = view.findViewById(R.id.third_card_title);
            fourth_card_title = view.findViewById(R.id.fourth_card_title);

            first_card_img = view.findViewById(R.id.first_card_img);
            second_card_img = view.findViewById(R.id.second_card_img);
            third_card_img = view.findViewById(R.id.third_card_img);
            fourth_card_img = view.findViewById(R.id.fourth_card_img);

            first_card.setOnClickListener(v -> {
                controlItem.setSwabControl(!controlItem.getSwabControl());
                controlRef.setValue(controlItem);
            });
            second_card.setOnClickListener(v -> {
                controlItem.setMovementControl(!controlItem.getMovementControl());
                controlRef.setValue(controlItem);
            });
            third_card.setOnClickListener(v -> {
                controlItem.setMaskControl(!controlItem.getMaskControl());
                controlRef.setValue(controlItem);
            });
            fourth_card.setOnClickListener(v -> {
                controlItem.setThermalControl(!controlItem.getThermalControl());
                controlRef.setValue(controlItem);
            });

            first_card_on_off.setOnClickListener(v -> {
                controlItem.setSwabControl(!controlItem.getSwabControl());
                controlRef.setValue(controlItem);
            });
            second_card_on_off.setOnClickListener(v -> {
                controlItem.setMovementControl(!controlItem.getMovementControl());
                controlRef.setValue(controlItem);
            });
            third_card_on_off.setOnClickListener(v -> {
                controlItem.setMaskControl(!controlItem.getMaskControl());
                controlRef.setValue(controlItem);
            });
            fourth_card_on_off.setOnClickListener(v -> {
                controlItem.setThermalControl(!controlItem.getThermalControl());
                controlRef.setValue(controlItem);
            });

            getControlInfo();


            AppBarLayout appbar = view.findViewById(R.id.appbar);
            appbar.addOnOffsetChangedListener((appBarLayout, i) -> {
                if (Math.abs(i) == appBarLayout.getTotalScrollRange()) {
                    // show toolbar
                    view.findViewById(R.id.toolbar_views).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.toolbar).setBackgroundResource(R.drawable.background_main_toolbar);
                } else if (Math.abs(i) < (appBarLayout.getTotalScrollRange()) / 2) {
                    //hide toolbar
                    view.findViewById(R.id.toolbar_views).setVisibility(View.GONE);
                    view.findViewById(R.id.toolbar).setBackgroundColor(Color.TRANSPARENT);
                } else {
                    //show toolbar
                    view.findViewById(R.id.toolbar_views).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.toolbar).setBackgroundResource(R.drawable.background_main_toolbar);
                }
            });


            egypt_live = view.findViewById(R.id.egypt_live);
            egypt_live_img = view.findViewById(R.id.egypt_live_img);
// setup animation
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                    egypt_live_img,
                    PropertyValuesHolder.ofFloat("scaleX", 0.85f),
                    PropertyValuesHolder.ofFloat("scaleY", 0.85f)
            );
            objectAnimator.setDuration(700);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();


            today_cases = view.findViewById(R.id.today_cases);
            today_recovered = view.findViewById(R.id.today_recovered);
            today_deaths = view.findViewById(R.id.today_deaths);
            total_cases = view.findViewById(R.id.total_cases);
            total_recovered = view.findViewById(R.id.total_recovered);
            total_deaths = view.findViewById(R.id.total_deaths);
            population = view.findViewById(R.id.population);
            last_update_date = view.findViewById(R.id.last_update_date);
            last_update_time = view.findViewById(R.id.last_update_time);
            /// Covid 19 API

            getOneCountryReport();

            egypt_live.setOnClickListener(v -> showLivePopupMenu(v));
            egypt_live_img.setOnClickListener(v -> showLivePopupMenu(egypt_live));
            view.findViewById(R.id.live_arrow).setOnClickListener(v -> showLivePopupMenu(egypt_live));
        }
        ///
        return view;
    }

    AlertDialog dialog;

    private void logoutAndGoToLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.session_expired_dialog, null);
        view.findViewById(R.id.ok).setOnClickListener(v -> dialog.dismiss());
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        dialog.setOnDismissListener(dialog1 -> {
            requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE).edit()
                    .putBoolean(MainActivity.adminLoggedIn, false)
                    .putBoolean(MainActivity.userLoggedIn, false)
                    .apply();
            new Handler().postDelayed(() -> restartApp(), 1000);
        });
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    private void showLivePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.egypt_live) {
                getOneCountryReport();
            } else if (item.getItemId() == R.id.worldwide_live) {
                getGeneralReport();
            }
            return true;
        });
        popupMenu.inflate(R.menu.live_menu);
        popupMenu.show();
    }


    private void getOneCountryReport() {
        ApiClient.getINSTANCE().getOneCountryReport("Egypt").enqueue(new Callback<Covid19ReportItem>() {
            @Override
            public void onResponse(Call<Covid19ReportItem> call, Response<Covid19ReportItem> response) {
                Log.w("Covid_API_Response", response.body() + "");
                if (response.isSuccessful()) {
                    egypt_live.setText(getString(R.string.egypt_live));
                    Covid19ReportItem covid19ReportItem = response.body();
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    today_cases.setText(String.format("%,d", covid19ReportItem.getTodayCases()));
                    today_recovered.setText(String.format("%,d", covid19ReportItem.getTodayRecovered()));
                    today_deaths.setText(String.format("%,d", covid19ReportItem.getTodayDeaths()));
                    total_cases.setText(String.format("%,d", covid19ReportItem.getTotalCases()));
                    total_recovered.setText(String.format("%,d", covid19ReportItem.getTotalRecovered()));
                    total_deaths.setText(String.format("%,d", covid19ReportItem.getTotalDeaths()));
                    population.setText(String.format("%,d", covid19ReportItem.getPopulation()));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(covid19ReportItem.getTime());
                    String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    String time = (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

                    last_update_date.setText(date);
                    last_update_time.setText(time);

                    //save these values to use in offline mode
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("egypt_offline", new Gson().toJson(covid19ReportItem)).apply();
                }
            }

            @Override
            public void onFailure(Call<Covid19ReportItem> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();

                //get values from shared prefs to use in offline mode
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);
                String json = sharedPreferences.getString("egypt_offline", "");
                if (!json.equals("")) {
                    Covid19ReportItem covid19ReportItem = new Gson().fromJson(json, Covid19ReportItem.class);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    today_cases.setText(String.format("%,d", covid19ReportItem.getTodayCases()));
                    today_recovered.setText(String.format("%,d", covid19ReportItem.getTodayRecovered()));
                    today_deaths.setText(String.format("%,d", covid19ReportItem.getTodayDeaths()));
                    total_cases.setText(String.format("%,d", covid19ReportItem.getTotalCases()));
                    total_recovered.setText(String.format("%,d", covid19ReportItem.getTotalRecovered()));
                    total_deaths.setText(String.format("%,d", covid19ReportItem.getTotalDeaths()));
                    population.setText(String.format("%,d", covid19ReportItem.getPopulation()));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(covid19ReportItem.getTime());
                    String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    String time = (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

                    last_update_date.setText(date);
                    last_update_time.setText(time);

                } else {
                    String def = "#,###";
                    today_cases.setText(def);
                    today_recovered.setText(def);
                    today_deaths.setText(def);
                    total_cases.setText(def);
                    total_recovered.setText(def);
                    total_deaths.setText(def);
                    population.setText(def);
                    last_update_date.setText("DD/MM/YYYY");
                    last_update_time.setText("HH:MM AM");
                }
            }
        });
    }

    private void getGeneralReport() {
        ApiClient.getINSTANCE().getGeneralReport().enqueue(new Callback<Covid19ReportItem>() {
            @Override
            public void onResponse(Call<Covid19ReportItem> call, Response<Covid19ReportItem> response) {
                Log.w("Covid_API_Response2", response.body() + "");
                if (response.isSuccessful()) {
                    egypt_live.setText(getString(R.string.worldwide_live));
                    Covid19ReportItem covid19ReportItem = response.body();
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    today_cases.setText(String.format("%,d", covid19ReportItem.getTodayCases()));
                    today_recovered.setText(String.format("%,d", covid19ReportItem.getTodayRecovered()));
                    today_deaths.setText(String.format("%,d", covid19ReportItem.getTodayDeaths()));
                    total_cases.setText(String.format("%,d", covid19ReportItem.getTotalCases()));
                    total_recovered.setText(String.format("%,d", covid19ReportItem.getTotalRecovered()));
                    total_deaths.setText(String.format("%,d", covid19ReportItem.getTotalDeaths()));
                    population.setText(String.format("%,d", covid19ReportItem.getPopulation()));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(covid19ReportItem.getTime());
                    String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    String time = (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

                    last_update_date.setText(date);
                    last_update_time.setText(time);

                    //save these values to use in offline mode
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("general_offline", new Gson().toJson(covid19ReportItem)).apply();
                }
            }

            @Override
            public void onFailure(Call<Covid19ReportItem> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();

                //get values from shared prefs to use in offline mode
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE);
                String json = sharedPreferences.getString("general_offline", "");
                if (!json.equals("")) {
                    Covid19ReportItem covid19ReportItem = new Gson().fromJson(json, Covid19ReportItem.class);
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    today_cases.setText(String.format("%,d", covid19ReportItem.getTodayCases()));
                    today_recovered.setText(String.format("%,d", covid19ReportItem.getTodayRecovered()));
                    today_deaths.setText(String.format("%,d", covid19ReportItem.getTodayDeaths()));
                    total_cases.setText(String.format("%,d", covid19ReportItem.getTotalCases()));
                    total_recovered.setText(String.format("%,d", covid19ReportItem.getTotalRecovered()));
                    total_deaths.setText(String.format("%,d", covid19ReportItem.getTotalDeaths()));
                    population.setText(String.format("%,d", covid19ReportItem.getPopulation()));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(covid19ReportItem.getTime());
                    String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                    String time = (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

                    last_update_date.setText(date);
                    last_update_time.setText(time);

                } else {
                    String def = "#,###";
                    today_cases.setText(def);
                    today_recovered.setText(def);
                    today_deaths.setText(def);
                    total_cases.setText(def);
                    total_recovered.setText(def);
                    total_deaths.setText(def);
                    population.setText(def);
                    last_update_date.setText("DD/MM/YYYY");
                    last_update_time.setText("HH:MM PM");
                }
            }
        });
    }

    private void getControlInfo() {
        controlRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                controlItem = snapshot.getValue(ControlItem.class);
                if (controlItem == null)
                    controlItem = new ControlItem();
                setupCard(controlItem.getSwabControl(), first_card, first_card_on_off, first_card_on_off_text, first_card_img, first_card_title);
                setupCard(controlItem.getMovementControl(), second_card, second_card_on_off, second_card_on_off_text, second_card_img, second_card_title);
                setupCard(controlItem.getMaskControl(), third_card, third_card_on_off, third_card_on_off_text, third_card_img, third_card_title);
                setupCard(controlItem.getThermalControl(), fourth_card, fourth_card_on_off, fourth_card_on_off_text, fourth_card_img, fourth_card_title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupCard(boolean state, CardView cardView, SwitchMaterial switchMaterial, TextView on_off_text, ImageView img, TextView title) {
        if (!isAdded())
            return;

        switchMaterial.setChecked(state);
        cardView.setCardBackgroundColor(state ? requireActivity().getColor(R.color.colorSecondary) : requireActivity().getColor(R.color.white));
        on_off_text.setText(state ? getString(R.string.on) : getString(R.string.off));
        on_off_text.setTextColor(state ? requireActivity().getColor(R.color.white) : requireActivity().getColor(R.color.colorPrimary));
        img.setImageTintList(ColorStateList.valueOf(requireActivity().getColor(state ? R.color.white : R.color.colorSecondary)));
        title.setTextColor(state ? requireActivity().getColor(R.color.white) : requireActivity().getColor(R.color.black));
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                /* exit only if the user click the back button twice in the Main Activity**/
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(requireContext(), getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
