package com.example.picster.Models;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import com.example.picster.PicsterApplication;
import com.facebook.Session.NewPermissionsRequest;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.anim;
import android.R.bool;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

public class PicUserImageMap {
	private String fbId;
	private HashMap<LocalDate, Bitmap> dateToImagesMap;
	private Map<String, Object> parseDatesMap;
	private ParseObject parseObject;
	
	public PicUserImageMap(String fbId) {
		this.fbId = fbId;
		dateToImagesMap = new HashMap<LocalDate, Bitmap>();
		parseDatesMap = new HashMap<String, Object>();
		
		try {
			List<ParseObject> queryResults = (new ParseQuery<ParseObject>("UserImages")).whereEqualTo("fbId", this.fbId).find();
			if (queryResults.size() == 0) {
				// No row; initialize and save
				parseObject = new ParseObject("UserImages");
				parseObject.put("fbId",  fbId);
				parseObject.put("dates", parseDatesMap);
				parseObject.save();
			} else if (queryResults.size() == 1) {
				// Row exists; get info
				parseObject = queryResults.get(0);
				parseDatesMap = (HashMap<String, Object>) parseObject.getMap("dates");
				for (Map.Entry<String, Object> keyValuePair : parseDatesMap.entrySet()) {
					//dateToImagesMap.put(new LocalDate(keyValuePair.getKey()), (Bitmap) keyValuePair.getValue());
					String columnName = keyValuePair.getKey();
					ParseFile imageFile = parseObject.getParseFile("a" + columnName.replace('-', 'b'));
					byte[] imageFileData = imageFile.getData();
					Bitmap bmp = BitmapFactory.decodeByteArray(imageFileData, 0, imageFileData.length);
					dateToImagesMap.put(LocalDate.parse(columnName), bmp);
				}
			} else {
				Log.wtf(PicsterApplication.TAG, "Error: There was more than one row with fbId " + fbId + " in parse dateToImages table");
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
		Bitmap bitmapToReturn = dateToImagesMap.get(date);
		return bitmapToReturn == null ? null : ThumbnailUtils.extractThumbnail(bitmapToReturn, PicsterApplication.IMAGE_DIMENSIONS, PicsterApplication.IMAGE_DIMENSIONS);
	}
	
	public void saveImage(LocalDate date, Bitmap image) {
		dateToImagesMap.put(date, image);
		// Convert to byte array and save to ParseFile
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		try {
			ParseFile bitMapFile = new ParseFile(byteArray);
			bitMapFile.save();
			boolean abcd = parseObject.isDirty();
			parseObject.fetchIfNeeded();
			String columnName = PicsterApplication.DATE_FORMAT.format(date.toDate());
			
			if (!parseDatesMap.containsKey(columnName)) {
				parseDatesMap.put(columnName, "");
				parseObject.put("dates", parseDatesMap);
			}
			parseObject.put("a" + columnName.replace('-', 'b'), bitMapFile);
			parseObject.saveInBackground();
		} catch (ParseException e) {
			Log.e(PicsterApplication.TAG, "Error: " + e.toString());
			e.printStackTrace();
		}
	}
}
