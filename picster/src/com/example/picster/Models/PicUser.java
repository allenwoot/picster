package com.example.picster.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import android.net.Uri;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	private String id;
	private HashMap<String, PicUser> friends;
	private HashMap<Date, Uri> dateToUri;
	private ParseUser parseUser;
	public static String defaultPassword = "password";
	
	public PicUser(String id, HashMap<String, Object> friends, ParseUser parseUser) {
		this.id = id;
		this.friends = null;
		this.parseUser = parseUser;
	}
	
	public Uri getUriOfDate(Date date) {
		return dateToUri.get(date);
	}
	
}