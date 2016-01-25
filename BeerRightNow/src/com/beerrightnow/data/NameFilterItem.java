package com.beerrightnow.data;

public class NameFilterItem implements Comparable<NameFilterItem> {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(NameFilterItem another) {
		// TODO Auto-generated method stub
		try {

			return getName().compareToIgnoreCase(another.getName());
		} catch (NullPointerException e) {

			e.printStackTrace();
			return 0;
		}
	}

}
