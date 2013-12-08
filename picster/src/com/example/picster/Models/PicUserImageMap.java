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
import android.media.ThumbnailUtils;
import android.util.Log;

public class PicUserImageMap {
	private String fbId;
	private HashMap<LocalDate, Bitmap> dateToImagesMap;
	private Map<String, Object> parseDateToImagesMap;
	private ParseObject parseObject;
	
	public PicUserImageMap(String fbId) {
		this.fbId = fbId;
		dateToImagesMap = new HashMap<LocalDate, Bitmap>();
		parseDateToImagesMap = new HashMap<String, Object>();
		
		try {
			List<ParseObject> queryResults = (new ParseQuery<ParseObject>("UserImage")).whereEqualTo("fbId", this.fbId).find();
			if (queryResults.size() == 1) {
				parseObject = queryResults.get(0);
				parseDateToImagesMap = (HashMap<String, Object>) parseObject.getMap("dateToImages");
				for (Map.Entry<String, Object> keyValuePair : parseDateToImagesMap.entrySet()) {
					dateToImagesMap.put(new LocalDate(keyValuePair.getKey()), (Bitmap) keyValuePair.getValue());
				}
			}
		} catch (ParseException e) {
			Log.e(PicsterApplication.TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public Bitmap getBitmapFromDate(LocalDate date) {
		return dateToImagesMap.get(date);
	}
	
	public Bitmap getThumbnailFromDate(LocalDate date) {
		return ThumbnailUtils.extractThumbnail(dateToImagesMap.get(date), PicsterApplication.IMAGE_DIMENSIONS, PicsterApplication.IMAGE_DIMENSIONS);
	}
	
	public void saveImageToParse(LocalDate date, Bitmap image) {
		parseDateToImagesMap.put(PicsterApplication.DATE_FORMAT.format(date.toDate()), (Object) image);
		parseObject.put("date", parseDateToImagesMap);
		parseObject.saveInBackground();
	}
}
