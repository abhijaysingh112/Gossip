package com.example.gossip.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gossip.Models.Message;
import com.example.gossip.Adapters.MessageAdapter;
import com.example.gossip.databinding.ActivityGossipBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GossipActivity extends AppCompatActivity {
ActivityGossipBinding binding;
MessageAdapter adapter;
ArrayList<Message> messages;
FirebaseDatabase database;
String SenderRoom,ReceiverRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGossipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messages=new ArrayList<>();
        adapter=new MessageAdapter(this,messages);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(adapter);
        String user = getIntent().getStringExtra("name");
        String receiverUid= getIntent().getStringExtra("uid");
        String token = getIntent().getStringExtra("token");
        String senderUid = FirebaseAuth.getInstance().getUid() ;
        SenderRoom=senderUid+receiverUid;
        ReceiverRoom=receiverUid+senderUid;
database=FirebaseDatabase.getInstance();

database.getReference().child("chats")
        .child(SenderRoom)
        .child("messages")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText= binding.editTextTextPersonName2.getText().toString();

                Date date=new Date();
                Message message=new Message(messageText, senderUid, date.getTime());
                binding.editTextTextPersonName2.setText("");

                HashMap<String,Object> lastmsg = new HashMap<>();
                lastmsg.put("lastMsg",message.getMessage());
                lastmsg.put("lastMsgTime",date.getTime());
                database.getReference().child("chats").child(SenderRoom).updateChildren(lastmsg);
                database.getReference().child("chats").child(ReceiverRoom).updateChildren(lastmsg);

        database.getReference().child("chats")
                .child(SenderRoom)
                .child("messages")
                .push()
                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.getReference().child("chats")
                        .child(ReceiverRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotification(user,message.getMessage(),token);

                    }
                });


            }
        });
            }
        });
        getSupportActionBar().setTitle(user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
void sendNotification(String user,String message,String token){
        try{
    RequestQueue queue= Volley.newRequestQueue(this);
    String url = "https://fcm.googleapis.com/fcm/send";

    JSONObject data = new JSONObject();
    data.put("title",user);
    data.put("body",message);
    JSONObject notificationData = new JSONObject();
    notificationData.put("notification",data);
    notificationData.put("to",token);

            JsonObjectRequest request= new JsonObjectRequest(url, notificationData
                    , new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(GossipActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(GossipActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    String key = "Key=AAAA1hv_mIg:APA91bE9cI4WCT0qta5ljPEUPYQzJSXoOzmC3rQEdy-RdasXyKKxcC3WxKaerWaA0-Iqvq0Af6uuDMouZyD560J5pSqT8w_PShMWkkpgEzVW7nPh4i6N_Bmak0PdQy_rlvnC8I2gyyou";
                    map.put("Authorization",key);
                    map.put("Content-Type","application/json");
                    return map;
                }
            };

            queue.add(request);

        }catch (Exception ex){

        }
}
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
