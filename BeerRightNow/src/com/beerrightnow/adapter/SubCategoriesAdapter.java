package com.beerrightnow.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beerrightnow.adapter.HomeAdapter.ViewHolder;
import com.beerrightnow.data.SubSection;
import com.beerrightnow.jon.R;

public class SubCategoriesAdapter extends ArrayAdapter<SubSection> {

	public SubCategoriesAdapter(Context context, List<SubSection> subSections) {
		super(context, 0, subSections);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		SubSection subSection = getItem(position);

		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.collection_row, null, true);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) view.findViewById(R.id.txtCollTitle);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtTitle.setText(subSection.getTypeName());
		return view;

	}

	static class viewHolder {
		TextView txtTitle;
	}

}
