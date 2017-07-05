package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jchavando on 7/3/17.
 */

public class UserTimelineFragment extends TweetsListFragment {


        TwitterClient client;
        private final String TAG = "TwitterClient";
        String screenName;

        public static UserTimelineFragment newInstance (String screenName){
            UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
            Bundle args = new Bundle();
            args.putString("screen_name", screenName);
            userTimelineFragment.setArguments(args);
            return userTimelineFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            client = TwitterApplication.getRestClient();
            populateTimeline();
        }

    @Override
    public void fetchTimelineAsync(){
        //send the network request to fetch the updated data
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                tweetAdapter.clear();
                tweets.clear();
                Tweet tweet;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tweetAdapter.addAll(tweets);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "fetch timeline error: " + throwable.toString());
            }
        });
    }

        private void populateTimeline(){
            //comes from the activity
            screenName = getArguments().getString("screen_name"); //unpackaging bundle

            client.getUserTimeline(screenName, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addItems(response);


                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(TAG, responseString);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d(TAG, errorResponse.toString());
                    throwable.printStackTrace();
                }
            });

        }



    }


