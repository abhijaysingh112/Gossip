package com.example.gossip.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gossip.Adapters.UsersAdapter;
import com.example.gossip.R;
import com.example.gossip.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatsFragment extends Fragment {

    FirebaseDatabase database;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    RecyclerView rv;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_chat, container, false);
        rv=view.findViewById(R.id.recyclerview);
             FirebaseMessaging.getInstance()
                     .getToken()
                     .addOnSuccessListener(new OnSuccessListener<String>() {
                         @Override
                         public void onSuccess(String token) {
                             HashMap<String,Object> map = new HashMap<>();
                             map.put("token",token);
                             database.getReference()
                                     .child("users")
                                     .child(FirebaseAuth.getInstance().getUid())
                                     .updateChildren(map);
                             //Toast.makeText(getContext(),token,Toast.LENGTH_SHORT).show();
                         }
                     });
            database = FirebaseDatabase.getInstance();
            users = new ArrayList<>();
            usersAdapter = new UsersAdapter(getContext(), users);
            rv.setAdapter(usersAdapter);

            database.getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                             users.add(user);
                    }
                    usersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        return view;
    }
}