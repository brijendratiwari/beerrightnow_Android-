package com.beerrightnow.jon;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.utility.Utils;

public class FinalActivity extends SherlockActivity {

	public static final String SHARING_BITMAP_URL = "sharing_bitmap_url";

	public static Bitmap bitmap;
	private Uri bitmapUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_activity);

		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		bitmap.recycle();
		bitmap = null;
	}

	private void init() {
		// TODO Auto-generated method stub
		bitmapUri = (Uri) getIntent().getParcelableExtra(SHARING_BITMAP_URL);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Share Your Photo");
		View relCart = (RelativeLayout) findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setVisibility(View.GONE);

		final ImageView ivCam = (ImageView) findViewById(R.id.ivCamImage);
		if (bitmap != null)
			ivCam.setImageBitmap(bitmap);
		else
			ivCam.setImageResource(R.drawable.logo);

		final ImageView ivClick = (ImageView) findViewById(R.id.ivClick);
		ivClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Utils.getInstance()
						.sharePhoto(
								FinalActivity.this,
								"I'm getting Beer, Wine, Liquor, Sake, Beverage, Snacks delivered from @BeerRightNow !",
								bitmapUri);
			}
		});

	}
}
