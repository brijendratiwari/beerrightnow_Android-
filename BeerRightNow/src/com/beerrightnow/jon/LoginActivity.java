package com.beerrightnow.jon;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.beerrightnow.fragment.OnSignFlowManager;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.Callback;
import com.beerrightnow.task.LoginTask;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.Utils;

public class LoginActivity extends SherlockActivity {

	EditText edtMail, edtPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		init();
		// forDemo();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent result) {

		if (requestCode == SignUpActivity.SIGN_REQUEST
				&& resultCode == RESULT_OK) {

			Bundle bundle = result
					.getBundleExtra(OnSignFlowManager.SIGN_BUNDLE);
			try {

				edtMail.setText(bundle.getString(OnSignFlowManager.MAIL));
				edtPass.setText(bundle.getString(OnSignFlowManager.PASSWORD));

			} catch (NullPointerException e) {

				e.printStackTrace();
			}
		}
	}

	private void forDemo() {
		// TODO Auto-generated method stub

		edtMail.setText("test@test.com");
		edtPass.setText("12345678");
	}

	private void init() {
		// TODO Auto-generated method stub

		// check if user loggedin then redirect to home screen
		ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
		imgLogo.setImageResource(R.drawable.header_logo);

		TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setTypeface(Utils.getInstance().getKalingaType());
		txtTitle.setText("Login");

		TextView txtSignUp = (TextView) findViewById(R.id.txtSignUp);
		txtSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(LoginActivity.this,
						SignUpActivity.class), SignUpActivity.SIGN_REQUEST);

			}
		});

		edtMail = (EditText) findViewById(R.id.edtEmail);
		edtPass = (EditText) findViewById(R.id.edtPassword);
		final Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String email, pass;
				email = edtMail.getText().toString().trim();
				pass = edtPass.getText().toString().trim();

				edtMail.setTextColor(Color.BLACK);
				edtMail.setHintTextColor(getResources().getColor(
						R.color.txt_col));
				edtPass.setTextColor(Color.BLACK);
				edtPass.setHintTextColor(getResources().getColor(
						R.color.txt_col));

				if (email.isEmpty()) {

					edtMail.setTextColor(Color.RED);
					edtMail.setHintTextColor(Color.RED);

					Toast.makeText(LoginActivity.this,
							R.string.all_fields_require, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (pass.isEmpty()) {

					edtPass.setTextColor(Color.RED);
					edtPass.setHintTextColor(Color.RED);
					Toast.makeText(LoginActivity.this,
							R.string.all_fields_require, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (!Utils.getInstance().validateEmail(email)) {

					edtMail.setTextColor(Color.RED);
					edtMail.setHintTextColor(Color.RED);
					Toast.makeText(LoginActivity.this, R.string.valid_email,
							Toast.LENGTH_SHORT).show();
					return;
				}

				edtMail.setTypeface(Utils.getInstance().getKalingaType());
				edtPass.setTypeface(Utils.getInstance().getKalingaType());
				btnLogin.setTypeface(Utils.getInstance().getKalingaType());

				new LoginTask(LoginActivity.this, new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						if (response.isEmpty()) {

							Toast.makeText(LoginActivity.this,
									R.string.network_error, Toast.LENGTH_LONG)
									.show();
							return;
						}

						try {
							JSONObject jobj = new JSONObject(response);

							if (jobj.getBoolean(Constants.DataKeys.STATUS)) {

								JSONObject userInfo = jobj
										.getJSONObject(Constants.DataKeys.USER_INFO);

								LoginInfo.getInstance().setLogined(true);
								LoginInfo.getInstance().setUserInfo(
										userInfo.toString());

								if (LoginInfo.getInstance().getZipCode()
										.isEmpty()) {

									startActivity(new Intent(
											LoginActivity.this, ChangeZip.class)
											.putExtra(ChangeZip.TITLE_KEY,
													"Confirm Zipcode"));
								} else {

									startActivity(new Intent(
											LoginActivity.this, Home.class));
								}

								finish();
							} else {

								Toast.makeText(
										LoginActivity.this,
										jobj.getString(Constants.DataKeys.ERROR),
										Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

							Toast.makeText(LoginActivity.this,
									R.string.unknown_error, Toast.LENGTH_LONG)
									.show();
						}
					}

				}).execute(new String[] { edtMail.getText().toString(),
						edtPass.getText().toString() });
			}
		});
	}

}
