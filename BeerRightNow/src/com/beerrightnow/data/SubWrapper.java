package com.beerrightnow.data;

import java.io.Serializable;
import java.util.List;

public class SubWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342221088466649308L;

	private List<SubSection> subSections;
	private Section section;

	public List<SubSection> getSubSections() {
		return subSections;
	}

	public void setSubSections(List<SubSection> subSections) {
		this.subSections = subSections;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

}
