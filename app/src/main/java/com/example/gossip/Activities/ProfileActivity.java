package com.example.gossip.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gossip.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    
    ActivityProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog= new ProgressDialog(this);
        dialog.setMessage("Please Wait we are Setting up Your Profile ");
        dialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();



        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);

            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =binding.editTextTextPersonName.getText().toString();
                if(name.isEmpty()){
                    binding.editTextTextPersonName.setError("Please Enter your Name ");
                    return;
                }
                dialog.show();
                if (selectedImage!=null){
                    StorageReference reference= storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                       reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               String ImageUrl= uri.toString();
                               String uid= auth.getUid();
                               String phone= auth.getCurrentUser().getPhoneNumber();
                               String name= binding.editTextTextPersonName.getText().toString();
                               User user = new User(uid,name,phone,ImageUrl);
                               database.getReference()
                                       .child("users")
                                       .child(uid)
                                       .setValue(user)
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               Toast.makeText(getApplicationContext(),"Welcome"+name,Toast.LENGTH_SHORT).show();
                                               dialog.dismiss();
                                               Intent intent= new Intent(ProfileActivity.this,ChatActivity.class);
                                         startActivity(intent);
                                         finish();
                                           }
                                       });
                           }
                       });
                            }
                        }
                    });
                }else
                {
                    String uid= auth.getUid();
                    String phone= auth.getCurrentUser().getPhoneNumber();

                    User user = new User(uid,name,phone,"No Image Found");
                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent= new Intent(ProfileActivity.this,ChatActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if(data != null){
    if(data.getData()!= null){
        binding.imageView.setImageURI(data.getData());
        selectedImage=data.getData();
    }
}
    }
}