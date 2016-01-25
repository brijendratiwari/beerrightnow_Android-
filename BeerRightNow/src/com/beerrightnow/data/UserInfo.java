package com.beerrightnow.data;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

	@SerializedName("customers_id")
	private String customersId;

	@SerializedName("purchased_without_account")
	private String purchasedWithoutAccount;

	@SerializedName("customers_gender")
	private String customersGender;

	@SerializedName("customers_firstname")
	private String customersFirstname;

	@SerializedName("customers_lastname")
	private String customersLastname;

	// 0000-00-00 00:00:00

	@SerializedName("customers_dob")
	private String customersDob;

	@SerializedName("customers_email_address")
	private String customersEmailAddress;

	@SerializedName("customers_default_address_id")
	private String customersDefaultAddressId;

	@SerializedName("customers_telephone")
	private String customersTelephone;

	@SerializedName("customers_fax")
	private String customersFax;

	@SerializedName("customers_newsletter")
	private String customersNewsletter;

	@SerializedName("customers_group_name")
	private String customersGroupName;

	@SerializedName("customers_group_id")
	private String customersGroupId;

	@SerializedName("customers_group_ra")
	private String customersGroupRa;

	@SerializedName("customers_payment_allowed")
	private String customersPaymentAllowed;

	@SerializedName("customers_shipment_allowed")
	private String customersShipmentAllowed;

	@SerializedName("admin_recover")
	private String adminRecover;

	public String getCustomersId() {
		return customersId;
	}

	public void setCustomersId(String customersId) {
		this.customersId = customersId;
	}

	public String getPurchasedWithoutAccount() {
		return purchasedWithoutAccount;
	}

	public void setPurchasedWithoutAccount(String purchasedWithoutAccount) {
		this.purchasedWithoutAccount = purchasedWithoutAccount;
	}

	public String getCustomersGender() {
		return customersGender;
	}

	public void setCustomersGender(String customersGender) {
		this.customersGender = customersGender;
	}

	public String getCustomersFirstname() {
		return customersFirstname;
	}

	public void setCustomersFirstname(String customersFirstname) {
		this.customersFirstname = customersFirstname;
	}

	public String getCustomersLastname() {
		return customersLastname;
	}

	public void setCustomersLastname(String customersLastname) {
		this.customersLastname = customersLastname;
	}

	public String getCustomersDob() {
		return customersDob;
	}

	public void setCustomersDob(String customersDob) {
		this.customersDob = customersDob;
	}

	public String getCustomersEmailAddress() {
		return customersEmailAddress;
	}

	public void setCustomersEmailAddress(String customersEmailAddress) {
		this.customersEmailAddress = customersEmailAddress;
	}

	public String getCustomersDefaultAddressId() {
		return customersDefaultAddressId;
	}

	public void setCustomersDefaultAddressId(String customersDefaultAddressId) {
		this.customersDefaultAddressId = customersDefaultAddressId;
	}

	public String getCustomersTelephone() {
		return customersTelephone;
	}

	public void setCustomersTelephone(String customersTelephone) {
		this.customersTelephone = customersTelephone;
	}

	public String getCustomersFax() {
		return customersFax;
	}

	public void setCustomersFax(String customersFax) {
		this.customersFax = customersFax;
	}

	public String getCustomersNewsletter() {
		return customersNewsletter;
	}

	public void setCustomersNewsletter(String customersNewsletter) {
		this.customersNewsletter = customersNewsletter;
	}

	public String getCustomersGroupName() {
		return customersGroupName;
	}

	public void setCustomersGroupName(String customersGroupName) {
		this.customersGroupName = customersGroupName;
	}

	public String getCustomersGroupId() {
		return customersGroupId;
	}

	public void setCustomersGroupId(String customersGroupId) {
		this.customersGroupId = customersGroupId;
	}

	public String getCustomersGroupRa() {
		return customersGroupRa;
	}

	public void setCustomersGroupRa(String customersGroupRa) {
		this.customersGroupRa = customersGroupRa;
	}

	public String getCustomersPaymentAllowed() {
		return customersPaymentAllowed;
	}

	public void setCustomersPaymentAllowed(String customersPaymentAllowed) {
		this.customersPaymentAllowed = customersPaymentAllowed;
	}

	public String getCustomersShipmentAllowed() {
		return customersShipmentAllowed;
	}

	public void setCustomersShipmentAllowed(String customersShipmentAllowed) {
		this.customersShipmentAllowed = customersShipmentAllowed;
	}

	public String getAdminRecover() {
		return adminRecover;
	}

	public void setAdminRecover(String adminRecover) {
		this.adminRecover = adminRecover;
	}

	public String getCustomerName() {

		return customersFirstname + " " + customersLastname;
	}

}
