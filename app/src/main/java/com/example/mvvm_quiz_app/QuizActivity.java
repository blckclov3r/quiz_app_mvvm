package com.example.mvvm_quiz_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm_quiz_app.model.Question;
import com.example.mvvm_quiz_app.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static long COUNTDOWN_IN_MILLIS = 30000;

    private static final String TAG = "QuizActivity";
    private static final String COMMON_TAG = "mAppLog";

    private TextView question_tv;
    private TextView score_tv;
    private TextView questionCount_tv;
    private TextView countDown_tv;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button confirmNext_btn;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private QuizViewModel mQuizViewModel;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score = 0;
    private boolean answered = false;

    private List<Question> mQuestionList = new ArrayList<>();
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        question_tv = findViewById(R.id.question_textView);
        score_tv = findViewById(R.id.score_textView);
        questionCount_tv = findViewById(R.id.questionCount_textView);
        countDown_tv = findViewById(R.id.countDown_textView);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        confirmNext_btn = findViewById(R.id.confirmNext_button);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultRb = rb2.getTextColors();
        textColorDefaultRb = rb3.getTextColors();
        textColorDefaultCd = countDown_tv.getTextColors();

        mQuestionList = new ArrayList<>();

        mQuizViewModel = ViewModelProviders.of(this).get(QuizViewModel.class);

        score_tv.setText("Score: " + score);
        try {

            mQuestionList = mQuizViewModel.getAllQuestion();
            questionCountTotal = mQuestionList.size();
            Log.d(COMMON_TAG,TAG+" questionTotal: "+questionCountTotal);

            Collections.shuffle(mQuestionList);
            showNextQuestion();
            confirmNext_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!answered) {
                        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                            checkAnswer();
                        } else {
                            Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showNextQuestion();
                    }
                }
            });

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = mQuestionList.get(questionCounter);
            question_tv.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            questionCount_tv.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            confirmNext_btn.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) ((timeLeftInMillis / 1000) / 60);
        int seconds  = (int) (timeLeftInMillis/1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        countDown_tv.setText(timeFormatted);
        if(timeLeftInMillis <= 10000){
            countDown_tv.setTextColor(Color.RED);
        }else{
            countDown_tv.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1; //start at 0 then add 1 to start at 1
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            score_tv.setText("Score: " + score);
        }
        showSolution();

    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                question_tv.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                question_tv.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                question_tv.setText("Answer 3 is correct");
                break;
            default:
                rb1.setTextColor(Color.RED);
                rb2.setTextColor(Color.RED);
                rb3.setTextColor(Color.RED);
                break;
        }
        if(questionCounter < questionCountTotal){
            confirmNext_btn.setText("Next");
        }else{
            confirmNext_btn.setText("Finish");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
