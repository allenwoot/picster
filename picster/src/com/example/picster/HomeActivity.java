package com.example.picster;

import com.devsmart.android.ui.HorizontalListView;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

public class HomeActivity extends Activity {
	private static final int SELECT_PHOTO = 100;
	private static final String POSITION_IN_LIST = "position_in_list";
	private DisplayPictureAdapter displayAdapter;
	private int positionLastClicked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PicsterApplication.currentUser == null) {
			startLoginActivity();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
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
		user_picture_row.setAdapter(displayAdapter);
		user_picture_row.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				onSetPictureClicked(position);
				return true;
			}
		}); 
	}
	//TODO remove this
    //private void setAddPictureButton() {
    //	TextView addPictureButton = (TextView) findViewById(R.id.add_picture_button);
	//	addPictureButton.setOnClickListener(new View.OnClickListener() {
	//		@Override
	//		public void onClick(View v) {
	//			onAddPictureClicked();
	//		}
	//	});
    //}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        if (resultCode == RESULT_OK) {
        	Log.d(PicsterApplication.TAG, "In activity result");
    	    Uri selectedImage = data.getData();
    	    PicsterApplication.currentUser.putInDateToImagesMap(selectedImage, this.positionLastClicked);
    	    this.displayAdapter.udpateView(PicsterApplication.currentUser.getUriList());
        } else {
        	Log.d(PicsterApplication.TAG, "result Code error'd");
        }
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
    
    private void onSetPictureClicked(int position) {
      Log.d(PicsterApplication.TAG, "POSITION IS: " + position);
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      this.positionLastClicked = position;
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);	
    }

	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
