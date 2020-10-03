package com.example.brsons.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brsons.R;
import com.example.brsons.adapter.RecyclerAdapter;
import com.example.brsons.pojo.ImageUploadInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    List<ImageUploadInfo> list = new ArrayList<>();
    SearchView searchView;
    private RecyclerView.Adapter recyclerAdapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        searchView = findViewById(R.id.searchJewelry);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        progressDialog = new ProgressDialog(ImageViewActivity.this);
        progressDialog.setMessage("Loading Images..");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference(ImageUploadActivity.Database_Path);
        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = snapshot.getValue(ImageUploadInfo.class);
                    list.add(imageUploadInfo);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(recyclerAdapter);
                progressDialog.dismiss();
                final RecyclerAdapter adapter = new RecyclerAdapter(ImageViewActivity.this, list);
                recyclerView.setAdapter(adapter);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }
}
