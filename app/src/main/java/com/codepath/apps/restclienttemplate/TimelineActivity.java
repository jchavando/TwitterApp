package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;

//basically now just loads fragments onto the screen
public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {
    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //at top
        setContentView(R.layout.activity_timeline);

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        //set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));

        //setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }

    /* get back data from Twitter api
        array for objects
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.miCompose:
//                composeMessage();
//                return true;
//            //case R.id.miProfile:
//                //showProfileView();
//               // return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
    public void composeMessage(View v) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("tweet", 2);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onProfileView(MenuItem item) {
        //launch the profile view
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        //move to user's profile
       // Intent intent = new Intent(this, ProfileActivity.class);
        //Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageSelected(Tweet tweet) {
        //move to user's profile
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("screen_name", tweet.user.screenName);
        startActivity(intent);

    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        // REQUEST_CODE is defined above
//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) { //check that code is the same
//            // Extract name value from result extras
//
//            Tweet unwrapped_tweet =  Parcels.unwrap(intent.getParcelableExtra("tweet"));
//            tweets.add(0, unwrapped_tweet);
//            tweetAdapter.notifyItemInserted(0);
//            rvTweets.scrollToPosition(0);
//
//        }
//    }



}
