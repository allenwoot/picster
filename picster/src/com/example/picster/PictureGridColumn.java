package com.example.picster;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class PictureGridColumn {

	public ArrayList<Bitmap> bitmaps;

	public PictureGridColumn() {
		this.bitmaps = new ArrayList<Bitmap>();
	}
	
	public PictureGridColumn(ArrayList<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	public void addBitmap(Bitmap bitmap) {
		this.bitmaps.add(bitmap);
	}
}
