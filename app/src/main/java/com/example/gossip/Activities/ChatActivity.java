package com.example.gossip.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gossip.fragment.ChatsFragment;
import com.example.gossip.R;
import com.example.gossip.fragment.StatusFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class ChatActivity extends AppCompatActivity {
    ChatsFragment chatFragment = new ChatsFragment();
    StatusFragment statusFragment= new StatusFragment();
    ActivityChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().show();

     binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public boolean onItemSelect(int i) {
             FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
             switch (i){
                 case 0:
                     transaction.replace(R.id.framelayout,new ChatsFragment());
                     break;
                 case 1:
                     transaction.replace(R.id.framelayout,new StatusFragment());
                     break;
                 case 2:
                     Intent intent = new Intent(ChatActivity.this,VideoActivity.class);
                     startActivity(intent);
                     break;
             }
             transaction.commit();
             return false;
         }
     });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.Search){
            Toast.makeText(getApplicationContext(),"search clicked",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.Profile){
            Toast.makeText(getApplicationContext(),"Profile clicked",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.Settings){
            Toast.makeText(getApplicationContext(),"settings clicked",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.Help){
            Toast.makeText(getApplicationContext(),"help clicked",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.AboutUs){
            Toast.makeText(getApplicationContext(),"Aboutus clicked",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}


