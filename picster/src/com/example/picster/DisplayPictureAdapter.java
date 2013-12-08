package com.example.picster;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
		this.uri_strings = uri_list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;
		final Uri currentImageUri = getItem(position); 
		//TODO(brianlin): handle case of null currentImageUri
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picture_list_item, null);
            holder = new ViewHolder();
            holder.pictureView = (ImageView) convertView.findViewById(R.id.daily_picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        InputStream imageStream;
        try {
			imageStream = context.getContentResolver().openInputStream(currentImageUri);
			Bitmap chosenImageBitmap = BitmapFactory.decodeStream(imageStream);	
			holder.pictureView.setImageBitmap(chosenImageBitmap);;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return convertView;
	}
}
	  