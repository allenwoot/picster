package com.example.picster;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;

public class HomeActivity extends Activity {
	private static final int SELECT_PHOTO = 100;
	
	private DisplayPictureAdapter displayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setAddPictureButton();
		loadUserPictures();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private void loadUserPictures() {
		HorizontalListView user_picture_row = (HorizontalListView) findViewById(R.id.user_picture_row);
		ArrayList<Uri> uriList = PicsterApplication.currentUser.getUriList();
		this.displayAdapter = new DisplayPictureAdapter(this, R.layout.picture_list_item, uriList);
	}
	
    private void setAddPictureButton() {
    	TextView addPictureButton = (TextView) findViewById(R.id.add_picture_button);
		addPictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAddPictureClicked();
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	super.onActivityResult(requestCode, resultCode, data);
    	Uri selectedImage = data.getData();
    	PicsterApplication.currentUser.addPic(selectedImage);
    	this.displayAdapter.udpateView(PicsterApplication.currentUser.getUriList());
        //InputStream imageStream;
        //// Upload to parse
        //// add to hashmap and update display adapter.

		//try {
		//	imageStream = getContentResolver().openInputStream(selectedImage);
		//	Bitmap chosenImageBitmap = BitmapFactory.decodeStream(imageStream);	
		//	ImageView imageView = (ImageView) findViewById(R.id.chosen_picture);
		//	imageView.setImageBitmap(chosenImageBitmap);
		//} catch (FileNotFoundException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
    }
    
    
    private void onAddPictureClicked() {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);	
    }

}
