package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Utils;

public class AddCreditCard extends SherlockActivity {

	public static final String ONLY_FOR_CC = "only_for_cc";

	private boolean isOnlyForCC;

	private EditText edtCardNum;
	private EditText edtCity;
	private EditText edtSuite;
	private EditText edtCVV;
	private EditText edtName;
	private EditText edtAddress;
	private EditText edtState;
	private EditText edtZipcode;
	private EditText edtCountry;
	private Spinner spinCardType;
	private Spinner spinMonth;
	private Spinner spinYear;

	public static class CreditCard {

		private String name;
		private int cardNumLength;
		private int cardCVVLength;

		public CreditCard(String name, int cardNumLength, int cardCVVLength) {

			this.name = name;
			this.cardNumLength = cardNumLength;
			this.cardCVVLength = cardCVVLength;
		}

		public String getName() {
			return name;
		}

		public int getCardNumLength() {
			return cardNumLength;
		}

		public int getCardCVVLength() {
			return cardCVVLength;
		}
	}

	public static final CreditCard[] CARDS = {
			new CreditCard("Master Card", 16, 3),
			new CreditCard("Visa", 16, 3),
			new CreditCard("American Express", 15, 4),
			new CreditCard("Discover", 16, 3) };

	private static final int EXPIRE_YEAR_COUNT = 30;

	private static final String CARD_NUMBER_ALERT = "Card Number should be %d digits";
	private static final String CVV_ALERT = "CVV Number should be %d digits";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_credit_card);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init();

	}

	@Override
	public void onDestroy() {

		if (!isOnlyForCC)
			saveCCInfo();
		super.onDestroy();

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

		isOnlyForCC = getIntent().getBooleanExtra(ONLY_FOR_CC, false);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Add a Credit Card");

		View relCart = (RelativeLayout) findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(AddCreditCard.this);
			}
		});

		edtCardNum = (EditText) findViewById(R.id.edtCardNum);
		edtCardNum.setText(CartInfo.getInstance().getCardNumber());

		edtCity = (EditText) findViewById(R.id.edtCity);
		edtCity.setText(CartInfo.getInstance().getBillingCity());

		edtSuite = (EditText) findViewById(R.id.edtSuite);
		edtSuite.setText(CartInfo.getInstance().getBillingSuite());

		edtCVV = (EditText) findViewById(R.id.edtCCV);
		edtCVV.setText(CartInfo.getInstance().getCardCVV());

		edtName = (EditText) findViewById(R.id.edtName);
		edtName.setText(CartInfo.getInstance().getBillingName());

		edtAddress = (EditText) findViewById(R.id.edtAddress);
		edtAddress.setText(CartInfo.getInstance().getBillingAddres());

		edtState = (EditText) findViewById(R.id.edtState);
		edtState.setText(CartInfo.getInstance().getBillingState());

		edtZipcode = (EditText) findViewById(R.id.edtZipcode);
		if (CartInfo.getInstance().getBillingPostCode().isEmpty())
			edtZipcode.setText(LoginInfo.getInstance().getZipCode());
		else
			edtZipcode.setText(CartInfo.getInstance().getBillingPostCode());

		edtCountry = (EditText) findViewById(R.id.edtCountry);
		if(CartInfo.getInstance().getBillingCountry().equalsIgnoreCase("United State"))
		{
			edtCountry.setText("United States");
		}
		else
		{
			edtCountry.setText(CartInfo.getInstance().getBillingCountry());
		}
		

		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(5);
		edtZipcode.setFilters(FilterArray);

		spinCardType = (Spinner) findViewById(R.id.cardTypeSpinner);
		ArrayAdapter<CreditCard> cardAdapter = new ArrayAdapter<CreditCard>(
				AddCreditCard.this, android.R.layout.simple_list_item_1,
				android.R.id.text1, CARDS) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View view = super.getView(position, convertView, parent);

				try {

					CreditCard card = getItem(position);
					TextView txtCardType = (TextView) view
							.findViewById(android.R.id.text1);

					txtCardType.setText(card.getName());
				} catch (Exception e) {

					e.printStackTrace();
				}

				return view;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {

				View view = super
						.getDropDownView(position, convertView, parent);

				try {

					CreditCard card = getItem(position);
					TextView txtCardType = (TextView) view
							.findViewById(android.R.id.text1);

					txtCardType.setText(card.getName());
				} catch (Exception e) {

				}

				return view;
			}

		};
		cardAdapter
				.setDropDownViewResource(android.R.layout.simple_list_item_1);
		spinCardType.setAdapter(cardAdapter);
		spinCardType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				CreditCard card = (CreditCard) spinCardType.getSelectedItem();
				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(card
						.getCardNumLength());
				edtCardNum.setFilters(FilterArray);
				FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(card
						.getCardCVVLength());
				edtCVV.setFilters(FilterArray);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		spinCardType.setSelection(CartInfo.getInstance().getCardType());

		spinMonth = (Spinner) findViewById(R.id.monthSpinner);
		final String months[] = { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12" };
		ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
				AddCreditCard.this, android.R.layout.simple_list_item_1,
				android.R.id.text1, months);
		spinMonth.setAdapter(monthAdapter);
		spinMonth.setSelection(CartInfo.getInstance().getCardExpiresMonth());

		spinYear = (Spinner) findViewById(R.id.yearSpinner);
		List<Integer> years = new ArrayList<Integer>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int savedYear = 0;
		for (int i = 0; i < EXPIRE_YEAR_COUNT; i++) {

			years.add(year + i);
			if (year + i == CartInfo.getInstance().getCardExpiresYear())
				savedYear = i;
		}
		ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(
				AddCreditCard.this, android.R.layout.simple_list_item_1,
				android.R.id.text1, years);
		spinYear.setAdapter(yearAdapter);
		spinYear.setSelection(savedYear);

		TextView txtReviewDetails = (TextView) findViewById(R.id.txtReviewDetails);
		Button btnSaveAdd = (Button) findViewById(R.id.btnSaveAdd);
		if (!isOnlyForCC) {

			btnSaveAdd.setVisibility(View.GONE);
			txtReviewDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					edtCardNum.setTextColor(Color.BLACK);
					edtCardNum.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtCVV.setTextColor(Color.BLACK);
					edtCVV.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtName.setTextColor(Color.BLACK);
					edtName.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtAddress.setTextColor(Color.BLACK);
					edtAddress.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtCity.setTextColor(Color.BLACK);
					edtCity.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtState.setTextColor(Color.BLACK);
					edtState.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtZipcode.setTextColor(Color.BLACK);
					edtZipcode.setHintTextColor(getResources().getColor(
							R.color.txt_col));

					CreditCard card = (CreditCard) spinCardType
							.getSelectedItem();
					if (card.getCardNumLength() != edtCardNum.getText()
							.length()) {

						edtCardNum.setTextColor(Color.RED);
						edtCardNum.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								String.format(CARD_NUMBER_ALERT,
										card.getCardNumLength()),
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (card.getCardCVVLength() != edtCVV.getText().length()) {

						edtCVV.setTextColor(Color.RED);
						edtCVV.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								String.format(CVV_ALERT,
										card.getCardCVVLength()),
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtName.getText().toString().isEmpty()) {
						edtName.setTextColor(Color.RED);
						edtName.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (edtAddress.getText().toString().isEmpty()) {
						edtAddress.setTextColor(Color.RED);
						edtAddress.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtCity.getText().toString().isEmpty()) {
						edtCity.setTextColor(Color.RED);
						edtCity.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtState.getText().toString().isEmpty()) {
						edtState.setTextColor(Color.RED);
						edtState.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtZipcode.getText().toString().length() != 5) {
						edtZipcode.setTextColor(Color.RED);
						edtZipcode.setHintTextColor(Color.RED);
						Toast.makeText(AddCreditCard.this,
								"ZipCode should be 5 digits",
								Toast.LENGTH_SHORT).show();
						return;

					}

					CartInfo.getInstance().setCardType(
							spinCardType.getSelectedItemPosition());
					CartInfo.getInstance().setCardCVV(
							edtCVV.getText().toString());
					CartInfo.getInstance().setCardExpiresMonth(
							spinMonth.getSelectedItemPosition());
					CartInfo.getInstance().setCardExpiresYear(
							(Integer) spinYear.getSelectedItem());
					CartInfo.getInstance().setCardExpires(
							spinMonth.getSelectedItem().toString() + "/"
									+ spinYear.getSelectedItem().toString());

					CartInfo.getInstance().setCardNumber(
							edtCardNum.getText().toString());
					CartInfo.getInstance().setBillingName(
							edtName.getText().toString());
					CartInfo.getInstance().setBillingAddres(
							edtAddress.getText().toString());
					CartInfo.getInstance().setBillingCity(
							edtCity.getText().toString());
					CartInfo.getInstance().setBillingPostCode(
							edtZipcode.getText().toString());
					CartInfo.getInstance().setBillingState(
							edtState.getText().toString());
					CartInfo.getInstance().setBillingSuite(
							edtSuite.getText().toString());

					startActivity(new Intent(AddCreditCard.this,
							ConfirmOrder.class));

				}
			});
		} else {

			txtReviewDetails.setVisibility(View.GONE);
			btnSaveAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					edtCardNum.setTextColor(Color.BLACK);
					edtCardNum.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtCVV.setTextColor(Color.BLACK);
					edtCVV.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtName.setTextColor(Color.BLACK);
					edtName.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtAddress.setTextColor(Color.BLACK);
					edtAddress.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtCity.setTextColor(Color.BLACK);
					edtCity.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtState.setTextColor(Color.BLACK);
					edtState.setHintTextColor(getResources().getColor(
							R.color.txt_col));
					edtZipcode.setTextColor(Color.BLACK);
					edtZipcode.setHintTextColor(getResources().getColor(
							R.color.txt_col));

					CreditCard card = (CreditCard) spinCardType
							.getSelectedItem();
					if (card.getCardNumLength() != edtCardNum.getText()
							.length()) {

						edtCardNum.setTextColor(Color.RED);
						edtCardNum.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								String.format(CARD_NUMBER_ALERT,
										card.getCardNumLength()),
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (card.getCardCVVLength() != edtCVV.getText().length()) {

						edtCVV.setTextColor(Color.RED);
						edtCVV.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								String.format(CVV_ALERT,
										card.getCardCVVLength()),
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtName.getText().toString().isEmpty()) {
						edtName.setTextColor(Color.RED);
						edtName.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (edtAddress.getText().toString().isEmpty()) {
						edtAddress.setTextColor(Color.RED);
						edtAddress.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtCity.getText().toString().isEmpty()) {
						edtCity.setTextColor(Color.RED);
						edtCity.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtState.getText().toString().isEmpty()) {
						edtState.setTextColor(Color.RED);
						edtState.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtCountry.getText().toString().isEmpty()) {
						edtCountry.setTextColor(Color.RED);
						edtCountry.setHintTextColor(Color.RED);
						Toast.makeText(
								AddCreditCard.this,
								"Please verify that you filled out all the required fields",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (edtZipcode.getText().toString().length() > 7) {
						edtZipcode.setTextColor(Color.RED);
						edtZipcode.setHintTextColor(Color.RED);
						Toast.makeText(AddCreditCard.this,
								"ZipCode should be less than 7 digits",
								Toast.LENGTH_SHORT).show();
						return;

					}

					saveCCInfo();
					finish();
				}
			});
		}

	}

	private void saveCCInfo() {

		CartInfo.getInstance().setCardType(
				spinCardType.getSelectedItemPosition());
		CartInfo.getInstance().setCardCVV(edtCVV.getText().toString());
		CartInfo.getInstance().setCardExpiresMonth(
				spinMonth.getSelectedItemPosition());
		CartInfo.getInstance().setCardExpiresYear(
				(Integer) spinYear.getSelectedItem());
		CartInfo.getInstance().setCardExpires(
				spinMonth.getSelectedItem().toString() + "/"
						+ spinYear.getSelectedItem().toString());

		CartInfo.getInstance().setCardNumber(edtCardNum.getText().toString());
		CartInfo.getInstance().setBillingName(edtName.getText().toString());
		CartInfo.getInstance()
				.setBillingAddres(edtAddress.getText().toString());
		CartInfo.getInstance().setBillingCity(edtCity.getText().toString());
		CartInfo.getInstance().setBillingPostCode(
				edtZipcode.getText().toString());
		CartInfo.getInstance().setBillingState(edtState.getText().toString());
		CartInfo.getInstance().setBillingSuite(edtSuite.getText().toString());
		CartInfo.getInstance().setBillingCountry(
				edtCountry.getText().toString());

	}

}
