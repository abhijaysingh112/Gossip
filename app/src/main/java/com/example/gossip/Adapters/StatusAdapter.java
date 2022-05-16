package com.example.gossip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gossip.Activities.ChatActivity;
import com.example.gossip.R;
import com.example.gossip.Models.Status;
import com.example.gossip.Models.UserStatus;
import com.example.gossip.databinding.ItemStatusBinding;
import java.util.ArrayList;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    Context context;

    ArrayList<UserStatus> userStatuses;

    public StatusAdapter(Context context,ArrayList<UserStatus> userStatuses){
        this.context=context;
        this.userStatuses=userStatuses;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        UserStatus userStatus =userStatuses.get(position);
        Status lastStatus=userStatus.getStatuses().get(userStatus.getStatuses().size()-1);
        Glide.with(context).load(lastStatus.getImageurl()).into(holder.binding.circleImage);
        holder.binding.circularstatusview.setPortionsCount(userStatus.getStatuses().size());
        holder.binding.textView23.setText(userStatus.getName());
    holder.binding.circularstatusview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<MyStory> myStories = new ArrayList<>();
            for(Status status : userStatus.getStatuses()){
                myStories.add(new MyStory(status.getImageurl()));
            }
            new StoryView.Builder(((ChatActivity)context).getSupportFragmentManager())
                    .setStoriesList(myStories) // Required
                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                    .setTitleText(userStatus.getName()) // Default is Hidden
                    .setSubtitleText("") // Default is Hidden
                    .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            //your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {
                            //your action
                        }
                    }) // Optional Listeners
                    .build() // Must be called before calling show method
                    .show();
        }
    });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }


    public class StatusViewHolder extends RecyclerView.ViewHolder{
        ItemStatusBinding binding;
        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemStatusBinding.bind(itemView);
        }
    }

}
