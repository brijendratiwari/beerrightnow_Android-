package com.beerrightnow.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpServer {

	public static final String API_URL = "http://www.beerrightnow.com/api/";

	public static class RequestAPI {

		public static final String CHECK_ZIPCODE = "check_zipcode";
		public static final String ADD_USER = "add_user";
		public static final String ADD_ADDRESS = "add_address";
		public static final String UPDATE_ADDRESS = "update_address";
		public static final String USER_AUTH = "user_auth";
		public static final String GET_PROUDCT_SERCTIONS = "get_product_sections";
		public static final String ORDER_DETAILS = "order_details";
		public static final String ORDER_LIST = "order_list";
		public static final String PLACE_ORDER = "place_order";
		public static final String GET_PRODUCT_TYPE_ON_SECTION = "get_product_types_on_section";
		public static final String GET_PRODUCT_BY_TYPE = "get_product_by_type";
		public static final String PRODUCT_DETAILS = "product_details";
		public static final String GET_CART = "get_cart";
		public static final String GET_REVIEWS = "get_reviews";
		public static final String CHECK_COUPON_CODE = "check_coupon_code";
		public static final String GET_ADDRESS = "get_address";
		public static final String GET_ADDRESSES = "get_addresses";
		public static final String DELETE_ADDRESS = "delete_address";
		public static final String GET_BRANDS = "get_brands";
		public static final String GET_PRODUCERS = "get_producers";
		public static final String GET_PRICE_RANGE = "get_price_range";
		public static final String GET_TYPE = "get_style";
		public static final String GET_STYLE_TYPE_NAME = "get_style_type_name";
		public static final String SEARCH_RESULT = "search_result";
		public static final String INVITE = "invite";
		public static final String INVITE_TO_PARTY = "invite_to_party";
		public static final String Distributor_Working_Hours = "distributor_working_hours";
	}

	public static final String POST = "POST";
	public static final String GET = "GET";

	public static final int CONNECT_TIME_OUT = 3000;
	public static final int READ_TIME_OUT = 5000;

	/**
	 * 
	 * @param requestMethod
	 * @param methodURL
	 * @param params
	 * @return
	 */
	public static synchronized String httpCall(String requestMethod,
			String methodURL, List<NameValuePair> params) {

		String responseContent = "";
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, READ_TIME_OUT);
		HttpClient httpClient = new DefaultHttpClient(httpParams);

		if (requestMethod.equals("GET")) {
			HttpGet httpget = new HttpGet(API_URL + methodURL);
			try {

				/*
				 * httpget.setHeader("Authorization", "Basic " +
				 * Base64.encodeBytes(auth.getBytes()));
				 */
				// httpget.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse response = httpClient.execute(httpget);
				Log.d("BRN", response.getStatusLine().getStatusCode() + "");

				if (response.getStatusLine().getStatusCode() == 200) {
					responseContent = EntityUtils
							.toString(response.getEntity());
					Log.d("BRN", "A" + responseContent);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (requestMethod.equals("POST")) {
			HttpPost httpPost = new HttpPost(API_URL + methodURL);

			// Passing params to url if any
			try {
				/*
				 * httpPost.setHeader("Authorization", "Basic " +
				 * Base64.encodeBytes(auth.getBytes()));
				 */

				// httpPost.setHeader("Content-Type", "text/plain");

				Log.d("BRN", params.toString());
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = httpClient.execute(httpPost);
				// responseContent = EntityUtils.toString(response.getEntity());

				if (response.getStatusLine().getStatusCode() == 200) {
					Log.d("BRN", httpPost.getRequestLine().getUri());

					responseContent = EntityUtils
							.toString(response.getEntity());
					Log.d("BRN", "" + responseContent);
				}

			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return responseContent;
	}

}
