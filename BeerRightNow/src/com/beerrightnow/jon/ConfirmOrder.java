package com.beerrightnow.jon;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.dialog.DeliveryExpectedDlg;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;

public class ConfirmOrder extends SherlockActivity {

	private TextView txtAddressName;
	private TextView txtAddress;
	private TextView txtCityState;
	private TextView txtZipcode;
	private TextView txtviewExprectedDelivery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_order);
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

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ConfirmOrder.this);
			}
		});
		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Confirm Order");

		txtAddressName = (TextView) findViewById(R.id.txtAddressName);
		txtAddress = (TextView) findViewById(R.id.txtStreet);
		txtCityState = (TextView) findViewById(R.id.txtCityState);
		txtZipcode = (TextView) findViewById(R.id.txtZipcode);
		txtviewExprectedDelivery=(TextView)findViewById(R.id.txtviewExprectedDelivery);
		
		if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0 && Integer.valueOf(CartInfo.getInstance().getLiquorCount())==0)
		{
			txtviewExprectedDelivery.setText(CartInfo
					.getInstance().getDeliveryExpected());
			
		}
		else if(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0 && Integer.valueOf(CartInfo.getInstance().getBeerCount())==0)
		{
			txtviewExprectedDelivery.setText(CartInfo
					.getInstance().getDeliveryExpected());
			
		}
		else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&!CartInfo.getInstance().getState().equalsIgnoreCase("Save"))
		{
			
			
			if(CartInfo.getInstance().getDeliveryExpected().equalsIgnoreCase("ASAP"))
			{
				txtviewExprectedDelivery.setText(CartInfo
						.getInstance().getDeliveryExpected());
				
			}
			else
			{
				txtviewExprectedDelivery.setText("Beer: "+CartInfo.getInstance().getDeliveryExpected_Beer()+'\n'+"Liquor: "+CartInfo.getInstance().getDeliveryExpected_Liquor());
			}
			
			
			
			/*arrParams.add(new BasicNameValuePair("expected_beer_delivery", CartInfo
					.getInstance().getDeliveryExpected_Beer()));
			arrParams.add(new BasicNameValuePair("expected_liquor_delivery", CartInfo
					.getInstance().getDeliveryExpected_Liquor()));*/
		}
		else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&DeliveryExpectedDlg.timeliquor.equalsIgnoreCase("Match"))
		{
			
			
			
			txtviewExprectedDelivery.setText(CartInfo
					.getInstance().getDeliveryExpected());
			/*arrParams.add(new BasicNameValuePair("expected_delivery", CartInfo
					.getInstance().getDeliveryExpected()));*/
		}
		else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)&&CartInfo.getInstance().getState().equalsIgnoreCase("Save"))
		{
			
			
			txtviewExprectedDelivery.setText(CartInfo
					.getInstance().getDeliveryExpected());
			
			/*arrParams.add(new BasicNameValuePair("expected_beer_delivery", CartInfo
					.getInstance().getDeliveryExpected_Beer()));
			arrParams.add(new BasicNameValuePair("expected_liquor_delivery", CartInfo
					.getInstance().getDeliveryExpected_Liquor()));*/
		}
		
		

		final TextView txtTotalPay = (TextView) findViewById(R.id.txtTotalPay);

		TextView txtCardNumber = (TextView) findViewById(R.id.txtCardNumber);
		txtCardNumber.setText(CartInfo.getInstance().getCardNumber());

		TextView txtCardExpire = (TextView) findViewById(R.id.txtCardExpire);
		txtCardExpire.setText(CartInfo.getInstance().getCardExpires());

		TextView txtComment = (TextView) findViewById(R.id.txtComment);
		txtComment.setText(CartInfo.getInstance().getDeliveryComment());

		new GetDefaultAddressTask().execute();

		txtTotalPay.setText("$"
				+ Utils.getInstance().convertToDecimalPointString(
						CartInfo.getInstance().getTotalPay(), 2));

		View giftGroup = findViewById(R.id.giftGroup);
		if (CartInfo.getInstance().isGift())
			giftGroup.setVisibility(View.VISIBLE);
		else
			giftGroup.setVisibility(View.GONE);

		Button orderBtn = (Button) findViewById(R.id.orderBtn);
		orderBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(ConfirmOrder.this, ProcessOrder.class));
				;
			}
		});

	}

	public class GetDefaultAddressTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(ConfirmOrder.this, "", "");
			pd.setContentView(new ProgressBar(ConfirmOrder.this));

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
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.CUSTOMER_ID, LoginInfo.getInstance()
							.getUserInfo().getCustomersId()));
			arrParams
					.add(new BasicNameValuePair("address_id", LoginInfo
							.getInstance().getUserInfo()
							.getCustomersDefaultAddressId()));
			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.GET_ADDRESS, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(response);

				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					jsonObj = jsonObj.getJSONObject(Constants.DataKeys.DATA);
					txtAddressName.setText(jsonObj.getString("firstname") + " "
							+ jsonObj.getString("lastname"));

					txtAddress.setText(jsonObj.getString("street_address"));
					txtCityState.setText(jsonObj.getString("city") + " , "
							+ jsonObj.getString("state"));
					txtZipcode.setText(jsonObj.getString("address_zipcode"));

				} else {

					Toast.makeText(ConfirmOrder.this,
							jsonObj.getString(Constants.DataKeys.MESSAGE),
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
}
