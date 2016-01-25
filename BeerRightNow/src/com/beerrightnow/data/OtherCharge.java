package com.beerrightnow.data;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OtherCharge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("options_values_price")
	private float optionsValuesPrice;

	@SerializedName("price_prefix")
	private String pricePrefix;

	@SerializedName("products_options_name")
	private String productsOptionsName;

	@SerializedName("products_options_values_name")
	private String productsOptionsValuesName;

	public float getOptionsValuesPrice() {
		return optionsValuesPrice;
	}

	public void setOptionsValuesPrice(float optionsValuesPrice) {
		this.optionsValuesPrice = optionsValuesPrice;
	}

	public String getPricePrefix() {
		return pricePrefix;
	}

	public void setPricePrefix(String pricePrefix) {
		this.pricePrefix = pricePrefix;
	}

	public String getProductsOptionsName() {
		return productsOptionsName;
	}

	public void setProductsOptionsName(String productsOptionsName) {
		this.productsOptionsName = productsOptionsName;
	}

	public String getProductsOptionsValuesName() {
		return productsOptionsValuesName;
	}

	public void setProductsOptionsValuesName(String productsOptionsValuesName) {
		this.productsOptionsValuesName = productsOptionsValuesName;
	}

}
