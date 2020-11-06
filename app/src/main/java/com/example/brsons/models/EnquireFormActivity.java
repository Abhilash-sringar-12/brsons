package com.example.brsons.models;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.brsons.BuildConfig;
import com.example.brsons.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class EnquireFormActivity extends AppCompatActivity {

    EditText clientName,clientEmail, productName, grams, gramPrice, gstValue, itemAmount, itemTotal;
    Button generateButton, openButton;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_form);

        clientName = (EditText) findViewById(R.id.client_name_text);
        clientEmail = (EditText) findViewById(R.id.client_email_text);
        productName = (EditText) findViewById(R.id.product_name_text);
        grams = (EditText) findViewById(R.id.billing_gram_text);
        gramPrice = (EditText) findViewById(R.id.billing_price_text);
        gstValue = (EditText) findViewById(R.id.billing_gst_text);
        itemAmount = (EditText) findViewById(R.id.billing_amount_text);
        itemTotal = (EditText) findViewById(R.id.billing_total_text);
        generateButton = (Button) findViewById(R.id.invoice_save_button);
        openButton = (Button) findViewById(R.id.invoice_open_button);

        progressDialog = new ProgressDialog(EnquireFormActivity.this);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openPdf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        int permission = ActivityCompat.checkSelfPermission(EnquireFormActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    EnquireFormActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }

    private void createPdf() {
        try {
            progressDialog.setTitle("Quotation is Generating...");
            progressDialog.show();
            String outpath=Environment.getExternalStorageDirectory()+"/quotation.pdf";
            Document document = new Document();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brs_logo);

            PdfWriter.getInstance(document, new FileOutputStream(outpath));
            document.open();
            float[] pointColumnWidths = {150f, 150f};
            PdfPTable table = new PdfPTable(pointColumnWidths);
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            table.setWidthPercentage(100f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image bitmapImage = Image.getInstance(stream1.toByteArray());
            PdfPCell imageCell = new PdfPCell();
            PdfPCell headingCell = new PdfPCell(new Phrase("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Estimate/Quotation", boldFont));
            headingCell.setColspan(2);
            imageCell.addElement(bitmapImage);
            table.addCell(imageCell);
            table.addCell("\t\t\tBR and Sons Jewellery\t\t\t\n" +
                    "\t\t\t24, 3rd Main Road, 3rd Cross, Chamrajpet,\t\t\t\n" +
                    "\t\t\tBengaluru 560018, Karnataka, INDIA. \t\t\t\n" +
                    "\t\t\tPhone: 8660093768\t\t\t\n" +
                    "\t\t\tE-mail: brsjewellery916@gmail.com\t\t\t");
            table.addCell(headingCell);
            table.addCell("Client Name :");
            table.addCell(clientName.getText().toString());
            table.addCell("Client Email :");
            table.addCell(clientEmail.getText().toString());
            PdfPCell itemsCell = new PdfPCell(new Phrase("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tItem Details", boldFont));
            itemsCell.setColspan(2);
            table.addCell(itemsCell);
            float[] itemColumnWidths = {150f, 150f, 150f,150f,150f,150f};
            PdfPTable itemTable = new PdfPTable(itemColumnWidths);
            itemTable.addCell("SL NO:");
            itemTable.addCell("Item Name:");
            itemTable.addCell("Grams:");
            itemTable.addCell("Price/gram:");
            itemTable.addCell("GST:");
            itemTable.addCell("Amount:");
            itemTable.addCell("1");
            itemTable.addCell(productName.getText().toString());
            itemTable.addCell(grams.getText().toString());
            itemTable.addCell(gramPrice.getText().toString());
            itemTable.addCell(gstValue.getText().toString());
            itemTable.addCell(itemAmount.getText().toString());
            PdfPCell commentCell = new PdfPCell(new Phrase(" "));
            commentCell.setColspan(3);
            commentCell.setMinimumHeight(100);
            itemTable.addCell(commentCell);
            PdfPCell totalCell = new PdfPCell(new Phrase("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTotal:", boldFont));
            totalCell.setColspan(2);
            itemTable.addCell(totalCell);
            itemTable.addCell(itemTotal.getText().toString());
            itemTable.setWidthPercentage(100);
            document.add(table);
            document.add(itemTable);
            document.close();

            progressDialog.dismiss();
            Toast.makeText(EnquireFormActivity.this, "Created...", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        clientName.setText("");
        clientEmail.setText("");
        productName.setText("");
        grams.setText("");
        gramPrice.setText("");
        gstValue.setText("");
        itemAmount.setText("");
        itemTotal.setText("");
    }

    public void openPdf()
    {
        File file = null;
        file = new File(Environment.getExternalStorageDirectory()+ "/quotation.pdf");
        Toast.makeText(getApplicationContext(), file.toString() , Toast.LENGTH_LONG).show();
        if(file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(FileProvider.getUriForFile(EnquireFormActivity.this, BuildConfig.APPLICATION_ID + ".provider",file), "application/pdf");
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        else
            Toast.makeText(getApplicationContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();
    }
}
