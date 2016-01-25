package com.beerrightnow.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beerrightnow.jon.R;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.AddAddressTask;
import com.beerrightnow.task.Callback;
import com.beerrightnow.utility.Constants;

public class SignUpAddressFragment extends Fragment {

	public static final String TAG = SignUpAddressFragment.class
			.getSimpleName();

	private EditText edtFirstName;
	private EditText edtLastName;
	private EditText edtSuite;
	private EditText edtPhone;
	private EditText edtStreet;
	private TextView edtAddressZip;
	private EditText edtState;
	private EditText edtCity;

	private OnSignFlowManager onSignFlowManager;

	public SignUpAddressFragment() {
	}

	public static SignUpAddressFragment create() {
		SignUpAddressFragment frag = new SignUpAddressFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.signup_add_address, container, false);

		Bundle bundle = onSignFlowManager.getBundle();

		edtFirstName = (EditText) v.findViewById(R.id.edtFirstName);
		edtFirstName.setText(bundle.getString(OnSignFlowManager.FIRST_NAME));
		edtLastName = (EditText) v.findViewById(R.id.edtLastName);
		edtLastName.setText(bundle.getString(OnSignFlowManager.LAST_NAME));
		edtSuite = (EditText) v.findViewById(R.id.edtSuite);
		edtPhone = (EditText) v.findViewById(R.id.edtPhone);
		edtPhone.setText(bundle.getString(OnSignFlowManager.PHONE));
		edtStreet = (EditText) v.findViewById(R.id.edtAddress);
		edtCity = (EditText) v.findViewById(R.id.edTxtCity);
		edtState = (EditText) v.findViewById(R.id.edTxtState);
		edtAddressZip = (TextView) v.findViewById(R.id.edTxtZipCode);
		edtAddressZip.setText(LoginInfo.getInstance().getZipCode());

		ImageView imgBack = (ImageView) v.findViewById(R.id.imgBack);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				getFragmentManager().popBackStack();
			}

		});

		TextView txtContinue = (TextView) v.findViewById(R.id.txtContinue);
		txtContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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

					Toast.makeText(getActivity(), "All fields are required",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (edtAddressZip.length() != 5) {
					edtAddressZip.setTextColor(Color.RED);
					edtAddressZip.setHintTextColor(Color.RED);
					Toast.makeText(getActivity(), "ZipCode should be 5 digits",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (onSignFlowManager == null)
					return;

				String address = street + " " + city + " " + state + "-"
						+ zipcode;

				Bundle bundle = onSignFlowManager.getBundle();
				bundle.putString(OnSignFlowManager.ADDRESS, address);

				AddAddressTask.AddressData data = new AddAddressTask.AddressData();
				data.setCustomerId(bundle
						.getString(OnSignFlowManager.CUSTOMER_ID));
				data.setFirstName(firstName);
				data.setLastName(lastName);
				data.setEmail(bundle.getString(OnSignFlowManager.MAIL));
				data.setStreet(street);
				data.setCity(city);
				data.setState(state);
				data.setAddressZipcode(zipcode);
				data.setPhone(phone);
				data.setSuit(suite);

				new AddAddressTask(getActivity(), new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {

							JSONObject jobj = new JSONObject(response);

							if (jobj.getBoolean(Constants.DataKeys.STATUS)
									&& onSignFlowManager != null) {

								Fragment signFinalFrag = SignUpFinalFragment
										.create();
								onSignFlowManager.onWillEnter(signFinalFrag);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}).execute(new AddAddressTask.AddressData[] { data });
			}

		});

		return v;
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
