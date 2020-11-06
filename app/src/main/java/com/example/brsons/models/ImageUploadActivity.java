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

import com.example.brsons.pojo.ImageUploadInfo;
import com.example.brsons.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Optional;

public class ImageUploadActivity extends AppCompatActivity {

    final String Storage_Path = "Jewellery_Image/";
    static final String Database_Path = "Jewelleries";
    String imageLatest, selectedCategory, selectedSubCategory, selectedSubCategory1, selectedSubCategory2;
    Button ChooseButton, UploadButton;
    EditText ImageName;
    ImageView SelectImage;
    Spinner ImageCategory, ImageSubCategory, ImageSubCategory1, ImageSubCategory2;
    CheckBox LatestProduct;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        ChooseButton = (Button) findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button) findViewById(R.id.ButtonUploadImage);
        ImageName = (EditText) findViewById(R.id.ImageNameEditText);
        ImageCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ImageSubCategory = (Spinner) findViewById(R.id.spinnerSubCategory);
        ImageSubCategory1 = (Spinner) findViewById(R.id.spinnerSubCategory1);
        LatestProduct = (CheckBox) findViewById(R.id.checkBox);
        SelectImage = (ImageView) findViewById(R.id.ShowImageView);
        progressDialog = new ProgressDialog(ImageUploadActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Calling method to upload selected image on Firebase storage.
         */
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UploadImageFileToFirebaseStorage();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectedCategory = parent.getItemAtPosition(i).toString();
                switch (selectedCategory) {
                    case "Gold":
                        ImageSubCategory.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,getResources()
                                .getStringArray(R.array.Category_gold)));
                        ImageSubCategory.setVisibility(View.VISIBLE);
                        break;

                    case "Silver":
                        ImageSubCategory.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,getResources()
                                .getStringArray(R.array.Category_silver)));
                        ImageSubCategory.setVisibility(View.VISIBLE);
                        break;

                    case "Platinum":
                        ImageSubCategory.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,getResources()
                                .getStringArray(R.array.Category_platinum)));
                        ImageSubCategory.setVisibility(View.VISIBLE);
                        break;

                    case "Diamond":
                        ImageSubCategory.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,getResources()
                                .getStringArray(R.array.Category_daimond)));
                        ImageSubCategory.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                 selectedSubCategory = parent.getItemAtPosition(i).toString();

                switch (selectedSubCategory) {
                    case "Jewellery":
                        ImageSubCategory.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.Category_gold)));
                        //ImageSubCategory.setVisibility(View.VISIBLE);
                        break;

                    case "Articles":
                        ImageSubCategory1.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.Category_silver_articles)));
                        ImageSubCategory1.setVisibility(View.VISIBLE);
                        break;

                    case "Men":
                        ImageSubCategory1.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.Category_sub_men)));
                        ImageSubCategory1.setVisibility(View.VISIBLE);
                        break;

                    case "Women":
                        ImageSubCategory1.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.Category_sub_women)));
                        ImageSubCategory1.setVisibility(View.VISIBLE);
                        break;

                    case "Children":
                        ImageSubCategory1.setAdapter(new ArrayAdapter<String>(ImageUploadActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, getResources()
                                .getStringArray(R.array.Category_sub_children)));
                        ImageSubCategory1.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageSubCategory1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                 selectedSubCategory1 = parent.getItemAtPosition(i).toString();

                Toast.makeText(ImageUploadActivity.this, "\n Category: \t " + selectedCategory +
                        "\n SubCategory: \t" + selectedSubCategory + "\n SubCategory: \t" + selectedSubCategory1
                        ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                SelectImage.setImageBitmap(bitmap);
                ChooseButton.setText("Image Selected");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Optional.ofNullable(FilePathUri).isPresent()) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String TempImageName = ImageName.getText().toString().trim();
                                    String TempCategoryName = ImageCategory.getSelectedItem().toString().trim();
                                    String TempSubCategoryName = ImageSubCategory.getSelectedItem().toString().trim();
                                    String TempSubCategoryName1 = ImageSubCategory1.getSelectedItem().toString().trim();
                                    if (LatestProduct.isChecked()) {
                                        imageLatest = LatestProduct.getText().toString().trim();
                                    }
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempCategoryName,
                                            TempSubCategoryName,TempSubCategoryName1, imageLatest, uri.toString());
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
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
       startActivity(new Intent(ImageUploadActivity.this, ImageUploadActivity.class));
    }
}

