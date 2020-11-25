package com.example.brsons.models;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.brsons.R;
import com.example.brsons.commons.MailUtility;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EnquiryFormActivity extends AppCompatActivity {

    private static final String MESSAGE_BODY = "Dear Customer,\n" +
            "Thank you for contacting us. \n " +
            "Please find the attachment of the requested quotation.\n" +
            "Any more quiries please free to contact us by email or through phone.\n" +
            "Thanks and Regards \n" +
            "BRSons and Jewellery  ";


    EditText clientName,clientEmail, clientPhone, productCategory, productDesc, grams, gramPrice, gstValue, itemAmount, itemTotal;
    Button generateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_form);


        clientName = (EditText) findViewById(R.id.client_name_text);
        clientEmail = (EditText) findViewById(R.id.client_email_text);
        clientPhone = (EditText) findViewById(R.id.client_phone_text);
        productCategory = (EditText) findViewById(R.id.product_category_text);
        productDesc = (EditText) findViewById(R.id.product_desc_text);
        grams = (EditText) findViewById(R.id.billing_gram_text);
        gramPrice = (EditText) findViewById(R.id.billing_price_text);
        gstValue = (EditText) findViewById(R.id.billing_gst_text);
        itemAmount = (EditText) findViewById(R.id.billing_amount_text);
        itemTotal = (EditText) findViewById(R.id.billing_total_text);
        generateButton = (Button) findViewById(R.id.invoice_save_button);

        final Intent startingIntent = getIntent();
        String name = startingIntent.getStringExtra("name");
        String email = startingIntent.getStringExtra("email");
        String phone = startingIntent.getStringExtra("phone");
        String category = startingIntent.getStringExtra("category");
        String description = startingIntent.getStringExtra("description");
        final String status = startingIntent.getStringExtra("status");

        clientName.setText(name);
        clientEmail.setText(email);
        productCategory.setText(category);
        productDesc.setText(description);
        clientPhone.setText(phone);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createPdf();
                Intent intent =  new Intent(EnquiryFormActivity.this, EnquiredUserActivity.class);
                intent.putExtra("resolved",status);
                startActivity(intent);
            }
        });


    }

    private void createPdf() {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            Drawable d = getResources().getDrawable(R.drawable.brs_logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleAbsolute(80f, 80f);
            image.setAlignment(Element.ALIGN_RIGHT);

            FontSelector fss = new FontSelector();
            Font fonts = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD);
            fss.addFont(fonts);
            Phrase bills = fss.process("BR and Sons Jewellery");// supplier information
            Paragraph sAddress = new Paragraph("24, 3rd Main Road, 3rd Cross, Chamrajpet, Bengaluru 560018.");
            sAddress.setIndentationRight(5);
            Paragraph scontact = new Paragraph("Phone no.: 8660093768");
            scontact.setIndentationRight(5);
            Paragraph semail = new Paragraph("Email: brsjewellery916@gmail.com");
            semail.setIndentationRight(5);

            Font red = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.RED);
            Chunk redText = new Chunk("Estimate/Quotation", red);
            Paragraph para = new Paragraph(redText);
            para.setAlignment(Element.ALIGN_CENTER);

            FontSelector fs = new FontSelector();
            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            fs.addFont(font);
            Phrase bill = fs.process("Quotation To"); // customer information
            Paragraph name = new Paragraph("Customer Name: " + clientName.getText().toString());
            name.setIndentationLeft(20);
            Paragraph contact = new Paragraph("Customer Phone: "+clientPhone.getText().toString());
            contact.setIndentationLeft(20);

            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);
            billTable.setWidths(new float[] { 1, 5,2,2,1,2 });
            billTable.setSpacingBefore(30.0f);
            billTable.addCell(getBillHeaderCell("Index"));
            billTable.addCell(getBillHeaderCell("Item Description"));
            billTable.addCell(getBillHeaderCell("Grams"));
            billTable.addCell(getBillHeaderCell("Price/gram"));
            billTable.addCell(getBillHeaderCell("GST"));
            billTable.addCell(getBillHeaderCell("Amount"));

            billTable.addCell(getBillRowCell("1"));
            billTable.addCell(getBillRowCell(productDesc.getText().toString()));
            billTable.addCell(getBillRowCell(grams.getText().toString()));
            billTable.addCell(getBillRowCell(gramPrice.getText().toString()));
            billTable.addCell(getBillRowCell(gstValue.getText().toString()));
            billTable.addCell(getBillRowCell(itemAmount.getText().toString()));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            billTable.addCell(getBillRowCell(" "));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));
            billTable.addCell(getBillRowCell(""));

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell("TERMS AND CONDITIONS"));
            validity.addCell(getValidityCell(" Thanks for doing business with us!"));
            validity.addCell(getValidityCell(" Rates on orders are subjected to changes and will\n" +
                    "be fixed as per the date on payment of advance"));
            validity.addCell(getValidityCell(" Goods once sold can only be exchanged and not\n" +
                    "returned or refunded!\n"));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setColspan (3);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("Total"));
            accounts.addCell(getAccountsCellR(itemTotal.getText().toString()));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setColspan (3);
            billTable.addCell(summaryR);

            FontSelector f = new FontSelector();
            Font fo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
            f.addFont(fo);
            Phrase pay = fs.process("Pay To"); // customer information
            Paragraph bank = new Paragraph("Bank Name: KOTAK\n" +
                    "MAHINDRA BANK ");
            bank.setIndentationLeft(20);
            Paragraph account = new Paragraph("Bank Account No.:\n" +
                    "5212547220");
            account.setIndentationLeft(20);
            Paragraph ifsc = new Paragraph("Bank IFSC code:\n" +
                    "KKBK0008051");
            ifsc.setIndentationLeft(20);


            document.open();

            document.add(image);
            document.add(bills);
            document.add(sAddress);
            document.add(scontact);
            document.add(semail);
            document.add(para);
            document.add(bill);
            document.add(name);
            document.add(contact);
            document.add(billTable);
            document.add(pay);
            document.add(bank);
            document.add(account);
            document.add(ifsc);

            document.close();
            byte[] bytes = outputStream.toByteArray();
            MailUtility.sendMail(clientEmail.getText().toString(), "BRSons Quotation Report", MESSAGE_BODY, bytes);
            Toast.makeText(EnquiryFormActivity.this, "Pdf Created and Sent Email...", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        refresh();
    }


    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    private void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
