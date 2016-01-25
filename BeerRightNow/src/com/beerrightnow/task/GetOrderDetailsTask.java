package com.beerrightnow.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class GetOrderDetailsTask extends AsyncTask<String, Void, String> {

	private ProgressDialog pd;
	private Context context;
	private Callback callback;

	public GetOrderDetailsTask(Context context, Callback callback) {

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
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		try {

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
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ORDER_ID,
					params[0]));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.ORDER_DETAILS, arrParams);
		} catch (NullPointerException e) {

			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {

			e.printStackTrace();
		}

		return "";
	}

	@Override
	protected void onPostExecute(String response) {
		// TODO Auto-generated method stub
		pd.dismiss();
		if (callback != null)
			callback.onResponse(response);

	}
}