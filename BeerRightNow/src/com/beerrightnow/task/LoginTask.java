package com.beerrightnow.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.beerrightnow.utility.HttpServer;

public class LoginTask extends AsyncTask<String, Void, String> {

	private Context context;
	private ProgressDialog pd;
	private Callback callback;

	public LoginTask(Context context, Callback callback) {

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
			arrParams.add(new BasicNameValuePair("email", params[0]));
			arrParams.add(new BasicNameValuePair("password", params[1]));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.USER_AUTH, arrParams);
		} catch (NullPointerException e) {

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

}