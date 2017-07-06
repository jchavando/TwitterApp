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

/**
 * Created by jchavando on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;
    private final int greenColor = 0xff17bf63;
    private final int blackColor = 0xff0000ff; //
    private final int redColor = 0xffff0000;
    private TweetAdapterListener mListener;


    //define an interface required by the ViewHolder
    public interface TweetAdapterListener{
        public void onItemSelected (View view, int position, boolean isPic);
    }
    //pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        client = TwitterApplication.getRestClient();
        mListener = listener;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //get the data according to position
        final Tweet tweet = mTweets.get(position); //returns tweet object

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        holder.tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));


//        if(tweet.retweeted) {
//            holder.ibRetweet.setColorFilter(greenColor);
//            holder.tvRetweetCount.setTextColor(greenColor);
//        } else {
//            holder.ibRetweet.setColorFilter(blackColor);
//            holder.tvRetweetCount.setTextColor(blackColor);
//        }
//        if(tweet.favorited){
//            holder.ibFavorites.setColorFilter(redColor);
//            holder.tvFavoriteCount.setTextColor(redColor);
//        } else {
//            holder.ibFavorites.setColorFilter(blackColor);
//            holder.tvFavoriteCount.setTextColor(blackColor);
//        }

        String relativeShortTimeAgo = replaceTime(getRelativeTimeAgo(tweet.createdAt));
        holder.tvRelativeTimeStamp.setText(" · " + relativeShortTimeAgo); //20 minutes ago

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ibProfileImage);


        holder.ibProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, position, true);
            }
        });

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

    //converts "minutes" to "m" and so on
    private String replaceTime(String relativeDate){
        String newTime = relativeDate;
        if (relativeDate.contains("minutes")) {
            newTime = newTime.replaceAll(" minutes", "m");
        } else if (relativeDate.contains("minute")){
            newTime = newTime.replaceAll(" minute", "m");
        } else if (relativeDate.contains("days")){
            newTime = newTime.replaceAll(" days", "d");
        } else if (relativeDate.contains("day")) {
            newTime = newTime.replaceAll(" day", "d");
        } else if (relativeDate.contains("hours")){
            newTime = newTime.replaceAll(" hours", "h");
        } else if (relativeDate.contains("hour")){
            newTime = newTime.replaceAll(" hour", "h");
        } else if (relativeDate.contains(" seconds")){
            newTime = newTime.replaceAll("seconds", "s");
        } else if (relativeDate.contains("second")) {
            newTime = newTime.replaceAll(" second", "s");
        } else if (relativeDate.contains("week")) {
            newTime = newTime.replaceAll(" week", "w");
        } else if (relativeDate.contains("weeks")) {
            newTime = newTime.replaceAll(" weeks", "w");
        }

        newTime = newTime.replaceAll("ago", "");
        return newTime;
    }

    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ibProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRelativeTimeStamp;
        public TextView tvHandle;
        public ImageButton ibRetweet;
        public TextView tvRetweetCount;
        public ImageButton ibFavorites;
        public TextView tvFavoriteCount;



        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ibProfileImage = (ImageView) itemView.findViewById(R.id.ibProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvRelativeTimeStamp = (TextView) itemView.findViewById(R.id.tvRelativeTimeStamp);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            ibFavorites = (ImageButton) itemView.findViewById(R.id.ibFavorites);
            tvFavoriteCount = (TextView) itemView.findViewById(R.id.tvFavoriteCount);


            //handle row click event
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        //get the position of row element
                        int position = getAdapterPosition();
                        //fire the listener callback
                        mListener.onItemSelected(v, position, false);
                    }

                }
            });

            //Retweet
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
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("retweet", "error");
                            }

                        });
                    }
                }

            });

            //Favorites

            ibFavorites.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //make sure the position is valid, actually exits in the view
                    if (position != RecyclerView.NO_POSITION) {
                        //get the movie at the position
                        Tweet tweet = mTweets.get(position);
                        client.favorite(tweet.uid, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("favorite", "success");
                                try {
                                    tvFavoriteCount.setText(String.valueOf(Tweet.fromJSON(response).favoriteCount));
                                    ibFavorites.setColorFilter(redColor);
                                    tvFavoriteCount.setTextColor(redColor);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("favorite", "error");
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
