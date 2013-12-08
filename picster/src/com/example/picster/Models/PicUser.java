package com.example.picster.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import android.net.Uri;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	private String id;
	private String name;
	private HashMap<String, PicUser> friends;
	private HashMap<Date, Uri> dateToUri;
	private ParseUser parseUser;
	public static String defaultPassword = "password";

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
	}
	
	public Uri getUriOfDate(Date date) {
		return dateToUri.get(date);
	}
	
}