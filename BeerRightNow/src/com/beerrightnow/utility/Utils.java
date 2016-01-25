package com.beerrightnow.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.beerrightnow.jon.R;
import com.google.gson.Gson;

public class Utils {

	private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	//private static final String emailPattern = "[A-Za-z0-9._-]+@[A-Za-z]+\\.+[A-Za-z]+";

	private String KALINGA_FONT = "fonts/Kalinga.ttf";

	private static Utils shared = new Utils();

	private Typeface kalinga;
	private Gson gson = new Gson();

	public static final String TAG = "com.ganyo.pushtest";
	public static final String DISPLAY_MESSAGE_ACTION = "com.ganyo.pushtest.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);

	}

	public static Utils getInstance() {

		return shared;
	}

	public void init(Context context) {

		kalinga = Typeface.createFromAsset(context.getAssets(), KALINGA_FONT);
	}

	public Gson getGson() {

		return gson;
	}

	public Typeface getKalingaType() {

		return kalinga;
	}

	public boolean validateEmail(String email) {
		if (email.matches(emailPattern)) {
			return true;
		} else
			return false;
	}

	// return date object
	@SuppressLint("SimpleDateFormat")
	public Date getDateObj(String fullDateTime) {
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		try {
			dt = sdf.parse(fullDateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}

	// Check Password length to minimum 4 char

	/**
	 * @param Password
	 * @return
	 */
	public boolean validatePassword(String Password) {
		if (Password.length() >= 4)
			return true;
		else
			return false;
	}

	public String convertToDecimalPointString(float value, int decimalCount) {

		String formatStr = "%." + decimalCount + "f";
		return String.format(formatStr, value);
	}

	public void share(Context context) {

		FileOutputStream outStream;
		File file;
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		String unitelDirectory = Environment.getExternalStorageDirectory()
				.toString() + "/" + Constants.CACHE_DIR;
		File dir = new File(unitelDirectory);
		if (!dir.exists())
			dir.mkdir();

		file = new File(unitelDirectory, "logo1234.png");
		if (!file.exists()) {
			try {
				outStream = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.d("link", e.getMessage());
				return;
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("link", e.getMessage());
				return;
			}
		}

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		// intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		intent.putExtra(Intent.EXTRA_SUBJECT,
				context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT,
				"I'm getting #Beer #Wine #Liquor & #Snacks #Delivered from @BeerRightNow!");
		Intent mailer = Intent.createChooser(intent, "Share with");
		context.startActivity(mailer);
	}

	public void sharePhoto(Context context, String text, Uri imageUri) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		intent.putExtra(Intent.EXTRA_SUBJECT,
				context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_TEXT, text);
		Intent mailer = Intent.createChooser(intent, "Share with");
		context.startActivity(mailer);
	}
}
