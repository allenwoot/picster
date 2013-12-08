package com.example.picster;

import com.example.picster.Models.PicUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class PicsterApplication extends Application {
	public static PicUser currentUser;
    static final String TAG = "PicsterApp";
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
