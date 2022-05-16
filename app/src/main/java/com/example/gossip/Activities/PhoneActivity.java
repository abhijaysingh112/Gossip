package com.example.gossip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gossip.R;
import com.hbb20.CountryCodePicker;

public class PhoneActivity extends AppCompatActivity {
 Button b;
 EditText et;
 CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        getSupportActionBar().hide();
        b=findViewById(R.id.button4);
        et=findViewById(R.id.editTextNumber);
        ccp=findViewById(R.id.countryCodePicker);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String countryCode=ccp.getSelectedCountryCode();
                String num =et.getText().toString();
                String number="+"+countryCode+num;
                    if(num.isEmpty()) {
                       Toast.makeText(getApplicationContext(),"Please enter a number", Toast.LENGTH_SHORT).show();
                   }
                    else {
                    Intent intent = new Intent(PhoneActivity.this, OtpActivity.class);
                    intent.putExtra("MobileNumber", number);
                    startActivity(intent); }


            }
        });
    }
}
