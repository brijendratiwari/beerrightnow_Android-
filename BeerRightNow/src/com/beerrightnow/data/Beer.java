package com.beerrightnow.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Beer {

	private String store;

	private List<CartProduct> products = new ArrayList<CartProduct>();

	@SerializedName("has_kegs")
	private boolean hasKegs;

	private float tax;

	@SerializedName("products_total")
	private float productsTotal;

	/**
	 * 
	 * @return The store
	 */
	public String getStore() {
		return store;
	}

	/**
	 * 
	 * @param store
	 *            The store
	 */
	public void setStore(String store) {
		this.store = store;
	}

	/**
	 * 
	 * @return The products
	 */
	public List<CartProduct> getProducts() {
		return products;
	}

	/**
	 * 
	 * @param products
	 *            The products
	 */
	public void setProducts(List<CartProduct> products) {
		this.products = products;
	}

	/**
	 * 
	 * @return The hasKegs
	 */
	public boolean getHasKegs() {
		return hasKegs;
	}

	/**
	 * 
	 * @param hasKegs
	 *            The has_kegs
	 */
	public void setHasKegs(boolean hasKegs) {
		this.hasKegs = hasKegs;
	}

	/**
	 * 
	 * @return The tax
	 */
	public float getTax() {
		return tax;
	}

	/**
	 * 
	 * @param tax
	 *            The tax
	 */
	public void setTax(float tax) {
		this.tax = tax;
	}

	/**
	 * 
	 * @return The productsTotal
	 */
	public float getProductsTotal() {
		return productsTotal;
	}

	/**
	 * 
	 * @param productsTotal
	 *            The products_total
	 */
	public void setProductsTotal(float productsTotal) {
		this.productsTotal = productsTotal;
	}

}