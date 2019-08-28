package com.example.mvvm_quiz_app.viewmodel;

import android.app.Application;

import com.example.mvvm_quiz_app.model.Question;
import com.example.mvvm_quiz_app.repository.QuizRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class QuizViewModel extends AndroidViewModel {

    private static final String COMMON_TAG = "mAppLog";
    private static final String TAG = "QuizViewModel";

    private QuizRepository quizRepository;
    private List<Question> allQuestion;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        quizRepository = new QuizRepository(application);

    }

    public List<Question> getAllQuestion() throws ExecutionException, InterruptedException {
        return quizRepository.getAllQuestion();
    }
}
