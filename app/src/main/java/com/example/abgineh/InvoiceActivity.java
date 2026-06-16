package com.example.abgineh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.abgineh.myapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    Button btnCalc;
    Button btnPdfInvoice;
    TextView tvInvoice;
    EditText etPrice;
    List<GlassPiece> pieces;

    double totalPrice;
    double totalArea;
    int sheetCount;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvInvoice = findViewById(R.id.tvInvoice);
        btnCalc=findViewById(R.id.btnCalc);
        etPrice = findViewById(R.id.etPrice);
        btnPdfInvoice=findViewById(R.id.btnPdfInvoice);

        pieces = (List<GlassPiece>)
                getIntent().getSerializableExtra("pieces");
         sheetCount =
                getIntent().getIntExtra(
                        "sheetCount", 0);

        btnPdfInvoice.setOnClickListener(
                view -> {

//                    if (sheets != null) {
                    exportInvoicePdf();
//                    }
                });

        btnCalc.setOnClickListener(v -> {

            String priceText =
                    etPrice.getText().toString();

            if(priceText.isEmpty()) {
                Toast.makeText(this, "مبلغ را وارد کنید", Toast.LENGTH_SHORT).show();

                return;
            }
            double pricePerMeter =
                    Double.parseDouble(priceText);

            StringBuilder result =
                    new StringBuilder();

             totalArea = 0;
             totalPrice = 0;

            for(GlassPiece p : pieces){

                double area =
                        (p.width * p.height * p.quantity)
                                / 10000.0;

                double itemPrice =
                        area * pricePerMeter;

                totalArea += area;
                totalPrice += itemPrice;

                result.append("ابعاد: ")
                        .append(p.width)
                        .append("×")
                        .append(p.height)
                        .append("\n");

                result.append("تعداد: ")
                        .append(p.quantity)
                        .append("\n");

                result.append("متراژ: ")
                        .append(String.format("%.2f", area))
                        .append("\n");

                result.append("مبلغ: ")
                        .append(String.format("%.0f", itemPrice))
                        .append("\n\n");
            }

            result.append("-----------------\n");
            result.append("متراژ کل: ")
                    .append(String.format("%.2f", totalArea))
                    .append("\n");

            result.append("جمع کل: ")
                    .append(String.format("%.0f", totalPrice));

            tvInvoice.setText(result.toString());
        });

        List<GlassPiece> pieces =
                (List<GlassPiece>)
                        getIntent().getSerializableExtra("pieces");

        StringBuilder invoice =
                new StringBuilder();

        invoice.append("فاکتور\n\n");

        if (pieces != null) {

            for (GlassPiece p : pieces) {

                invoice.append("ابعاد: ")
                        .append(p.width)
                        .append(" × ")
                        .append(p.height)
                        .append("\n");

                invoice.append("تعداد: ")
                        .append(p.quantity)
                        .append("\n\n");
                StringBuilder result =
                        new StringBuilder();

                result.append("فاکتور\n\n");

                result.append("تعداد شیشه خام مصرف شده: ")
                        .append(sheetCount)
                        .append("\n\n");
            }
        }

        tvInvoice.setText(invoice.toString());
    }

    private void exportInvoicePdf() {

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        1200,
                        1600,
                        1
                ).create();

        PdfDocument.Page page =
                document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();

        paint.setTextSize(30);

        int y = 100;

        canvas.drawText(
                "فاکتور شیشه",
                500,
                y,
                paint);

        y += 80;

        canvas.drawText(
                "تعداد شیشه خام: "
                        + sheetCount,
                50,
                y,
                paint);

        y += 80;

        for (GlassPiece p : pieces) {

            double area =
                    (p.width * p.height * p.quantity)
                            / 10000.0;

            canvas.drawText(
                    p.width + " × "
                            + p.height
                            + " تعداد:"
                            + p.quantity
                            + " متراژ:"
                            + String.format("%.2f", area),
                    50,
                    y,
                    paint);

            y += 60;
        }

        canvas.drawText(
                "جمع کل: "
                        + totalPrice
                        + " تومان",
                50,
                y + 80,
                paint);

        document.finishPage(page);

        try {

            File file =
                    new File(
                            getExternalFilesDir(
                                    Environment.DIRECTORY_DOCUMENTS),
                            "invoice.pdf");

            document.writeTo(
                    new FileOutputStream(file));

            document.close();

            sharePdf(file);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    private void sharePdf(File file) {

        Uri uri =
                FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".provider",
                        file
                );
        Intent intent =
                new Intent(Intent.ACTION_SEND);

        intent.setType("application/pdf");

        intent.putExtra(
                Intent.EXTRA_STREAM,
                uri
        );

        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
        );

        startActivity(
                Intent.createChooser(
                        intent,
                        "اشتراک PDF"
                )
        );
    }


}
