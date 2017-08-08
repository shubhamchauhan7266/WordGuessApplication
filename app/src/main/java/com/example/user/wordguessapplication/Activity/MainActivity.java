package com.example.user.wordguessapplication.Activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.wordguessapplication.Models.WordGuessModel;
import com.example.user.wordguessapplication.Adapters.RecyclerAdapter;
import com.example.user.wordguessapplication.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mWord;
    private final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<WordGuessModel> mFeedsList;
    private RecyclerAdapter mRecyclerAdapter;
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWord = (EditText) findViewById(R.id.et_word);
        Button btCheck = (Button) findViewById(R.id.bt_check);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        mFeedsList = new ArrayList<>();
        mRecyclerAdapter = new RecyclerAdapter(mFeedsList);
        recyclerView.setAdapter(mRecyclerAdapter);

        btCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_check:
                if(mWord.getText().toString().trim().length()<4){
                    alertShow((R.string.valid_length_message));
                    return;
                }

                String word = mWord.getText().toString();
                String[] wordArray=word.split("");
                HashSet<String> set =new HashSet<>();
                for (String singleChar : wordArray){
                    if(singleChar.equals(""))
                        continue;
                    set.add(singleChar);
                }

                if(set.size()<4){
                    alertShow(R.string.error_validation_message);
                    mWord.setText("");
                    return;
                }

                new CallbackTask().execute(inflections(mWord.getText().toString()));
                break;
            default:
                break;
        }
    }

    private String inflections(String word) {
        final String language = "en";
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/inflections/" + language + "/" + word_id;
    }

    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "af4bde17";
            final String app_key = "2d40e79b8f49f27c0b24538185093ea2";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                return stringBuilder.toString();

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                setData(false);
                Log.d(TAG, "0");
            } else {
                setData(true);
                Log.d(TAG, "1");
            }
        }
    }

    private void setData(boolean status) {
        if (status) {
            String word = mWord.getText().toString();
            int bull = 0;
            int cow = 0;
            String originalWord = "luck";
            String[] singleCharArray = originalWord.split("");

            for (String singleChar : singleCharArray) {
                if (singleChar.equals(""))
                    continue;
                if (word.contains(singleChar)) {
                    if (originalWord.indexOf(singleChar) == word.indexOf(singleChar)) {
                        bull++;
                    } else {
                        cow++;
                    }
                }
            }

            count++;
            mFeedsList.add(new WordGuessModel(word, bull, cow));
            mRecyclerAdapter.setmFeedItemList(mFeedsList);
            mRecyclerAdapter.notifyDataSetChanged();
            mWord.setText("");

            if (bull == 4) {
                alertShow(R.string.win_message);
                mFeedsList.clear();
                mRecyclerAdapter.setmFeedItemList(mFeedsList);
                mRecyclerAdapter.notifyDataSetChanged();
                count = 0;
            } else if (count >= 15) {
                alertShow(R.string.Lose_message);
                mFeedsList.clear();
                mRecyclerAdapter.setmFeedItemList(mFeedsList);
                mRecyclerAdapter.notifyDataSetChanged();
                count = 0;
            }
        } else {
            alertShow(R.string.error_validation_message);
            mWord.setText("");
        }
    }

    void alertShow(int message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Alert Dialog");

        alertDialog.setMessage(message);


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
