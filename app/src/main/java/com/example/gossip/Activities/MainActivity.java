package com.example.gossip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gossip.R;

public class MainActivity extends AppCompatActivity {
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        b=findViewById(R.id.button3);
       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent= new Intent(MainActivity.this,PhoneActivity.class);
               startActivity(intent);
           }
       });


    }
}