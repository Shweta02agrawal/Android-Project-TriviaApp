package com.example.triviaapp.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.controller.AppController;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class QuestionBank {
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionArrayList=new ArrayList<>();

    public ArrayList<Question> getQuestions(final AnswerListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON Stuff", "onResponse: "+ response);
                        for(int i=0;i<response.length();i++){
                            try {
                                Question question=new Question();
                                question.setAnswer( response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                //Add question object to arraylist
                                questionArrayList.add(question);

                               // response.getJSONArray(i).get(0).toString();
                                //Log.d("ANSWER", "onResponse: "+response.getJSONArray(i).getBoolean(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null!=callback) callback.processFinishes(questionArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
