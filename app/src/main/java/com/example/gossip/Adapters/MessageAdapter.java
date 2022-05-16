package com.example.gossip.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gossip.Models.Message;
import com.example.gossip.R;
import com.example.gossip.databinding.MessageReceivedBinding;
import com.example.gossip.databinding.MessageSentBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Message> messages;
    final int MESSAGE_SENT=1;
    final int MESSAGE_RECEIVED=2;

    public MessageAdapter(Context context , ArrayList<Message> messages){
        this.context= context;
        this.messages=messages;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MESSAGE_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
            return new SentViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.message_received,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return MESSAGE_SENT;
        } else {
            return MESSAGE_RECEIVED;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message= messages.get(position);
if(holder.getClass()== SentViewHolder.class){
    SentViewHolder viewHolder=(SentViewHolder)holder;
    viewHolder.binding.textView13.setText(message.getMessage());
}else {
    ReceiverViewHolder viewHolder= (ReceiverViewHolder)holder;
    viewHolder.binding.textView14.setText(message.getMessage());
}
    }

    @Override
    public int getItemCount() {

        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        MessageSentBinding binding;
         public SentViewHolder(@NonNull View itemView){
             super(itemView);
             binding= MessageSentBinding.bind(itemView);
         }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        MessageReceivedBinding binding;
        public ReceiverViewHolder(@NonNull View itemView){
            super(itemView);
            binding=MessageReceivedBinding.bind(itemView);
        }
    }
}
