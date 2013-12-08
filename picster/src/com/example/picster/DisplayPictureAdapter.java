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

public class DisplayPictureAdapter extends ArrayAdapter<Bitmap> {
	
	private Context context;
	private ArrayList<Bitmap> bitmaps;
	public DisplayPictureAdapter(Context context, int resource, ArrayList<Bitmap> bitmaps) {
		super(context, resource, bitmaps);
		this.context = context;
		this.bitmaps = bitmaps;
	}
	
	private class ViewHolder {
		ImageView pictureView;
	}
	
	public void udpateView(ArrayList<Bitmap> bitmaps) {
		this.bitmaps.clear();
	    this.bitmaps.addAll(bitmaps);
		Log.d(PicsterApplication.TAG, "updating VIEW!");
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		final Bitmap bitmap = getItem(position); 
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_list_item, null);
            holder = new ViewHolder();
            holder.pictureView = (ImageView) convertView.findViewById(R.id.daily_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Setting date text
		TextView dateTextView = (TextView) convertView.findViewById(R.id.image_date);
		dateTextView.setText(PicsterApplication.IMAGE_DATE_FORMAT.format(LocalDate.parse(PicsterApplication.DATE_FORMAT.format(new Date())).minusDays(position).toDate()));
    	if (bitmap == null) {
    		holder.pictureView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.question_mark));
    	} else {
			holder.pictureView.setImageBitmap(bitmap);
    	}
		
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
	  