package com.beerrightnow.jon;

import static com.beerrightnow.utility.Utils.DISPLAY_MESSAGE_ACTION;
import static com.beerrightnow.utility.Utils.EXTRA_MESSAGE;
import static com.beerrightnow.utility.Utils.TAG;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.adapter.HomeAdapter;
import com.beerrightnow.data.Section;
import com.beerrightnow.data.SubSection;
import com.beerrightnow.data.SubWrapper;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Home extends SherlockActivity {

	private TextView txtTotCart, txtBrowsingAt;
	private MenuDrawer mDrawer;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView lstCollection;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDrawer = MenuDrawer.attach(this, Type.BEHIND, Position.START,
				MenuDrawer.MENU_DRAG_WINDOW);
		mDrawer.setMenuView(R.layout.sliding_menu);
		mDrawer.setContentView(R.layout.home);

		// facebook hashkey

		// try {
		// // getting application package name, as defined in manifest
		// String packageName = getPackageName();
		//
		// // Retriving package info
		// PackageInfo packageInfo =
		// getPackageManager().getPackageInfo(packageName,
		// PackageManager.GET_SIGNATURES);
		//
		// String key = "";
		//
		// for (Signature signature : packageInfo.signatures) {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// key = new String(Base64.encode(md.digest(), 0));
		//
		// // String key = new String(Base64.encodeBytes(md.digest()));
		// }
		//
		// new AlertDialog.Builder(this)
		// .setTitle(packageName)
		// .setMessage(key)
		// .setPositiveButton(android.R.string.yes,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // continue with delete
		// }
		// })
		// .setNegativeButton(android.R.string.no,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // do nothing
		// }
		// }).setIcon(android.R.drawable.ic_dialog_alert)
		// .show();
		// } catch (NameNotFoundException e1) {
		// Log.e("Name not found", e1.toString());
		// } catch (NoSuchAlgorithmException e) {
		// Log.e("No such an algorithm", e.toString());
		// } catch (Exception e) {
		// Log.e("Exception", e.toString());
		// }

		// ---;

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		try {
			Class.forName("android.os.AsyncTask");
		} catch (Exception ignored) {
		}

		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		init();
		
		

		AppServices.loginAndRegisterForPush(this);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mDrawer.toggleMenu();
		AppServices.sendMyselfANotification(this);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_MENU:
			mDrawer.toggleMenu();

			return true;
		}
		return super.onKeyDown(keycode, e);
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isMenuVisible())
			mDrawer.closeMenu();
		else
			super.onBackPressed();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ImageLoader.getInstance().clearMemoryCache();
		// ImageLoader.getInstance().clearDiskCache();

		unregisterReceiver(notificationReceiver);

	}

	private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take some action upon receiving a push notification here!
			 **/
			String message = intent.getExtras().getString(EXTRA_MESSAGE);
			if (message == null) {
				message = "Empty Message";
			}

			Log.i(TAG, message);
			/* alert.showAlertDialog(context,
			 getString(R.string.gcm_alert_title), message);
			 Toast.makeText(getApplicationContext(),
			getString(R.string.gcm_message, message), Toast.LENGTH_LONG)
			 .show();*/

			WakeLocker.release();
		}
	};

	private void init() {

		registerReceiver(notificationReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.ic_slide);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		txtTotCart = (TextView) findViewById(R.id.txtCartTot);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(Home.this);
			}
		});

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange,
				R.color.green, R.color.blue, R.color.red);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

				new GetProductSectionTask().execute();
			}

		});
		lstCollection = (ListView) findViewById(R.id.lstCollection);
		lstCollection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Section section = (Section) lstCollection.getAdapter().getItem(
						position);
				new GetProductTypeOnSectionTask()
						.execute(new Section[] { section });
			}

		});
		// spawn new collection task
		new GetProductSectionTask().execute();

		View changeZip = findViewById(R.id.txtChangeZip);
		changeZip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlertDialog.Builder builder = new AlertDialog.Builder(
						Home.this);
				builder.setMessage(R.string.change_zip_alert);
				builder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								dialog.dismiss();
								startActivity(new Intent(Home.this,
										ChangeZip.class).putExtra(
										ChangeZip.TITLE_KEY, "Change Zipcode"));
							}
						});

				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								dialog.cancel();
							}
						});

				AlertDialog alertDlg = builder.create();
				alertDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDlg.show();
			}

		});

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("BeerRightNow");
		txtBrowsingAt = (TextView) findViewById(R.id.txtYouRat);
		txtBrowsingAt.setSelected(true);

		final EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
		edtSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {
						// TODO Auto-generated method stub
						if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edtSearch.getWindowToken(), 0);

							Intent intent = new Intent(Home.this,
									KeywordProductDisplay.class);
							intent.putExtra(
									KeywordProductDisplay.SEARCH_KEYWORD,
									edtSearch.getText().toString());
							startActivity(intent);
							return true;
						}
						return false;
					}
				});

		View relCart = findViewById(R.id.relCart);
		relCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (mDrawer.isMenuVisible()) {

					mDrawer.closeMenu();
					return;
				}

				if (LoginInfo.getInstance().getTotalCarts() > 0) {

					startActivity(new Intent(Home.this, ShopingCart.class));
				} else {

					Toast.makeText(Home.this, R.string.no_proudcts_in_cart,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		TextView txtHome = (TextView) findViewById(R.id.itemHome);
		txtHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
			}
		});

		TextView txtMenuName = (TextView) findViewById(R.id.txtMenuName);
		txtMenuName.setText(LoginInfo.getInstance().getUserInfo()
				.getCustomerName());

		TextView txtDeliveryAdd = (TextView) findViewById(R.id.item2);
		txtDeliveryAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
				startActivity(new Intent(Home.this, ShippingAddress.class)
						.putExtra(ShippingAddress.ONLY_FOR_ADDRESS, true));
			}
		});

		TextView txtOrderHis = (TextView) findViewById(R.id.item3);
		txtOrderHis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
				startActivity(new Intent(Home.this, OrderHistory.class));
			}
		});

		TextView txtSupport = (TextView) findViewById(R.id.item4);
		txtSupport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
				startActivity(new Intent(Home.this, Support.class));
			}
		});

		TextView textBreak = (TextView) findViewById(R.id.item5);
		textBreak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mDrawer.toggleMenu();
				startActivity(new Intent(Home.this, TakeBreak.class));
			}

		});

		TextView txtLogout = (TextView) findViewById(R.id.item6);
		txtLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
				AlertDialog.Builder adb = new AlertDialog.Builder(Home.this);
				adb.setMessage("Are you sure want to logout ?");
				adb.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							//
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								LoginInfo.getInstance().setLogined(false);
								finish();
							}
						});
				adb.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

				adb.show();

			}
		});
		TextView creditcard = (TextView) findViewById(R.id.creditcard);
		creditcard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleMenu();
				startActivity(new Intent(Home.this, AddCreditCard.class)
						.putExtra(AddCreditCard.ONLY_FOR_CC, true));
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		txtBrowsingAt.setText("You are browsing : "
				+ LoginInfo.getInstance().getZipCode());
		updateCartCount();
	}

	protected void setDeliveryAddScreen() {

	}

	protected void setOrderHistoryScreen() {

	}

	class GetProductSectionTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			if (!swipeRefreshLayout.isRefreshing())
				swipeRefreshLayout.setRefreshing(true);
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

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.GET_PROUDCT_SERCTIONS, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {

			swipeRefreshLayout.setRefreshing(false);

			if (response.isEmpty()) {

				Toast.makeText(Home.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jobj = new JSONObject(response);

				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {

					JSONArray jArr = jobj.getJSONArray(Constants.DataKeys.DATA);

					List<Section> sections = Utils
							.getInstance()
							.getGson()
							.fromJson(jArr.toString(),
									new TypeToken<List<Section>>() {
									}.getType());
					HomeAdapter adatper = new HomeAdapter(Home.this, sections);
					lstCollection.setAdapter(adatper);
				} else {

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				Toast.makeText(Home.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
			}
		}

	}

	class GetProductTypeOnSectionTask extends AsyncTask<Section, Void, String> {

		private ProgressDialog pd;
		private Section section;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(Home.this, "", "");
			pd.setContentView(new ProgressBar(Home.this));

		}

		@Override
		protected String doInBackground(Section... params) {
			// TODO Auto-generated method stub

			try {

				section = params[0];
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
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.SECTION_ID, section.getSectionId()));
				return HttpServer.httpCall(HttpServer.POST,
						HttpServer.RequestAPI.GET_PRODUCT_TYPE_ON_SECTION,
						arrParams);
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(String response) {

			pd.dismiss();

			if (response.isEmpty()) {

				Toast.makeText(Home.this, R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jobj = new JSONObject(response);

				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {

					JSONArray jArr = jobj.getJSONArray(Constants.DataKeys.DATA);

					List<SubSection> subSections = Utils
							.getInstance()
							.getGson()
							.fromJson(jArr.toString(),
									new TypeToken<List<SubSection>>() {
									}.getType());

					int count = subSections.size();
					if (count > 1) {

						SubWrapper subWrapper = new SubWrapper();
						subWrapper.setSubSections(subSections);
						subWrapper.setSection(section);

						Intent intent = new Intent(Home.this,
								SubCategories.class);
						intent.putExtra(SubCategories.SUB_WRAPPER, subWrapper);
						startActivity(intent);
					} else if (count == 1) {

						Intent intent = new Intent(Home.this,
								ProductDisplay.class);
						intent.putExtra(ProductDisplay.SUB_SECTION,
								subSections.get(0));
						intent.putExtra(ProductDisplay.SECTION, section);
						startActivity(intent);
					}

				} else {

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				Toast.makeText(Home.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
