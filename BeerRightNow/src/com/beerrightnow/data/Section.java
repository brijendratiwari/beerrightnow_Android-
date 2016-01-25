package com.beerrightnow.data;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Section implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4599604562133961424L;
	@SerializedName("sectionid")
	private String sectionId;

	@SerializedName("name")
	private String name;

	@SerializedName("image")
	private String image;

	@SerializedName("lrn")
	private String lrn;

	@SerializedName("brn")
	private String brn;

	@SerializedName("large_image")
	private String imageLarge;

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLrn() {
		return lrn;
	}

	public void setLrn(String lrn) {
		this.lrn = lrn;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getImageLarge() {
		return imageLarge;
	}

	public void setImageLarge(String imageLarge) {
		this.imageLarge = imageLarge;
	}

}