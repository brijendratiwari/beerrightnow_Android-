package com.beerrightnow.jon;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.data.OtherCharge;
import com.beerrightnow.data.Product;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ProductDescription extends SherlockActivity {

	public static final String PRODUCT = "product";

	private TextView txtQuantity;
	private TextView txtTotCart;
	private TextView title;
	private Product product;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.stub)
			.showImageForEmptyUri(R.drawable.stub)
			.showImageOnFail(R.drawable.stub).cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.considerExifParams(true).displayer(new SimpleBitmapDisplayer())
			.build();

	private Handler mHandler = new Handler();

	private Runnable mDecTask = new Runnable() {
		public void run() {

			try {

				int qua = Integer.parseInt(txtQuantity.getText().toString());
				if (qua > 1)
					txtQuantity.setText("" + (--qua));
			} catch (NullPointerException e) {

				e.printStackTrace();
			}
			mHandler.postDelayed(this, Constants.REPEAT_CLICK_TIME);
		}
	};

	private Runnable mIncTask = new Runnable() {
		public void run() {

			try {

				int qua = Integer.parseInt(txtQuantity.getText().toString());
				txtQuantity.setText("" + (++qua));
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			mHandler.postDelayed(this, Constants.REPEAT_CLICK_TIME);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_description);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		product = (Product) getIntent().getSerializableExtra(PRODUCT);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		txtTotCart = (TextView) findViewById(R.id.txtCartTot);

		View relCart = findViewById(R.id.relCart);
		relCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (LoginInfo.getInstance().getTotalCarts() != 0) {

					startActivity(new Intent(ProductDescription.this,
							ShopingCart.class));
				} else {

					Toast.makeText(ProductDescription.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ProductDescription.this);
			}
		});
		TextView txtProductName = (TextView) findViewById(R.id.txtProName);
		TextView txtContent = (TextView) findViewById(R.id.txtContent);
		String wholeName = product.getProductsName();
		String[] names;
		if (wholeName.contains(","))
			names = wholeName.split(",");
		else
			names = new String[] { wholeName, "" };

		txtProductName.setText(names[0]);
		txtContent.setText(names[1]);
		title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText(names[0]);

		TextView txtOrigin = (TextView) findViewById(R.id.txtOrigin);
		txtOrigin.setText(product.getOriginName());

		TextView txtProductPrice = (TextView) findViewById(R.id.txtProPrice);

		TextView txtProCent = (TextView) findViewById(R.id.txtProCents);
		String wholePrice = product.getProductsPrice();
		String[] prices;
		if (wholePrice.contains(".")) {
			prices = wholePrice.replace(".", ",").split(",");
		} else {
			prices = new String[] { wholePrice, "00" };
		}
		txtProductPrice.setText("$" + prices[0]);
		txtProCent.setText(prices[1]);

		TextView txtStyle = (TextView) findViewById(R.id.txtStyle);
		txtStyle.setText(product.getStyleName());

		TextView txtDesc = (TextView) findViewById(R.id.txtDesc);
		txtDesc.setText(product.getProductsDescription());

		TextView txtAddCart = (TextView) findViewById(R.id.txtAddCart);
		// Add to cart
		txtAddCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addToCart();
			}
		});

		if (product.getOtherCharges().isEmpty()) {

			View otherGroup = findViewById(R.id.otherGroup);
			otherGroup.setVisibility(View.GONE);
		} else {

			try {

				OtherCharge deposit = product.getOtherCharges().get(0);
				OtherCharge other = product.getOtherCharges().get(1);

				TextView txtOtherTitle1 = (TextView) findViewById(R.id.txtOtherTitle1);
				txtOtherTitle1.setText(deposit.getProductsOptionsName());

				TextView txtOtherValue1 = (TextView) findViewById(R.id.txtOtherValue1);
				txtOtherValue1.setText(deposit.getPricePrefix()
						+ "$"
						+ Utils.getInstance().convertToDecimalPointString(
								deposit.getOptionsValuesPrice(), 2));

				TextView txtOtherTitle2 = (TextView) findViewById(R.id.txtOtherTitle2);
				txtOtherTitle2.setText(other.getProductsOptionsName());

				TextView txtOtherValue2 = (TextView) findViewById(R.id.txtOtherValue2);
				txtOtherValue2.setText(other.getPricePrefix()
						+ "$"
						+ Utils.getInstance().convertToDecimalPointString(
								other.getOptionsValuesPrice(), 2));

			} catch (IndexOutOfBoundsException e) {

				e.printStackTrace();
			}
		}

		txtQuantity = (TextView) findViewById(R.id.txtQuantity);
		txtQuantity.setText("1");

		View llDecrease = findViewById(R.id.llDecQuantity);
		llDecrease.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					mHandler.post(mDecTask);
					return true;
				}

				if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_CANCEL) {
					mHandler.removeCallbacks(mDecTask);
				}
				return false;
			}

		});
		View llIncrease = findViewById(R.id.llIncQuantity);
		llIncrease.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					mHandler.post(mIncTask);
					return true;
				}

				if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_CANCEL) {

					mHandler.removeCallbacks(mIncTask);
				}
				return false;
			}

		});

		View llRatingMain = findViewById(R.id.llRatingMain);
		llRatingMain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(ProductDescription.this,
						RatingReview.class);
				intent.putExtra(RatingReview.PRODUCT_ID,
						product.getProductsId());
				intent.putExtra(RatingReview.PRODUCT_NAME, title.getText()
						.toString());
				startActivity(intent);

			}
		});

		ImageView imgProduct = (ImageView) findViewById(R.id.imgProduct);
		ImageLoader.getInstance().displayImage(product.getProductsImage(),
				imgProduct, options);

		new GetProductDescTask().execute();
	}

	@Override
	protected void onDestroy() {

		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateCartCount();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
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

	private void addToCart() {

		String productCount = txtQuantity.getText().toString();
		String result = LoginInfo.getInstance().addProductToCart(
				product.getProductsId(), productCount);

		if (result.isEmpty())
			return;

		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		updateCartCount();
	}

	private class GetProductDescTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(ProductDescription.this, "", "");
			pd.setContentView(new ProgressBar(ProductDescription.this));

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
					product.getProductsId()));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.PRODUCT_DETAILS, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();

			if (response.isEmpty()) {

				Toast.makeText(ProductDescription.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {
					JSONObject dataObject = jobj
							.getJSONObject(Constants.DataKeys.DATA);
					updateDescView(dataObject);
				} else {
					Toast.makeText(ProductDescription.this,
							"No Product Description found...",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception ex) {
				Toast.makeText(ProductDescription.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}

	}

	void updateDescView(JSONObject dataObject) throws JSONException {
		// TODO Auto-generated method stub

		try {

			TextView txtReview = (TextView) findViewById(R.id.txtReview);
			txtReview.setText(dataObject.getString("total_reviews"));

			TextView txtOrigin = (TextView) findViewById(R.id.txtOrigin);
			txtOrigin.setText(dataObject.getString("origin_name"));

			if (txtOrigin.getText().toString().equalsIgnoreCase("-NA-")) {

				View countryGroup = findViewById(R.id.countryGroup);
				countryGroup.setVisibility(View.GONE);
			}

			TextView txtAlchoholContent = (TextView) findViewById(R.id.txtAlco_Content);
			txtAlchoholContent.setText(dataObject
					.getString("alcholol_percentage") + "%");

			if (txtAlchoholContent.getText().toString().equalsIgnoreCase("0%")) {

				View alcholGroup = findViewById(R.id.alcholGroup);
				alcholGroup.setVisibility(View.GONE);
			}

			RatingBar ratAvg = (RatingBar) findViewById(R.id.ratAvg);
			ratAvg.setProgress(dataObject.getInt("average_rating"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
