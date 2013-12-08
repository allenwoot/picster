package com.example.picster.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.example.picster.PicsterApplication;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.graphics.Bitmap;
import android.util.Log;

public class PicUserImageMap {
	private String fbId;
	private HashMap<LocalDate, Bitmap> dateToImagesMap;
	
	public PicUserImageMap(String fbId) {
		this.fbId = fbId;
		this.dateToImagesMap = new HashMap<LocalDate, Bitmap>();
		
		try {
			List<ParseObject> queryResults = (new ParseQuery<ParseObject>("UserImage")).whereEqualTo("fbId", this.fbId).find();
			if (queryResults.size() == 1) {
				ParseObject queryResult = queryResults.get(0);
				HashMap<String, Object> parseDateToImagesMap = (HashMap<String, Object>) queryResult.getMap("dateToImages");
				for (Map.Entry<String, Object> keyValuePair : parseDateToImagesMap.entrySet()) {
				}
			}
		} catch (ParseException e) {
			Log.e(PicsterApplication.TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public Bitmap getBitmapFromDate(LocalDate date) {
		return null;
	}
	
	public Bitmap getThumbnailFromDate(LocalDate date) {
		return null;
	}
	
	public void saveImageToParse(String fbId, Bitmap image) {
		
	}
}
