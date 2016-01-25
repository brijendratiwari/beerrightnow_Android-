package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.OrderHistoryAdapter;
import com.beerrightnow.data.OrderedItem;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class OrderHistory extends SherlockActivity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_history);

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

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Order History");
		View relCart = (RelativeLayout) findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(OrderHistory.this);
			}
		});
		listView = (ListView) findViewById(R.id.lstOrderHistory);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				try {

					OrderedItem item = (OrderedItem) listView.getAdapter()
							.getItem(position);
					Intent intent = new Intent(OrderHistory.this,
							OrderDetails.class);
					intent.putExtra(OrderDetails.ORDERED_ITEM, item);
					startActivity(intent);

				} catch (NullPointerException e) {

					e.printStackTrace();
				} catch (ClassCastException e) {

					e.printStackTrace();
				}
			}
		});

		new GetOrderListTask().execute();

	}

	private class GetOrderListTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(OrderHistory.this, "", "");
			pd.setContentView(new ProgressBar(OrderHistory.this));

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
					HttpServer.RequestAPI.ORDER_LIST, arrParams);
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

					List<OrderedItem> orderedItems = Utils
							.getInstance()
							.getGson()
							.fromJson(jsonArr.toString(),
									new TypeToken<List<OrderedItem>>() {
									}.getType());

					listView.setAdapter(new OrderHistoryAdapter(
							OrderHistory.this, orderedItems));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

		}
	}
}
