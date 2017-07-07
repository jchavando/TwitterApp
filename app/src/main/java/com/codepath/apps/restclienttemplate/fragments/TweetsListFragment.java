package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.Tweet;
import com.codepath.apps.restclienttemplate.TweetAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by jchavando on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener{
    public interface TweetSelectedListener {
        //handle tweet selection
        public void onTweetSelected(Tweet tweet);
        public void onImageSelected(Tweet tweet);

    }
    private final int REQUEST_CODE = 20;
    public TweetAdapter tweetAdapter;
    public ArrayList<Tweet> tweets;
    public RecyclerView rvTweets;
    public SwipeRefreshLayout swipeContainer; //do I move this?


    //inflation happens inside onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);

        //find RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        //init the arraylist (data source)
        tweets = new ArrayList<>();
        //construct adapter from datasource
        tweetAdapter = new TweetAdapter(tweets, this); //this
        //recyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);


        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshes list
                fetchTimelineAsync();

            }
        });

        //configure refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }

    public void fetchTimelineAsync(){}



    public void addItems (JSONArray response){
        for (int i = 0; i < response.length(); i++){
            //convert each object to a Tweet model
            //add that Tweet model to our data source
            //notify the adapter that we've added an item (list view)
            Tweet tweet = null;
            try {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size()-1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //adds one tweet at top
    public void postTweet(Tweet tweet){
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    @Override
    public void onItemSelected(View view, int position, boolean isPic) {
       Tweet tweet = tweets.get(position);
        if(!isPic) {
           ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
       } else {
           ((TweetSelectedListener) getActivity()).onImageSelected(tweet);
       }

    }





}
