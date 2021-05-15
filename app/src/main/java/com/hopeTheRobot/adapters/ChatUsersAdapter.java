package com.hopeTheRobot.adapters;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.ChatUser;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ChatUser> Models;
    private ChatUser current_model;

    public ChatUsersAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<ChatUser> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_chat_user_item, parent, false);
        return new ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        final ChatUserViewHolder ViewHolder = (ChatUserViewHolder) holder;

        Glide.with(context)
                .load(current_model.getUserPhoto())
                .placeholder(R.drawable.robot_img_small)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(ViewHolder.chat_user_image);

        ViewHolder.chat_user_name.setText(current_model.getUserName());

        ViewHolder.last_message.setText(current_model.getLastMessage());

        Calendar calendar = Calendar.getInstance(), today = Calendar.getInstance();
        calendar.setTimeInMillis(current_model.getLastMessageDate());

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))
            ViewHolder.last_message_time.setText((calendar.get(Calendar.HOUR) == 0 ? "12" : calendar.get(Calendar.HOUR)) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)) + (calendar.get(Calendar.AM_PM) == Calendar.AM ? " am" : " pm"));
        else
            ViewHolder.last_message_time.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        ViewHolder.seen.setVisibility(Models.get(position).getSeen() == null || Models.get(position).getSeen() == true ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class ChatUserViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView chat_user_image;
        ImageView seen;
        TextView chat_user_name, last_message_time, last_message;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view by id
            chat_user_image = itemView.findViewById(R.id.chat_user_image);
            chat_user_name = itemView.findViewById(R.id.chat_user_name);
            last_message_time = itemView.findViewById(R.id.last_message_time);
            last_message = itemView.findViewById(R.id.last_message);
            seen = itemView.findViewById(R.id.seen);

        }
    }

}
