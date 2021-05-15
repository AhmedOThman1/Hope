package com.hopeTheRobot.adapters;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.HighTempItem;

import java.util.ArrayList;
import java.util.Calendar;

public class HighTempAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<HighTempItem> Models;
    private HighTempItem current_model;

    public HighTempAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<HighTempItem> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_high_temp_item, parent, false);
        return new HighTempViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        final HighTempViewHolder ViewHolder = (HighTempViewHolder) holder;

        Glide.with(context)
                .load(current_model.getImage()+"")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(ViewHolder.high_temp_person_img);

        ViewHolder.temp.setText(current_model.getTemp()+" °C");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current_model.getDateMillis());
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR)+"\t"+
        (calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" +
                (calendar.get(Calendar.MINUTE) > 9 ?
                calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

        ViewHolder.date.setText(date);

    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class HighTempViewHolder extends RecyclerView.ViewHolder {
        // views
        ImageView high_temp_person_img;
        TextView temp,date;
        public HighTempViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view by id
            high_temp_person_img = itemView.findViewById(R.id.high_temp_person_img);
            temp = itemView.findViewById(R.id.temp);
            date = itemView.findViewById(R.id.date);
        }
    }

}
