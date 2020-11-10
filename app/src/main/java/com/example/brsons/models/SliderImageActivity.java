package com.example.brsons.models;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brsons.R;
import com.example.brsons.pojo.SliderImageInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Optional;

public class SliderImageActivity extends AppCompatActivity {

    final String Storage_Path = "Slider_Image/";
    static final String Database_Path = "Sliders";
    ImageView SliderImage1, SliderImage2, SliderImage3;
    EditText SliderTitle1,SliderTitle2,SliderTitle3,SliderDes1,SliderDes2,SliderDes3;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    int Image_Request_Code1 = 8;
    int Image_Request_Code2 = 9;
    ProgressDialog progressDialog;
    Button UploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image);

        UploadButton = (Button) findViewById(R.id.sliderUploadBtn);

        SliderTitle1 = (EditText) findViewById(R.id.sliderTitle1);
        SliderTitle2 = (EditText) findViewById(R.id.sliderTitle2);
        SliderTitle3 = (EditText) findViewById(R.id.sliderTitle3);
        SliderDes1 = (EditText) findViewById(R.id.sliderDesc1);
        SliderDes2 = (EditText) findViewById(R.id.sliderDesc2);
        SliderDes3 = (EditText) findViewById(R.id.sliderDesc3);

        SliderImage1 = (ImageView) findViewById(R.id.sliderImage1);
        SliderImage2 = (ImageView) findViewById(R.id.sliderImage2);
        SliderImage3 = (ImageView) findViewById(R.id.sliderImage3);

        progressDialog = new ProgressDialog(SliderImageActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SliderImage1.setOnClickListener(new View.OnClickListener() {
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

        SliderImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SliderImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code2);
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
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null) {
            FilePathUri = data.getData();
            if (FilePathUri != null) {
                SliderImage1.setImageURI(FilePathUri);
            }
        }
        if (requestCode == Image_Request_Code1 && resultCode == RESULT_OK && data != null) {
            FilePathUri = data.getData();
            if (FilePathUri != null) {
                SliderImage2.setImageURI(FilePathUri);
            }
        }
        if (requestCode == Image_Request_Code2 && resultCode == RESULT_OK && data != null) {
            FilePathUri = data.getData();
            if (FilePathUri != null) {
                SliderImage3.setImageURI(FilePathUri);
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
    private void UploadImageFileToFirebaseStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Optional.ofNullable(FilePathUri).isPresent()) {
            progressDialog.setTitle("Slider Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String TempSliderTitle1 = SliderTitle1.getText().toString().trim();
                                    String TempSliderTitle2 = SliderTitle2.getText().toString().trim();
                                    String TempSliderTitle3 = SliderTitle3.getText().toString().trim();
                                    String TempSliderDesc1 = SliderDes1.getText().toString().trim();
                                    String TempSliderDesc2 = SliderDes2.getText().toString().trim();
                                    String TempSliderDesc3 = SliderDes3.getText().toString().trim();

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    SliderImageInfo sliderImageInfo = new SliderImageInfo(TempSliderTitle1, TempSliderTitle2,
                                            TempSliderTitle3, TempSliderDesc1, TempSliderDesc2,TempSliderDesc3,
                                            uri.toString(), uri.toString(), uri.toString());
                                    String SliderUploadId = databaseReference.child("Slider1").getKey();
                                    databaseReference.child(SliderUploadId).setValue(sliderImageInfo);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(SliderImageActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("Slider Image is Uploading...");
                }
            });
        }
      //  startActivity(new Intent(SliderImageActivity.this, SliderImageActivity.class));
    }
}
