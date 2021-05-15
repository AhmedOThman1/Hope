package com.hopeTheRobot.ui.fragments.user.highTemp;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hopeTheRobot.R;
import com.hopeTheRobot.adapters.HighTempAdapter;
import com.hopeTheRobot.pojo.ChatUser;
import com.hopeTheRobot.pojo.HighTempItem;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class OneHighTempFragment extends Fragment {


    PhotoView image;
    TextView date, temp;
    HighTempItem highTempItem = new HighTempItem();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_high_temp, container, false);

        image = view.findViewById(R.id.image);
        date = view.findViewById(R.id.date);
        temp = view.findViewById(R.id.temp);

        Bundle args = getArguments();
        if (args != null) {
            String json = args.getString("item", "");
            if (json != null && !json.isEmpty()) {
                highTempItem = new Gson().fromJson(json, HighTempItem.class);
                Glide.with(requireContext())
                        .load(highTempItem.getImage()+"")
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(image);

                temp.setText(highTempItem.getTemp()+" °C");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(highTempItem.getDateMillis());
                String dateString = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR)+"\t"+
                        (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" +
                        (calendar.get(Calendar.MINUTE) > 9 ?
                                calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

                date.setText(dateString);

            }
        }

        view.findViewById(R.id.back).setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        return view;
    }

}
