package com.beerrightnow.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.beerrightnow.data.OrderedItem;
import com.beerrightnow.data.OrderedProduct;
import com.beerrightnow.jon.R;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.Callback;
import com.beerrightnow.task.GetOrderDetailsTask;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class OrderHistoryAdapter extends ArrayAdapter<OrderedItem> {

	private Activity activity;

	public OrderHistoryAdapter(Activity activity, List<OrderedItem> items) {
		super(activity, 0, items);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentGroup) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		final OrderedItem item = getItem(position);
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.order_hist_row, null, true);
			holder = new ViewHolder();

			holder.txtOrderId = (TextView) convertView
					.findViewById(R.id.txtOrderId);
			holder.txtBillingName = (TextView) convertView
					.findViewById(R.id.txtBillingName);
			holder.txtBillingName.setSelected(true);
			holder.txtOrderTotal = (TextView) convertView
					.findViewById(R.id.txtOrderTotal);
			holder.txtPurchasedDate = (TextView) convertView
					.findViewById(R.id.txtPurchasedDate);
			holder.txtPurchasedDate.setSelected(true);
			holder.btnAddCart = (Button) convertView
					.findViewById(R.id.btnAddCart);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.txtOrderId.setText(item.getOrderId());
		holder.txtBillingName.setText(item.getBillingName());
		holder.txtPurchasedDate.setText(item.getPurchasedDate());
		holder.txtOrderTotal.setText(item.getOrderTotal());

		holder.btnAddCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new GetOrderDetailsTask(activity, new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {
							JSONObject jsonObj = new JSONObject(response);
							if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {
								JSONObject data = jsonObj
										.getJSONObject(Constants.DataKeys.DATA);

								JSONArray jsonArr = data
										.getJSONArray("products");

								List<OrderedProduct> orderedProuducts = Utils
										.getInstance()
										.getGson()
										.fromJson(
												jsonArr.toString(),
												new TypeToken<List<OrderedProduct>>() {
												}.getType());

								for (OrderedProduct orderedProudct : orderedProuducts) {

									LoginInfo.getInstance().addProductToCart(
											orderedProudct.getId(),
											orderedProudct.getQuantity());
								}

								activity.finish();

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).execute(new String[] { item.getOrderId() });
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView txtOrderId;
		TextView txtBillingName;
		TextView txtPurchasedDate;
		TextView txtOrderTotal;
		Button btnAddCart;
	}

}
