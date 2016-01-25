package com.beerrightnow.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beerrightnow.data.Review;
import com.beerrightnow.jon.R;

public class ReviewAdapter extends ArrayAdapter<Review> {

	public ReviewAdapter(Context context, List<Review> reviews) {
		super(context, 0, reviews);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		Review review = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.row_rating, null, true);
			holder = new ViewHolder();
			holder.txtReviewCity = (TextView) view
					.findViewById(R.id.txtReviewCity);
			holder.txtReviewComment = (TextView) view
					.findViewById(R.id.txtReviewComment);
			holder.txtReviewName = (TextView) view
					.findViewById(R.id.txtReviewName);
			holder.ivUserImage = (ImageView) view
					.findViewById(R.id.ivAvtar_rating);
			holder.ratingReview = (RatingBar) view
					.findViewById(R.id.ratBarReview);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.txtReviewCity.setText("");
		holder.txtReviewComment.setText(review.getReview());
		holder.txtReviewName.setText(review.getCustomersName());
		// holder.ratingReview.setProgress(Float.parseInt(arrReviews.get(position)
		// .getReviewsRating()));

		return view;
	}

	static class ViewHolder {
		TextView txtReviewName, txtReviewCity, txtReviewComment;
		ImageView ivUserImage;
		RatingBar ratingReview;
	}

}
