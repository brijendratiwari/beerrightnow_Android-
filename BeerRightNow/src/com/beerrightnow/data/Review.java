package com.beerrightnow.data;

import com.google.gson.annotations.SerializedName;

public class Review {

	@SerializedName("reviews_id")
	private String reviewsId;

	@SerializedName("products_id")
	private String productsId;

	@SerializedName("customers_id")
	private String customersId;

	@SerializedName("customers_name")
	private String customersName;

	@SerializedName("reviews_rating")
	private String reviewsRating;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("last_modified")
	private Object lastModified;

	@SerializedName("reviews_read")
	private String reviewsRead;

	private String approved;

	private String review;

	/**
	 * 
	 * @return The reviewsId
	 */
	public String getReviewsId() {
		return reviewsId;
	}

	/**
	 * 
	 * @param reviewsId
	 *            The reviews_id
	 */
	public void setReviewsId(String reviewsId) {
		this.reviewsId = reviewsId;
	}

	/**
	 * 
	 * @return The productsId
	 */
	public String getProductsId() {
		return productsId;
	}

	/**
	 * 
	 * @param productsId
	 *            The products_id
	 */
	public void setProductsId(String productsId) {
		this.productsId = productsId;
	}

	/**
	 * 
	 * @return The customersId
	 */
	public String getCustomersId() {
		return customersId;
	}

	/**
	 * 
	 * @param customersId
	 *            The customers_id
	 */
	public void setCustomersId(String customersId) {
		this.customersId = customersId;
	}

	/**
	 * 
	 * @return The customersName
	 */
	public String getCustomersName() {
		return customersName;
	}

	/**
	 * 
	 * @param customersName
	 *            The customers_name
	 */
	public void setCustomersName(String customersName) {
		this.customersName = customersName;
	}

	/**
	 * 
	 * @return The reviewsRating
	 */
	public String getReviewsRating() {
		return reviewsRating;
	}

	/**
	 * 
	 * @param reviewsRating
	 *            The reviews_rating
	 */
	public void setReviewsRating(String reviewsRating) {
		this.reviewsRating = reviewsRating;
	}

	/**
	 * 
	 * @return The dateAdded
	 */
	public String getDateAdded() {
		return dateAdded;
	}

	/**
	 * 
	 * @param dateAdded
	 *            The date_added
	 */
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * 
	 * @return The lastModified
	 */
	public Object getLastModified() {
		return lastModified;
	}

	/**
	 * 
	 * @param lastModified
	 *            The last_modified
	 */
	public void setLastModified(Object lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * 
	 * @return The reviewsRead
	 */
	public String getReviewsRead() {
		return reviewsRead;
	}

	/**
	 * 
	 * @param reviewsRead
	 *            The reviews_read
	 */
	public void setReviewsRead(String reviewsRead) {
		this.reviewsRead = reviewsRead;
	}

	/**
	 * 
	 * @return The approved
	 */
	public String getApproved() {
		return approved;
	}

	/**
	 * 
	 * @param approved
	 *            The approved
	 */
	public void setApproved(String approved) {
		this.approved = approved;
	}

	/**
	 * 
	 * @return The review
	 */
	public String getReview() {
		return review;
	}

	/**
	 * 
	 * @param review
	 *            The review
	 */
	public void setReview(String review) {
		this.review = review;
	}

}
