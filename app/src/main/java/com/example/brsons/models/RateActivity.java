package com.example.brsons.models;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brsons.R;
import com.example.brsons.pojo.RateInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateActivity extends AppCompatActivity {

    Button submit;
    EditText GoldRate, SilverRate;
    String goldRate, silverRate;

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        submit = (Button) findViewById(R.id.submit);
        GoldRate = (EditText) findViewById(R.id.goldRate);
        SilverRate = (EditText) findViewById(R.id.silverRate);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(RateActivity.this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateInfo rateInfo = new RateInfo();
                GetRateData();
                rateInfo.setGoldRate(goldRate);
                rateInfo.setSilverRate(silverRate);
                databaseReference.child("Rates").setValue(rateInfo);

                Toast.makeText(RateActivity.this,"Gold and Silver Rates are added", Toast.LENGTH_LONG).show();
                GoldRate.setText("");
                SilverRate.setText("");

            }
        });



    }

    private void GetRateData() {

        goldRate = GoldRate.getText().toString().trim();

        silverRate = SilverRate.getText().toString().trim();
    }
}
