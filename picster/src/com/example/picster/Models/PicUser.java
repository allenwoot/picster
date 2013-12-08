package com.example.picster.Models;

import java.text.SimpleDateFormat;
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
	private HashSet<String> friends;
	private HashMap<LocalDate, Uri> dateToImagesMap;
	private ParseUser parseUser;
	public static String defaultPassword = "password";

	public PicUser(String id) {
		this.id = id;
	}
	
	public PicUser(String id, String name, ParseUser parseUser) {
		this.id = id;
		this.parseUser = parseUser;
		this.dateToImagesMap = new HashMap<LocalDate, Uri>();
	}

	public void putInDateToImagesMap(Uri uri, int offset) {
		dateToImagesMap.put(LocalDate.parse((PicsterApplication.DATE_FORMAT.format(new Date())).toString()).minusDays(offset), uri);
	}
	
	public Uri getImageFromOffset(int offset) {
		return dateToImagesMap.get(LocalDate.parse(PicsterApplication.DATE_FORMAT.format(new Date())).minusDays(offset));
	}

	public HashSet<String> getFriends() {
		return friends;
	}


	public void setFriends(HashMap<String, Object> friends) {
		this.friends = new HashSet<String>();
		for (String key : friends.keySet()) {
			this.friends.add(key);
		}
	}
	
	public ArrayList<Uri> getUriList() {
		ArrayList<Uri> uriList = new ArrayList<Uri>();
		
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
	        uriList.add(getImageFromOffset(i)); 
		}
		return uriList;
	}
}