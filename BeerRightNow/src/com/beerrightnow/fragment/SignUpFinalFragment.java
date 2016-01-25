package com.beerrightnow.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beerrightnow.jon.R;

public class SignUpFinalFragment extends Fragment {

	public static final String TAG = SignUpFinalFragment.class.getSimpleName();

	private OnSignFlowManager onSignFlowManager;

	public SignUpFinalFragment() {
	}

	public static SignUpFinalFragment create() {
		SignUpFinalFragment frag = new SignUpFinalFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signup_final, container, false);

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

				if (onSignFlowManager != null)
					onSignFlowManager.onComplete();
			}

		});

		try {

			TextView txtFName = (TextView) v.findViewById(R.id.txtFname);
			txtFName.setText(onSignFlowManager.getBundle().getString(
					OnSignFlowManager.FIRST_NAME));

			TextView txtLName = (TextView) v.findViewById(R.id.txtLname);
			txtLName.setText(onSignFlowManager.getBundle().getString(
					OnSignFlowManager.LAST_NAME));

			TextView txtAddress = (TextView) v.findViewById(R.id.txtAdress);
			txtAddress.setText(onSignFlowManager.getBundle().getString(
					OnSignFlowManager.ADDRESS));

			TextView txtPhone = (TextView) v.findViewById(R.id.txtPhone);
			txtPhone.setText(onSignFlowManager.getBundle().getString(
					OnSignFlowManager.PHONE));

			TextView txtMail = (TextView) v.findViewById(R.id.txtMail);
			txtMail.setText(onSignFlowManager.getBundle().getString(
					OnSignFlowManager.MAIL));

		} catch (NullPointerException e) {

			e.printStackTrace();
		}
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
