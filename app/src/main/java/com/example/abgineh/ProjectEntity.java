package com.example.abgineh;


    import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity(tableName = "projects")
    public class ProjectEntity {

        @PrimaryKey(autoGenerate = true)
        public int id;

        public String name;

        public String data;

        public String date;



    }

