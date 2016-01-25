package com.beerrightnow.jon;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.Callback;
import com.beerrightnow.task.CheckZipTask;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;

public class ChangeZip extends SherlockActivity {

	public static final String TITLE_KEY = "title";

	EditText edtChangeZip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_zipcode);

		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {
		// TODO Auto-generated method stub

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));
		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText(getIntent().getStringExtra(TITLE_KEY));
		edtChangeZip = (EditText) findViewById(R.id.edTxtChngZip);

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(ChangeZip.this);
			}
		});
		Button btnChangeZip = (Button) findViewById(R.id.btnChngZip);

		btnChangeZip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtChangeZip.getWindowToken(), 0);
				new CheckZipTask(ChangeZip.this, new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {

							JSONObject jobj = new JSONObject(response);
							if (jobj.getBoolean(Constants.DataKeys.STATUS) == false) {
								Toast.makeText(ChangeZip.this,
										"Zip not supported", Toast.LENGTH_LONG)
										.show();
							} else {

								jobj = jobj
										.getJSONObject(Constants.DataKeys.DATA);

								LoginInfo.getInstance().setZipCode(
										edtChangeZip.getText().toString());
								LoginInfo
										.getInstance()
										.setDistributorId(
												jobj.getString(Constants.DataKeys.DISTRIBUTOR_ID));
								LoginInfo
										.getInstance()
										.setLrnDistributorId(
												jobj.getString(Constants.DataKeys.LRN_DISTRIBUTOR_ID));
								startActivity(new Intent(ChangeZip.this,
										Home.class)
										.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
								finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).execute(new String[] { edtChangeZip.getText().toString() });
			}
		});
	}
}
