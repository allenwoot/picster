package com.example.picster;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.LocalDate;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.DeleteCallback;
import com.parse.ParseException;

public class DisplayPictureAdapter extends ArrayAdapter<PictureGridColumn> {
	
	private Context context;
	private ArrayList<PictureGridColumn> columns;
	public DisplayPictureAdapter(Context context, int resource, ArrayList<PictureGridColumn> column_list) {
		super(context, resource, column_list);
		this.context = context;
		this.columns = column_list;
	}
	
	private class ViewHolder {
		ImageView pictureView;
		ImageView friend1PictureView;
		ImageView friend2PictureView;
		ImageView friend3PictureView;
		TextView dateTextView;
	}
	
	public void udpateView(ArrayList<PictureGridColumn> column_list) {
		columns.clear();
	    columns.addAll(column_list);
		Log.d(PicsterApplication.TAG, "updating VIEW!");
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		final PictureGridColumn currentColumn = getItem(position); 
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_list_item, null);
            holder = new ViewHolder();
            holder.pictureView = (ImageView) convertView.findViewById(R.id.daily_picture);
            holder.friend1PictureView = (ImageView) convertView.findViewById(R.id.friend1_daily_picture);
            holder.friend2PictureView = (ImageView) convertView.findViewById(R.id.friend2_daily_picture);
            holder.friend3PictureView = (ImageView) convertView.findViewById(R.id.friend3_daily_picture);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.image_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
		holder.dateTextView.setText(PicsterApplication.IMAGE_DATE_FORMAT.format(LocalDate.parse(PicsterApplication.DATE_FORMAT.format(new Date())).minusDays(position).toDate()));
        // Fix to use real current Column values
    	Bitmap defaultImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.question_mark);
        if (currentColumn.bitmaps.size() > 0) {
        	if (currentColumn.bitmaps.get(0) != null) {
        		holder.pictureView.setImageBitmap(ThumbnailUtils.extractThumbnail(currentColumn.bitmaps.get(0), 125, 125));
        	} else {
        		holder.pictureView.setImageBitmap(defaultImage);
        	}
        }
        if (currentColumn.bitmaps.size() > 1) {
        	if (currentColumn.bitmaps.get(1) != null) {
        		holder.pictureView.setImageBitmap(ThumbnailUtils.extractThumbnail(currentColumn.bitmaps.get(1), 125, 125));
        	} else {
        		holder.friend1PictureView.setImageBitmap(defaultImage);
        	}
        }
        if (currentColumn.bitmaps.size() > 2) {
        	if (currentColumn.bitmaps.get(2) != null) {
        		holder.friend2PictureView.setImageBitmap(ThumbnailUtils.extractThumbnail(currentColumn.bitmaps.get(2), 125, 125));
        	} else {
        		holder.friend2PictureView.setImageBitmap(defaultImage);
        	}
        }
        if (currentColumn.bitmaps.size() > 3) {
        	if (currentColumn.bitmaps.get(3) != null) {
        		holder.friend3PictureView.setImageBitmap(ThumbnailUtils.extractThumbnail(currentColumn.bitmaps.get(3), 125, 125));
        	} else {
        		holder.friend3PictureView.setImageBitmap(defaultImage);
        	}
        }
        //TODO make them disappear if they're not in currentColumn, set imageveiw to GONE
	    return convertView;
	}
	
	public Bitmap getBitmap(Uri uri) {

		InputStream in = null;
		try {
		    final long IMAGE_MAX_SIZE = PicsterApplication.MAX_IMAGE_DIMENSIONS;
		    ContentResolver mContentResolver = getContext().getContentResolver();
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
	  