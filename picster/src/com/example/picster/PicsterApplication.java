package com.example.picster;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.picster.Models.PicUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class PicsterApplication extends Application {
	public static PicUser currentUser;
    public static final String TAG = "PicsterApp";
    @SuppressLint("SimpleDateFormat") // Specific date format set for joda LocalTime
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final int IMAGE_DIMENSIONS = 125;
    public static final long MAX_IMAGE_DIMENSIONS = 500000;
    
    @Override
    public void onCreate() {
        // Initialize Parse
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id)); 
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
                   
        // Call superclass's onCreate method
        super.onCreate();
    }

	// Show toast message
	public static void showToast(Context context, String message) {
	        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
	        toast.show();
	}
}
