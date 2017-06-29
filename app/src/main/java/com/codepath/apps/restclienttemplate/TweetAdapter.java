package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.R.id.tvRelativeTimeStamp;

/**
 * Created by jchavando on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;
    private final int greenColor = 0xff17bf63;

    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
        client = TwitterApplication.getRestClient();
    }

    //for each row, inflate layout and cache (pass) references into ViewHolder
    //only invoked when need to create new row, otherwise the adapter will call onBindViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    //bind the values based on the position of the element
    //repopulate data based on position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet = mTweets.get(position); //returns tweet object

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvRetweetCount.setText(String.valueOf(tweet.retweetCount));

        if(tweet.retweeted){
            holder.ibRetweet.setColorFilter(greenColor);
            holder.tvRetweetCount.setTextColor(greenColor);
        }


        String relativeTimeAgo = getRelativeTimeAgo(tweet.createdAt);
//        if( ){
//            holder.tvRelativeTimeStamp.setText(relativeTimeAgo);
//        }

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRelativeTimeStamp;
        public TextView tvHandle;
        public ImageButton ibRetweet;
        public TextView tvRetweetCount;



        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvRelativeTimeStamp = (TextView) itemView.findViewById(tvRelativeTimeStamp);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);

            ibRetweet.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //make sure the position is valid, actually exits in the view
                    if (position != RecyclerView.NO_POSITION) {
                        //get the movie at the position
                        Tweet tweet = mTweets.get(position);
                        client.retweet(tweet.uid, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("reweet", "success");
                                try {
                                    tvRetweetCount.setText(String.valueOf(Tweet.fromJSON(response).retweetCount));

                                    ibRetweet.setColorFilter(greenColor);
                                    tvRetweetCount.setTextColor(greenColor);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Toast.makeText(this, "Retweeted", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("retweet", "error");
                                //Toast.makeText(this, "Retweet failed because account in protected", Toast.LENGTH_LONG).show();

                            }

                        });
                    }
                }

            });
        }
    }
        public void clear() {
            mTweets.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Tweet> list) {
            mTweets.addAll(list);
            notifyDataSetChanged();
        }



}
