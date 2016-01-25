package com.beerrightnow.jon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.beerrightnow.shared.LoginInfo;

public class Splashscreen extends Activity {
	private static int SPLASH_TIME_OUT = 2000;
	String email, pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if (!LoginInfo.getInstance().isLogined()) {
					Intent i = new Intent(Splashscreen.this,
							LoginActivity.class);
					startActivity(i);
				} else if (LoginInfo.getInstance().getZipCode().isEmpty()) {

					startActivity(new Intent(Splashscreen.this, ChangeZip.class)
							.putExtra(ChangeZip.TITLE_KEY, "Confirm Zipcode"));
				} else {
					Intent i = new Intent(Splashscreen.this, Home.class);
					startActivity(i);
				}

				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}
