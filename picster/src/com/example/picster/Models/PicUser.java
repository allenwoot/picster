package com.example.picster.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.joda.time.LocalDate;

import android.R.integer;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.picster.PicsterApplication;
import com.example.picster.PictureGridColumn;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	public final static int MAX_LIST_SIZE = 25;
	public final static int CURRENT_DAY = 5;
	private String id;
	private String name;
	private HashSet<String> friends;
	private ParseUser parseUser;
	private PicUserImageMap picUserImageMap;
	public static String defaultPassword = "password";

	public PicUser(String id) {
		this.id = id;
		picUserImageMap = new PicUserImageMap(id);
	}
	
	public PicUser(String id, String name, ParseUser parseUser) {
		this.id = id;
		this.parseUser = parseUser;
		this.picUserImageMap = new PicUserImageMap(id);
	}
	
	public void saveImage(int offset, Bitmap image) {
		picUserImageMap.saveImage(LocalDate.parse((PicsterApplication.DATE_FORMAT.format(new Date())).toString()).minusDays(offset), image);
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
	
	public ArrayList<Bitmap> getBitmaps() {
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
			bitmaps.add(picUserImageMap.getThumbnailFromDate(LocalDate.parse((PicsterApplication.DATE_FORMAT.format(new Date()))).minusDays(i))); 
		}
		return bitmaps;
	}
	
	public ArrayList<PictureGridColumn> getColumnList(int num) {
		ArrayList<PictureGridColumn> returnList = new ArrayList<PictureGridColumn>();
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
			ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
			bitmaps.add(null);
			bitmaps.add(null);
			bitmaps.add(null);
			bitmaps.add(null);
			returnList.add(new PictureGridColumn(bitmaps));
		}
		return returnList;
	}
}