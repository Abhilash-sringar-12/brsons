package com.example.brsons.models;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brsons.R;
import com.example.brsons.pojo.RateInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RateActivity extends AppCompatActivity {

    Button submit;
    EditText GoldRate, SilverRate;
    String goldRate, silverRate;
    TextView RateGold, RateSilver;

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

        RateGold = (TextView) findViewById(R.id.rateGold);
        RateSilver = (TextView) findViewById(R.id.rateSilver);

        getDataFromFirebase();

        /**
         * Calling method to upload rates data to Firebase database.
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateInfo rateInfo = new RateInfo();
                GetRateData();
                rateInfo.setGoldRate(goldRate);
                rateInfo.setSilverRate(silverRate);
                databaseReference.child("Rates").setValue(rateInfo);

                Toast.makeText(RateActivity.this, "Gold and Silver Rates are added", Toast.LENGTH_LONG).show();
                GoldRate.setText("");
                SilverRate.setText("");

            }
        });
    }

    /**
     * Calling method to retreive Rates from Firebase database.
     */
    private void getDataFromFirebase() {
        DatabaseReference mRef = databaseReference.child("Rates");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gold = snapshot.child("goldRate").getValue().toString();
                String silver = snapshot.child("silverRate").getValue().toString();
                RateGold.setText(gold);
                RateSilver.setText(silver);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void GetRateData() {

        goldRate = GoldRate.getText().toString().trim();

        silverRate = SilverRate.getText().toString().trim();
    }
}
