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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.ProductGridAdapter;
import com.beerrightnow.data.Product;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class KeywordProductDisplay extends SherlockActivity {

	public static final String SEARCH_KEYWORD = "search_keyword";

	private SwipeRefreshLayout swipeRefreshLayout;
	private GridView gridView;
	private TextView txtTotCart;
	private ProductGridAdapter adapter;
	private int totalPages;
	private int curPage = 1;

	private String keyword;
	public String price="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_display);
		price= getIntent().getStringExtra("price");
		System.out.println("price: "+price);
		
		init();
	}

	@Override
	protected void onDestroy() {

		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {

		keyword = getIntent().getStringExtra(SEARCH_KEYWORD);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Search Results");

		View filterGroup = findViewById(R.id.filterGroup);
		filterGroup.setVisibility(View.GONE);

		View searchGroup = findViewById(R.id.searchGroup);
		searchGroup.setVisibility(View.GONE);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue, R.color.red);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				curPage = 1;
				try {

					adapter.clear();
					adapter.notifyDataSetChanged();

				} catch (NullPointerException e) {

					e.printStackTrace();
				}
				new GetSearchTask().execute();
			}

		});

		gridView = (GridView) findViewById(R.id.gridProducts);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Product product = (Product) adapter.getItem(position);

				Intent intent = new Intent(KeywordProductDisplay.this,
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
							new GetSearchTask().execute();
						}
					}

				});
		gridView.setOnScrollListener(listener);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(KeywordProductDisplay.this);
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

					startActivity(new Intent(KeywordProductDisplay.this,
							ShopingCart.class));
				} else {

					Toast.makeText(KeywordProductDisplay.this,
							R.string.no_proudcts_in_cart, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		new GetSearchTask().execute();

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

	private class GetSearchTask extends AsyncTask<Void, Void, String> {

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

			arrParams.add(new BasicNameValuePair("keyword", keyword));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.PAGE,
					Integer.valueOf(curPage).toString()));
			if(!price.equalsIgnoreCase("0"))
			{
				arrParams.add(new BasicNameValuePair(
						"price_range_id", price));
			}
			

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.SEARCH_RESULT, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub

			swipeRefreshLayout.setRefreshing(false);

			if (response.isEmpty()) {

				Toast.makeText(KeywordProductDisplay.this,
						R.string.network_error, Toast.LENGTH_LONG).show();
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

					if (adapter == null) {

						adapter = new ProductGridAdapter(
								KeywordProductDisplay.this, products);
						gridView.setAdapter(adapter);

					} else {

						adapter.addAll(products);
						adapter.notifyDataSetChanged();
					}

				} else {

					Toast.makeText(
							KeywordProductDisplay.this,
							"Sorry. I couldn't find that.\nLet's try something else!",
							Toast.LENGTH_LONG).show();
					finish();
				}

			} catch (JSONException ex) {
				Toast.makeText(KeywordProductDisplay.this,
						R.string.unknown_error, Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}

	}

}
