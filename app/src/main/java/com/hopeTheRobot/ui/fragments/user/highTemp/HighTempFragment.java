package com.hopeTheRobot.ui.fragments.user.highTemp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hopeTheRobot.R;
import com.hopeTheRobot.RecyclerViewTouchListener;
import com.hopeTheRobot.adapters.HighTempAdapter;
import com.hopeTheRobot.pojo.ControlItem;
import com.hopeTheRobot.pojo.HighTempItem;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighTempFragment extends Fragment {


    RecyclerView high_temp_recycler;
    ShimmerFrameLayout high_temp_shimmer;
    HighTempAdapter highTempAdapter;

    FirebaseDatabase database;
    DatabaseReference highTempRef, controlRef;

    ArrayList<HighTempItem> highTempItems = new ArrayList<>();
    CardView warning_card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_high_temp, container, false);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        highTempRef = FirebaseDatabase.getInstance().getReference("HighTemp");

        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_high_temp);
        if (UserMainFragment.userToolbar.getVisibility() != View.VISIBLE)
            UserMainFragment.userToolbar.setVisibility(View.VISIBLE);
        UserMainFragment.userToolbar.setTitle(requireContext().getString(R.string.high_temp));

        high_temp_shimmer = view.findViewById(R.id.high_temp_shimmer);
        high_temp_recycler = view.findViewById(R.id.high_temp_recycler);

        highTempAdapter = new HighTempAdapter(requireContext());
        highTempAdapter.setModels(highTempItems);
        high_temp_recycler.setAdapter(highTempAdapter);
        high_temp_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        high_temp_shimmer.setVisibility(highTempItems.isEmpty() ? View.VISIBLE : View.GONE);
        getAllHighTemp();


        high_temp_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), high_temp_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("item", new Gson().toJson(highTempItems.get(position)));
                Navigation.findNavController(view).navigate(R.id.action_highTempFragment_to_oneHighTempFragment, bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
            controlItem.setThermalControl(!controlItem.getThermalControl());
            controlRef.setValue(controlItem);
            warning_card.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show();
        });


        return view;
    }

    private void getAllHighTemp() {
        highTempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                highTempItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HighTempItem highTempItem = dataSnapshot.getValue(HighTempItem.class);
                    highTempItems.add(highTempItem);
                }
                highTempItems.sort(new Comparator<HighTempItem>() {
                    @Override
                    public int compare(HighTempItem highTempItem1, HighTempItem highTempItem2) {
                        return highTempItem1.getDateMillis().compareTo(highTempItem2.getDateMillis());
                    }
                });
                Collections.reverse(highTempItems);

                if (highTempItems.isEmpty())
                    Toast.makeText(requireContext(), "No Items", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> {
                    highTempAdapter.setModels(highTempItems);
                    highTempAdapter.notifyDataSetChanged();
                    high_temp_shimmer.setVisibility(highTempItems.isEmpty() ? View.VISIBLE : View.GONE);
                }, 950);

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
                warning_card.setVisibility(controlItem.getThermalControl() ? View.GONE : View.VISIBLE);
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
