package com.example.picster;

import com.devsmart.android.ui.HorizontalListView;
import com.parse.ParseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.example.picster.FragmentPagerAdapter;
import com.example.picster.VerticalViewPager;

public class HomeActivity extends FragmentActivity {
	private static final int SELECT_PHOTO = 100;
	private static int NUM_ITEMS = 2;
	private DisplayPictureAdapter displayAdapter;
	private static int positionLastClicked;
    MyAdapter mAdapter;
    VerticalViewPager mPager;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PicsterApplication.currentUser == null) {
			startLoginActivity();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		mAdapter = new MyAdapter(getSupportFragmentManager());
	    mPager = (VerticalViewPager) findViewById(R.id.pager);
	    mPager.setAdapter(mAdapter);

		//loadUserPictures();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	private void loadUserPictures() {
		HorizontalListView user_picture_row = (HorizontalListView) findViewById(R.id.user_picture_row);
		ArrayList<PictureGridColumn> columnList = PicsterApplication.currentUser.getColumnList(0);
		this.displayAdapter = new DisplayPictureAdapter(this, R.layout.picture_list_item, columnList);
		user_picture_row.setAdapter(displayAdapter);
<<<<<<< HEAD
		user_picture_row.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				onSetPictureClicked(position);
				return true;
			}
		}); 
		user_picture_row.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Bitmap bmp = PicsterApplication.currentUser.getBitMap(position);
				if (bmp == null) {
					return;
				}
				Intent intent = new Intent(HomeActivity.this, FullScreenImageActivity.class);
				intent.putExtra("bmp", bmp);
				startActivityForResult(intent, 99);
			}
		}); 
=======
		//user_picture_row.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		//	@Override
		//	public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
		//		onSetPictureClicked(position);
		//		return true;
		//	}
		//}); 
>>>>>>> 561d88e9db3854ca1a7c995a511120151530cad6
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
    	    PicsterApplication.currentUser.saveImage(this.positionLastClicked, displayAdapter.getBitmap(data.getData()));
    	    this.displayAdapter.udpateView(PicsterApplication.currentUser.getColumnList(0));
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
    


	private void startLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	// FRAGMENT STUFF FOR VERTICAL PAGE SCROLLING
	
	public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
        	return (PicsterApplication.currentUser.getFriends().size() / 3) + 1;
        }

        @Override
        public Fragment getItem(int position) {
            return HorizontalListFragment.newInstance(position);
        }
    }

    public static class HorizontalListFragment extends Fragment {
        int mNum;
        DisplayPictureAdapter displayAdapter;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static HorizontalListFragment newInstance(int num) {
            HorizontalListFragment f = new HorizontalListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : -1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_image, container, false);
        
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            HorizontalListView tv = (HorizontalListView) getView().findViewById(R.id.user_picture_row);
            ArrayList<PictureGridColumn> columnList = PicsterApplication.currentUser.getColumnList(mNum);
    		this.displayAdapter = new DisplayPictureAdapter(getActivity(), R.layout.picture_list_item, columnList);
    		tv.setAdapter(displayAdapter);
    		tv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    			@Override
    			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
    				onSetPictureClicked(position);
    				return true;
    			}
    		});

        }
        private void onSetPictureClicked(int position) {
          Log.d(PicsterApplication.TAG, "POSITION IS: " + position);
          Intent intent = new Intent();
          intent.setType("image/*");
          intent.setAction(Intent.ACTION_GET_CONTENT);
          HomeActivity.positionLastClicked = position;
          startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);	
        }
        //@Override
        //public void onListItemClick(ListView l, View v, int position, long id) {
        //    Log.i("FragmentList", "Item clicked: " + id);
        //}
    }
}
