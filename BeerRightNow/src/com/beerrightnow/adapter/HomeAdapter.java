package com.beerrightnow.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beerrightnow.data.Section;
import com.beerrightnow.jon.R;

public class HomeAdapter extends ArrayAdapter<Section> {

	public static ArrayList<Section> arrSubSections;

	public HomeAdapter(Context context, List<Section> sections) {
		super(context, 0, sections);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		Section section = getItem(position);

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

		holder.txtTitle.setText(section.getName());
		return view;
	}

	static class ViewHolder {
		TextView txtTitle;
	}

}
