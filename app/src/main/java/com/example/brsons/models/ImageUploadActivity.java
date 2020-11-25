package com.example.brsons.models;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.brsons.pojo.ImageUploadInfo;
import com.example.brsons.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

public class ImageUploadActivity extends AppCompatActivity {

    final String Storage_Path = "Jewellery_Image/";
    static final String Database_Path = "Jewelleries";
    static final String OPTIONS = "Options";
    boolean imageLatest;
    String selectedCategory;
    String selectedSubCategory;
    String selectedSubCategory1;
    String selectedSubCategory2;
    Button uploadButton;
    EditText imageName;
    ImageView selectImage;
    Spinner imageCategory, imageSubCategory, imageSubCategory1, imageSubCategory2;
    CheckBox latestProduct;
    Uri filePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference optionsReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    List<String> mainCategoryKeys = new ArrayList();
    Map<String, Map<String, List<String>>> optionsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        Toolbar toolbar = findViewById(R.id.toolbarUpload);
        setSupportActionBar(toolbar);

        uploadButton = (Button) findViewById(R.id.ButtonUploadImage);
        imageName = (EditText) findViewById(R.id.ImageNameEditText);
        imageCategory = (Spinner) findViewById(R.id.spinnerCategory);
        imageSubCategory = (Spinner) findViewById(R.id.spinnerSubCategory);
        imageSubCategory1 = (Spinner) findViewById(R.id.spinnerSubCategory1);
        latestProduct = (CheckBox) findViewById(R.id.checkBox);
        selectImage = (ImageView) findViewById(R.id.ShowImageView);
        progressDialog = new ProgressDialog(ImageUploadActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        optionsReference = FirebaseDatabase.getInstance().getReference().child(OPTIONS);

        Intent startingIntent = getIntent();
        if (getIntent().hasExtra("imageURL")) {
            String imgURL = startingIntent.getStringExtra("imageURL");
            String imgName = startingIntent.getStringExtra("imageName");
            int imgCategory = startingIntent.getIntExtra("imageCategory",0);
            int imgSubCategory = startingIntent.getIntExtra("imageSubCategory", 0);
            int imgSubCategory1 = startingIntent.getIntExtra("imageSubCategory1", 0);
            Glide.with(this).load(imgURL).into(selectImage);
            imageName.setText(imgName);
            imageCategory.setSelection(imgCategory);
            imageSubCategory.setSelection(imgSubCategory, true);
            imageSubCategory1.setSelection(imgSubCategory1, true);

            Boolean imgLat = startingIntent.getBooleanExtra("imageLatest",true);
            if (imgLat.equals(true)) {
                latestProduct.setChecked(true);
            } else {
                latestProduct.setChecked(false);
            }
        }

        setMainCategories();
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectedCategory = parent.getItemAtPosition(i).toString();
                setSubCategories(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imageSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectedCategory = imageCategory.getSelectedItem().toString().trim();
                String selectedSubCategory = parent.getItemAtPosition(i).toString();
                setSuperSubCategories(selectedCategory, selectedSubCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * Calling method to upload selected image on Firebase storage.
         */
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UploadImageFileToFirebaseStorage();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                selectImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function to return image extension
     *
     * @param uri
     * @return
     */
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Method to store the uploaded image in fire base storage location
     */
    public void UploadImageFileToFirebaseStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Optional.ofNullable(filePathUri).isPresent()) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(filePathUri));
            storageReference2nd.putFile(filePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String TempImageName = imageName.getText().toString().trim();
                                    String TempCategoryName = imageCategory.getSelectedItem().toString().trim();
                                    String TempSubCategoryName = imageSubCategory.getSelectedItem().toString().trim();
                                    String TempSubCategoryName1 = imageSubCategory1.getSelectedItem().toString().trim();
                                    if (latestProduct.isChecked()) {
                                        //latestProduct.setEnabled(true);
                                        imageLatest = true;
                                    }else {
                                        imageLatest = false;
                                    }
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempCategoryName,
                                            TempSubCategoryName, TempSubCategoryName1, imageLatest, uri.toString());
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                    refresh();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(ImageUploadActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("Image is Uploading...");
                }
            });
        }

     //  startActivity(new Intent(ImageUploadActivity.this, ImageUploadActivity.class));
    }

    /**
     * to setMain categories and map all the related categories to min categories
     */
    public void setMainCategories() {
        ValueEventListener options = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mainCategories : snapshot.getChildren()) {
                    mainCategoryKeys.add(mainCategories.getKey());
                    addOptions(mainCategoryKeys, imageCategory);
                    Map<String, List<String>> subCategoriesMap = new HashMap<>();
                    for (DataSnapshot subCategories : mainCategories.getChildren()) {
                        List<String> superSubCategoriesList = new ArrayList<>();
                        if (subCategories.hasChildren()) {
                            for (DataSnapshot superSubCategories : subCategories.getChildren()) {
                                superSubCategoriesList.add(superSubCategories.getKey());
                                subCategoriesMap.put(subCategories.getKey(), superSubCategoriesList);
                            }
                        } else {
                            subCategoriesMap.put(subCategories.getKey(), new ArrayList<String>());
                        }
                        optionsMap.put(mainCategories.getKey(), subCategoriesMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        optionsReference.addValueEventListener(options);

    }

    /**
     * To populate all the subCategories of the selected categories
     *
     * @param selectedCategory
     */
    public void setSubCategories(String selectedCategory) {
        List<String> subCategories = new ArrayList<>();
        for (String optionsKey : optionsMap.get(selectedCategory).keySet()) {
            subCategories.add(optionsKey);
        }
        imageSubCategory.setVisibility(View.VISIBLE);
        addOptions(subCategories, imageSubCategory);
    }

    /**
     * To populate all the superSubCategories of the selected subCategories
     *
     * @param selectedSubCategory
     */
    public void setSuperSubCategories(String selectedCategory, String selectedSubCategory) {
        List<String> superSubCategories = new ArrayList<>();
        for (ListIterator<String> it = optionsMap.get(selectedCategory).get(selectedSubCategory).listIterator(); it.hasNext(); ) {
            superSubCategories.add(it.next());
        }
        if (superSubCategories.size() > 0) {
            imageSubCategory1.setVisibility(View.VISIBLE);
            addOptions(superSubCategories, imageSubCategory1);
        } else {
            imageSubCategory1.setVisibility(View.GONE);
        }
    }

    /*
    Populate values to spinner
     */
    public void addOptions(List<String> keys, Spinner spinner) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ImageUploadActivity.this, R.layout.simple_spinner_item, keys);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(dataAdapter);
        spinner.setPrompt("Categories");
    }
}

