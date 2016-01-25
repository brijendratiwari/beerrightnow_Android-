package com.beerrightnow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface OnSignFlowManager {

	public static final String CUSTOMER_ID = "customer_id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String MAIL = "mail";
	public static final String PHONE = "phone";
	public static final String CHECK_ZIP = "check_zip";
	public static final String PASSWORD = "password";
	public static final String ADDRESS = "address";

	public static final String SIGN_BUNDLE = "sign_bundle";

	public void onWillEnter(Fragment fragment);

	public void onComplete();

	public Bundle getBundle();
}
