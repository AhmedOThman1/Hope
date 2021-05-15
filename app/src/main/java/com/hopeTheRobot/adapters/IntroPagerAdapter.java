package com.hopeTheRobot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.IntroItem;

import java.util.ArrayList;

public class IntroPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<IntroItem> introItems = new ArrayList<>();

    public IntroPagerAdapter(Context context) {
        this.context = context;
    }

    public void setIntroItems(ArrayList<IntroItem> introItems) {
        this.introItems = introItems;
    }

    @Override
    public int getCount() {
        return introItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View LayoutScreen = layoutInflater.inflate(R.layout.one_intro_item, null);
        ImageView intro_img = LayoutScreen.findViewById(R.id.intro_img);
        TextView intro_title = LayoutScreen.findViewById(R.id.intro_title);
        TextView intro_description = LayoutScreen.findViewById(R.id.intro_description);

        intro_img.setImageResource(introItems.get(position).getImg());
        intro_title.setText(introItems.get(position).getTitle());
        intro_description.setText(introItems.get(position).getDescription());

        container.addView(LayoutScreen);
        return LayoutScreen;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
