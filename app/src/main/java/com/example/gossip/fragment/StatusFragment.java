package com.example.gossip.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gossip.Adapters.StatusAdapter;
import com.example.gossip.R;
import com.example.gossip.Models.Status;
import com.example.gossip.Models.User;
import com.example.gossip.Models.UserStatus;
import com.example.gossip.databinding.ActivityStatusBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StatusFragment extends Fragment {
    FirebaseDatabase database;
    ActivityStatusBinding binding;
    FirebaseAuth auth;
    StatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog pd;
    RecyclerView rv;
    Button b;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_status, container, false);
        rv=view.findViewById(R.id.recyclerview1);
        b=view.findViewById(R.id.button5);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading Image...");
        pd.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutManager);
        userStatuses = new ArrayList<>();

        database.getReference().child("users").child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        statusAdapter = new StatusAdapter(getContext(), userStatuses);
        rv.setAdapter(statusAdapter);


        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot MomentSnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setName(MomentSnapshot.child("name").getValue(String.class));
                        status.setProfileImage(MomentSnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(MomentSnapshot.child("lastUpdated").getValue(long.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for (DataSnapshot StatusSnapshot : MomentSnapshot.child("statuses").getChildren()) {
                            Status sampleStatus = StatusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);


                        }
                        status.setStatuses(statuses);
                        userStatuses.add(status);

                    }
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 75);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getData() != null) {
                pd.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String ImageUrl = uri.toString();
                                    Status status = new Status(ImageUrl, userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    pd.dismiss();


                                }
                            });
                        }
                    }
                });

            }

        }
    }

}