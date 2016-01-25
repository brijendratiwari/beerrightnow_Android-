package com.beerrightnow.jon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class InviteParty extends SherlockActivity {

	public static final String IS_INVITE = "is_invite";

	private boolean isInvite;
	private EditText edtSMS;
	private EditText edtSearchFilter;
	private ListView contactListView;
	private ContactAdapter adapter;
	private List<ContactInfo> selectedContact = new ArrayList<ContactInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_party);
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

		isInvite = getIntent().getBooleanExtra(IS_INVITE, true);

		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.invite_actionbar);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setIcon(R.drawable.head_back);
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));
		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		edtSMS = (EditText) findViewById(R.id.edtSMS);
		edtSearchFilter = (EditText) findViewById(R.id.edtSearchFilter);
		edtSearchFilter
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView arg0, int arg1,
							KeyEvent arg2) {
						// TODO Auto-generated method stub
						if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edtSearchFilter.getWindowToken(), 0);

							try {

								adapter.getFilter().filter(
										edtSearchFilter.getText().toString());
							} catch (NullPointerException e) {
								e.printStackTrace();
							}
							return true;
						}
						return false;
					}
				});

		edtSearchFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				try {

					adapter.getFilter().filter(s.toString());
				} catch (NullPointerException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		if (isInvite) {

			title.setText("Invite Friends");
		} else {

			title.setText("Having Party");
		}

		contactListView = (ListView) findViewById(R.id.contactListView);

		CheckBox checkAll = (CheckBox) findViewById(R.id.checkAll);
		checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				selectedContact.clear();
				try {

					if (isChecked) {

						for (int i = 0; i < adapter.getCount(); i++) {

							selectedContact.add(adapter.getItem(i));
						}
					}

				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}

				adapter.notifyDataSetChanged();
			}
		});

		Button sendBtn = (Button) findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (selectedContact.isEmpty()) {

					Toast.makeText(InviteParty.this, "Please Select",
							Toast.LENGTH_SHORT).show();
					return;
				}

				new InvitePartyTask().execute();
			}
		});

		new LoadContactsAyscn().execute();
	}

	private class ContactInfo implements Comparable<ContactInfo> {

		private String name;
		private String phoneNumber;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@Override
		public int compareTo(ContactInfo another) {
			// TODO Auto-generated method stub
			try {

				return getName().compareToIgnoreCase(another.getName());
			} catch (NullPointerException e) {

				e.printStackTrace();
				return 0;
			}
		}

	}

	private class LoadContactsAyscn extends
			AsyncTask<Void, Void, List<ContactInfo>> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pd = ProgressDialog.show(InviteParty.this, "", "");
			pd.setContentView(new ProgressBar(InviteParty.this));
		}

		@Override
		protected List<ContactInfo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<ContactInfo> contacts = new ArrayList<ContactInfo>();

			Cursor c = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					null, null, null);
			while (c.moveToNext()) {

				String contactName = c
						.getString(c
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String phNumber = c
						.getString(c
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

				ContactInfo contactInfo = new ContactInfo();
				contactInfo.setName(contactName);
				contactInfo.setPhoneNumber(phNumber);
				contacts.add(contactInfo);
			}
			c.close();

			Collections.sort(contacts);
			return contacts;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> contacts) {
			// TODO Auto-generated method stub
			super.onPostExecute(contacts);

			pd.dismiss();
			adapter = new ContactAdapter(InviteParty.this, contacts);
			contactListView.setAdapter(adapter);

		}

	}

	private class ContactAdapter extends ArrayAdapter<ContactInfo> {

		private ItemFilter mFilter = new ItemFilter();
		private List<ContactInfo> originItems;

		public ContactAdapter(Context context, List<ContactInfo> contacts) {
			super(context, 0, contacts);
			// TODO Auto-generated constructor stub
			originItems = new ArrayList<ContactInfo>(contacts);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ContactInfo contactInfo = getItem(position);

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = inflater.inflate(R.layout.contact_item, null, true);

			CheckBox checkBtn = (CheckBox) view.findViewById(R.id.checkBtn);
			if (selectedContact.contains(contactInfo))
				checkBtn.setChecked(true);
			else
				checkBtn.setChecked(false);

			checkBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub

					if (isChecked)
						selectedContact.add(contactInfo);
					else
						selectedContact.remove(contactInfo);
				}
			});

			TextView txtContactName = (TextView) view
					.findViewById(R.id.txtContactName);
			txtContactName.setText(contactInfo.getName());

			TextView txtContactMobile = (TextView) view
					.findViewById(R.id.txtContactMobile);
			txtContactMobile.setText(contactInfo.getPhoneNumber());

			return view;

		}

		@Override
		public Filter getFilter() {
			return mFilter;
		}

		private class ItemFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();

				String filterString = constraint.toString().toLowerCase();

				if (filterString == null || filterString.length() == 0) {

					List<ContactInfo> items = new ArrayList<ContactInfo>(
							originItems);
					results.values = items;
					results.count = items.size();
				} else {

					final List<ContactInfo> items = originItems;
					int count = items.size();
					final List<ContactInfo> nItems = new ArrayList<ContactInfo>(
							count);

					for (ContactInfo item : items) {

						String match = item.getName() + item.getPhoneNumber();
						if (match.toLowerCase().contains(filterString))
							nItems.add(item);
					}

					results.values = nItems;
					results.count = nItems.size();

				}

				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				clear();
				addAll((ArrayList<ContactInfo>) results.values);
				notifyDataSetChanged();
			}

		}
	}

	private class InvitePartyTask extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = ProgressDialog.show(InviteParty.this, "", "");
			pd.setContentView(new ProgressBar(InviteParty.this));

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ArrayList<NameValuePair> arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.DISTRIBUTOR_ID, LoginInfo.getInstance()
							.getDistributorId()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo
							.getInstance().getLrnDistributorId()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					LoginInfo.getInstance().getZipCode()));

			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.CUSTOMER_ID, LoginInfo.getInstance()
							.getUserInfo().getCustomersId()));
			arrParams.add(new BasicNameValuePair("message", edtSMS.getText()
					.toString()));

			try {

				JSONArray jsonArr = new JSONArray();

				for (ContactInfo contactInfo : selectedContact) {

					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", contactInfo.getName());
					jsonObj.put("number", contactInfo.getPhoneNumber());

					jsonArr.put(jsonObj);
				}

				arrParams.add(new BasicNameValuePair(Constants.DataKeys.DATA,
						jsonArr.toString()));
			} catch (JSONException e) {

				e.printStackTrace();
			}

			return HttpServer.httpCall(HttpServer.POST,
					isInvite ? HttpServer.RequestAPI.INVITE
							: HttpServer.RequestAPI.INVITE_TO_PARTY, arrParams);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub
			pd.dismiss();

			try {
				JSONObject jobj = new JSONObject(response);
				if (jobj.getBoolean(Constants.DataKeys.STATUS)) {
					Toast.makeText(InviteParty.this, "Sent SMS",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(InviteParty.this, "Sending SMS is failed.",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception ex) {
				Toast.makeText(InviteParty.this, R.string.unknown_error,
						Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}

	}
}
