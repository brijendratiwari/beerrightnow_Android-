package com.beerrightnow.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CartProduct {

	@SerializedName("products_id")
	private String productsId;

	@SerializedName("color_name")
	private String colorName;

	@SerializedName("type_id")
	private String typeId;

	@SerializedName("products_model")
	private String productsModel;

	@SerializedName("products_image")
	private String productsImage;

	@SerializedName("products_image2")
	private String productsImage2;

	@SerializedName("products_date_added")
	private String productsDateAdded;

	@SerializedName("products_last_modified")
	private String productsLastModified;

	@SerializedName("products_date_available")
	private String productsDateAvailable;

	@SerializedName("products_weight")
	private String productsWeight;

	@SerializedName("products_status")
	private String productsStatus;

	@SerializedName("products_tax_class_id")
	private String productsTaxClassId;

	@SerializedName("manufacturers_id")
	private String manufacturersId;

	@SerializedName("brand_id")
	private String brandId;

	@SerializedName("products_ordered")
	private String productsOrdered;

	@SerializedName("products_ship_price")
	private String productsShipPrice;

	@SerializedName("products_length")
	private String productsLength;

	@SerializedName("products_width")
	private String productsWidth;

	@SerializedName("products_height")
	private String productsHeight;

	@SerializedName("products_ready_to_ship")
	private String productsReadyToShip;

	@SerializedName("style_id")
	private String styleId;

	@SerializedName("origin_id")
	private String originId;

	@SerializedName("alcholol_percentage")
	private String alchololPercentage;

	@SerializedName("region_origin")
	private String regionOrigin;

	private String isDelivery;

	@SerializedName("producer_id")
	private String producerId;

	@SerializedName("style_type_name")
	private String styleTypeName;

	@SerializedName("vareity_name")
	private String vareityName;

	@SerializedName("vintage_year")
	private String vintageYear;

	@SerializedName("size_name")
	private String sizeName;

	private String organic;

	private String kosher;

	private String prefecture;

	private String polishing;

	@SerializedName("products_name")
	private String productsName;

	@SerializedName("product_count")
	private String productCount;

	@SerializedName("products_price")
	private String productsPrice;

	@SerializedName("special_price")
	private String specialPrice;

	private String tax;

	// 0: Refundable Deposit
	// 1: Other Charges
	@SerializedName("other_charges")
	private List<OtherCharge> otherCharges = new ArrayList<OtherCharge>();

	public String getProductsId() {
		return productsId;
	}

	public void setProductsId(String productsId) {
		this.productsId = productsId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getProductsModel() {
		return productsModel;
	}

	public void setProductsModel(String productsModel) {
		this.productsModel = productsModel;
	}

	public String getProductsImage() {
		return productsImage;
	}

	public void setProductsImage(String productsImage) {
		this.productsImage = productsImage;
	}

	public String getProductsImage2() {
		return productsImage2;
	}

	public void setProductsImage2(String productsImage2) {
		this.productsImage2 = productsImage2;
	}

	public String getProductsDateAdded() {
		return productsDateAdded;
	}

	public void setProductsDateAdded(String productsDateAdded) {
		this.productsDateAdded = productsDateAdded;
	}

	public String getProductsLastModified() {
		return productsLastModified;
	}

	public void setProductsLastModified(String productsLastModified) {
		this.productsLastModified = productsLastModified;
	}

	public String getProductsDateAvailable() {
		return productsDateAvailable;
	}

	public void setProductsDateAvailable(String productsDateAvailable) {
		this.productsDateAvailable = productsDateAvailable;
	}

	public String getProductsWeight() {
		return productsWeight;
	}

	public void setProductsWeight(String productsWeight) {
		this.productsWeight = productsWeight;
	}

	public String getProductsStatus() {
		return productsStatus;
	}

	public void setProductsStatus(String productsStatus) {
		this.productsStatus = productsStatus;
	}

	public String getProductsTaxClassId() {
		return productsTaxClassId;
	}

	public void setProductsTaxClassId(String productsTaxClassId) {
		this.productsTaxClassId = productsTaxClassId;
	}

	public String getManufacturersId() {
		return manufacturersId;
	}

	public void setManufacturersId(String manufacturersId) {
		this.manufacturersId = manufacturersId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getProductsOrdered() {
		return productsOrdered;
	}

	public void setProductsOrdered(String productsOrdered) {
		this.productsOrdered = productsOrdered;
	}

	public String getProductsShipPrice() {
		return productsShipPrice;
	}

	public void setProductsShipPrice(String productsShipPrice) {
		this.productsShipPrice = productsShipPrice;
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

	public String getProductsReadyToShip() {
		return productsReadyToShip;
	}

	public void setProductsReadyToShip(String productsReadyToShip) {
		this.productsReadyToShip = productsReadyToShip;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getAlchololPercentage() {
		return alchololPercentage;
	}

	public void setAlchololPercentage(String alchololPercentage) {
		this.alchololPercentage = alchololPercentage;
	}

	public String getRegionOrigin() {
		return regionOrigin;
	}

	public void setRegionOrigin(String regionOrigin) {
		this.regionOrigin = regionOrigin;
	}

	public String getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(String isDelivery) {
		this.isDelivery = isDelivery;
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	public String getStyleTypeName() {
		return styleTypeName;
	}

	public void setStyleTypeName(String styleTypeName) {
		this.styleTypeName = styleTypeName;
	}

	public String getVareityName() {
		return vareityName;
	}

	public void setVareityName(String vareityName) {
		this.vareityName = vareityName;
	}

	public String getVintageYear() {
		return vintageYear;
	}

	public void setVintageYear(String vintageYear) {
		this.vintageYear = vintageYear;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public String getOrganic() {
		return organic;
	}

	public void setOrganic(String organic) {
		this.organic = organic;
	}

	public String getKosher() {
		return kosher;
	}

	public void setKosher(String kosher) {
		this.kosher = kosher;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String prefecture) {
		this.prefecture = prefecture;
	}

	public String getPolishing() {
		return polishing;
	}

	public void setPolishing(String polishing) {
		this.polishing = polishing;
	}

	public String getProductsName() {
		return productsName;
	}

	public void setProductsName(String productsName) {
		this.productsName = productsName;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getProductsPrice() {
		return productsPrice;
	}

	public void setProductsPrice(String productsPrice) {
		this.productsPrice = productsPrice;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public List<OtherCharge> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<OtherCharge> otherCharges) {
		this.otherCharges = otherCharges;
	}

}