package com.example.mvvm_quiz_app.persistence;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mvvm_quiz_app.model.Question;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Question.class},version = 1,exportSchema = false)
public abstract class QuizDatabase extends RoomDatabase {

    public static final String DATABASENAME = "question_db.db";
    private static QuizDatabase instance = null;
    public abstract QuizDao quizDao();

    public synchronized static QuizDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),QuizDatabase.class,DATABASENAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class InsertAsyncTask extends AsyncTask<Void,Void,Void>{
        private QuizDao quizDao;
        public InsertAsyncTask(QuizDatabase db){
            quizDao = db.quizDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            quizDao.insertNotes(new Question("A is correct","A","B","C",1));
            quizDao.insertNotes(new Question("B is correct","A","B","C",2));
            quizDao.insertNotes(new Question("C is correct","A","B","C",3));
            quizDao.insertNotes(new Question("A is correct again","A","B","C",1));
            quizDao.insertNotes(new Question("B is correct again","A","B","C",2));
            return null;
        }
    }
}
