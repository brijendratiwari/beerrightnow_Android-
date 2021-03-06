package com.beerrightnow.jon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.apigee.google.api.client.json.Json;
import com.beerrightnow.data.BeerBean;
import com.beerrightnow.data.LiquorBean;
import com.beerrightnow.dialog.DeliveryExpectedDlg;
import com.beerrightnow.dialog.OnGetDlgItemListener;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;
import com.beerrightnow.utility.Utils;

public class DeliveryOptions extends SherlockActivity {

	private EditText edtTxtComment;
	private EditText edTxtContPerson;
	private EditText edTxtReceptNumber;
	private EditText edTxtRecOfficeNumber;
	private EditText edTxtYourNumber;
	private EditText edTxtServiceEntAddress;
	private TextView txtExpectedDelivery;
	private TextView txtRole;
	private ToggleButton tgBtnGift;
	private ToggleButton tgBtnCorporate;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_options);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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
		title.setText("Delivery Options");

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(DeliveryOptions.this);
			}
		});
		edtTxtComment = (EditText) findViewById(R.id.edtTxtComment);
		edTxtContPerson = (EditText) findViewById(R.id.edTxtContPerson);
		edTxtReceptNumber = (EditText) findViewById(R.id.edTxtReceptNumber);
		edTxtRecOfficeNumber = (EditText) findViewById(R.id.edTxtRecOfficeNumber);
		edTxtYourNumber = (EditText) findViewById(R.id.edTxtYourNumber);
		edTxtServiceEntAddress = (EditText) findViewById(R.id.edTxtServiceEntAddress);

		txtExpectedDelivery = (TextView) findViewById(R.id.txtExprectedDelivery);
		txtExpectedDelivery.setText("ASAP");
	/*	txtExpectedDelivery.setText(CartInfo.getInstance()
				.getDeliveryExpected());*/

		View deliveryGroup = findViewById(R.id.deliveryGroup);
		deliveryGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DeliveryExpectedDlg dlg = new DeliveryExpectedDlg(
						DeliveryOptions.this, new OnGetDlgItemListener() {

							@Override
							public void onGetItem(String item) {
								// TODO Auto-generated method stub

								txtExpectedDelivery.setText(item);

								System.out.println("txtExpectedDelivery: "+item);


							}
						});

				dlg.setTitle("Select Delivery Time");
				dlg.show();


			}

		});

		final CharSequence[] items = { "Ordering for self",
				"Executive assistant", "Coordinator", "Event Planner" };
		txtRole = (TextView) findViewById(R.id.txtRole);
		txtRole.setText(items[0]);
		View roleGroup = findViewById(R.id.roleGroup);
		roleGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(
						DeliveryOptions.this);
				builder.setTitle("Select Your Role");
				builder.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {

						try {

							txtRole.setText(items[item]);
						} catch (IndexOutOfBoundsException e) {

							e.printStackTrace();
						}

						dialog.dismiss();
					}
				});

				AlertDialog roleDialog = builder.create();
				roleDialog.show();
			}
		});

		final View giftGroup = findViewById(R.id.giftGroup);
		tgBtnGift = (ToggleButton) findViewById(R.id.tgBtnGift);
		tgBtnGift.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

					giftGroup.setVisibility(View.VISIBLE);
				} else {

					giftGroup.setVisibility(View.GONE);
				}
			}
		});
		tgBtnGift.setChecked(CartInfo.getInstance().isGift());

		final View corporateGroup = findViewById(R.id.corporateGroup);
		tgBtnCorporate = (ToggleButton) findViewById(R.id.tgBtnCorporate);
		tgBtnCorporate
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {

					corporateGroup.setVisibility(View.VISIBLE);
				} else {

					corporateGroup.setVisibility(View.GONE);
				}
			}
		});
		tgBtnCorporate.setChecked(CartInfo.getInstance().isCorporateOrder());

		TextView txtMakePayment = (TextView) findViewById(R.id.txtMakePayment);
		txtMakePayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				saveInfo();
				DeliveryExpectedDlg.beer="";
				DeliveryExpectedDlg.liquior="";

				if(txtExpectedDelivery.getText().toString().equalsIgnoreCase("ASAP"))
				{
					CartInfo.getInstance().setDeliveryExpected(
							txtExpectedDelivery.getText().toString());
				}
				Intent intent = new Intent(DeliveryOptions.this,
						AddCreditCard.class);
				startActivity(intent);
			}
		});
	}


	
	private void saveInfo() {

		CartInfo.getInstance().setDeliveryExpected(
				txtExpectedDelivery.getText().toString());
		CartInfo.getInstance().setGift(tgBtnGift.isChecked());
		CartInfo.getInstance().setCustomerNumber(
				edTxtYourNumber.getText().toString());
		CartInfo.getInstance().setGiftForNumber(
				edTxtReceptNumber.getText().toString());

		CartInfo.getInstance().setCorporateOrder(tgBtnCorporate.isChecked());
		CartInfo.getInstance().setOfficeExtension(
				edTxtRecOfficeNumber.getText().toString());
		CartInfo.getInstance().setContactCell(
				edTxtContPerson.getText().toString());
		CartInfo.getInstance().setServiceEnterenceAddress(
				edTxtServiceEntAddress.getText().toString());
		CartInfo.getInstance().setDeliveryComment(
				edtTxtComment.getText().toString());

	}

	@Override
	public void onDestroy() {

		saveInfo();
		super.onDestroy();
	}

}
