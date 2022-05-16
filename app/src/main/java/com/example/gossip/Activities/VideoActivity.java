package com.example.gossip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gossip.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoActivity extends AppCompatActivity {
 Button b,b1;
 EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        b=findViewById(R.id.button6);
        b1=findViewById(R.id.button7);
        et=findViewById(R.id.editTextTextPersonName3);

        URL serverURL;
        try{
            serverURL=new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                        .setRoom(et.getText().toString())
                        .setWelcomePageEnabled(false)
                        .build();

                JitsiMeetActivity.launch(VideoActivity.this,options);
            }
        });

      b1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String msg="Hey Buddy!!! Join the meeting in Gossip by entering this Code:";
              String string = et.getText().toString();
              String code=msg+string;
              Intent intent =new Intent();
              intent.setAction(Intent.ACTION_SEND);
              intent.putExtra(Intent.EXTRA_TEXT,code);
              intent.setType("text/plain");
              startActivity(intent);
          }
      });



    }
}