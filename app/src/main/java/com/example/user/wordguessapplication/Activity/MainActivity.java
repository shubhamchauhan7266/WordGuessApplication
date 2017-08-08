package com.example.user.wordguessapplication.Activity;

import android.os.AsyncTask;
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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mWord;
    private final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<WordGuessModel> mFeedsList;
    private RecyclerAdapter mRecyclerAdapter;

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
                new CallbackTask().execute(inflections(mWord.getText().toString()));
                mWord.setText("");
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
            if(result==null){
                setData(false);
                Log.d(TAG,"0");
            }else{
                setData(true);
                Log.d(TAG,"1");
            }
        }
    }

    private void setData(boolean status) {
        if(status){
            String word=mWord.getText().toString();
            int bull = 0;
            int cow = 0;
            String originalWord="luck";
            String[] singleCharArray=originalWord.split("");

            Log.d(TAG,String.valueOf("length = "+singleCharArray.length));

            for(String singleChar:singleCharArray){
                if(singleChar.equals(""))
                    continue;

                if(word.contains(singleChar)){
                    if(originalWord.indexOf(singleChar)==word.indexOf(singleChar)){
                        bull++;
                    }else {
                        cow++;
                    }
                }
                Log.d(TAG,String.valueOf("bull ="+bull+" cow ="+cow+" char = "+singleChar));
            }

            mFeedsList.add(new WordGuessModel(word,bull,cow));
            mRecyclerAdapter.setmFeedItemList(mFeedsList);
            mRecyclerAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, R.string.error_validation_message, Toast.LENGTH_SHORT).show();
        }
    }
}
