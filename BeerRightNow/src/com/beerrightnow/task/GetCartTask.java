package com.beerrightnow.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.beerrightnow.data.CartResponse;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.JsonArray;

public class GetCartTask extends AsyncTask<Void, Void, String> {

	private ProgressDialog pd;
	private Context context;
	private CallbackCart callback;

	public GetCartTask(Context context, CallbackCart callback) {

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
	protected String doInBackground(Void... arg0) {
		// TODO Auto-generated method stub

		ArrayList<NameValuePair> arrParams;
		arrParams = new ArrayList<NameValuePair>();
		arrParams.add(new BasicNameValuePair(Constants.DataKeys.DISTRIBUTOR_ID,
				LoginInfo.getInstance().getDistributorId()));
		arrParams.add(new BasicNameValuePair(
				Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo.getInstance()
						.getLrnDistributorId()));
		arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
				LoginInfo.getInstance().getZipCode()));
		arrParams.add(new BasicNameValuePair(Constants.DataKeys.DATA, LoginInfo
				.getInstance().getCartData()));

		return HttpServer.httpCall(HttpServer.POST,
				HttpServer.RequestAPI.GET_CART, arrParams);
	}

	@Override
	protected void onPostExecute(String response) {
		// TODO Auto-generated method stub
		pd.dismiss();
		CartResponse cartResponse = null;
		JSONArray notifi_array = null;
		try {

			JSONObject jsonObj = new JSONObject(response);
			
			if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

				cartResponse = Utils
						.getInstance()
						.getGson()
						.fromJson(jsonObj.getString(Constants.DataKeys.DATA),
								CartResponse.class);
			}
			if(jsonObj.has(Constants.DataKeys.NOTIFICATIONS))
			{
				notifi_array=jsonObj.getJSONArray(Constants.DataKeys.NOTIFICATIONS);
				
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (callback != null)
			callback.onCartResponse(cartResponse,notifi_array);
	}

	public interface CallbackCart {

		void onCartResponse(CartResponse cartResponse, JSONArray notifi_array);
	}

}