package com.beerrightnow.shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beerrightnow.data.UserInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;

public class LoginInfo {

	private static final String LOGINED_KEY = "is_loggedin";

	private static final LoginInfo shared = new LoginInfo();

	private boolean logined;
	private String zipCode;
	private String distributorId;
	private String lrnDistributorId;
	private JSONArray cart;
	private UserInfo userInfo;

	private SharedPreferences sharedPrefs;

	public static LoginInfo getInstance() {

		return shared;
	}

	public void init(Context context) {

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		initialize();
	}

	private void initialize() {

		logined = sharedPrefs.getBoolean(LOGINED_KEY, false);
		zipCode = sharedPrefs.getString(Constants.DataKeys.ZIP_CODE, "");
		distributorId = sharedPrefs.getString(
				Constants.DataKeys.DISTRIBUTOR_ID, "");
		lrnDistributorId = sharedPrefs.getString(
				Constants.DataKeys.LRN_DISTRIBUTOR_ID, "");
		try {

			String jsonCart = sharedPrefs.getString(Constants.DataKeys.CART,
					"[]");
			cart = new JSONArray(jsonCart);
		} catch (JSONException e) {

			e.printStackTrace();
		}

		try {

			String user_info = sharedPrefs.getString(
					Constants.DataKeys.USER_INFO, "");
			userInfo = Utils.getInstance().getGson()
					.fromJson(user_info, UserInfo.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public boolean isLogined() {
		return logined;
	}

	public void setLogined(boolean logined) {
		this.logined = logined;
		if (logined)
			sharedPrefs.edit().putBoolean(LOGINED_KEY, true).commit();
		else {

			clear();
			CartInfo.getInstance().clear();
		}
	}

	public void orderProcessed() {

		sharedPrefs.edit().remove(Constants.DataKeys.CART).commit();
		initialize();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
		sharedPrefs.edit().putString(Constants.DataKeys.ZIP_CODE, zipCode)
				.commit();
		sharedPrefs.edit().remove(Constants.DataKeys.CART).commit();
		initialize();
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
		sharedPrefs.edit()
				.putString(Constants.DataKeys.DISTRIBUTOR_ID, distributorId)
				.commit();
	}

	public String getLrnDistributorId() {
		return lrnDistributorId;
	}

	public void setLrnDistributorId(String lrnDistributorId) {
		this.lrnDistributorId = lrnDistributorId;
		sharedPrefs
				.edit()
				.putString(Constants.DataKeys.LRN_DISTRIBUTOR_ID,
						lrnDistributorId).commit();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String user_info) {

		sharedPrefs.edit().putString(Constants.DataKeys.USER_INFO, user_info)
				.commit();
		try {

			userInfo = Utils.getInstance().getGson()
					.fromJson(user_info, UserInfo.class);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public void setDefaultAddressId(String addressId) {
		try {

			userInfo.setCustomersDefaultAddressId(addressId);
			String user_info = Utils.getInstance().getGson()
					.toJson(userInfo, UserInfo.class);

			setUserInfo(user_info);

		} catch (NullPointerException e) {

			e.printStackTrace();
		}
	}

	public void removeRroductFromCart(String productId) {

		try {

			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < cart.length(); i++) {

				JSONObject cartObj = cart.getJSONObject(i);
				if (productId.equalsIgnoreCase(cartObj
						.getString(Constants.DataKeys.PRODUCT_ID)))
					continue;
				jsonArray.put(cartObj);
			}

			cart = jsonArray;

			sharedPrefs.edit()
					.putString(Constants.DataKeys.CART, cart.toString())
					.commit();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String addProductToCart(String productId, String productCount) {

		String result = "";
		JSONObject productCartObj = null;

		for (int i = 0; i < cart.length(); i++) {

			try {

				JSONObject cartObj = cart.getJSONObject(i);

				if (productId.equalsIgnoreCase(cartObj
						.getString(Constants.DataKeys.PRODUCT_ID))) {

					productCartObj = cartObj;
					break;
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

		}

		try {

			if (productCartObj == null) {

				productCartObj = new JSONObject();
				productCartObj.put(Constants.DataKeys.PRODUCT_ID, productId);
				productCartObj.put(Constants.DataKeys.PRODUCT_COUNT,
						productCount);
				cart.put(productCartObj);
				sharedPrefs.edit()
						.putString(Constants.DataKeys.CART, cart.toString())
						.commit();
				result = "Product added to Cart...";
			} else {

				if (productCount.equalsIgnoreCase(productCartObj
						.getString(Constants.DataKeys.PRODUCT_COUNT))) {

					result = "Product already in Cart";
				} else {

					productCartObj.put(Constants.DataKeys.PRODUCT_COUNT,
							productCount);
					sharedPrefs
							.edit()
							.putString(Constants.DataKeys.CART, cart.toString())
							.commit();
					result = "Product updated in Cart...";
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return result;

	}

	public String getCartData() {

		try {

			return cart.toString();

		} catch (NullPointerException e) {

			e.printStackTrace();
			return "";
		}
	}

	public int getTotalCarts() {

		try {

			return cart.length();

		} catch (NullPointerException e) {
			e.printStackTrace();
			return 0;
		}

	}

	public void clear() {

		sharedPrefs.edit().remove(LOGINED_KEY).commit();
		sharedPrefs.edit().remove(Constants.DataKeys.DISTRIBUTOR_ID).commit();
		sharedPrefs.edit().remove(Constants.DataKeys.LRN_DISTRIBUTOR_ID)
				.commit();
		sharedPrefs.edit().remove(Constants.DataKeys.ZIP_CODE).commit();
		sharedPrefs.edit().remove(Constants.DataKeys.USER_INFO).commit();

		initialize();
	}
}
