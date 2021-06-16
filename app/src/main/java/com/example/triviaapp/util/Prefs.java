package com.example.triviaapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;
    public Prefs(Activity activity){
        this.preferences=activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHishestScore(int score){
        int currentScore=score;

        int lastScore= preferences.getInt("high_score",0);
        if(currentScore>lastScore){
            //we have new highest and we save it

            preferences.edit().putInt("high_score",currentScore ).apply();
        }

    }

    public int getHishestScore(){
        return preferences.getInt("high_score",0);
    }

    //function for saving state of app

    public void saveState(int index){
        preferences.edit().putInt("index",index).apply();
    }
    public int getState(){
        return preferences.getInt("index",0);
    }
}
