package com.hopeTheRobot.ui.fragments.admin.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.hopeTheRobot.R;
import com.hopeTheRobot.adapters.ChatAdapter;
import com.hopeTheRobot.pojo.ChatUser;
import com.hopeTheRobot.pojo.MessageItem;
import com.hopeTheRobot.ui.activities.MainActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminChatFragment extends Fragment {

    ArrayList<MessageItem> messageItems = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference chatRef, chatUsersRef;
    ChatAdapter chatAdapter;
    CircleImageView chat_user_img;
    TextView chat_user_name;
    ChatUser chatUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);


        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.logout_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {

                requireContext().getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE).edit()
                        .putBoolean(MainActivity.adminLoggedIn, false)
                        .putBoolean(MainActivity.userLoggedIn, false)
                        .apply();
                new Handler().postDelayed(() -> restartApp(), 1000);
                return true;
            }
            return false;
        });

//        StoreMainFragment.storeBottom_navigation.setVisibility(View.GONE);

        chat_user_img = view.findViewById(R.id.chat_user_img);
        chat_user_name = view.findViewById(R.id.chat_user_name);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            String json = args.getString("chatUser", "");
            if (json != null && !json.isEmpty()) {
                chatUser = new Gson().fromJson(json, ChatUser.class);
                chatRef = database.getReference("Chat").child("Messages").child(chatUser.getUserUid());
                chatUsersRef = database.getReference("Chat").child("Users").child(chatUser.getUserUid());
//            To Do here get user imagge & name and set it
                Glide.with(requireContext())
                        .load(chatUser.getUserPhoto())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(chat_user_img);

                chat_user_name.setText(chatUser.getUserName());
                if (chatUser.getSeen() == null || chatUser.getSeen() == false) {
                    chatUser.setSeen(true);
                    chatUsersRef.setValue(chatUser);
                }

            }
        }
        RecyclerView chat_recyclerview = view.findViewById(R.id.chat_recyclerview);
        chatAdapter = new ChatAdapter(requireContext(), currentUser.getUid());
        chatAdapter.setModels(messageItems);
        chat_recyclerview.setAdapter(chatAdapter);
        chat_recyclerview.scrollToPosition(messageItems.size() - 1);
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        EditText newMessage = view.findViewById(R.id.message);

        view.findViewById(R.id.send).setOnClickListener(v -> {
            if (!newMessage.getText().toString().trim().isEmpty()) {
                MessageItem newMessageItem = new MessageItem();
                newMessageItem.setMessageBody(newMessage.getText().toString().trim());
                newMessageItem.setUserUid(currentUser.getUid());
                chatRef.child(chatRef.push().getKey()).setValue(newMessageItem);
                ChatUser newChatUser = new ChatUser();
                newChatUser.setUserUid(chatUser.getUserUid());
                newChatUser.setLastMessageDate(System.currentTimeMillis());
                newChatUser.setLastMessage(newMessageItem.getMessageBody());
                chatUsersRef.setValue(newChatUser);
                newMessage.setText("");
            }
        });

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageItem messageItem = snapshot.getValue(MessageItem.class);
                if (messageItem.getUserUid().equals(chatUser.getUserUid()))
                    messageItem.setUserImage(chatUser.getUserPhoto());
                messageItems.add(messageItem);
                chatAdapter.notifyItemInserted(messageItems.size() - 1);
                chat_recyclerview.smoothScrollToPosition(messageItems.size() - 1);
                chatUsersRef.child("seen").setValue(true);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
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
}
