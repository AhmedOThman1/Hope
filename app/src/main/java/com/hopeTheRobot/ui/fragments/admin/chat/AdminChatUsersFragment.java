package com.hopeTheRobot.ui.fragments.admin.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hopeTheRobot.R;
import com.hopeTheRobot.RecyclerViewTouchListener;
import com.hopeTheRobot.adapters.ChatUsersAdapter;
import com.hopeTheRobot.pojo.ChatUser;
import com.hopeTheRobot.pojo.UserItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AdminChatUsersFragment extends Fragment {

    View view;
    FirebaseDatabase database;
    DatabaseReference allUsersRef, chatUsersRef;
    ArrayList<ChatUser> chatUsers = new ArrayList<>();
    Map<String, UserItem> usersItems = new HashMap<>();

    ChatUsersAdapter chatUsersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_chat_users, container, false);

        //show action bar
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Chats");


        database = FirebaseDatabase.getInstance();
        allUsersRef = database.getReference("Users");
        chatUsersRef = database.getReference("Chat").child("Users");
        RecyclerView chat_users_recycler = view.findViewById(R.id.chat_users_recycler);
        chatUsersAdapter = new ChatUsersAdapter(requireContext());
        chatUsersAdapter.setModels(chatUsers);
        chat_users_recycler.setAdapter(chatUsersAdapter);
        chat_users_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        chat_users_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), chat_users_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("chatUser", new Gson().toJson(chatUsers.get(position)));
                Navigation.findNavController(view).navigate(R.id.action_adminChatUsersFragment_to_adminChatFragment,bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllUsers();

        return view;
    }

    private void getAllUsers() {
        // Read from the database
        allUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersItems = new HashMap<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserItem user = dataSnapshot.getValue(UserItem.class);
                    Log.w("GetUSER", "user name is: " + user.getUsername());
                    usersItems.put(user.getUserId(), user);
                }
                getAllChatUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAllChatUsers() {
        // Read from the database
        chatUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatUsers = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);
                    Log.w("GetChatUSER", "user name is: " + chatUser.getUserName());
                    chatUser.setUserName(usersItems.get(chatUser.getUserUid()).getUsername());
                    chatUser.setUserPhoto(usersItems.get(chatUser.getUserUid()).getUserImage());
                    chatUsers.add(chatUser);
                }
                Collections.sort(chatUsers, new Comparator<ChatUser>() {
                    @Override
                    public int compare(ChatUser user1, ChatUser user2) {
                        return user1.getLastMessageDate().compareTo(user2.getLastMessageDate());
                    }
                });
                Collections.reverse(chatUsers);

                chatUsersAdapter.setModels(chatUsers);
                chatUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//
//                /* exit only if the user click the back button twice in the Main Activity**/
////                Navigation.findNavController(requireActivity(), R.id.nav_store_host_fragment).popBackStack(R.id.storeMaterialCountFragment, false);
////                StoreMainFragment.storeBottom_navigation.setSelectedItemId(R.id.nav_material_count);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }
}