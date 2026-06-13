package com.example.abgineh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abgineh.myapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etWidth;
    EditText etHeight;
    EditText etQty;
   AppDatabase db;
    Button btnAdd;
    Button btnnew;
    Toolbar toolbar;
    Button btnSave;
    Button btnOptimize;
    TextView pageNum;
    Button btnNext;
    Button btnPrev;
    Button btnPdf;
    Button btnProjects;
    EditText nameproject;
//    TextView txtInfo;
    DrawerLayout drawer_layout;
    SheetView sheetView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    public List<GlassPiece> pieces =
            new ArrayList<>();

    List<List<Placement>> sheets;

    GlassOptimizer optimizer;

    int currentSheet = 0;
    RecyclerView recyclerPieces;
    PieceAdapter adapter;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
       setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_about) {
                Toast.makeText(MainActivity.this,"tttt",Toast.LENGTH_SHORT).show();

            }

            else if (id == R.id.nav_contact) {
                // تماس با ما
            }

            else if (id == R.id.nav_tutorial) {
                // آموزش
            }

            else if (id == R.id.nav_exit) {
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });


        // دیتابیس
        db = androidx.room.Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "glass_db"
                )
                .allowMainThreadQueries()
                .build();

        // RecyclerView
        recyclerPieces = findViewById(R.id.recyclerPieces);

        recyclerPieces.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new PieceAdapter(pieces, () -> {

        });

        recyclerPieces.setAdapter(adapter);

        nameproject=findViewById(R.id.nameproject);

        // گرفتن پروژه
        int projectId =
                getIntent().getIntExtra(
                        "project_Id",
                        -1
                );

        // لود پروژه
        if (projectId != -1) {

            LoadProject(projectId);
        }

        recyclerPieces=findViewById(R.id.recyclerPieces);

        recyclerPieces.setLayoutManager(new LinearLayoutManager(this));
        adapter= new PieceAdapter(pieces, () -> {

        });
        recyclerPieces.setAdapter(adapter);

        etWidth = findViewById(R.id.etWidth);
        etHeight = findViewById(R.id.etHeight);
        etQty = findViewById(R.id.etQty);
        btnAdd = findViewById(R.id.btnAdd);
        btnOptimize = findViewById(R.id.btnOptimize);
        btnSave=findViewById(R.id.btnSave);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnPdf = findViewById(R.id.btnPdf);
        btnProjects = findViewById(R.id.btnProjects);
        pageNum=findViewById(R.id.pageNum);
        btnnew = findViewById(R.id.btnnew);

//        txtInfo = findViewById(R.id.txtInfo);

        sheetView = findViewById(R.id.sheetView);
        btnnew.setOnClickListener(v -> {

            nameproject.setText("");

            pieces.clear();

            adapter.notifyDataSetChanged();

            Toast.makeText(this,
                    "پروژه جدید ایجاد شد",
                    Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(
                view ->{

                    saveProject();
                    Toast.makeText(MainActivity.this, "پروژه ذخیره شد", Toast.LENGTH_SHORT).show();
                }

        );
        btnProjects.setOnClickListener(
                view -> {
                   Intent intent = new Intent(MainActivity.this, ProjectsActivity.class);
                    startActivity(intent);

//                        startActivities(
//                                new Intent(MainActivity.this,
//                                        ProjectsActivity.class
//                                )
//                        );
                }
        );


        btnAdd.setOnClickListener(
                view -> {
                    if (
                            etWidth.getText()
                                    .toString()
                                    .isEmpty()

                                    ||

                                    etHeight.getText()
                                            .toString()
                                            .isEmpty()

                                    ||

                                    etQty.getText()
                                            .toString()

                                            .isEmpty()
                    ) {

//                        txtInfo.setText(
//                                "همه فیلدها را وارد کنید"
//                        );

                        return;
                    }


                    int w =
                            Integer.parseInt(
                                    etWidth
                                            .getText()
                                            .toString()
                            );

                    int h =
                            Integer.parseInt(
                                    etHeight
                                            .getText()
                                            .toString()
                            );

                    int q =
                            Integer.parseInt(
                                    etQty
                                            .getText()
                                            .toString()
                            );

                    pieces.add(
                            new GlassPiece(w, h, q)

                    );
                    adapter.notifyDataSetChanged();

//                    txtInfo.append(
//                            "\n" +
//                                    w +
//                                    " x " +
//                                    h +
//                                    " | تعداد: " +
//                                    q
//                    );

                    etWidth.setText("");
                    etHeight.setText("");
                    etQty.setText("");
                    Toast.makeText(MainActivity.this, "قطعه اضافه شد", Toast.LENGTH_SHORT).show();
                });



        btnOptimize.setOnClickListener(

                view -> {
                    Intent intent =new Intent(MainActivity.this,OptimizActivity.class);
                    intent.putExtra(
                            "pieces",
                            (Serializable) pieces
                    );
                    startActivity(intent);

//                    optimizer =
//                            new GlassOptimizer(
//                                    3210,
//                                    2250
//                            );
//
//                sheets =
//                        optimizer.optimize(pieces);
//
//                currentSheet = 0;
//
//                    showSheet(currentSheet);
//                    Toast.makeText(MainActivity.this, "بهینه انجام شد", Toast.LENGTH_SHORT).show();
               });

//        btnNext.setOnClickListener(
//                view -> {
//
//                    if (sheets == null) return;
//
//                    if (currentSheet
//                            < sheets.size() - 1) {
//
//                        currentSheet++;
//
//                        showSheet(currentSheet);
//                    }
//                });
//
//        btnPrev.setOnClickListener(
//                view -> {
//
//                    if (sheets == null) return;
//
//                    if (currentSheet > 0) {
//
//                        currentSheet--;
//
//                        showSheet(currentSheet);
//                    }
//                });

//        btnPdf.setOnClickListener(
//                view -> {
//
//                    if (sheets != null) {
//                        exportPdf();
//                    }
//                });
    }

@SuppressLint("NotifyDataSetChanged")
private void LoadProject(int id){

    ProjectEntity project =
            db.projectDao().getById(id);

    if(project == null){

        Toast.makeText(
                this,
                "پروژه پیدا نشد",
                Toast.LENGTH_SHORT
        ).show();

        return;
    }
//    nameproject.setText(project.name);
    Toast.makeText(this, project.name, Toast.LENGTH_SHORT).show();
      nameproject.setText(project.name);
    pieces.clear();

    String[] items =
            project.data.split(";");

    for(String item : items){

        if(item.trim().isEmpty())
            continue;

        String[] values =
                item.split(",");

        int w =
                Integer.parseInt(values[0]);

        int h =
                Integer.parseInt(values[1]);

        int q =
                Integer.parseInt(values[2]);

        pieces.add(
                new GlassPiece(w, h, q)

        );

    }

    adapter.notifyDataSetChanged();

//    Toast.makeText(
//            this,
//            "پروژه باز شد",
//            Toast.LENGTH_SHORT
//    ).show();
}


//    @SuppressLint({"SetTextI18n", "DefaultLocale"})
//    private void showSheet(int index) {
//
//        List<Placement> placements =
//                sheets.get(index);
//
//        sheetView.sheetWidth = 3210;
//        sheetView.sheetHeight = 2250;
//
//        sheetView.placements = placements;
//        sheetView.setPageInfo(index, sheets.size());
//
//        sheetView.invalidate();
//
//        double waste =
//                optimizer.calculateWaste(
//                        placements
//                );
//        pageNum.setText((index + 1) + " از " + sheets.size());
//
//    }

    @SuppressLint("SetTextI18n")

//    private void exportPdf() {
//
//        PdfDocument document =
//                new PdfDocument();
//
//        int pageNum = 1;
//
//        Random random =
//                new Random();
//
//        for (List<Placement> sheet : sheets) {
//
//            PdfDocument.PageInfo pageInfo =
//                    new PdfDocument.PageInfo.Builder(
//                            1200,
//                            1600,
//                            pageNum
//                    ).create();
//
//            PdfDocument.Page page =
//
//                    document.startPage(pageInfo);
//
//            Canvas canvas =
//                    page.getCanvas();
//
//            Paint fillPaint =
//                    new Paint();
//
//            Paint borderPaint =
//                    new Paint();
//
//            Paint textPaint =
//                    new Paint();
//
//            borderPaint.setStyle(
//                    Paint.Style.STROKE
//            );
//
//            borderPaint.setStrokeWidth(4);
//
//            textPaint.setColor(Color.BLACK);
//
//
//            textPaint.setTextSize(28);
//
//            int margin = 80;
//
//            float scaleX =
//                    (1200f - margin * 2)
//                            / optimizer.sheetWidth;
//
//            float scaleY =
//                    (1500f - margin * 2)
//                            / optimizer.sheetHeight;
//
//            float scale =
//                    Math.min(scaleX, scaleY);
//
//            // کادر شیت
//
//            canvas.drawRect(
//                    margin,
//                    margin,
//
//
//                    margin +
//                            optimizer.sheetWidth * scale,
//
//                    margin +
//                            optimizer.sheetHeight * scale,
//
//                    borderPaint
//            );
//
//            for (Placement p : sheet) {
//
//                fillPaint.setColor(
//                        Color.rgb(
//                                random.nextInt(200),
//                                random.nextInt(200),
//                                random.nextInt(200)
//                        )
//                );
//
//                float left =
//
//                        margin + p.x * scale;
//
//                float top =
//                        margin + p.y * scale;
//
//                float right =
//                        margin +
//                                (p.x + p.width) * scale;
//
//                float bottom =
//                        margin +
//                                (p.y + p.height) * scale;
//
//                // رنگ
//
//                canvas.drawRect(
//                        left,
//                        top,
//                        right,
//                        bottom,
//                        fillPaint
//
//                );
//
//                // خط دور
//
//                if (p.rotated) {
//
//                    borderPaint.setColor(
//                            Color.RED
//                    );
//
//                } else {
//
//                    borderPaint.setColor(
//                            Color.BLACK
//                    );
//                }
//
//                canvas.drawRect(
//                        left,
//                        top,
//                        right,
//
//                        bottom,
//                        borderPaint
//                );
//
//                // متن ابعاد
//
//                canvas.drawText(
//
//                        p.width +
//                                "×" +
//                                p.height,
//
//                        left + 10,
//
//                        top + 35,
//
//                        textPaint
//                );
//            }
//
//            // عنوان صفحه
//
//
//            canvas.drawText(
//
//                    "Sheet " + pageNum,
//
//                    80,
//
//                    40,
//
//                    textPaint
//            );
//
//            document.finishPage(page);
//
//            pageNum++;
//        }
//
//        try {
//
//            File file =
//                    new File(
//
//
//                            getExternalFilesDir(
//                                    Environment
//                                            .DIRECTORY_DOCUMENTS
//                            ),
//
//                            "glass_output.pdf"
//                    );
//
//            document.writeTo(
//                    new FileOutputStream(file)
//            );
//
//            document.close();
//
//            sharePdf(file);
//
////            txtInfo.setText(
////                    "PDF ساخته شد"
////            );
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
////            txtInfo.setText(
////                    e.getMessage()
////            );
//        }
//    }

    private void saveProject() {

        String name = nameproject.getText().toString().trim();
        if (name == null || name.isEmpty()) {
            Toast.makeText(this,
                    "نام وارد کنید",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(db.projectDao().countbyName(name)>0)
        {
            Toast.makeText(this,"پروژه با این نام قبلا ذخیره شده است",Toast.LENGTH_SHORT).show();
            return;
        }

            ProjectEntity project =
                    new ProjectEntity();

            project.name =
                    nameproject.getText().toString();
//                    "Project " +
//                            System.currentTimeMillis();

            StringBuilder builder =
                    new StringBuilder();
            for (GlassPiece p : pieces) {

                builder.append(p.width).append(",").append(p.height).append(",").append(p.quantity).append(";");
            }

            project.data = builder.toString();

            project.date =
                    String.valueOf(
                            System.currentTimeMillis()
                    );

            db.projectDao().insert(project);

//        nameproject.setText("");

//         pieces.clear();

//            txtInfo.setText("پروژه ذخیره شد");
        }
//    private void sharePdf(File file) {
//
//        Uri uri =
//                FileProvider.getUriForFile(
//                        this,
//                        getPackageName() + ".provider",
//                        file
//                );
//        Intent intent =
//                new Intent(Intent.ACTION_SEND);
//
//        intent.setType("application/pdf");
//
//        intent.putExtra(
//                Intent.EXTRA_STREAM,
//                uri
//        );
//
//        intent.addFlags(
//                Intent.FLAG_GRANT_READ_URI_PERMISSION
//        );
//
//        startActivity(
//                Intent.createChooser(
//                        intent,
//                        "اشتراک PDF"
//                )
//        );
//    }
    }
