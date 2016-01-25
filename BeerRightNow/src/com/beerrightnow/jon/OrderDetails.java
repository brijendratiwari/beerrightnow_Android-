package com.beerrightnow.jon;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.OrderDetailAdapter;
import com.beerrightnow.data.OrderedItem;
import com.beerrightnow.data.OrderedProduct;
import com.beerrightnow.task.Callback;
import com.beerrightnow.task.GetOrderDetailsTask;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class OrderDetails extends SherlockActivity {

	public static final String ORDERED_ITEM = "ordered_item";

	private TextView txtDeliveryName;
	private TextView txtDeliveryStreet;
	private TextView txtDeliveryCityState;
	private TextView txtDeliveryCountry;
	private TextView txtBillingName;
	private TextView txtBillingStreet;
	private TextView txtBillingCityState;
	private TextView txtBillingCountry;
	private ListView listView;
	private OrderedItem orderedItem;
	private List<OrderedProduct> orderedProuducts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_details);

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

		orderedItem = (OrderedItem) getIntent().getSerializableExtra(
				ORDERED_ITEM);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Order Details");
		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(OrderDetails.this);
			}
		});

		txtBillingName = (TextView) findViewById(R.id.txtBillingName);
		txtBillingStreet = (TextView) findViewById(R.id.txtBillingStreet);
		txtBillingCityState = (TextView) findViewById(R.id.txtBillingCityState);
		txtBillingCountry = (TextView) findViewById(R.id.txtBillingCountry);
		txtDeliveryName = (TextView) findViewById(R.id.txtDeliveryName);
		txtDeliveryStreet = (TextView) findViewById(R.id.txtDeliveryStreet);
		txtDeliveryCityState = (TextView) findViewById(R.id.txtDeliveryCityState);
		txtDeliveryCountry = (TextView) findViewById(R.id.txtDeliveryCountry);
		TextView txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
		txtTotalPrice.setText(orderedItem.getOrderTotal());

		listView = (ListView) findViewById(R.id.productListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
		new GetOrderDetailsTask(OrderDetails.this, new Callback() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub

				try {
					JSONObject jsonObj = new JSONObject(response);
					if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {
						JSONObject data = jsonObj
								.getJSONObject(Constants.DataKeys.DATA);
						JSONObject delivery = data.getJSONObject("delivery");
						txtDeliveryName.setText(delivery.getString("name"));
						txtDeliveryStreet.setText(delivery
								.getString("street_address"));
						txtDeliveryCityState.setText(delivery.getString("city")
								+ " , " + delivery.getString("state"));
						txtDeliveryCountry.setText(delivery
								.getString("country"));

						JSONObject billing = data.getJSONObject("billing");
						txtBillingName.setText(billing.getString("name"));
						txtBillingName.setText(billing.getString("name"));
						txtBillingStreet.setText(billing
								.getString("street_address"));
						txtBillingCityState.setText(billing.getString("city")
								+ " , " + billing.getString("state"));
						txtBillingCountry.setText(billing.getString("country"));

						JSONArray jsonArr = data.getJSONArray("products");

						orderedProuducts = Utils
								.getInstance()
								.getGson()
								.fromJson(jsonArr.toString(),
										new TypeToken<List<OrderedProduct>>() {
										}.getType());

						listView.setAdapter(new OrderDetailAdapter(
								OrderDetails.this, orderedProuducts));

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).execute(new String[] { orderedItem.getOrderId() });

	}

}
