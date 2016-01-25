package com.beerrightnow.data;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OrderedItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("orders_id")
	private String orderId;

	@SerializedName("date_purchased")
	private String purchasedDate;

	@SerializedName("delivery_name")
	private String deliveryName;

	@SerializedName("billing_name")
	private String billingName;

	@SerializedName("order_total")
	private String orderTotal;

	@SerializedName("orders_status_name")
	private String orderStatusName;

	@SerializedName("total_products")
	private String totalProducts;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(String purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	public String getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(String totalProducts) {
		this.totalProducts = totalProducts;
	}

}
