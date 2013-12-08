package com.example.picster.Models;

import java.util.HashMap;
import java.util.HashSet;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	private String id;
	private String name;
	private HashMap<String, PicUser> friends;
	private ParseUser parseUser;
	public static String defaultPassword = "password";
	
	public PicUser(String id, String name, HashMap<String, Object> friends, ParseUser parseUser) {
		this.id = id;
		this.friends = null;
		this.parseUser = parseUser;
	}
}