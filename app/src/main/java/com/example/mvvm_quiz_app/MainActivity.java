package com.example.mvvm_quiz_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private Button start_btn;
    private TextView highscore_tv;

    private int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn = findViewById(R.id.start_button);
        highscore_tv = findViewById(R.id.highscore_textView);

        loadHighScore();
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                startActivityForResult(intent,REQUEST_CODE_QUIZ);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_QUIZ && resultCode == RESULT_OK){
            int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
            if(score > highScore){
                updateHighScore(score);
            }
        }
    }

    private void loadHighScore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGHSCORE,0);
        highscore_tv.setText("Highscore: "+highScore);
    }

    private void updateHighScore(int highScore){
        this.highScore = highScore;
        highscore_tv.setText("Highscore: "+highScore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE,highScore);
        editor.apply();
    }
}
