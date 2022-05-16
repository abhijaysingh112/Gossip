package com.example.gossip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gossip.databinding.ActivityRowchatBinding;

public class Rowchat extends AppCompatActivity {
    ActivityRowchatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRowchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}