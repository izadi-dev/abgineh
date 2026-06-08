package com.example.abgineh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.abgineh.myapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;


public class OptimizActivity extends AppCompatActivity {
    TextView pageNum;
    Button btnNext;
    Button btnPrev;
    SheetView sheetView;
    Button btnPdf;
    List<List<Placement>> sheets;

    GlassOptimizer optimizer;

    int currentSheet = 0;
    List<GlassPiece> pieces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_optimiz);
        sheetView = findViewById(R.id.sheetView);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        pageNum=findViewById(R.id.pageNum);
        btnPdf=findViewById(R.id.btnPdf);
        pieces = (List<GlassPiece>)
                getIntent().getSerializableExtra("pieces");

        runOptimaiz();


    }
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showSheet(int index) {


        List<Placement> placements =
                sheets.get(index);

        sheetView.sheetWidth = 3210;
        sheetView.sheetHeight = 2250;

        sheetView.placements = placements;
        sheetView.setPageInfo(index, sheets.size());

        sheetView.invalidate();

        double waste =
                optimizer.calculateWaste(
                        placements
                );
        pageNum.setText((index + 1) + " از " + sheets.size());

    }
private  void runOptimaiz(){
    if (pieces == null || pieces.isEmpty()) {
        Toast.makeText(this,
                "قطعه‌ای برای بهینه‌سازی وجود ندارد",
                Toast.LENGTH_SHORT).show();
        return;
    }
    optimizer =
            new GlassOptimizer(
                    3210,
                    2250
            );

    sheets =
            optimizer.optimize(pieces);

    currentSheet = 0;

    showSheet(currentSheet);
    Toast.makeText(OptimizActivity.this, "بهینه انجام شد", Toast.LENGTH_SHORT).show();



  btnNext.setOnClickListener(
    view -> {

        if (sheets == null) return;

        if (currentSheet
                < sheets.size() - 1) {

            currentSheet++;

            showSheet(currentSheet);
        }
    });

        btnPrev.setOnClickListener(
    view -> {

        if (sheets == null) return;

        if (currentSheet > 0) {

            currentSheet--;

            showSheet(currentSheet);
        }
    });

    btnPdf.setOnClickListener(
            view -> {

                if (sheets != null) {
                    exportPdf();
                }
            });


    }
    private void exportPdf() {

        PdfDocument document =
                new PdfDocument();

        int pageNum = 1;

        Random random =
                new Random();

        for (List<Placement> sheet : sheets) {

            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(
                            1200,
                            1600,
                            pageNum
                    ).create();

            PdfDocument.Page page =

                    document.startPage(pageInfo);

            Canvas canvas =
                    page.getCanvas();

            Paint fillPaint =
                    new Paint();

            Paint borderPaint =
                    new Paint();

            Paint textPaint =
                    new Paint();

            borderPaint.setStyle(
                    Paint.Style.STROKE
            );

            borderPaint.setStrokeWidth(4);

            textPaint.setColor(Color.BLACK);


            textPaint.setTextSize(28);

            int margin = 80;

            float scaleX =
                    (1200f - margin * 2)
                            / optimizer.sheetWidth;

            float scaleY =
                    (1500f - margin * 2)
                            / optimizer.sheetHeight;

            float scale =
                    Math.min(scaleX, scaleY);

            // کادر شیت

            canvas.drawRect(
                    margin,
                    margin,


                    margin +
                            optimizer.sheetWidth * scale,

                    margin +
                            optimizer.sheetHeight * scale,

                    borderPaint
            );

            for (Placement p : sheet) {

                fillPaint.setColor(
                        Color.rgb(
                                random.nextInt(200),
                                random.nextInt(200),
                                random.nextInt(200)
                        )
                );

                float left =

                        margin + p.x * scale;

                float top =
                        margin + p.y * scale;

                float right =
                        margin +
                                (p.x + p.width) * scale;

                float bottom =
                        margin +
                                (p.y + p.height) * scale;

                // رنگ

                canvas.drawRect(
                        left,
                        top,
                        right,
                        bottom,
                        fillPaint

                );

                // خط دور

                if (p.rotated) {

                    borderPaint.setColor(
                            Color.RED
                    );

                } else {

                    borderPaint.setColor(
                            Color.BLACK
                    );
                }

                canvas.drawRect(
                        left,
                        top,
                        right,

                        bottom,
                        borderPaint
                );

                // متن ابعاد

                canvas.drawText(

                        p.width +
                                "×" +
                                p.height,

                        left + 10,

                        top + 35,

                        textPaint
                );
            }

            // عنوان صفحه


            canvas.drawText(

                    "Sheet " + pageNum,

                    80,

                    40,

                    textPaint
            );

            document.finishPage(page);

            pageNum++;
        }

        try {

            File file =
                    new File(


                            getExternalFilesDir(
                                    Environment
                                            .DIRECTORY_DOCUMENTS
                            ),

                            "glass_output.pdf"
                    );

            document.writeTo(
                    new FileOutputStream(file)
            );

            document.close();

            sharePdf(file);

//            txtInfo.setText(
//                    "PDF ساخته شد"
//            );


        } catch (Exception e) {

            e.printStackTrace();

//            txtInfo.setText(
//                    e.getMessage()
//            );
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