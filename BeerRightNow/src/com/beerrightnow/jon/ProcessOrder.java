package com.beerrightnow.jon;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.beerrightnow.dialog.DeliveryExpectedDlg;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;

public class ProcessOrder extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_processing);

		init();

	}

	@Override
	public void onBackPressed() {

	}

	private void init() {
		// TODO Auto-generated method stub

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setIcon(new ColorDrawable(Color.TRANSPARENT));
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Processing Order...");

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ProcessOrder.this);
			}
		});
		ImageView imgWheel = (ImageView) findViewById(R.id.imgWheel);
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.anim_rotate);
		imgWheel.startAnimation(animation);

		new ProcessOrderTask().execute();
	}

	class ProcessOrderTask extends AsyncTask<Void, Void, String> {

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

			arrParams.add(new BasicNameValuePair(Constants.DataKeys.DATA,
					LoginInfo.getInstance().getCartData()));
			arrParams.add(new BasicNameValuePair("comments", CartInfo
					.getInstance().getDeliveryComment()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.CUSTOMER_ID, LoginInfo.getInstance()
							.getUserInfo().getCustomersId()));
			arrParams.add(new BasicNameValuePair("customer_email", LoginInfo
					.getInstance().getUserInfo().getCustomersEmailAddress()));

			arrParams
					.add(new BasicNameValuePair("address_id", LoginInfo
							.getInstance().getUserInfo()
							.getCustomersDefaultAddressId()));

			arrParams.add(new BasicNameValuePair("tip", Float.valueOf(
					CartInfo.getInstance().getServiceTip()).toString()));
			arrParams.add(new BasicNameValuePair("billing_name", CartInfo
					.getInstance().getBillingName()));
			arrParams.add(new BasicNameValuePair("billing_street_address",
					CartInfo.getInstance().getBillingAddres()));
			arrParams.add(new BasicNameValuePair("billing_city", CartInfo
					.getInstance().getBillingCity()));
			arrParams.add(new BasicNameValuePair("billing_postcode", CartInfo
					.getInstance().getBillingPostCode()));
			arrParams.add(new BasicNameValuePair("billing_state", CartInfo
					.getInstance().getBillingCountry()));
			arrParams.add(new BasicNameValuePair("billing_country", CartInfo
					.getInstance().getBillingState()));

			AddCreditCard.CreditCard cardType = AddCreditCard.CARDS[CartInfo
					.getInstance().getCardType()];
			arrParams
					.add(new BasicNameValuePair("cc_type", cardType.getName()));

			arrParams.add(new BasicNameValuePair("cc_number", CartInfo
					.getInstance().getCardNumber()));
			arrParams.add(new BasicNameValuePair("cc_expires", CartInfo
					.getInstance().getCardExpires()));
			arrParams.add(new BasicNameValuePair("cc_cvv", CartInfo
					.getInstance().getCardCVV()));
			arrParams.add(new BasicNameValuePair("isexecutive", CartInfo
					.getInstance().isExecutive() ? "yes" : "no"));
		
			
			if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0 && Integer.valueOf(CartInfo.getInstance().getLiquorCount())==0)
			{
				arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
						.getInstance().getDeliveryExpected()));
			}
			else if(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0 && Integer.valueOf(CartInfo.getInstance().getBeerCount())==0)
			{
				arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
						.getInstance().getDeliveryExpected()));
			}
			else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&!CartInfo.getInstance().getState().equalsIgnoreCase("Save"))
			{
				if(CartInfo.getInstance().getDeliveryExpected().equalsIgnoreCase("ASAP"))
				{
					arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
							.getInstance().getDeliveryExpected()));
					
				}
				else
				{
					arrParams.add(new BasicNameValuePair("expected_beer_delivery", CartInfo
							.getInstance().getDeliveryExpected_Beer()));
					arrParams.add(new BasicNameValuePair("expected_liquor_delivery", CartInfo
							.getInstance().getDeliveryExpected_Liquor()));
				}
				
				
			}
			else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&DeliveryExpectedDlg.timeliquor.equalsIgnoreCase("Match"))
			{
				
				
				arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
						.getInstance().getDeliveryExpected()));
			}
			else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&CartInfo.getInstance().getState().equalsIgnoreCase("Save"))
			{
				
				
				arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
						.getInstance().getDeliveryExpected()));
			}
			
			else if(CartInfo.getInstance().getDeliveryExpected().equalsIgnoreCase("ASAP"))
			{
				arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
						.getInstance().getDeliveryExpected()));
				
			}
			

			arrParams.add(new BasicNameValuePair("is_gift", CartInfo
					.getInstance().isGift() ? "yes" : "no"));

			arrParams.add(new BasicNameValuePair("customer_number", CartInfo
					.getInstance().getCustomerNumber()));
			arrParams.add(new BasicNameValuePair("gift_for_number", CartInfo
					.getInstance().getGiftForNumber()));
			arrParams.add(new BasicNameValuePair("is_corporate_order", CartInfo
					.getInstance().isCorporateOrder() ? "yes" : "no"));
			arrParams.add(new BasicNameValuePair("office_extensijon", CartInfo
					.getInstance().getOfficeExtension()));
			arrParams.add(new BasicNameValuePair("contact_cell", CartInfo
					.getInstance().getContactCell()));
			arrParams.add(new BasicNameValuePair("service_enterence_address",
					CartInfo.getInstance().getServiceEnterenceAddress()));
			arrParams.add(new BasicNameValuePair("device", "1"));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.PLACE_ORDER, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub

			if (response.isEmpty()) {

				Toast.makeText(ProcessOrder.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}
			try {
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					jsonObj = new JSONObject(
							jsonObj.getString(Constants.DataKeys.DATA));

					LoginInfo.getInstance().orderProcessed();
					CartInfo.getInstance().orderProcessed();

					Intent intent = new Intent(ProcessOrder.this,
							OrderComplete.class);
					intent.putExtra(OrderComplete.ORDER_ID,
							jsonObj.getString("order_id"));
					startActivity(intent);

					// Intent intent = new Intent(ProcessOrder.this,
					// Home.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// startActivity(intent);

				} else {

					Toast.makeText(ProcessOrder.this,
							jsonObj.getString(Constants.DataKeys.MESSAGE),
							Toast.LENGTH_LONG).show();
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(ProcessOrder.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				finish();
			}

		}
	}
}
