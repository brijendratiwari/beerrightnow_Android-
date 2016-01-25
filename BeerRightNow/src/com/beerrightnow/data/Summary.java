package com.beerrightnow.data;

public class Summary {

	private String title;

	private String operator;

	private float price;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public float getPrice() {

		try {

			if (operator.charAt(0) == '-')
				return price * (-1);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}