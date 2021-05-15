package com.hopeTheRobot.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hopeTheRobot.R;
import com.hopeTheRobot.pojo.MessageItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageItem> Models = new ArrayList<>();
    private MessageItem current_model;
    private String currentUserUid = "";

    public ChatAdapter(@NonNull Context context, String currentUserUid) {
        this.context = context;
        this.currentUserUid = currentUserUid;
    }

    public void setModels(ArrayList<MessageItem> models) {
        Models = models;
    }

    @Override
    public int getItemViewType(int position) {
        Log.w("Eror", "" + position + "," + Models.get(position).getUserUid() + "@" + currentUserUid);
        return Models.get(position).getUserUid().equals(currentUserUid) ? 1 : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_current_user_message_item, parent, false);
            return new CurrentUserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_other_users_message_item, parent, false);
            return new OtherUsersMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        if (holder.getItemViewType() == 1) {
            final CurrentUserMessageViewHolder currentUserMessageViewHolder = (CurrentUserMessageViewHolder) holder;

            currentUserMessageViewHolder.messageBody.setText(Models.get(position).getMessageBody());

            if (position == Models.size() - 1 || !Models.get(position).getUserUid().equals(Models.get(position + 1).getUserUid()))
                currentUserMessageViewHolder.messageBody.setBackgroundResource(R.drawable.background_your_message0);
            else if (Models.get(position).getUserUid().equals(Models.get(position + 1).getUserUid()))
                currentUserMessageViewHolder.messageBody.setBackgroundResource(R.drawable.background_your_message);

            currentUserMessageViewHolder.messageBody.setPadding(pxToDp(10), pxToDp(8), pxToDp(10), pxToDp(8));

        } else {
            final OtherUsersMessageViewHolder otherUsersMessageViewHolder = (OtherUsersMessageViewHolder) holder;

            otherUsersMessageViewHolder.messageBody.setText(Models.get(position).getMessageBody());

            if (current_model.getUserImage() == null)
                otherUsersMessageViewHolder.message_user_image.setImageResource(R.drawable.robot_img);
            else
                Glide.with(context)
                        .load(current_model.getUserImage() + "")
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(otherUsersMessageViewHolder.message_user_image);

            if (position == Models.size() - 1 || !Models.get(position).getUserUid().equals(Models.get(position + 1).getUserUid())) {
                otherUsersMessageViewHolder.message_user_image.setVisibility(View.VISIBLE);
                otherUsersMessageViewHolder.messageBody.setBackgroundResource(R.drawable.background_other_message0);
            } else if (Models.get(position).getUserUid().equals(Models.get(position + 1).getUserUid())) {
                otherUsersMessageViewHolder.message_user_image.setVisibility(View.GONE);
                otherUsersMessageViewHolder.messageBody.setBackgroundResource(R.drawable.background_other_message);
            }

            otherUsersMessageViewHolder.messageBody.setPadding(pxToDp(10), pxToDp(8), pxToDp(10), pxToDp(8));
        }

    }

    private int pxToDp(int px) {
        return ((int) ((px * context.getResources().getDisplayMetrics().density) + 0.5f));
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class CurrentUserMessageViewHolder extends RecyclerView.ViewHolder {
        // views
        TextView messageBody;

        public CurrentUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view by id
            messageBody = itemView.findViewById(R.id.message_body);
        }
    }

    class OtherUsersMessageViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView message_user_image;
        TextView messageBody;

        public OtherUsersMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view by id
            messageBody = itemView.findViewById(R.id.message_body);
            message_user_image = itemView.findViewById(R.id.message_user_image);
        }
    }

}
