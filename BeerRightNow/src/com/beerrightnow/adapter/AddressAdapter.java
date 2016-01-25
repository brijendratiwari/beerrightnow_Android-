package com.beerrightnow.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beerrightnow.data.Address;
import com.beerrightnow.jon.R;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.task.Callback;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class AddressAdapter extends ArrayAdapter<Address> {

	public AddressAdapter(Context context, List<Address> addresses) {
		super(context, 0, addresses);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		Address address = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.address_row, null, true);
			holder = new ViewHolder();
			holder.txtAddressName = (TextView) view
					.findViewById(R.id.txtAddressName);
			holder.txtStreet = (TextView) view.findViewById(R.id.txtStreet);
			holder.txtCityState = (TextView) view
					.findViewById(R.id.txtCityState);
			holder.txtZipcode = (TextView) view.findViewById(R.id.txtZipcode);
			holder.imgDefault = (ImageView) view.findViewById(R.id.imgDefault);
			holder.imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtAddressName.setText(address.getFirstName() + " "
				+ address.getLastName());

		holder.txtStreet.setText(address.getStreetAddress());
		holder.txtCityState.setText(address.getCity() + " , "
				+ address.getState());
		holder.txtZipcode.setText(address.getAddressZipCode());

		holder.imgEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// String[] splitAddress = address.getAddress().split(" ");

				Address address = getItem(position);
				final Dialog dlg = new Dialog(getContext());
				dlg.setTitle("Update Address");
				dlg.setContentView(R.layout.update_address);
				dlg.show();

				final EditText edtFirstName = (EditText) dlg
						.findViewById(R.id.edtFirstName);
				edtFirstName.setText(address.getFirstName());

				final EditText edtLastName = (EditText) dlg
						.findViewById(R.id.edtLastName);
				edtLastName.setText(address.getLastName());

				final EditText edtSuite = (EditText) dlg
						.findViewById(R.id.edtSuite);
				edtSuite.setText(address.getSuite());

				final EditText edtPhone = (EditText) dlg
						.findViewById(R.id.edtPhone);
				edtPhone.setText(address.getPhone());

				final EditText edtAddress = (EditText) dlg
						.findViewById(R.id.edtAddress);
				edtAddress.setText(address.getStreetAddress());

				final EditText edTxtChngCity = (EditText) dlg
						.findViewById(R.id.edTxtChngCity);
				edTxtChngCity.setText(address.getCity());

				final EditText edTxtChngState = (EditText) dlg
						.findViewById(R.id.edTxtChngState);
				edTxtChngState.setText(address.getState());

				final EditText edtAddressZipcode = (EditText) dlg
						.findViewById(R.id.edtAddressZipcode);
				edtAddressZipcode.setText(address.getAddressZipCode());

				Button btnSave = (Button) dlg.findViewById(R.id.btnSave);
				btnSave.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Address address = getItem(position);
						String firstName = edtFirstName.getText().toString();
						String lastName = edtLastName.getText().toString();
						String suite = edtSuite.getText().toString();
						String phone = edtPhone.getText().toString();
						String cityChange = edTxtChngCity.getText().toString();
						String stateChange = edTxtChngState.getText()
								.toString();
						String zipcode = edtAddressZipcode.getText().toString();
						String street = edtAddress.getText().toString();

						if (firstName.isEmpty() || lastName.isEmpty()
								|| zipcode.isEmpty() || street.isEmpty()
								|| cityChange.isEmpty()
								|| stateChange.isEmpty()) {

							Toast.makeText(getContext(),
									R.string.all_fields_require,
									Toast.LENGTH_SHORT).show();
							return;

						}

						String addressZipcode = edtAddressZipcode.getText()
								.toString();
						if (addressZipcode.length() != 5) {
							edtAddressZipcode.setTextColor(Color.RED);
							edtAddressZipcode.setHintTextColor(Color.RED);
							Toast.makeText(getContext(),
									"ZipCode should be 5 digits",
									Toast.LENGTH_SHORT).show();
							return;
						}

						new UpdateAddressTask(getContext(), new Callback() {

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub

								try {

									JSONObject jsonObj = new JSONObject(
											response);
									if (jsonObj
											.getBoolean(Constants.DataKeys.STATUS)) {

										Address address = getItem(position);

										address.setFirstName(edtFirstName
												.getText().toString());
										address.setLastName(edtLastName
												.getText().toString());
										address.setSuite(edtSuite.getText()
												.toString());
										address.setPhone(edtPhone.getText()
												.toString());
										address.setStreetAddress(edtAddress
												.getText().toString());
										address.setCity(edTxtChngCity.getText()
												.toString());
										address.setState(edTxtChngState
												.getText().toString());
										address.setAddressZipCode(edtAddressZipcode
												.getText().toString());
										notifyDataSetChanged();
									}
								} catch (NullPointerException e) {

									e.printStackTrace();
								} catch (JSONException e) {

									e.printStackTrace();
								}
							}
						}).execute(new String[] { firstName, lastName, suite,
								phone, street, cityChange, stateChange,
								addressZipcode, address.getAddressId() });

						dlg.dismiss();
					}

				});

				Button btnCancel = (Button) dlg.findViewById(R.id.btnCanel);
				btnCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						dlg.cancel();
					}

				});

			}
		});

		if (LoginInfo.getInstance().getUserInfo()
				.getCustomersDefaultAddressId()
				.equalsIgnoreCase(address.getAddressId())) {

			holder.imgDefault.setImageResource(R.drawable.checkmark);
			holder.imgDefault.setOnClickListener(null);
			view.setBackgroundResource(R.drawable.lst_default);
		} else {

			holder.imgDefault.setImageResource(R.drawable.cross);
			holder.imgDefault.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							getContext());
					builder.setMessage("Would you like to delete it?");
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									dialog.dismiss();
									Address address = getItem(position);
									new DeleteAddressTask(address).execute();
								}
							});

					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									dialog.cancel();
								}
							});

					AlertDialog alertDlg = builder.create();
					alertDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDlg.show();
				}

			});
			view.setBackgroundResource(R.drawable.list_back);
		}

		return view;
	}

	static class ViewHolder {
		TextView txtAddressName;
		TextView txtStreet;
		TextView txtCityState;
		TextView txtZipcode;
		ImageView imgDefault;
		ImageView imgEdit;
	}

	class DeleteAddressTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;
		private Address address;

		public DeleteAddressTask(Address address) {

			this.address = address;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(getContext(), "", "");
			pd.setContentView(new ProgressBar(getContext()));
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo
								.getInstance().getDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
								.getInstance().getLrnDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.ZIP_CODE, LoginInfo.getInstance()
								.getZipCode()));
				arrParams.add(new BasicNameValuePair("address_id", address
						.getAddressId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.CUSTOMER_ID, LoginInfo.getInstance()
								.getUserInfo().getCustomersId()));
				return HttpServer.httpCall(HttpServer.POST,
						HttpServer.RequestAPI.DELETE_ADDRESS, arrParams);
			} catch (Exception e) {

				e.printStackTrace();
				return "";
			}
		}

		@Override
		protected void onPostExecute(String response) {
			pd.dismiss();
			if (response.isEmpty()) {

				Toast.makeText(getContext(), R.string.network_error,
						Toast.LENGTH_LONG).show();
				return;
			}

			try {
				JSONObject jsonObj = new JSONObject(response);
				if (jsonObj.getBoolean(Constants.DataKeys.STATUS)) {

					remove(address);
					notifyDataSetChanged();
				}

			} catch (JSONException ex) {
				Toast.makeText(getContext(), R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			} catch (NullPointerException e) {

				e.printStackTrace();
			}
		}

	}

	class UpdateAddressTask extends AsyncTask<String, Void, String> {

		private Context context;
		ProgressDialog pd;

		private Callback callback;

		public UpdateAddressTask(Context context, Callback callback) {

			this.context = context;
			this.callback = callback;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(context, "", "");
			pd.setContentView(new ProgressBar(context));

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();

				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.ZIP_CODE, LoginInfo.getInstance()
								.getZipCode()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo
								.getInstance().getDistributorId()));
				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
								.getInstance().getLrnDistributorId()));

				arrParams.add(new BasicNameValuePair(
						Constants.DataKeys.CUSTOMER_ID, LoginInfo.getInstance()
								.getUserInfo().getCustomersId()));
				arrParams
						.add(new BasicNameValuePair("email", LoginInfo
								.getInstance().getUserInfo()
								.getCustomersEmailAddress()));
				arrParams.add(new BasicNameValuePair("firstname", params[0]));
				arrParams.add(new BasicNameValuePair("lastname", params[1]));
				arrParams.add(new BasicNameValuePair("suite", params[2]));
				arrParams.add(new BasicNameValuePair("phone", params[3]));
				arrParams.add(new BasicNameValuePair("street_address",
						params[4]));

				arrParams.add(new BasicNameValuePair("city", params[5]));
				arrParams.add(new BasicNameValuePair("state", params[6]));
				arrParams.add(new BasicNameValuePair("address_zipcode",
						params[7]));
				arrParams.add(new BasicNameValuePair("address_id", params[8]));
				return HttpServer.httpCall(HttpServer.POST,
						HttpServer.RequestAPI.UPDATE_ADDRESS, arrParams);
			} catch (Exception e) {

				e.printStackTrace();
				return "";
			}

		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if (callback != null)
				callback.onResponse(response);
		}

	}

}
