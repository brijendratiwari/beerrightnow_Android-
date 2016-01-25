package com.beerrightnow.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8825120389075529053L;

	@SerializedName("star_rating")
	private float rating;

	@SerializedName("products_name")
	private String productsName;

	@SerializedName("products_length")
	private String productsLength;

	@SerializedName("products_width")
	private String productsWidth;

	@SerializedName("products_height")
	private String productsHeight;

	@SerializedName("products_description")
	private String productsDescription;

	@SerializedName("products_id")
	private String productsId;

	@SerializedName("products_quantity")
	private String productsQuantity;

	@SerializedName("products_price")
	private String productsPrice;

	@SerializedName("products_image")
	private String productsImage;

	@SerializedName("products_ship_price")
	private String productsShipPrice;

	@SerializedName("products_image_large")
	private String productsImageLarge;

	@SerializedName("alcohol_percentage")
	private String alcoholPercentage;

	@SerializedName("origin_name")
	private String originName;

	@SerializedName("style_name")
	private String styleName;

	// 0: Refundable Deposit
	// 1: Other Charges
	@SerializedName("other_charges")
	private List<OtherCharge> otherCharges = new ArrayList<OtherCharge>();

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getProductsName() {
		return productsName;
	}

	public void setProductsName(String productsName) {
		this.productsName = productsName;
	}

	public String getProductsLength() {
		return productsLength;
	}

	public void setProductsLength(String productsLength) {
		this.productsLength = productsLength;
	}

	public String getProductsWidth() {
		return productsWidth;
	}

	public void setProductsWidth(String productsWidth) {
		this.productsWidth = productsWidth;
	}

	public String getProductsHeight() {
		return productsHeight;
	}

	public void setProductsHeight(String productsHeight) {
		this.productsHeight = productsHeight;
	}

	public String getProductsDescription() {
		return productsDescription;
	}

	public void setProductsDescription(String productsDescription) {
		this.productsDescription = productsDescription;
	}

	public String getProductsId() {
		return productsId;
	}

	public void setProductsId(String productsId) {
		this.productsId = productsId;
	}

	public String getProductsQuantity() {
		return productsQuantity;
	}

	public void setProductsQuantity(String productsQuantity) {
		this.productsQuantity = productsQuantity;
	}

	public String getProductsPrice() {
		return productsPrice;
	}

	public void setProductsPrice(String productsPrice) {
		this.productsPrice = productsPrice;
	}

	public String getProductsImage() {
		return productsImage;
	}

	public void setProductsImage(String productsImage) {
		this.productsImage = productsImage;
	}

	public String getProductsShipPrice() {
		return productsShipPrice;
	}

	public void setProductsShipPrice(String productsShipPrice) {
		this.productsShipPrice = productsShipPrice;
	}

	public String getProductsImageLarge() {
		return productsImageLarge;
	}

	public void setProductsImageLarge(String productsImageLarge) {
		this.productsImageLarge = productsImageLarge;
	}

	public String getAlcoholPercentage() {
		return alcoholPercentage;
	}

	public void setAlcoholPercentage(String alcoholPercentage) {
		this.alcoholPercentage = alcoholPercentage;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public List<OtherCharge> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<OtherCharge> otherCharges) {
		this.otherCharges = otherCharges;
	}

}
