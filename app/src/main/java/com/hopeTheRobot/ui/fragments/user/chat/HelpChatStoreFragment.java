package com.hopeTheRobot.ui.fragments.user.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hopeTheRobot.R;
import com.hopeTheRobot.adapters.ChatAdapter;
import com.hopeTheRobot.pojo.ChatUser;
import com.hopeTheRobot.pojo.MessageItem;
import com.hopeTheRobot.ui.fragments.user.userMainScreen.UserMainFragment;

import java.util.ArrayList;

public class HelpChatStoreFragment extends Fragment {

    ArrayList<MessageItem> messageItems = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference chatRef, chatUsersRef;
    ChatAdapter chatAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_get_help_chat, container, false);


        UserMainFragment.navigation_view.setCheckedItem(R.id.nav_help);
        if (UserMainFragment.userToolbar.getVisibility() != View.GONE)
            UserMainFragment.userToolbar.setVisibility(View.GONE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        chatRef = database.getReference("Chat").child("Messages").child(currentUser.getUid());
        chatUsersRef = database.getReference("Chat").child("Users").child(currentUser.getUid());

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
                Log.w("REF",""+chatRef.getPath().toString());
                chatRef.child(chatRef.push().getKey()).setValue(newMessageItem);
                ChatUser newChatUser = new ChatUser();
                newChatUser.setUserUid(currentUser.getUid());
                newChatUser.setLastMessageDate(System.currentTimeMillis());
                newChatUser.setLastMessage(newMessageItem.getMessageBody());
                newChatUser.setSeen(false);
                chatUsersRef.setValue(newChatUser);
                newMessage.setText("");
            }
        });

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageItem messageItem = snapshot.getValue(MessageItem.class);
                Log.w("Here", "" + messageItem + "," + messageItem.getMessageBody() + "," + messageItem.getUserUid());
                messageItems.add(messageItem);
                chatAdapter.notifyItemInserted(messageItems.size() - 1);
                if (messageItems.size() - 2 >= 0)
                    chatAdapter.notifyItemChanged(messageItems.size() - 2);

                chat_recyclerview.smoothScrollToPosition(messageItems.size() - 1);
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(requireActivity(), R.id.nav_user_host_fragment).popBackStack(R.id.userHomeFragment,false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}
