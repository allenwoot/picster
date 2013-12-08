package com.example.picster;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class FullScreenImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_image);
		Bitmap bmp = this.getIntent().getParcelableExtra("bmp");
		ImageView imageView = (ImageView) findViewById(R.id.fullScreenImage);
		imageView.setImageBitmap(bmp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_screen_image, menu);
		return true;
	}

}
