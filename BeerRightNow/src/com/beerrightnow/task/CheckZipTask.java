package com.beerrightnow.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class CheckZipTask extends AsyncTask<String, Void, String> {

	Callback callBack;
	Context context;
	ProgressDialog pd;

	public CheckZipTask() {
	}

	public CheckZipTask(Context context, Callback callBack) {
		this.callBack = callBack;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = ProgressDialog.show(context, "", "Checking Zip Code...");
		pd.setContentView(new ProgressBar(context));

	}

	@Override
	protected String doInBackground(String... zipcodes) {
		// TODO Auto-generated method stub
		try {

			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					zipcodes[0]));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.CHECK_ZIPCODE, arrParams);
		} catch (NullPointerException e) {

			e.printStackTrace();
		}

		return "";
	}

	@Override
	protected void onPostExecute(String response) {
		// TODO Auto-generated method stub
		pd.dismiss();
		if (callBack != null)
			callBack.onResponse(response);
	}
}