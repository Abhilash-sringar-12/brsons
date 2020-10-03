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
    String imageLatest;
    Button ChooseButton, UploadButton;
    EditText ImageName;
    ImageView SelectImage;
    Spinner ImageCategory, ImageSubCategory;
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
     * Method to store the uploaded image in fire base storeage location
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
                                    if (LatestProduct.isChecked()) {
                                        imageLatest = LatestProduct.getText().toString().trim();
                                    }
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, TempCategoryName, TempSubCategoryName, imageLatest, uri.toString());
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
        Toast.makeText(ImageUploadActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
    }
}

