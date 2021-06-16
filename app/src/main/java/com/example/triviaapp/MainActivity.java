package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.data.AnswerListAsyncResponse;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.model.Score;
import com.example.triviaapp.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question_Counter,questionTextview;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private int currentquestion=0;
    private List<Question> questionList;
    private TextView score_view;
    private TextView finalscoreview;
    private int scoreCounter=0;
    private Score score;

    private Prefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            score=new Score();
            prefs=new Prefs(MainActivity.this);

       //Log.d("Second", "onClick: "+ prefs.getHishestScore());

            nextButton=findViewById(R.id.next_button);
            prevButton= findViewById(R.id.prev_button);
            trueButton=findViewById(R.id.true_button);
            falseButton=findViewById(R.id.false_button);
            questionTextview=findViewById(R.id.question_textview);
            question_Counter=findViewById(R.id.questionTextCounter);
            score_view=findViewById(R.id.score_textView);
            finalscoreview=findViewById(R.id.finalscore_view);
            nextButton.setOnClickListener(this);
            prevButton.setOnClickListener(this);
            trueButton.setOnClickListener(this);
            falseButton.setOnClickListener(this);

            currentquestion=prefs.getState();
            questionList=new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinishes(ArrayList<Question> questionArrayList) {
                questionTextview.setText(questionArrayList.get(currentquestion).getAnswer());
                question_Counter.setText(currentquestion+ " / " + questionArrayList.size());
                score_view.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
                finalscoreview.setText(String.format("Hishest Score: %d", prefs.getHishestScore()));
                Log.d("Inside", "processFinishes: " + questionArrayList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prev_button:
                if(currentquestion!=0) {
                    currentquestion = (currentquestion - 1) % questionList.size();
                    updateQuestion();
//                    questionTextview.setText(questionList.get(currentquestion).getAnswer());
//                    question_Counter.setText(currentquestion + " / " + questionList.size());
                }
                break;
            case R.id.next_button:
                goNext();
               // currentquestion=(currentquestion+1)%questionList.size();
                //updateQuestion();
//                prefs.saveHishestScore(scoreCounter);
//                Log.d("Prefs", "onClick: "+ prefs.getHishestScore());

//                questionTextview.setText(questionList.get(currentquestion).getAnswer());
//                question_Counter.setText(currentquestion + " / " + questionList.size());
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }
    private void updateQuestion(){
        questionTextview.setText(questionList.get(currentquestion).getAnswer());
        question_Counter.setText(currentquestion + " / " + questionList.size());
    }

    private void checkAnswer(boolean userChoice) {
        boolean answerIsTrue=questionList.get(currentquestion).getAnswerTrue();
        int toastid=0;
        if(userChoice==answerIsTrue) {
            trueView();
            addPoints();
            toastid = R.string.correct_answer;
        }
        else {
            falseView();
            deductPoint();
            toastid = R.string.wrong_answer;

        }
        Toast.makeText(MainActivity.this,toastid,Toast.LENGTH_SHORT).show();

    }

    private void addPoints(){
        scoreCounter+=10;
        score.setScore(scoreCounter);
        score_view.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        Log.d("Score", "addPoints: " + score.getScore());
    }
    private void deductPoint(){
        scoreCounter-=10;
        if(scoreCounter>0){
            score.setScore(scoreCounter);
            score_view.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        }
        else{
            scoreCounter=0;
            score.setScore(scoreCounter);
            score_view.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        }
    }
    private void trueView(){
        CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaanimation=new AlphaAnimation(1.0f,0.0f);
        alphaanimation.setDuration(350);
        alphaanimation.setRepeatCount(1);
        alphaanimation.setRepeatMode(Animation.REVERSE);


        cardView.setAnimation(alphaanimation);
        alphaanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    cardView.setCardBackgroundColor(Color.WHITE);
                    //for going to next question as soon as we get the answer of current one

                goNext();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void falseView(){
        //instantiating animation class
        //Animation shake= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        CardView cardView=findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setDuration(350);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Adding listener in animation

        cardView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.YELLOW);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goNext(){
        currentquestion=(currentquestion+1)%questionList.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHishestScore(score.getScore());
        prefs.saveState(currentquestion);
        super.onPause();
    }
}