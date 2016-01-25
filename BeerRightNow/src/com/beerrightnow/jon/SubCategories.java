package com.beerrightnow.jon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.SubCategoriesAdapter;
import com.beerrightnow.data.SubSection;
import com.beerrightnow.data.SubWrapper;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class SubCategories extends SherlockActivity {

	public static final String SUB_WRAPPER = "sub_wrapper";

	private ListView listView;
	private TextView txtTotCart;
	private SubWrapper subWrapper;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.stub)
			.showImageForEmptyUri(R.drawable.stub)
			.showImageOnFail(R.drawable.stub).cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.considerExifParams(true).displayer(new SimpleBitmapDisplayer())
			.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_categories);
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {

		subWrapper = (SubWrapper) getIntent().getSerializableExtra(SUB_WRAPPER);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText(subWrapper.getSection().getName());

		TextView browseEntire = (TextView) findViewById(R.id.browseEntire);
		browseEntire.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}

		});

		txtTotCart = (TextView) findViewById(R.id.txtCartTot);
		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(SubCategories.this);
			}
		});
		View relCart = findViewById(R.id.relCart);
		relCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (LoginInfo.getInstance().getTotalCarts() > 0) {

					startActivity(new Intent(SubCategories.this,
							ShopingCart.class));
				} else {

					Toast.makeText(SubCategories.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		listView = (ListView) findViewById(R.id.lstSubCate);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				SubSection subSection = (SubSection) listView.getAdapter()
						.getItem(position);
				Intent intent = new Intent(SubCategories.this,
						ProductDisplay.class);
				intent.putExtra(ProductDisplay.SUB_SECTION, subSection);
				intent.putExtra(ProductDisplay.SECTION, subWrapper.getSection());
				startActivity(intent);
			}

		});

		listView.setAdapter(new SubCategoriesAdapter(this, subWrapper
				.getSubSections()));

		ImageView ivSubCat = (ImageView) findViewById(R.id.ivSubCat);
		ImageLoader.getInstance().displayImage(
				subWrapper.getSection().getImage(), ivSubCat, options);

	}

	@Override
	protected void onResume() {
		super.onResume();
		updateCartCount();
	}

	void updateCartCount() {

		int cartCount = LoginInfo.getInstance().getTotalCarts();
		if (cartCount != 0) {
			txtTotCart.setVisibility(View.VISIBLE);
			txtTotCart.setText(Integer.valueOf(cartCount).toString());
		} else {
			txtTotCart.setVisibility(View.INVISIBLE);
		}
	}

}
