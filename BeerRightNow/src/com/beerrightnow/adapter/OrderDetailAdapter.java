package com.beerrightnow.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beerrightnow.data.OrderedProduct;
import com.beerrightnow.jon.R;
import com.beerrightnow.utility.Utils;

public class OrderDetailAdapter extends ArrayAdapter<OrderedProduct> {

	public OrderDetailAdapter(Context context, List<OrderedProduct> products) {
		super(context, 0, products);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentGroup) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		OrderedProduct product = getItem(position);
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_product_ordered, null,
					true);
			holder = new ViewHolder();
			holder.txtProductName = (TextView) convertView
					.findViewById(R.id.txtProudctName);
			holder.txtProductName.setSelected(true);
			holder.txtOrderedQTY = (TextView) convertView
					.findViewById(R.id.txtOrderedQTY);
			holder.txtOrderedPrice = (TextView) convertView
					.findViewById(R.id.txtOrderedPrice);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.txtProductName.setText(product.getName());
		holder.txtOrderedQTY.setText(product.getQuantity());
		holder.txtOrderedPrice.setText("$"
				+ Utils.getInstance().convertToDecimalPointString(
						Float.parseFloat(product.getFinalPrice()), 2));
		return convertView;
	}

	static class ViewHolder {

		TextView txtProductName;
		TextView txtOrderedQTY;
		TextView txtOrderedPrice;
	}

}
