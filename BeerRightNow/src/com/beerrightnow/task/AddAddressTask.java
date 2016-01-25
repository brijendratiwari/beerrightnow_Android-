package com.beerrightnow.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.AddAddressTask.AddressData;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class AddAddressTask extends AsyncTask<AddressData, Void, String> {

	private Context context;
	ProgressDialog pd;

	private Callback callback;

	public AddAddressTask(Context context, Callback callback) {

		this.context = context;
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = ProgressDialog.show(context, "", "");
		pd.setContentView(new ProgressBar(context));

	}

	@Override
	protected String doInBackground(AddressData... datas) {
		// TODO Auto-generated method stub
		try {

			AddressData addressData = datas[0];

			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();

			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					LoginInfo.getInstance().getZipCode()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo.getInstance()
							.getDistributorId()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
							.getInstance().getLrnDistributorId()));

			arrParams
					.add(new BasicNameValuePair(Constants.DataKeys.CUSTOMER_ID,
							addressData.getCustomerId()));
			arrParams.add(new BasicNameValuePair("email", addressData
					.getEmail()));
			arrParams.add(new BasicNameValuePair("firstname", addressData
					.getFirstName()));
			arrParams.add(new BasicNameValuePair("lastname", addressData
					.getLastName()));
			arrParams.add(new BasicNameValuePair("street_address", addressData
					.getStreet()));
			arrParams.add(new BasicNameValuePair("phone", addressData
					.getPhone()));
			arrParams
					.add(new BasicNameValuePair("city", addressData.getCity()));
			arrParams.add(new BasicNameValuePair("state", addressData
					.getState()));
			arrParams.add(new BasicNameValuePair("address_zipcode", addressData
					.getAddressZipcode()));

			arrParams
					.add(new BasicNameValuePair("suite", addressData.getSuit()));
			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.ADD_ADDRESS, arrParams);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}

	}

	@Override
	protected void onPostExecute(String response) {
		// TODO Auto-generated method stub
		pd.dismiss();
		if (callback != null)
			callback.onResponse(response);
	}

	public static class AddressData {

		private String customerId;
		private String firstName;
		private String lastName;
		private String email;
		private String street;
		private String phone;
		private String suit;
		private String city;
		private String state;
		private String addressZipcode;

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getSuit() {
			return suit;
		}

		public void setSuit(String suit) {
			this.suit = suit;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getAddressZipcode() {
			return addressZipcode;
		}

		public void setAddressZipcode(String addressZipcode) {
			this.addressZipcode = addressZipcode;
		}

	}
}
