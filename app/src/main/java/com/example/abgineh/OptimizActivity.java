package com.example.abgineh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abgineh.myapp.R;

import java.util.List;


public class OptimizActivity extends AppCompatActivity {
    TextView pageNum;
    Button btnNext;
    Button btnPrev;
    SheetView sheetView;
    List<List<Placement>> sheets;

    GlassOptimizer optimizer;

    int currentSheet = 0;
    List<GlassPiece> pieces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_optimiz);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        sheetView = findViewById(R.id.sheetView);
        pageNum=findViewById(R.id.pageNum);

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


}

    }