package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


/**
 * Created by jchavando on 6/26/17.
 */
@Parcel
public class Tweet {
    //list out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public String createdAt;
    //public String screenName;

    public User user;

    public Tweet() {}
    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }
}
