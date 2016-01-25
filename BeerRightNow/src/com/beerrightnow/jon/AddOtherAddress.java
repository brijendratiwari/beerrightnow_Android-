package com.beerrightnow.jon;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.AddAddressTask;
import com.beerrightnow.task.Callback;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;

public class AddOtherAddress extends SherlockActivity {

	public static final int ADD_ADDRESS_REQUEST = 617;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);

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
		title.setText("Shipping Address");

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(AddOtherAddress.this);
			}
		});
		final EditText edtFirstName = (EditText) findViewById(R.id.edtFirstName);
		edtFirstName.setText(LoginInfo.getInstance().getUserInfo()
				.getCustomersFirstname());
		final EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
		edtLastName.setText(LoginInfo.getInstance().getUserInfo()
				.getCustomersLastname());
		final EditText edtSuite = (EditText) findViewById(R.id.edtSuite);
		final EditText edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtPhone.setText(LoginInfo.getInstance().getUserInfo()
				.getCustomersTelephone());
		final EditText edtStreet = (EditText) findViewById(R.id.edtAddress);
		final EditText edtCity = (EditText) findViewById(R.id.edTxtCity);
		final EditText edtState = (EditText) findViewById(R.id.edTxtState);
		final EditText edtAddressZip = (EditText) findViewById(R.id.edTxtZipCode);
		edtAddressZip.setText(LoginInfo.getInstance().getZipCode());

		Button saveBtn = (Button) findViewById(R.id.btnSaveAdd);
		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// validate
				String firstName = edtFirstName.getText().toString();
				String lastName = edtLastName.getText().toString();
				String suite = edtSuite.getText().toString();
				String phone = edtSuite.getText().toString();
				String street = edtStreet.getText().toString();
				String zipcode = edtAddressZip.getText().toString();
				String city = edtCity.getText().toString();
				String state = edtState.getText().toString();

				if (firstName.isEmpty() || lastName.isEmpty()
						|| street.isEmpty() || zipcode.isEmpty()
						|| city.isEmpty() || state.isEmpty()) {

					Toast.makeText(AddOtherAddress.this,
							"All fields are required", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (edtAddressZip.length() != 5) {
					edtAddressZip.setTextColor(Color.RED);
					edtAddressZip.setHintTextColor(Color.RED);
					Toast.makeText(AddOtherAddress.this,
							"ZipCode should be 5 digits", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				AddAddressTask.AddressData data = new AddAddressTask.AddressData();
				data.setCustomerId(LoginInfo.getInstance().getUserInfo()
						.getCustomersId());
				data.setFirstName(firstName);
				data.setLastName(lastName);
				data.setEmail(LoginInfo.getInstance().getUserInfo()
						.getCustomersEmailAddress());
				data.setStreet(street);
				data.setCity(city);
				data.setState(state);
				data.setAddressZipcode(zipcode);
				data.setPhone(phone);
				data.setSuit(suite);

				new AddAddressTask(AddOtherAddress.this, new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {

							JSONObject jsonObj = new JSONObject(response);

							if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

								setResult(RESULT_OK);
								finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}).execute(new AddAddressTask.AddressData[] { data });
			}
		});

	}
}
