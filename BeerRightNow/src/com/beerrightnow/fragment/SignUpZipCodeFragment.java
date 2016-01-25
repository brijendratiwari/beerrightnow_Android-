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

import com.beerrightnow.jon.R;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.Callback;
import com.beerrightnow.task.CheckZipTask;
import com.beerrightnow.utility.Constants;

public class SignUpZipCodeFragment extends Fragment {

	public static final String TAG = SignUpZipCodeFragment.class
			.getSimpleName();

	private TextView edTxtZipCheck;

	private TextView txtCheers;
	private TextView txtZipMessage;

	private OnSignFlowManager onSignFlowManager;

	public SignUpZipCodeFragment() {
	}

	public static SignUpZipCodeFragment create() {
		SignUpZipCodeFragment frag = new SignUpZipCodeFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signup_check_zipcode, container,
				false);

		edTxtZipCheck = (TextView) v.findViewById(R.id.edTxtZipCheck);
		if (onSignFlowManager != null) {

			Bundle bundle = onSignFlowManager.getBundle();
			edTxtZipCheck
					.setText(bundle.getString(OnSignFlowManager.CHECK_ZIP));
		}

		txtCheers = (TextView) v.findViewById(R.id.txtCheers);
		txtZipMessage = (TextView) v.findViewById(R.id.txtZipMessage);

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

				new CheckZipTask(getActivity(), new Callback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

						try {
							JSONObject jsonObj = new JSONObject(response);
							if (!jsonObj.getBoolean(Constants.DataKeys.STATUS)) {
								txtCheers.setText(R.string.hic);
								txtZipMessage.setText(R.string.str_sorryZip);
								txtCheers.setTextColor(getResources().getColor(
										R.color.c_red));

							} else {

								jsonObj = jsonObj
										.getJSONObject(Constants.DataKeys.DATA);

								LoginInfo.getInstance().setZipCode(
										edTxtZipCheck.getText().toString());
								LoginInfo
										.getInstance()
										.setDistributorId(
												jsonObj.getString(Constants.DataKeys.DISTRIBUTOR_ID));
								LoginInfo
										.getInstance()
										.setLrnDistributorId(
												jsonObj.getString(Constants.DataKeys.LRN_DISTRIBUTOR_ID));

								if (onSignFlowManager != null) {

									Fragment addAddressFrag = SignUpAddressFragment
											.create();
									onSignFlowManager
											.onWillEnter(addAddressFrag);
								}

								txtCheers.setText(R.string.cheers);
								txtZipMessage.setText(R.string.happy_zip_code);
								txtCheers.setTextColor(Color.GREEN);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}).execute(new String[] { edTxtZipCheck.getText().toString() });
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
