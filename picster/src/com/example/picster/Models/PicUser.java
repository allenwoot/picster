package com.example.picster.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.joda.time.LocalDate;

import android.R.integer;
import android.net.Uri;
import android.util.Log;

import com.example.picster.PicsterApplication;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	public final static int MAX_LIST_SIZE = 100;
	public final static int CURRENT_DAY = 5;
	private String id;
	private String name;
	private HashMap<String, PicUser> friends;
	private HashMap<Integer, Uri> dateToUri;
	private HashMap<LocalDate, Uri> dateToImagesMap;
	private ParseUser parseUser;
	public static String defaultPassword = "password";

	public void putInDateToImagesMap(Uri uri, int offset) {
		dateToImagesMap.put(LocalDate.parse((new Date()).toString()).minusDays(offset), uri);
	}
	
	public HashMap<String, PicUser> getFriends() {
		return friends;
	}

	public void setFriends(HashMap<String, Object> friends) {
		this.friends = new HashMap<String, PicUser>();
		for (String key : friends.keySet()) {
			this.friends.put(key, new PicUser(key));
		}
	}

	public PicUser(String id) {
		this.id = id;
	}
	
	public PicUser(String id, String name, ParseUser parseUser) {
		this.id = id;
		this.parseUser = parseUser;
		this.dateToUri = new HashMap<Integer, Uri>();
	}
	
	public ArrayList<Uri> getUriList() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();

		ArrayList<Uri> uriList = new ArrayList<Uri>();
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
	        int day = CURRENT_DAY - i;
	        if( day < 0) {
	        	break;
	        }
	        Log.d(PicsterApplication.TAG, "in getUriList: " + day);
	        uriList.add(getUriOfDate(day));
	        
		}
		return uriList;
	}
	
	public Uri getUriOfDate(int date) {
	    Log.d(PicsterApplication.TAG, "getting day: " + date);
		return dateToUri.get(date);
	}
	
	public void addPic(Uri uri) {
	    Log.d(PicsterApplication.TAG, "adding day: " + uri.toString());
		dateToUri.put(CURRENT_DAY, uri);
	}
	
}