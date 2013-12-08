package com.example.picster;

<<<<<<< HEAD
import com.parse.ParseUser;

=======
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.net.Uri;
>>>>>>> a7e883a206a4b1a0d10b4d931b5f02637f1c5e92
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
>>>>>>> a7e883a206a4b1a0d10b4d931b5f02637f1c5e92
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private static final int SELECT_PHOTO = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PicsterApplication.currentUser == null) {
			startLoginActivity();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setAddPictureButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
        InputStream imageStream;
        // Upload to parse
		try {
			imageStream = getContentResolver().openInputStream(selectedImage);
			Bitmap chosenImageBitmap = BitmapFactory.decodeStream(imageStream);	
			ImageView imageView = (ImageView) findViewById(R.id.chosen_picture);
			imageView.setImageBitmap(chosenImageBitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    private void onAddPictureClicked() {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);	
    }

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
