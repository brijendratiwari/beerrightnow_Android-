package com.beerrightnow.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.beerrightnow.data.BeerBean;
import com.beerrightnow.data.LiquorBean;
import com.beerrightnow.jon.R;
import com.beerrightnow.shared.CartInfo;
import com.beerrightnow.shared.LoginInfo;
import com.beerrightnow.utility.Constants;
import com.beerrightnow.utility.HttpServer;

public class DeliveryExpectedDlg extends Dialog implements View.OnClickListener {

	private OnGetDlgItemListener listener;

	public static  int year = Calendar.getInstance().get(Calendar.YEAR);
	public static int  month = Calendar.getInstance().get(Calendar.MONTH);
	public static  int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	private int hour;
	private int minutes;


	public static  int year2 = Calendar.getInstance().get(Calendar.YEAR);
	public static int  month2 = Calendar.getInstance().get(Calendar.MONTH);
	public static  int day2 = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	private int hour2 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	private int minutes2 = Calendar.getInstance().get(Calendar.MINUTE);

	private TextView txtSelDate;
	private TextView txtSelTime;

	private RadioButton asapRadio;
	private RadioButton dateTimeRadio;

	private ProgressDialog loadingDialog;
	private String Dates="",Time="";

	private String months="",days="",years="";
	String dayOfTheWeek="";
	private ArrayList<BeerBean >beertime= new ArrayList<BeerBean>();
	private ArrayList<LiquorBean >liquortime= new ArrayList<LiquorBean>();
	public static String beer="";
	public static String liquior="";
	String CurrentString;
	private Context context;

	private String Type="";
	View dateTimeGroup;

	public static String timeliquor="";

	LinearLayout alertlayout;
	TextView textalert;
	RadioButton saveradio;
	RadioButton sceduleradio;

	LinearLayout setdateTimeGroup;
	TextView txtSelDate2;
	TextView txtSelTime2;

	RadioGroup delvieryRadioGroup2;

	ListView timelist1;

	String[] beer_time;
	String[] liquor_time;

	ListView timelist2;


	public DeliveryExpectedDlg(Context context, OnGetDlgItemListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.listener = listener;
		this.context=context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_expected_dlg);

		setCancelable(true);

		dateTimeGroup = findViewById(R.id.dateTimeGroup);

		RadioGroup delvieryRadioGroup = (RadioGroup) findViewById(R.id.delvieryRadioGroup);
		asapRadio = (RadioButton) findViewById(R.id.asapRadio);
		dateTimeRadio = (RadioButton) findViewById(R.id.dateTimeRadio);

		timelist1=(ListView)findViewById(R.id.timelist1);

		timelist2=(ListView)findViewById(R.id.timelist2);

		alertlayout=(LinearLayout)findViewById(R.id.alertlayout);
		textalert=(TextView)findViewById(R.id.textalert);
		saveradio=(RadioButton)findViewById(R.id.saveradio);
		sceduleradio=(RadioButton)findViewById(R.id.sceduleradio);


		setdateTimeGroup=(LinearLayout)findViewById(R.id.setdateTimeGroup);
		txtSelDate2=(TextView)findViewById(R.id.txtSelDate2);
		txtSelDate2.setBackgroundResource(R.drawable.blue_rect);

		txtSelTime2=(TextView)findViewById(R.id.txtSelTime2);
		txtSelTime2.setBackgroundResource(R.drawable.grey_rect);

		delvieryRadioGroup2=(RadioGroup)findViewById(R.id.delvieryRadioGroup2);

		delvieryRadioGroup
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				switch (checkedId) {

				case R.id.asapRadio:
					dateTimeGroup.setVisibility(View.GONE);
					alertlayout.setVisibility(View.GONE);
					textalert.setVisibility(View.GONE);
					delvieryRadioGroup2.setVisibility(View.GONE);
					timelist1.setVisibility(View.GONE);
					timelist2.setVisibility(View.GONE);
					setdateTimeGroup.setVisibility(View.GONE);

					saveradio.setVisibility(View.GONE);
					sceduleradio.setVisibility(View.GONE);

					txtSelDate.setText("Select Date");
					txtSelTime.setText("Select Time");

					txtSelDate2.setText("Select Date");
					txtSelTime2.setText("Select Time");

					break;
				case R.id.dateTimeRadio:
					dateTimeGroup.setVisibility(View.VISIBLE);
					txtSelTime.setBackgroundResource(R.drawable.blue_rect);
					txtSelDate.setBackgroundResource(R.drawable.grey_rect);

					year = Calendar.getInstance().get(Calendar.YEAR);
					month = Calendar.getInstance().get(Calendar.MONTH);
					day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


					DatePickerDialog dlg = new DatePickerDialog(getContext(),
							new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int yy,
								int mm, int dd) {
							// TODO Auto-generated method stub

							timelist1.setVisibility(View.GONE);

							txtSelTime.setText("Select Time");


							alertlayout.setVisibility(View.GONE);
							textalert.setVisibility(View.GONE);
							delvieryRadioGroup2.setVisibility(View.GONE);


							saveradio.setVisibility(View.GONE);
							sceduleradio.setVisibility(View.GONE);

							year = yy;
							month = mm;
							day = dd;

							String date = "";
							if (month < 10)
								date += "0";
							date += month + 1;
							date += "/";

							if (day < 10)
								date += "0";
							date += day;
							date += "/" + year;

							txtSelDate.setTextColor(Color.WHITE);
							txtSelDate.setText(date);


						}
					}, year, month, day);
					dlg.show();

					break;
				}
			}
		});

		delvieryRadioGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				switch (checkedId) {

				case R.id.saveradio:
					setdateTimeGroup.setVisibility(View.GONE);
					CartInfo.getInstance().setState("Save");
					timelist2.setVisibility(View.GONE);
					break;
				case R.id.sceduleradio:
					setdateTimeGroup.setVisibility(View.VISIBLE);

					txtSelTime2.setBackgroundResource(R.drawable.grey_rect);
					txtSelDate2.setBackgroundResource(R.drawable.blue_rect);

					year2 = Calendar.getInstance().get(Calendar.YEAR);
					month2 = Calendar.getInstance().get(Calendar.MONTH);
					day2 = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

					DatePickerDialog dlg = new DatePickerDialog(getContext(),
							new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int yy,
								int mm, int dd) {
							// TODO Auto-generated method stub


							timelist2.setVisibility(View.GONE);
							txtSelTime2.setText("Select Time");

							year2 = yy;
							month2 = mm;
							day2 = dd;

							String date = "";
							if (month2 < 10)
								date += "0";
							date += month2 + 1;
							date += "/";

							if (day2 < 10)
								date += "0";
							date += day2;
							date += "/" + year2;

							txtSelDate2.setTextColor(Color.WHITE);
							txtSelDate2.setText(date);
						}
					}, year2, month2, day2);
					dlg.show();
					break;
				}
			}
		});

		Button okBtn = (Button) findViewById(R.id.okBtn);
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

		txtSelTime = (TextView) findViewById(R.id.txtSelTime);
		txtSelTime.setBackgroundResource(R.drawable.grey_rect);
		txtSelTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if(txtSelDate.getText().toString().equalsIgnoreCase("Select Date"))
				{

					txtSelTime.setBackgroundResource(R.drawable.blue_rect);
					txtSelDate.setBackgroundResource(R.drawable.grey_rect);

					txtSelTime.setTextColor(Color.WHITE);
					txtSelDate.setTextColor(Color.WHITE);

					Calendar c = Calendar.getInstance();
					System.out.println("Current time => " + c.getTime());

					SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
					String formattedDate = df.format(c.getTime());
					System.out.println("formattedDate: "+formattedDate);

					txtSelDate.setText(formattedDate);

					int da=Integer.valueOf(formattedDate.substring(0, 2));


					Date date = null;
					try {
						date = df.parse(formattedDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
					dayOfTheWeek = outFormat.format(date); 


					/*dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", da);*/
					System.out.println("dayOfTheWeek: "+dayOfTheWeek);

					GetDistributorHour getAsync= new GetDistributorHour();
					getAsync.execute();




				}
				else
				{
					
					alertlayout.setVisibility(View.GONE);
					textalert.setVisibility(View.GONE);
					delvieryRadioGroup2.setVisibility(View.GONE);
					timelist1.setVisibility(View.GONE);
					timelist2.setVisibility(View.GONE);
					setdateTimeGroup.setVisibility(View.GONE);

					saveradio.setVisibility(View.GONE);
					sceduleradio.setVisibility(View.GONE);
					
						txtSelTime.setBackgroundResource(R.drawable.blue_rect);
						txtSelDate.setBackgroundResource(R.drawable.grey_rect);

						txtSelDate.setTextColor(Color.WHITE);
						txtSelTime.setTextColor(Color.WHITE);
						System.out.println("txtSelDate: "+txtSelDate.getText().toString());

						SimpleDateFormat df = new SimpleDateFormat("mm/dd/yy");
						Date date = null;
						try {
							date = df.parse(txtSelDate.getText().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
						dayOfTheWeek = outFormat.format(date); 


						/*dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", da);*/
						System.out.println("dayOfTheWeek: "+dayOfTheWeek);

						GetDistributorHour getAsync= new GetDistributorHour();
						getAsync.execute();
					

				}


			}
		});
		txtSelDate = (TextView) findViewById(R.id.txtSelDate);
		txtSelDate.setBackgroundResource(R.drawable.blue_rect);
		txtSelDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				alertlayout.setVisibility(View.GONE);
				textalert.setVisibility(View.GONE);
				delvieryRadioGroup2.setVisibility(View.GONE);
				timelist1.setVisibility(View.GONE);
				timelist2.setVisibility(View.GONE);
				setdateTimeGroup.setVisibility(View.GONE);

				saveradio.setVisibility(View.GONE);
				sceduleradio.setVisibility(View.GONE);

				txtSelTime.setBackgroundResource(R.drawable.grey_rect);
				txtSelDate.setBackgroundResource(R.drawable.blue_rect);

				year = Calendar.getInstance().get(Calendar.YEAR);
				month= Calendar.getInstance().get(Calendar.MONTH);
				day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dlg = new DatePickerDialog(getContext(),
						new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int yy,
							int mm, int dd) {
						// TODO Auto-generated method stub

						timelist1.setVisibility(View.GONE);

						txtSelTime.setText("Select Time");


						alertlayout.setVisibility(View.GONE);
						textalert.setVisibility(View.GONE);
						delvieryRadioGroup2.setVisibility(View.GONE);


						saveradio.setVisibility(View.GONE);
						sceduleradio.setVisibility(View.GONE);

						year = yy;
						month = mm;
						day = dd;

						String date = "";
						if (month < 10)
							date += "0";
						date += month + 1;
						date += "/";

						if (day < 10)
							date += "0";
						date += day;
						date += "/" + year;

						txtSelDate.setTextColor(Color.WHITE);
						txtSelDate.setText(date);


					}
				}, year, month, day);
				dlg.show();
			}
		});




		txtSelDate2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				txtSelTime2.setBackgroundResource(R.drawable.grey_rect);
				txtSelDate2.setBackgroundResource(R.drawable.blue_rect);

				year2 = Calendar.getInstance().get(Calendar.YEAR);
				month2 = Calendar.getInstance().get(Calendar.MONTH);
				day2 = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dlg = new DatePickerDialog(getContext(),
						new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int yy,
							int mm, int dd) {
						// TODO Auto-generated method stub


						timelist2.setVisibility(View.GONE);
						txtSelTime2.setText("Select Time");

						year2 = yy;
						month2 = mm;
						day2 = dd;

						String date = "";
						if (month2 < 10)
							date += "0";
						date += month2 + 1;
						date += "/";

						if (day2 < 10)
							date += "0";
						date += day2;
						date += "/" + year2;

						txtSelDate2.setTextColor(Color.WHITE);
						txtSelDate2.setText(date);
					}
				}, year2, month2, day2);
				dlg.show();
			}
		});

		txtSelTime2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(txtSelDate2.getText().toString().equalsIgnoreCase("Select Date"))
				{

					txtSelTime2.setBackgroundResource(R.drawable.blue_rect);
					txtSelDate2.setBackgroundResource(R.drawable.grey_rect);

					txtSelDate2.setTextColor(Color.WHITE);
					txtSelTime2.setTextColor(Color.WHITE);
					Calendar c = Calendar.getInstance();
					System.out.println("Current time => " + c.getTime());

					SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					String formattedDate = df.format(c.getTime());
					System.out.println("formattedDate: "+formattedDate);

					txtSelDate2.setText(formattedDate);

				//	int da=Integer.valueOf(formattedDate.substring(0, 2));


					Date date = null;
					try {
						date = df.parse(formattedDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
					dayOfTheWeek = outFormat.format(date); 


					/*dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", da);*/
					System.out.println("dayOfTheWeek: "+dayOfTheWeek);

					GetDistributorHour getAsync= new GetDistributorHour();
					getAsync.execute();


				}
				else
				{
					
				
						txtSelTime2.setBackgroundResource(R.drawable.blue_rect);
						txtSelDate2.setBackgroundResource(R.drawable.grey_rect);

						txtSelDate2.setTextColor(Color.WHITE);
						txtSelTime2.setTextColor(Color.WHITE);
						System.out.println("txtSelDate2: "+txtSelDate2.getText().toString());

						SimpleDateFormat df = new SimpleDateFormat("mm/dd/yy");
						Date date = null;
						try {
							date = df.parse(txtSelDate2.getText().toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
						dayOfTheWeek = outFormat.format(date); 


						/*dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", da);*/
						System.out.println("dayOfTheWeek: "+dayOfTheWeek);

						GetDistributorHour getAsync= new GetDistributorHour();
						getAsync.execute();
					


				}
			}
		});




	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.okBtn:

			if (dateTimeRadio.isChecked()) {

				if (txtSelDate
						.getText()
						.toString()
						.equalsIgnoreCase(
								getContext().getResources().getString(
										R.string.select_date))) {
					txtSelDate.setTextColor(Color.RED);
					return;
				}

				

				if (txtSelTime
						.getText()
						.toString()
						.equalsIgnoreCase(
								getContext().getResources().getString(
										R.string.select_time))) {
					txtSelTime.setTextColor(Color.RED);
					return;
				}


				if(sceduleradio.isChecked())
				{
					if (txtSelDate2
							.getText()
							.toString()
							.equalsIgnoreCase(
									getContext().getResources().getString(
											R.string.select_date))) {
						txtSelDate2.setTextColor(Color.RED);
						return;
					}
					else if (txtSelTime2
							.getText()
							.toString()
							.equalsIgnoreCase(
									getContext().getResources().getString(
											R.string.select_time))) {
						txtSelTime2.setTextColor(Color.RED);
						return;
					}
					else if(!txtSelDate2.getText().toString().equalsIgnoreCase(getContext().getResources().getString(R.string.select_date))&&!txtSelTime2
							.getText().toString().equalsIgnoreCase(getContext().getResources().getString(R.string.select_time)))
					{
						dismiss();
						CartInfo.getInstance().setState("SCHEDULE");
					}
				}




				if(saveradio.isChecked())
				{
					dismiss();
					CartInfo.getInstance().setState("Save");
				}


				if (listener != null)

					listener.onGetItem(txtSelDate.getText().toString() + " "
							+ txtSelTime.getText().toString());
				if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0 && Integer.valueOf(CartInfo.getInstance().getLiquorCount())==0)
				{
					CartInfo.getInstance().setDeliveryExpected_Beer(txtSelDate.getText().toString() + " "+ txtSelTime.getText().toString());
					//CartInfo.getInstance().setDeliveryExpected_Liquor("");
					dismiss();
				}
				else if(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0 && Integer.valueOf(CartInfo.getInstance().getBeerCount())==0)
				{
					CartInfo.getInstance().setDeliveryExpected_Liquor(txtSelDate.getText().toString() + " "+ txtSelTime.getText().toString());
					//CartInfo.getInstance().setDeliveryExpected_Beer("");
					dismiss();
				}
				else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0&&(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0))
				{
					CartInfo.getInstance().setDeliveryExpected_Beer(txtSelDate.getText().toString() + " "+ txtSelTime.getText().toString());
					CartInfo.getInstance().setDeliveryExpected_Liquor(txtSelDate2.getText().toString() + " "+ txtSelTime2.getText().toString());
					
					if(!timeliquor.equalsIgnoreCase("Match"))
					{
						listener.onGetItem("Beer: "+txtSelDate.getText().toString() + " "
								+ txtSelTime.getText().toString()+'\n'+"Liquor: "+txtSelDate2.getText().toString() + " "
								+ txtSelTime2.getText().toString());
					}
					
					if(saveradio.isChecked())
					{
						listener.onGetItem(txtSelDate.getText().toString() + " "
								+ txtSelTime.getText().toString());
					}
					
					
					
					dismiss();
				}


			} else if (asapRadio.isChecked()) {

				if (listener != null)
					listener.onGetItem("ASAP");
				dismiss();
			}

			System.out.println("Beer: "+CartInfo.getInstance().getDeliveryExpected_Beer());
			System.out.println("Liquor: "+CartInfo.getInstance().getDeliveryExpected_Liquor());
			break;
		case R.id.cancelBtn:
			dismiss();
			break;

		}



	}


	public class GetDistributorHour extends AsyncTask<String, Void, String>
	{
		String response;
		String resultconn = "";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingDialog = ProgressDialog.show(context, "", "");
			loadingDialog.setContentView(new ProgressBar(context));
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub




			ArrayList<NameValuePair> arrParams;
			arrParams = new ArrayList<NameValuePair>();
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.DISTRIBUTOR_ID,
					LoginInfo.getInstance().getDistributorId()));
			arrParams.add(new BasicNameValuePair(
					Constants.DataKeys.LRN_DISTRIBUTOR_ID, LoginInfo.getInstance()
					.getLrnDistributorId()));
			arrParams.add(new BasicNameValuePair(Constants.DataKeys.ZIP_CODE,
					LoginInfo.getInstance().getZipCode()));
			arrParams.add(new BasicNameValuePair("day_name", dayOfTheWeek));
			arrParams.add(new BasicNameValuePair("is_today", "1"));

			return HttpServer.httpCall(HttpServer.POST,
					HttpServer.RequestAPI.Distributor_Working_Hours, arrParams);


		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			System.out.println("Response: "+result);

		

			try {
				JSONObject OBJ1= new JSONObject(result);

				JSONObject data= OBJ1.getJSONObject("data");

				if(data.has(LoginInfo.getInstance().getDistributorId()))
				{
					JSONObject obj2= data.getJSONObject(LoginInfo.getInstance().getDistributorId());

					JSONObject obj3= obj2.getJSONObject("delivery_time");

					if(obj3.has(dayOfTheWeek.toUpperCase()))
					{	
						JSONArray obj4= obj3.getJSONArray(dayOfTheWeek.toUpperCase());

						beer_time= new String[obj4.length()];
						for(int i=0;i<obj4.length();i++)
						{
							JSONObject obj5=obj4.getJSONObject(i);

							BeerBean bean= new BeerBean();

							bean.setId(obj5.getString("id"));
							bean.setTime(obj5.getString("time"));
							bean.setHours(obj5.getString("hours"));
							bean.setMinutes(obj5.getString("minutes"));
							bean.setSeconds(obj5.getString("seconds"));

							beertime.add(bean);

							beer_time[i]=obj5.getString("time");

						}

					}

				}

				if(data.has(LoginInfo.getInstance().getLrnDistributorId()))
				{
					JSONObject obj2= data.getJSONObject(LoginInfo.getInstance().getLrnDistributorId());

					JSONObject obj3= obj2.getJSONObject("delivery_time");

					if(obj3.has(dayOfTheWeek.toUpperCase()))
					{	
						JSONArray obj4= obj3.getJSONArray(dayOfTheWeek.toUpperCase());
						liquor_time=new String[obj4.length()];

						for(int i=0;i<obj4.length();i++)
						{
							JSONObject obj5=obj4.getJSONObject(i);

							LiquorBean bean= new LiquorBean();

							bean.setId(obj5.getString("id"));
							bean.setTime(obj5.getString("time"));
							bean.setHours(obj5.getString("hours"));
							bean.setMinutes(obj5.getString("minutes"));
							bean.setSeconds(obj5.getString("seconds"));

							liquortime.add(bean);

							liquor_time[i]=obj5.getString("time");


						}

					}
				}

				loadingDialog.dismiss();

				System.out.println("Beer Count: "+Integer.valueOf(CartInfo.getInstance().getBeerCount()));
				System.out.println("Liquor Count: "+CartInfo.getInstance().getLiquorCount());


				if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0 && Integer.valueOf(CartInfo.getInstance().getLiquorCount())==0)
				{
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
							android.R.layout.simple_list_item_1, android.R.id.text1, beer_time);

					timelist1.setAdapter(adapter);
					timelist1.setVisibility(View.VISIBLE);

					timelist1.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							txtSelTime.setText(beertime.get(position).getTime());

						}
					});
				}
				else if(Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0 && Integer.valueOf(CartInfo.getInstance().getBeerCount())==0)
				{
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
							android.R.layout.simple_list_item_1, android.R.id.text1, liquor_time);

					timelist1.setAdapter(adapter);
					timelist1.setVisibility(View.VISIBLE);

					timelist1.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							txtSelTime.setText(liquortime.get(position).getTime());

						}
					});
				}
				else if(Integer.valueOf(CartInfo.getInstance().getBeerCount())>0 && Integer.valueOf(CartInfo.getInstance().getLiquorCount())>0)
				{





					if(alertlayout.getVisibility()==View.GONE)
					{
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
								android.R.layout.simple_list_item_1, android.R.id.text1, beer_time);

						timelist1.setAdapter(adapter);
						timelist1.setVisibility(View.VISIBLE);
					}
					

					

					timelist1.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							txtSelTime.setText(beertime.get(position).getTime());

							timeliquor="";
							for(LiquorBean d : liquortime){
								if(d.getTime() != null && d.getTime().contains(txtSelTime.getText().toString()))
								{
									timeliquor="Match";
								}
								//something here
							}

							if(timeliquor.equalsIgnoreCase("Match"))
							{
								txtSelDate2.setText(txtSelDate.getText().toString());
								txtSelTime2.setText(txtSelTime.getText().toString());
							}
							else
							{
								alertlayout.setVisibility(View.VISIBLE);
								textalert.setVisibility(View.VISIBLE);
								delvieryRadioGroup2.setVisibility(View.VISIBLE);

								sceduleradio.setVisibility(View.VISIBLE);

								alertlayout.setVisibility(View.VISIBLE);
								textalert.setText("Oh oh! You're attempting to have liquor delivered outside of delivery hours. Would you like to:");
								delvieryRadioGroup2.setVisibility(View.VISIBLE);

								saveradio.setChecked(true);
								saveradio.setVisibility(View.VISIBLE);
								saveradio.setText("Receive your beer and snacks as requested. Save your liquor to your cart.");
								sceduleradio.setText("Receive your beer and snacks as requested. Schedule liquor delivery for:");
								timelist1.setVisibility(View.GONE);
							}




						}
					});



					if(alertlayout.getVisibility()==View.VISIBLE)
					{
						if(sceduleradio.isChecked())
						{
							timelist2.setVisibility(View.VISIBLE);
						}
					}


					ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,
							android.R.layout.simple_list_item_1, android.R.id.text1, liquor_time);

					timelist2.setAdapter(adapter2);




					timelist2.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							txtSelTime2.setText(liquortime.get(position).getTime());


						}
					});
				}







			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}