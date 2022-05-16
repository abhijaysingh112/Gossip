package com.example.gossip.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gossip.Activities.GossipActivity;
import com.example.gossip.R;
import com.example.gossip.Models.User;
import com.example.gossip.databinding.ActivityRowchatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    ArrayList<User> users;

    public UsersAdapter(Context context,ArrayList<User> users){
        this.context = context;
        this.users= users;


    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view= LayoutInflater.from(context).inflate(R.layout.activity_rowchat,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
          User user=users.get(position);
          String senderId= FirebaseAuth.getInstance().getUid();
          String senderRoom=senderId+user.getUid();
        FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);


                                            holder.binding.textView10.setText(lastMsg);
                                        }else{
                                            holder.binding.textView10.setText("Tap to chat");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

holder.binding.textView8.setText(user.getName());
Glide.with(context).load(user.getProfileImage())
        .placeholder(R.drawable.avatar)
        .into(holder.binding.imageView2);
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, GossipActivity.class);
        intent.putExtra("name",user.getName());
        intent.putExtra("uid",user.getUid());
        intent.putExtra("token",user.getToken());
        context.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ActivityRowchatBinding binding;
        public UsersViewHolder(@NonNull View itemView){
            super(itemView);
            binding=ActivityRowchatBinding.bind(itemView);
        }
    }
}

