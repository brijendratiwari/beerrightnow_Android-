package com.beerrightnow.jon;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.beerrightnow.utility.Utils;

public class OrderComplete extends SherlockActivity {

	public static final String ORDER_ID = "order_id";

	// Intent share = new Intent(Intent.ACTION_SEND);

	private static final int RESULT_LOAD_IMAGE = 100;
	private static final int CAMERA_REQUEST = 1888;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_complete);

		init();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(OrderComplete.this, Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		// super.onBackPressed();
	}

	private void init() {
		// TODO Auto-generated method stub
		ActionBar ab = getSupportActionBar();
		ab.setCustomView(R.layout.something_abt_you);
		ab.setDisplayShowCustomEnabled(true);

		ab.setIcon(new ColorDrawable(Color.TRANSPARENT));
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.app_black)));

		TextView title = (TextView) findViewById(R.id.txtHeaderTitle);
		title.setText("Order Confirmation!");
		title.setGravity(Gravity.CENTER);

		View relCart = findViewById(R.id.relCart);
		relCart.setVisibility(View.GONE);

		ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
		imgShare.setVisibility(View.GONE);
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utils.getInstance().share(OrderComplete.this);
			}
		});
		TextView txtOrderId = (TextView) findViewById(R.id.txtConfirmationNumber);
		String orderId = getIntent().getStringExtra(ORDER_ID);
		txtOrderId.setText(orderId);

		View takePicGroup = findViewById(R.id.takePicGroup);
		takePicGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// LoadContactsAyscn lca = new LoadContactsAyscn();
				// lca.execute();

				final Dialog dialog = new Dialog(OrderComplete.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.camera);
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);

				Button cameraa = (Button) dialog.findViewById(R.id.carmeraBtn);
				Button gallery = (Button) dialog.findViewById(R.id.galleryBtn);
				Button continueBtn = (Button) dialog
						.findViewById(R.id.continueBtn);

				cameraa.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent cameraIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, CAMERA_REQUEST);
						dialog.cancel();
					}

				});
				gallery.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								RESULT_LOAD_IMAGE);
						dialog.cancel();
					}

				});
				continueBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Bitmap largeIcon = BitmapFactory.decodeResource(
								getResources(), R.drawable.logo);
						Intent intent = new Intent(OrderComplete.this,
								FinalActivity.class);
						FinalActivity.bitmap = largeIcon;
						intent.putExtra(FinalActivity.SHARING_BITMAP_URL,
								getImageUri(largeIcon));
						startActivity(intent);
						dialog.cancel();
					}
				});
				dialog.show();

			}
		});

		Button inviteFriendsBtn = (Button) findViewById(R.id.inviteFriendsBtn);
		inviteFriendsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(OrderComplete.this,
						InviteParty.class);
				intent.putExtra(InviteParty.IS_INVITE, true);
				startActivity(intent);
			}
		});

		Button havingPartyBtn = (Button) findViewById(R.id.havingPartyBtn);
		havingPartyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(OrderComplete.this,
						InviteParty.class);
				intent.putExtra(InviteParty.IS_INVITE, false);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImageUri = data.getData();
			String selectedImagePath = getPath(selectedImageUri);
			Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
			Intent i = new Intent(getApplicationContext(), FinalActivity.class);
			FinalActivity.bitmap = bitmap;
			i.putExtra(FinalActivity.SHARING_BITMAP_URL, selectedImageUri);
			startActivity(i);

		} else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");

			Intent intent = new Intent(getApplicationContext(),
					FinalActivity.class);
			FinalActivity.bitmap = photo;
			intent.putExtra(FinalActivity.SHARING_BITMAP_URL,
					getImageUri(photo));
			startActivity(intent);

		}

	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public Uri getImageUri(Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media
				.insertImage(OrderComplete.this.getContentResolver(), inImage,
						"Title", null);
		return Uri.parse(path);
	}

	// private Uri getImageUri(Context inContext, Bitmap inImage) {
	// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	// inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	// String path = Images.Media.insertImage(inContext.getContentResolver(),
	// inImage, "Title", null);
	// return Uri.parse(path);
	// }
	//
	// private String getRealPathFromURI(Uri uri) {
	// Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	// cursor.moveToFirst();
	// int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
	// return cursor.getString(idx);
	// }

	class AppAdapter extends ArrayAdapter<ResolveInfo> {
		private PackageManager pm = null;

		AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
			super(OrderComplete.this, R.layout.row, apps);
			this.pm = pm;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = newView(parent);
			}

			bindView(position, convertView);

			return (convertView);
		}

		private View newView(ViewGroup parent) {
			return (getLayoutInflater().inflate(R.layout.row, parent, false));
		}

		private void bindView(int position, View row) {
			TextView label = (TextView) row.findViewById(R.id.label);

			label.setText(getItem(position).loadLabel(pm));

			ImageView icon = (ImageView) row.findViewById(R.id.icon);

			icon.setImageDrawable(getItem(position).loadIcon(pm));
		}
	}

}
