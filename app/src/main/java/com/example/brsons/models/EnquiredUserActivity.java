package com.example.brsons.models;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brsons.R;
import com.example.brsons.adapter.EnquiryAdapter;
import com.example.brsons.pojo.EnquiryInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnquiredUserActivity extends AppCompatActivity {

    private RecyclerView enquiredRecyclerView;
    private RecyclerView.Adapter enquiryRecyclerAdapter;
    DatabaseReference mbase;
    ProgressDialog progressDialog;
    SearchView searchView;

    private List<EnquiryInfo> enquiryData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_users);

        searchView = findViewById(R.id.searchUser);
        enquiredRecyclerView = findViewById(R.id.enquiredRecyclerView);
        enquiredRecyclerView.setHasFixedSize(true);
        enquiredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(EnquiredUserActivity.this);
        progressDialog.setMessage("Loading Enquired user details..");
        progressDialog.show();

        mbase = FirebaseDatabase.getInstance().getReference("Enquiries");
        GetDetailsFromFirebase();

    }

    /**
     * Calling method to retreive enquired users from Firebase database.
     */
    private void GetDetailsFromFirebase() {

        mbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()){
                        EnquiryInfo enquiryInfo=npsnapshot.getValue(EnquiryInfo.class);
                        enquiryData.add(enquiryInfo);
                    }
                    enquiryRecyclerAdapter = new EnquiryAdapter(getApplicationContext(),enquiryData );
                    enquiredRecyclerView.setAdapter(enquiryRecyclerAdapter);
                    progressDialog.dismiss();
                    final EnquiryAdapter adapter = new EnquiryAdapter(EnquiredUserActivity.this, enquiryData);
                    enquiredRecyclerView.setAdapter(adapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            adapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}