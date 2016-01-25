/**
 * NOTE : This activity will display products in a grid
 * fromSubSection : this is intent is passed by subcategories class to send some data
 * isResetFromFilter : this boolean variable maintain value for reset click or not
 * isFilterApplied : this boolean variable maintain the state for gridview to apply the endless 
 * scroll action
 */
package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;
import net.simonvt.menudrawer.Position;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.ProductGridAdapter;
import com.beerrightnow.data.FilterItem;
import com.beerrightnow.data.NameFilterItem;
import com.beerrightnow.data.Product;
import com.beerrightnow.data.Section;
import com.beerrightnow.data.SubSection;
import com.beerrightnow.fragment.FilterFragment;
import com.beerrightnow.fragment.FilterFragment.FILTER;
import com.beerrightnow.fragment.FilterFragment.OnFilterListener;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class ProductDisplay extends SherlockActivity implements
		OnFilterListener {

	public static final String SUB_SECTION = "sub_section";
	public static final String SECTION = "section";

	private MenuDrawer mDrawer;
	private SwipeRefreshLayout swipeRefreshLayout;
	private GridView gridView;
	private TextView txtTotCart;
	private ProductGridAdapter adapter;
	private int totalPages;
	private int curPage = 1;

	private SubSection subSection;
	private Section section;

	private FilterFragment filterFragment;
	private boolean isFilterMode = false;
	private List<NameFilterItem> selectedItems;
	private FILTER by;
	private List<NameFilterItem> selectedFilters;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDrawer = MenuDrawer.attach(this, Type.BEHIND, Position.RIGHT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mDrawer.setMenuView(R.layout.filters);
		mDrawer.setContentView(R.layout.product_display);
		mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		init();
	}

	@Override
	protected void onDestroy() {

		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isMenuVisible())
			mDrawer.closeMenu();
		else
			super.onBackPressed();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawer.isMenuVisible())
				mDrawer.closeMenu();
			else
				finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {

		subSection = (SubSection) getIntent().getSerializableExtra(SUB_SECTION);
		section = (Section) getIntent().getSerializableExtra(SECTION);

		filterFragment = FilterFragment.create(section, subSection);
		getFragmentManager().beginTransaction()
				.add(R.id.filterFrag, filterFragment).commit();

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText(subSection.getTypeName());

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue, R.color.red);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				curPage = 1;
				adapter.clear();
				adapter.notifyDataSetChanged();

				if (!isFilterMode) {

					new GetProductTask().execute();
				} else {

					filterFragment.loadTask(by, selectedItems, curPage);
				}
			}

		});

		gridView = (GridView) findViewById(R.id.gridProducts);
		adapter = new ProductGridAdapter(ProductDisplay.this,
				new ArrayList<Product>());
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Product product = (Product) adapter.getItem(position);

				Intent intent = new Intent(ProductDisplay.this,
						ProductDescription.class);
				intent.putExtra(ProductDescription.PRODUCT, product);

				startActivity(intent);

			}

		});
		PauseOnScrollListener listener = new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true, new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub
						int lastInScreen = firstVisibleItem + visibleItemCount;
						if (lastInScreen == totalItemCount
								&& curPage < totalPages
								&& !swipeRefreshLayout.isRefreshing()) {

							curPage++;
							if (!isFilterMode) {

								new GetProductTask().execute();
							} else {

								if (!swipeRefreshLayout.isRefreshing())
									swipeRefreshLayout.setRefreshing(true);
								filterFragment.loadTask(by, selectedItems,
										curPage);
							}
						}
					}

				});
		gridView.setOnScrollListener(listener);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ProductDisplay.this);
			}
		});
		View relCart = findViewById(R.id.relCart);
		txtTotCart = (TextView) findViewById(R.id.txtCartTot);
		txtTotCart.setVisibility(View.INVISIBLE);
		relCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (LoginInfo.getInstance().getTotalCarts() > 0) {

					startActivity(new Intent(ProductDisplay.this,
							ShopingCart.class));
				} else {

					Toast.makeText(ProductDisplay.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		Button brandsBtn = (Button) findViewById(R.id.brandsBtn);
		brandsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				filterFragment.openMenuDrawer(FILTER.BRANDS);
			}
		});
		Button producersBtn = (Button) findViewById(R.id.producersBtn);
		producersBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				filterFragment.openMenuDrawer(FILTER.PRODUCERS);
			}
		});
		Button priceBtn = (Button) findViewById(R.id.priceBtn);
		priceBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				filterFragment.openMenuDrawer(FILTER.PRICE);
			}
		});
		Button typeBtn = (Button) findViewById(R.id.typeBtn);
		typeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				filterFragment.openMenuDrawer(FILTER.TYPE);
			}
		});

		// if (section.getBrn().equalsIgnoreCase("1")) {
		// brandsBtn.setVisibility(View.VISIBLE);
		// }

		if (section.getSectionId().equalsIgnoreCase("1")) {

			brandsBtn.setVisibility(View.VISIBLE);
		}

		
		final EditText edTxtSearchFilter = (EditText) findViewById(R.id.edtSearch);
		edTxtSearchFilter
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {
						// TODO Auto-generated method stub
						if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edTxtSearchFilter.getWindowToken(), 0);
					
							Intent intent = new Intent(ProductDisplay.this,
									KeywordProductDisplay.class);
							intent.putExtra(
									KeywordProductDisplay.SEARCH_KEYWORD,
									edTxtSearchFilter.getText().toString());
							if(FilterFragment.filterid==null)
							{
								FilterFragment.filterid="0";
							}
							intent.putExtra("price", FilterFragment.filterid);
							startActivity(intent);
							FilterFragment.filterid="";
							return true;
						}
						return false;
					}
				});

		new GetProductTask().execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateCartCount();

	}

	void updateCartCount() {

		int cartCount = LoginInfo.getInstance().getTotalCarts();
		if (cartCount != 0) {
			txtTotCart.setVisibility(View.VISIBLE);
			txtTotCart.setText(Integer.valueOf(cartCount).toString());
		} else {
			txtTotCart.setVisibility(View.INVISIBLE);
		}
	}

	private class GetProductTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// stop to make more async calls
			if (!swipeRefreshLayout.isRefreshing())
				swipeRefreshLayout.setRefreshing(true);

		}

		@Override
		protected String doInBackground(Void... params) {
			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo.getInstance()
							.getDistributorId()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
							.getInstance().getLrnDistributorId()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					LoginInfo.getInstance().getZipCode()));

			arrParams.add(new BasicNameValuePair(Constants.DataKeys.TYPE_ID,
					subSection.getTypeId()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.PAGE,
					Integer.valueOf(curPage).toString()));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.GET_PRODUCT_BY_TYPE, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub

			swipeRefreshLayout.setRefreshing(false);

			if (response.isEmpty()) {

				Toast.makeText(ProductDisplay.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {

					totalPages = jobj.getJSONObject("pagination").getInt(
							"total_pages");

					JSONArray jArr = jobj.getJSONArray(Constants.DataKeys.DATA);

					List<Product> products = Utils
							.getInstance()
							.getGson()
							.fromJson(jArr.toString(),
									new TypeToken<List<Product>>() {
									}.getType());

					adapter.addAll(products);
					adapter.notifyDataSetChanged();

				} else {

					Toast.makeText(ProductDisplay.this, "No Products Found...",
							Toast.LENGTH_LONG).show();
				}

			} catch (JSONException ex) {
				Toast.makeText(ProductDisplay.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}

	}

	@Override
	public void onFilter(List<Product> products, FILTER by,
			List<NameFilterItem> selectedItems, int pages, boolean isNewTask) {
		// TODO Auto-generated method stub

		if (swipeRefreshLayout.isRefreshing())
			swipeRefreshLayout.setRefreshing(false);

		if (!isFilterMode)
			isFilterMode = true;

		if (isNewTask) {

			curPage = 1;
			totalPages = pages;
			this.by = by;
			this.selectedItems = selectedItems;
			adapter.clear();
			gridView.setAdapter(adapter);
		}

		adapter.addAll(products);
		adapter.notifyDataSetChanged();
	}

	@Override
	public MenuDrawer getAttachedMenuDrawer() {
		// TODO Auto-generated method stub
		return mDrawer;
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub

		if (swipeRefreshLayout.isRefreshing())
			swipeRefreshLayout.setRefreshing(false);
	}
}
