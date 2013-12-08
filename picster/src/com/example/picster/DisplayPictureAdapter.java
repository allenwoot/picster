package com.example.picster;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.DeleteCallback;
import com.parse.ParseException;

public class DisplayPictureAdapter extends ArrayAdapter<Uri> {
	
	private Context context;
	private ArrayList<Uri> uri_strings;
	public DisplayPictureAdapter(Context context, int resource, ArrayList<Uri> uri_list) {
		super(context, resource, uri_list);
		this.context = context;
		this.uri_strings = uri_list;
	}
	
	private class ViewHolder {
		ImageView pictureView;
	}
	
	public void udpateView(ArrayList<Uri> uri_list) {
		uri_strings.clear();
	    uri_strings.addAll(uri_list);
		Log.d(PicsterApplication.TAG, "updating VIEW!");
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		final Uri currentImageUri = getItem(position); 
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_list_item, null);
            holder = new ViewHolder();
            holder.pictureView = (ImageView) convertView.findViewById(R.id.daily_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
    	if (currentImageUri == null) {
    		Log.d(PicsterApplication.TAG, "current image uri is NULL");
    		Bitmap chosenImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.question_mark);
    		holder.pictureView.setImageBitmap(chosenImageBitmap);
    	} else {
    	Bitmap chosenImageBitmap = getBitmap(currentImageUri);
		holder.pictureView.setImageBitmap(ThumbnailUtils.extractThumbnail(chosenImageBitmap, 125, 125));
    	}
		
	    return convertView;
	}
	
	private Bitmap getBitmap(Uri uri) {

		InputStream in = null;
		try {
		    final int IMAGE_MAX_SIZE = 500000; // 0.5 MB
		    ContentResolver mContentResolver = this.getContext().getContentResolver();
		    in = mContentResolver.openInputStream(uri);

		    // Decode image size
		    BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, o);
		    in.close();

		    int scale = 1;
		    while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > 
		          IMAGE_MAX_SIZE) {
		       scale++;
		    }
		    Log.d(PicsterApplication.TAG, "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

		    Bitmap b = null;
		    in = mContentResolver.openInputStream(uri);
		    if (scale > 1) {
		        scale--;
		        // scale to max possible inSampleSize that still yields an image
		        // larger than target
		        o = new BitmapFactory.Options();
		        o.inSampleSize = scale;
		        b = BitmapFactory.decodeStream(in, null, o);

		        // resize to desired dimensions
		        int height = b.getHeight();
		        int width = b.getWidth();
		        Log.d(PicsterApplication.TAG, "1th scale operation dimenions - width: " + width + ", height: " + height);

		        double y = Math.sqrt(IMAGE_MAX_SIZE
		                / (((double) width) / height));
		        double x = (y / height) * width;

		        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, 
		           (int) y, true);
		        b.recycle();
		        b = scaledBitmap;

		        System.gc();
		    } else {
		        b = BitmapFactory.decodeStream(in);
		    }
		    in.close();

		    Log.d(PicsterApplication.TAG, "bitmap size - width: " +b.getWidth() + ", height: " + 
		       b.getHeight());
		    return b;
		} catch (IOException e) {
		    Log.e(PicsterApplication.TAG, e.getMessage(),e);
		    return null;
		}
	}
}
	  