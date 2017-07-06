package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class ComposeActivity extends AppCompatActivity {


    //view objects
    EditText etEnteringText;
    TwitterClient client;
    Button bTweet;

    TextView mTextView;
    ImageButton ibExit;



    private final String TAG = "ComposeActivity";



    //character count
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            mTextView.setText(String.valueOf(140-s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();

        etEnteringText = (EditText) findViewById(R.id.etEnteringText);

        mTextView = (TextView) findViewById(R.id.tvCharacters);
        etEnteringText.addTextChangedListener(mTextEditorWatcher);


        bTweet = (Button) findViewById(R.id.bTweet);
        bTweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                client.sendTweet(etEnteringText.getText().toString(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet;
                        try {
                            tweet = Tweet.fromJSON(response);

                            Intent i = new Intent();
                            i.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, i);
                            finish(); //go home where launched this activity from

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(TAG, "error");
                    }
                });
            }

        });

        String reply = getIntent().getStringExtra("username");
        if (reply != null) {
            etEnteringText.setText("@" + reply);
            etEnteringText.setSelection(etEnteringText.getText().length());
        }

        ibExit = (ImageButton) findViewById(R.id.ibExit);
        ibExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }


}
