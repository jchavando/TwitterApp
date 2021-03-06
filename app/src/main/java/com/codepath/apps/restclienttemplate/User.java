package com.codepath.apps.restclienttemplate;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by jchavando on 6/26/17.
 */
@Parcel
public class User {

    //list the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String tagLine;
    public int followersCount;
    public int followingCount;

    public User(){}

    //deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();
        //extract and fill the values
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = (json.getString("profile_image_url")).replace("_normal", "");
        user.tagLine = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt ("friends_count");
        return user;

    }
}
