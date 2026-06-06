package com.example.abgineh;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

    @Dao
    public interface ProjectDao {

        @Insert
        void insert(ProjectEntity project);

        @Delete
        void delete(ProjectEntity project);

        @Query("SELECT * FROM projects ORDER BY id DESC")
        List<ProjectEntity> getAll();

        @Query("SELECT * FROM projects WHERE id=:id")
        ProjectEntity getById(int id);

        @Query("SELECT COUNT(*) FROM projects WHERE name = :projectName")
        int countbyName(String projectName);

    }
