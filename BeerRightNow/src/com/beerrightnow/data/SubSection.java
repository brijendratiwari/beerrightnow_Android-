package com.beerrightnow.data;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SubSection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7720976992639135917L;
	@SerializedName("type_id")
	private String typeId;

	@SerializedName("type_name")
	private String typeName;

	@SerializedName("sort_order")
	private String sortOrder;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
