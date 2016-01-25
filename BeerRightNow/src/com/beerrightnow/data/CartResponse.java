package com.beerrightnow.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CartResponse {

	private Liquor liquor;
	private Beer beer;
	
	private String notification;

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	// total 6 items
	// 0. Beer total
	// 1. Liquor total
	// 2. Beer tax
	// 3. Liquor tax
	// 4. Pickup fee
	// 5. Service charges
	private List<Summary> summary = new ArrayList<Summary>();

	@SerializedName("convenience_fee")
	private float convenienceFee;

	public Liquor getLiquor() {
		return liquor;
	}

	public void setLiquor(Liquor liquor) {
		this.liquor = liquor;
	}

	public Beer getBeer() {
		return beer;
	}

	public void setBeer(Beer beer) {
		this.beer = beer;
	}

	public List<Summary> getSummary() {
		return summary;
	}

	public void setSummary(List<Summary> summary) {
		this.summary = summary;
	}

	public float getConvenienceFee() {
		return convenienceFee;
	}

	public void setConvenienceFee(float convenienceFee) {
		this.convenienceFee = convenienceFee;
	}

	private UtilMethod utilMethod = new UtilMethod();

	public UtilMethod getUtilMethod() {
		return utilMethod;
	}

	public class UtilMethod {

		public int getStoreCount() {

			int storeCount = 0;
			try {

				if (!liquor.getProducts().isEmpty())
					storeCount++;

				if (!beer.getProducts().isEmpty())
					storeCount++;
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			return storeCount;
		}

		public int getProductCount() {

			int productCount = 0;
			try {

				productCount = liquor.getProducts().size()
						+ beer.getProducts().size();
			} catch (NullPointerException e) {

				e.printStackTrace();
			}

			return productCount;
		}

		// 0. Beer total
		// 1. Liquor total
		// 2. Beer tax
		// 3. Liquor tax
		// 4. Pickup fee
		// 5. Service charges
		public float getBeersPrice() {

			try {

				return summary.get(0).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getLiquorsPrice() {

			try {

				return summary.get(1).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getBeersTax() {

			try {

				return summary.get(2).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getLiquorsTax() {

			try {

				return summary.get(3).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getPickupFee() {

			try {

				return summary.get(4).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getServiceCharge() {

			try {

				return summary.get(5).getPrice();
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		public float getTotal() {

			return getBeersPrice() + getLiquorsPrice() + getBeersTax()
					+ getLiquorsTax() + getPickupFee() + getConvenienceFee();
		}

		public float getProductsTotal() {

			return getBeersPrice() + getLiquorsPrice();
		}
	}
}