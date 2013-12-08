package com.example.picster.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.joda.time.LocalDate;

import android.R.integer;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.example.picster.PicsterApplication;
import com.example.picster.PictureGridColumn;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PicUser {
	public final static int MAX_LIST_SIZE = 25;
	public final static int CURRENT_DAY = 5;
	private String id;
	private String name;
	private ArrayList<String> friends;
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
	
	public ArrayList<String> getFriends() {
		return friends;
	}

	public void setFriends(HashMap<String, Object> friends) {
		this.friends = new ArrayList<String>();
		for (String key : friends.keySet()) {
			this.friends.add(key);
		}
	}
	
	public Bitmap getBitMap(int index) {
		return picUserImageMap.getThumbnailFromDate(LocalDate.parse((PicsterApplication.DATE_FORMAT.format(new Date()))).minusDays(index));
	}
	
	public ArrayList<Bitmap> getBitmaps() {
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
			bitmaps.add(picUserImageMap.getThumbnailFromDate(LocalDate.parse((PicsterApplication.DATE_FORMAT.format(new Date()))).minusDays(i))); 
		}
		return bitmaps;
	}
	
	public ArrayList<PictureGridColumn> getColumnList(int num) {
		num *= 3;
		ArrayList<Bitmap> currentUserList = getBitmaps();
		ArrayList<Bitmap> friend1List = null;
		ArrayList<Bitmap> friend2List = null;
		ArrayList<Bitmap> friend3List = null;
		Log.d(PicsterApplication.TAG, "friends list size" + friends.size());
		
		if (num < friends.size()) {
			PicUser friend1 = new PicUser(friends.get(num));
			friend1List = friend1.getBitmaps(); 
		}
		if (num < friends.size() - 1) {
			PicUser friend2 = new PicUser(friends.get(num));
			friend2List = friend2.getBitmaps(); 
		}
		if (num < friends.size() - 2) {
			PicUser friend3 = new PicUser(friends.get(num));
			friend3List = friend3.getBitmaps(); 
		}
		ArrayList<PictureGridColumn> returnList = new ArrayList<PictureGridColumn>();
		for (int i = 0; i < MAX_LIST_SIZE; i++) {
			ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
			if (currentUserList != null) {
				bitmaps.add(currentUserList.get(i));
			} else {
				Log.d(PicsterApplication.TAG, "currentUserList is Null??");
			}
			if (friend1List != null) {
				bitmaps.add(friend1List.get(i));
			}
			if (friend2List != null) {
				bitmaps.add(friend2List.get(i));
			}
			if (friend3List!=null) {
				bitmaps.add(friend3List.get(i));
			}
			returnList.add(new PictureGridColumn(bitmaps));
		}
		return returnList;
	}
}