package com.example.mvvm_quiz_app.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mvvm_quiz_app.model.Question;
import com.example.mvvm_quiz_app.persistence.QuizDao;
import com.example.mvvm_quiz_app.persistence.QuizDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class QuizRepository {
    private QuizDao quizDao;

    private QuizDatabase quizDatabase;

    public QuizRepository(Context context) {
        quizDatabase = QuizDatabase.getInstance(context);
        quizDao = quizDatabase.quizDao();

    }

    public List<Question> getAllQuestion() throws ExecutionException, InterruptedException {
        return new AllQuestionAsync(quizDao).execute().get();
    }

    private static class AllQuestionAsync extends AsyncTask<Void, Void, List<Question>> {
        private QuizDao quizDao;

        public AllQuestionAsync(QuizDao quizDao) {
            this.quizDao = quizDao;
        }

        @Override
        protected List<Question> doInBackground(Void... voids) {
            return quizDao.getAllQuestion();
        }
    }
}
