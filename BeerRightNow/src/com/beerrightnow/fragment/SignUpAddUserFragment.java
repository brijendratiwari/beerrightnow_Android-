package com.beerrightnow.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beerrightnow.jon.R;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

public class SignUpAddUserFragment extends Fragment {

	public static final String TAG = SignUpAddUserFragment.class
			.getSimpleName();

	private EditText edtFname;
	private EditText edtLname;
	private EditText edtEmail;
	private EditText edtMobile;
	private EditText edtPassword;
	private EditText edTxtRePassword;
	private EditText edtZip;

	private TextView txtDOBDisp;

	private OnSignFlowManager onSignFlowManager;
	private int year = Calendar.getInstance().get(Calendar.YEAR);
	private int month = Calendar.getInstance().get(Calendar.MONTH);
	private int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	// facebook
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if (state.isOpened()) {

			} else if (state.isClosed()) {

			}
		}
	};

	public SignUpAddUserFragment() {
	}

	public static SignUpAddUserFragment create() {
		SignUpAddUserFragment frag = new SignUpAddUserFragment();
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
					}
				});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signup_add_user, container, false);

		LoginButton loginButton = (LoginButton) v.findViewById(R.id.loginBtn);
		loginButton.setFragment(this);

		loginButton
				.setReadPermissions(Arrays.asList("public_profile", "email"));

		loginButton.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				// TODO Auto-generated method stub

				try {

					edtFname.setText(user.getFirstName());
					edtLname.setText(user.getLastName());
					edtEmail.setText(user.asMap().get("email").toString());

				} catch (NullPointerException e) {

					edtFname.setText("");
					edtLname.setText("");
					edtEmail.setText("");
				}

			}
		});

		edtEmail = (EditText) v.findViewById(R.id.edTxtEmail);
		edtFname = (EditText) v.findViewById(R.id.edTxtFirstName);
		edtLname = (EditText) v.findViewById(R.id.edTxtLastName);
		edtMobile = (EditText) v.findViewById(R.id.edTxtMobile);
		edtPassword = (EditText) v.findViewById(R.id.edTxtPassword);
		edTxtRePassword = (EditText) v.findViewById(R.id.edTxtRePassword);
		edtZip = (EditText) v.findViewById(R.id.edTxtZip);

		ImageView imgLogo = (ImageView) v.findViewById(R.id.imgLogo);
		imgLogo.setImageResource(R.drawable.head_back);
		imgLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				getActivity().finish();
			}

		});

		txtDOBDisp = (TextView) v.findViewById(R.id.txtDOBDisp);

		TextView txtDOBSelect = (TextView) v.findViewById(R.id.txtDOBSelect);
		txtDOBSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DatePickerDialog dlg = new DatePickerDialog(getActivity(),
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int yy,
									int mm, int dd) {
								// TODO Auto-generated method stub

								year = yy;
								month = mm;
								day = dd;

								String birthday = year + "-";
								if (month < 10)
									birthday += "0";
								birthday += month + 1;
								birthday += "-";

								if (day < 10)
									birthday += "0";
								birthday += day;

								txtDOBDisp.setText(birthday);
							}

						}, year, month, day);
				dlg.show();
			}

		});

		TextView txtContinue = (TextView) v.findViewById(R.id.txtContinue);
		txtContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name = edtFname.getText().toString();
				String lname = edtLname.getText().toString();
				String email = edtEmail.getText().toString();
				String pass = edtPassword.getText().toString();
				String passConfirm = edTxtRePassword.getText().toString();
				String zip = edtZip.getText().toString();
				String phone = edtMobile.getText().toString();

				if (!email.isEmpty() && !new Utils().validateEmail(email)) {

					Toast.makeText(getActivity(), "Enter valid email",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!pass.isEmpty() && passConfirm.isEmpty()) {

					Toast.makeText(getActivity(), "Retype password",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!passConfirm.equalsIgnoreCase(pass)) {

					edtPassword.setText("");
					edTxtRePassword.setText("");

					Toast.makeText(getActivity(), "Confirm Password",
							Toast.LENGTH_SHORT).show();
					return;

				}

				if (name.isEmpty() || lname.isEmpty() || email.isEmpty()
						|| pass.isEmpty() || zip.isEmpty()) {

					Toast.makeText(getActivity(), "All fields are required",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (onSignFlowManager != null) {

					Bundle bundle = onSignFlowManager.getBundle();
					bundle.putString(OnSignFlowManager.FIRST_NAME, name);
					bundle.putString(OnSignFlowManager.LAST_NAME, lname);
					bundle.putString(OnSignFlowManager.MAIL, email);
					bundle.putString(OnSignFlowManager.CHECK_ZIP, zip);
					bundle.putString(OnSignFlowManager.PHONE, phone);
					bundle.putString(OnSignFlowManager.PASSWORD, pass);
				}

				new RegisterTask().execute();
			}
		});
		return v;
	}

	private class RegisterTask extends AsyncTask<Void, Void, String> {

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(getActivity(), "", "");
			pd.setContentView(new ProgressBar(getActivity()));

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair("firstname", edtFname
					.getText().toString()));
			arrParams.add(new BasicNameValuePair("lastname", edtLname.getText()
					.toString()));
			arrParams.add(new BasicNameValuePair("emailid", edtEmail.getText()
					.toString()));
			arrParams.add(new BasicNameValuePair("mobile", edtMobile.getText()
					.toString()));
			arrParams.add(new BasicNameValuePair("dateofbirth", txtDOBDisp
					.getText().toString()));
			arrParams.add(new BasicNameValuePair("password", edtPassword
					.getText().toString()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					edtZip.getText().toString()));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.ADD_USER, arrParams);

		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();
			// check with response
			if (response.isEmpty()) {

				Toast.makeText(getActivity(), R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					if (onSignFlowManager != null) {

						onSignFlowManager.getBundle().putString(
								OnSignFlowManager.CUSTOMER_ID,
								jsonObj.getString("userid"));
						Fragment checkZipFrag = SignUpZipCodeFragment.create();
						onSignFlowManager.onWillEnter(checkZipFrag);
					}

				} else {
					Toast.makeText(getActivity(),
							jsonObj.getString(Constants.DataKeys.MESSAGE),
							Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getActivity(), R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {

			onSignFlowManager = (OnSignFlowManager) activity;
		} catch (ClassCastException e) {

			e.printStackTrace();
		}
	}

}
