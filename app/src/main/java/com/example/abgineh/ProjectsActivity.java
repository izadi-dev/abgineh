package com.example.abgineh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import com.abgineh.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectsActivity
        extends AppCompatActivity {

    ListView listView;

    AppDatabase db;

    List<ProjectEntity> projects;

    ArrayList<String> titles =
            new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_projects
        );

        listView = findViewById(R.id.listProjects);

        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "glass_db"
                )
                .allowMainThreadQueries()
                .build();

        loadProjects();
        Log.d("DBG", "projects size: " + projects.size());
        // باز کردن پروژه
        listView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    ProjectEntity project =
                            projects.get(position);

                    Intent intent =
                            new Intent(
                                    ProjectsActivity.this,
                                    MainActivity.class
                            );

                    intent.putExtra(
                            "project_Id",
                            project.id
                                                );

                    startActivity(intent);
                });

        // حذف پروژه
        listView.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    ProjectEntity project =
                            projects.get(position);

                    new AlertDialog.Builder(
                            ProjectsActivity.this
                    )
                            .setTitle("حذف پروژه")
                            .setMessage(
                                    "پروژه حذف شود؟"
                            )

                            .setPositiveButton(
                                    "بله",
                                    (dialog, which) -> {

                                        db.projectDao()
                                                .delete(project);

                                        loadProjects();

                                        Toast.makeText(
                                                ProjectsActivity.this,
                                                "حذف شد",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    })

                            .setNegativeButton(
                                    "خیر",
                                    null
                            )

                            .show();

                    return true;
                });
    }

    private void loadProjects() {

        projects =
                db.projectDao().getAll();

        titles.clear();

        for (ProjectEntity p : projects) {

            titles.add(
                    p.name+
                            "  -  "+
//                    p.name +
//                            "\n" +
                           p.date
            );
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                      R.layout.item_list,
                        titles
                );
        listView.setAdapter(adapter);

    }

}
