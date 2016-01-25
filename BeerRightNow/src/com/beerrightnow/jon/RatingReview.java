package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.ReviewAdapter;
import com.beerrightnow.data.Review;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class RatingReview extends SherlockActivity {

	public static final String PRODUCT_ID = "product_id";
	public static final String PRODUCT_NAME = "product_name";

	private ListView listView;
	private TextView txtTotCart;
	private TextView txtBottomTotCart;
	private TextView txtReviewCount;
	private RatingBar ratingBar;

	private String productId;
	private String productName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_rating);
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

	private void init() {
		// TODO Auto-generated method stub
		productId = getIntent().getStringExtra(PRODUCT_ID);
		productName = getIntent().getStringExtra(PRODUCT_NAME);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));
		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText(productName);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		txtReviewCount = (TextView) findViewById(R.id.txtReviewCount);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(RatingReview.this);
			}
		});
		View relCart = findViewById(R.id.relCart);
		txtTotCart = (TextView) findViewById(R.id.txtCartTot);
		relCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (LoginInfo.getInstance().getTotalCarts() != 0) {
					startActivity(new Intent(RatingReview.this,
							ShopingCart.class));
				} else {

					Toast.makeText(RatingReview.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		txtBottomTotCart = (TextView) findViewById(R.id.txtCartBottom);

		View llCheckout = findViewById(R.id.llCheckout);
		llCheckout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (LoginInfo.getInstance().getTotalCarts() != 0) {
					startActivity(new Intent(RatingReview.this,
							ShopingCart.class));
				} else {

					Toast.makeText(RatingReview.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		listView = (ListView) findViewById(R.id.lstReviews);
		new GetReviewTask().execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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

		txtBottomTotCart.setText(Integer.valueOf(cartCount).toString());
	}

	class GetReviewTask extends AsyncTask<Void, Void, String> {

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(RatingReview.this, "", "");
			pd.setContentView(new ProgressBar(RatingReview.this));

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo.getInstance()
							.getDistributorId()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
							.getInstance().getLrnDistributorId()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					LoginInfo.getInstance().getZipCode()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.PRODUCT_ID,
					productId));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.GET_REVIEWS, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if (response.isEmpty()) {

				Toast.makeText(RatingReview.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {
					List<Review> reviews = Utils
							.getInstance()
							.getGson()
							.fromJson(
									jobj.getJSONArray(Constants.DataKeys.DATA)
											.toString(),
									new TypeToken<List<Review>>() {
									}.getType());

					if (reviews.size() == 0) {

						Toast.makeText(RatingReview.this, R.string.no_reviews,
								Toast.LENGTH_SHORT).show();
					} else {

						listView.setAdapter(new ReviewAdapter(
								RatingReview.this, reviews));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(RatingReview.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		}
	}

}
