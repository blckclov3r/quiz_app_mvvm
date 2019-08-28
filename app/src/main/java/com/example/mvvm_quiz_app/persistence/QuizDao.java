package com.example.mvvm_quiz_app.persistence;

import com.example.mvvm_quiz_app.model.Question;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface QuizDao {
    @Insert
    void insertNotes(Question question);

    @Query("SELECT * FROM quiz_table")
    List<Question> getAllQuestion();

    @Delete
    void delete(Question question);

    @Update
    void update(Question question);

}
