package com.beerrightnow.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beerrightnow.data.FilterItem;
import com.beerrightnow.data.NameFilterItem;
import com.beerrightnow.data.Product;
import com.beerrightnow.data.Section;
import com.beerrightnow.data.SubSection;
import com.beerrightnow.jon.R;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;

public class FilterFragment extends Fragment {

	public static final String TAG = FilterFragment.class.getSimpleName();

	private static final String SECTION_ID_2 = "2";
	private static final String SECTION_ID_3 = "3";

	private OnFilterListener listener;

	private ListView listView;
	private TextView txtFilterBy;

	private Section section;
	private SubSection subSection;

	private FILTER filter;

	private List<NameFilterItem> selectedFilters;
	private FilterItemAdapter adapter;
	private boolean isNewTask = true;
	public static String filterid;

	public FilterFragment() {
	}

	public static FilterFragment create(Section section, SubSection subSection) {
		FilterFragment frag = new FilterFragment();
		frag.section = section;
		frag.subSection = subSection;
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.filters_frag, container, false);

		txtFilterBy = (TextView) v.findViewById(R.id.txtFilterBy);
		final EditText edTxtSearchFilter = (EditText) v
				.findViewById(R.id.edTxtSearchFilter);
		edTxtSearchFilter
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {
						// TODO Auto-generated method stub
						if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager imm = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edTxtSearchFilter.getWindowToken(), 0);
							try {

								adapter.getFilter().filter(
										edTxtSearchFilter.getText().toString());
							} catch (NullPointerException e) {

								e.printStackTrace();
							}
							return true;
						}
						return false;
					}
				});

		edTxtSearchFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				try {

					adapter.getFilter().filter(s.toString());
				} catch (NullPointerException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		listView = (ListView) v.findViewById(R.id.sortListView);

		Button btnApply = (Button) v.findViewById(R.id.btnApply);
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					new ApplyFilterTask().execute(new Integer[] { 1 });
				} catch (NullPointerException e) {

					e.printStackTrace();
				}

			}

		});

		return v;
	}

	public void openMenuDrawer(FILTER by) {

		filter = by;
		isNewTask = true;

		switch (filter) {
		case BRANDS: {

			txtFilterBy.setText(R.string.brands);
		}
			break;
		case PRODUCERS: {

			txtFilterBy.setText(R.string.producers);
		}
			break;
		case PRICE: {

			txtFilterBy.setText(R.string.price);
		}
			break;
		case TYPE: {

			txtFilterBy.setText(R.string.type);
		}
			break;
		}

		try {

			listener.getAttachedMenuDrawer().openMenu();
			selectedFilters = new ArrayList<NameFilterItem>();
			adapter = new FilterItemAdapter(getActivity(),
					new ArrayList<NameFilterItem>());
			listView.setAdapter(adapter);

			new FilterByTask().execute();
		} catch (NullPointerException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {

			listener = (OnFilterListener) activity;
		} catch (ClassCastException e) {

			e.printStackTrace();
		}
	}

	public void loadTask(FILTER by, List<NameFilterItem> selectedItems, int page) {

		filter = by;
		selectedFilters = selectedItems;
		isNewTask = false;

		new ApplyFilterTask().execute(new Integer[] { page });

	}

	private class FilterByTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), "", "");
			pd.setContentView(new ProgressBar(getActivity()));

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo
								.getInstance().getDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
								.getInstance().getLrnDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.ZIP_CODE, LoginInfo.getInstance()
								.getZipCode()));

				String method;
				switch (filter) {
				case PRODUCERS:
					arrParams.add(new BasicNameValuePair("section_id", section
							.getSectionId()));
					method = HttpServer.RequestAPI.GET_PRODUCERS;
					break;
				case TYPE:
					arrParams.add(new BasicNameValuePair("section_id", section
							.getSectionId()));
					if (section.getSectionId().equalsIgnoreCase(SECTION_ID_2)
							|| section.getSectionId().equalsIgnoreCase(
									SECTION_ID_3))
						method = HttpServer.RequestAPI.GET_STYLE_TYPE_NAME;
					else
						method = HttpServer.RequestAPI.GET_TYPE;
					break;
				case BRANDS:
					arrParams.add(new BasicNameValuePair("type_id", subSection
							.getTypeId()));
					method = HttpServer.RequestAPI.GET_BRANDS;
					break;
				case PRICE:
					method = HttpServer.RequestAPI.GET_PRICE_RANGE;
					break;

				default:
					method = "";
					break;

				}

				return HttpServer.httpCall(HttpServer.POST, method, arrParams);
			} catch (NullPointerException e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					if (filter == FILTER.TYPE
							&& (section.getSectionId().equalsIgnoreCase(
									SECTION_ID_2) || section.getSectionId()
									.equalsIgnoreCase(SECTION_ID_3))) {

						JSONArray jArr = jsonObj
								.getJSONArray(Constants.DataKeys.DATA);

						List<String> items = Utils
								.getInstance()
								.getGson()
								.fromJson(jArr.toString(),
										new TypeToken<List<String>>() {
										}.getType());

						List<NameFilterItem> filtersItem = new ArrayList<NameFilterItem>();

						for (String item : items) {

							NameFilterItem filerItem = new NameFilterItem();
							filerItem.setName(item);
							filtersItem.add(filerItem);
						}

						Collections.sort(items);
						adapter.notifyItems(filtersItem);
					} else {

						jsonObj = jsonObj
								.getJSONObject(Constants.DataKeys.DATA);
						List<NameFilterItem> items = new ArrayList<NameFilterItem>();
						Iterator<?> it = jsonObj.keys();
						while (it.hasNext()) {

							String key = (String) it.next();
							String name = jsonObj.getString(key);
							FilterItem item = new FilterItem();
							item.setId(key);
							item.setName(name);
							items.add(item);
						}

						Collections.sort(items);
						adapter.notifyItems(items);
					}

				}
			} catch (JSONException e) {
			}

		}
	}

	private class ApplyFilterTask extends AsyncTask<Integer, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {

				if (listener.getAttachedMenuDrawer().isMenuVisible()) {

					pd = ProgressDialog.show(getActivity(), "", "");
					pd.setContentView(new ProgressBar(getActivity()));
				}
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

		}

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {

				ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo
								.getInstance().getDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
								.getInstance().getLrnDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.ZIP_CODE, LoginInfo.getInstance()
								.getZipCode()));
				arrParams.add(new BasicNameValuePair("type_id", subSection
						.getTypeId()));
				arrParams.add(new BasicNameValuePair(Constants.DataKeys.PAGE,
						params[0].toString()));

				switch (filter) {
				case PRODUCERS:

					for (NameFilterItem item : selectedFilters) {

						if (item instanceof FilterItem) {

							FilterItem filterItem = (FilterItem) item;
							arrParams.add(new BasicNameValuePair(
									"producer_id[]", filterItem.getId()));
						}
					}
					break;
				case TYPE:
					for (NameFilterItem item : selectedFilters) {

						if (section.getSectionId().equalsIgnoreCase(
								SECTION_ID_2)
								|| section.getSectionId().equalsIgnoreCase(
										SECTION_ID_3))
							arrParams.add(new BasicNameValuePair(
									"style_type_name", item.getName()));
						else if (item instanceof FilterItem) {

							FilterItem filterItem = (FilterItem) item;
							arrParams.add(new BasicNameValuePair("style_id[]",
									filterItem.getId()));
						}
					}
					break;
				case BRANDS:
					for (NameFilterItem item : selectedFilters) {

						if (item instanceof FilterItem) {

							FilterItem filterItem = (FilterItem) item;
							arrParams.add(new BasicNameValuePair("brand_id[]",
									filterItem.getId()));
						}
					}
					break;
				case PRICE:
					for (NameFilterItem item : selectedFilters) {

						if (item instanceof FilterItem) {

							FilterItem filterItem = (FilterItem) item;
							arrParams.add(new BasicNameValuePair(
									"price_range_id", filterItem.getId()));
						}
					}
					break;

				}

				return HttpServer.httpCall(HttpServer.POST,
						HttpServer.RequestAPI.SEARCH_RESULT, arrParams);
			} catch (NullPointerException e) {

				e.printStackTrace();
				return null;
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			try {

				if (listener.getAttachedMenuDrawer().isMenuVisible()) {

					pd.dismiss();
					listener.getAttachedMenuDrawer().closeMenu();
				}
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			try {
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					int totalPages = jsonObj.getJSONObject("pagination")
							.getInt("total_pages");
					JSONArray jsonArr = jsonObj
							.getJSONArray(Constants.DataKeys.DATA);

					List<Product> products = Utils
							.getInstance()
							.getGson()
							.fromJson(jsonArr.toString(),
									new TypeToken<List<Product>>() {
									}.getType());

					listener.onFilter(products, filter, selectedFilters,
							totalPages, isNewTask);
					return;

				} else {
					Toast.makeText(getActivity(), "No Products found",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), "Unknown Server error",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			if (listener != null)
				listener.onError();
		}
	}

	private class FilterItemAdapter extends ArrayAdapter<NameFilterItem> {

		private ItemFilter mFilter = new ItemFilter();
		private List<NameFilterItem> originItems;

		public FilterItemAdapter(Context context, List<NameFilterItem> items) {
			super(context, 0, items);
			originItems = items;
		}

		public void notifyItems(List<NameFilterItem> items) {

			clear();
			originItems = items;
			addAll(originItems);
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final NameFilterItem item = getItem(position);

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = inflater.inflate(R.layout.row_filter, null, true);
			CheckBox checkFilter = (CheckBox) view
					.findViewById(R.id.checkFilter);
			checkFilter.setChecked(selectedFilters.contains(item));

			checkFilter
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub

							if (filter == FILTER.PRICE) {

								FilterItem filterItem = (FilterItem) item;
							
								filterid=filterItem.getId();
								selectedFilters.clear();
								selectedFilters.add(item);
								notifyDataSetChanged();
							} else if (filter == FILTER.TYPE
									&& (section.getSectionId()
											.equalsIgnoreCase(SECTION_ID_2) || section
											.getSectionId().equalsIgnoreCase(
													SECTION_ID_3))) {

								selectedFilters.clear();
								selectedFilters.add(item);
								notifyDataSetChanged();

							} else {

								if (isChecked) {

									selectedFilters.add(item);
								} else {

									selectedFilters.remove(item);
								}
							}

						}

					});
			TextView txtFilterName = (TextView) view
					.findViewById(R.id.txtFilterName);

			txtFilterName.setSelected(true);
			txtFilterName.setText(item.getName());
			return view;
		}

		@Override
		public Filter getFilter() {
			return mFilter;
		}

		private class ItemFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();

				String filterString = constraint.toString().toLowerCase();

				if (filterString == null || filterString.length() == 0) {

					List<NameFilterItem> items = new ArrayList<NameFilterItem>(
							originItems);
					results.values = items;
					results.count = items.size();
				} else {

					final List<NameFilterItem> items = originItems;
					int count = items.size();
					final List<NameFilterItem> nItems = new ArrayList<NameFilterItem>(
							count);

					for (NameFilterItem item : items) {

						if (item.getName().toLowerCase().contains(filterString))
							nItems.add(item);
					}

					results.values = nItems;
					results.count = nItems.size();

				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				clear();
				addAll((ArrayList<FilterItem>) results.values);
				notifyDataSetChanged();
			}

		}
	}

	public static interface OnFilterListener {

		public void onFilter(List<Product> products, FILTER by,
				List<NameFilterItem> selectedItems, int pages, boolean isNewTask);

		public void onError();

		public MenuDrawer getAttachedMenuDrawer();
	}

	public static enum FILTER {

		BRANDS, PRODUCERS, PRICE, TYPE
	}
}
