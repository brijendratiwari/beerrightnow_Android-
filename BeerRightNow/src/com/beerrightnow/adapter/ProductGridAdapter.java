package com.beerrightnow.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerrightnow.data.Product;
import com.beerrightnow.jon.R;
import com.beerrightnow.utility.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ProductGridAdapter extends ArrayAdapter<Product> {

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.stub)
			.showImageForEmptyUri(R.drawable.stub)
			.showImageOnFail(R.drawable.stub).cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.considerExifParams(true).displayer(new SimpleBitmapDisplayer())
			.build();

	public ProductGridAdapter(Context context, List<Product> products) {
		super(context, 0, products);
	}

	static class ViewHolder {
		TextView txtProductName, txtProductSize, txtProductPrice;
		ImageView imgProduct;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder holder;
		Product product = getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.grid_cell, null, true);
			holder = new ViewHolder();
			holder.txtProductName = (TextView) view
					.findViewById(R.id.txtProductName);
			holder.txtProductPrice = (TextView) view
					.findViewById(R.id.txtProductPrice);
			holder.txtProductSize = (TextView) view
					.findViewById(R.id.txtProductSize);
			holder.imgProduct = (ImageView) view
					.findViewById(R.id.ivProductImg);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String wholeName = product.getProductsName();
		String[] names;
		if (wholeName.contains(","))
			names = wholeName.split(",");
		else
			names = new String[] { wholeName, "" };

		holder.txtProductName.setSelected(true);
		holder.txtProductName.setText(names[0].trim());
		holder.txtProductPrice.setText("$"
				+ Utils.getInstance().convertToDecimalPointString(
						Float.valueOf(product.getProductsPrice()), 2));
		;
		holder.txtProductSize.setSelected(true);
		holder.txtProductSize.setText(names[1]);

		ImageLoader.getInstance().displayImage(product.getProductsImage(),
				holder.imgProduct, options);

		return view;
	}

}
