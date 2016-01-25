package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.AddressAdapter;
import com.beerrightnow.data.Address;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class ShippingAddress extends SherlockActivity {

	public static final String ONLY_FOR_ADDRESS = "only_for_address";

	private ListView listView;
	private AddressAdapter adapter;
	private CheckBox sameChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shipping_address);
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {

		if (requestCode == AddOtherAddress.ADD_ADDRESS_REQUEST
				&& resultCode == RESULT_OK) {

			try {

				new GetAddressTask().execute();

			} catch (NullPointerException e) {

				e.printStackTrace();
			}
		}
	}

	private void init() {
		// TODO Auto-generated method stub

		boolean isOnlyForAddress = getIntent().getBooleanExtra(
				ONLY_FOR_ADDRESS, false);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));
		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Shipping Address");

		View relCart = (View) findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ShippingAddress.this);
			}
		});
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View footerView = inflater.inflate(R.layout.row_address_footer, null);
		TextView txtAddAddress = (TextView) footerView
				.findViewById(R.id.txtOtherAddress);
		txtAddAddress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(ShippingAddress.this,
						AddOtherAddress.class),
						AddOtherAddress.ADD_ADDRESS_REQUEST);
			}
		});

		sameChecked = (CheckBox) findViewById(R.id.sameChecked);

		listView = (ListView) findViewById(R.id.lstAddress);
		listView.addFooterView(footerView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				try {

					Address address = (Address) adapter.getItem(position);
					LoginInfo.getInstance().setDefaultAddressId(
							address.getAddressId());
					adapter.notifyDataSetChanged();
				} catch (IndexOutOfBoundsException e) {

					e.printStackTrace();
				}

			}

		});

		TextView txtDeliveryOptions = (TextView) findViewById(R.id.txtDeliveryOptions);
		if (isOnlyForAddress) {

			txtDeliveryOptions.setVisibility(View.GONE);
		} else {

			txtDeliveryOptions.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					try {

						if (sameChecked.isChecked()) {

							for (int i = 0; i < adapter.getCount(); i++) {

								Address address = adapter.getItem(i);
								if (address
										.getAddressId()
										.equalsIgnoreCase(
												LoginInfo
														.getInstance()
														.getUserInfo()
														.getCustomersDefaultAddressId())) {

									CartInfo.getInstance().setBillingName(
											address.getFirstName() + " "
													+ address.getLastName());
									CartInfo.getInstance().setBillingPostCode(
											address.getAddressZipCode());
									CartInfo.getInstance().setBillingAddres(
											address.getStreetAddress());
									CartInfo.getInstance().setBillingCity(
											address.getCity());
									CartInfo.getInstance().setBillingSuite(
											address.getSuite());
									CartInfo.getInstance().setBillingState(
											address.getState());
								}
							}

						}
					} catch (Exception e) {

						e.printStackTrace();
					}

					startActivity(new Intent(ShippingAddress.this,
							DeliveryOptions.class));
				}
			});

		}

		new GetAddressTask().execute();

	}

	public class GetAddressTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(ShippingAddress.this, "", "");
			pd.setContentView(new ProgressBar(ShippingAddress.this));

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
			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.GET_ADDRESSES, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(response);

				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {
					JSONArray jsonArr = jsonObj
							.getJSONArray(Constants.DataKeys.DATA);
					List<Address> addresses = Utils
							.getInstance()
							.getGson()
							.fromJson(jsonArr.toString(),
									new TypeToken<List<Address>>() {
									}.getType());

					adapter = new AddressAdapter(ShippingAddress.this,
							addresses);
					listView.setAdapter(adapter);
				} else {

					Toast.makeText(ShippingAddress.this,
							jsonObj.getString(Constants.DataKeys.MESSAGE),
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
}
