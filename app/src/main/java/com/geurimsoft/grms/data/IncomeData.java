package com.geurimsoft.grms.data;

public class IncomeData {

	private String account;
	private String place;
	private double gemstone_arm;
	private double gemstone_poong;
	private double gemstone_wara;
	
	public IncomeData() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public double getGemstone_arm() {
		return gemstone_arm;
	}

	public void setGemstone_arm(String gemstone_arm) {
		this.gemstone_arm = Double.parseDouble(gemstone_arm);
	}

	public double getGemstone_poong() {
		return gemstone_poong;
	}

	public void setGemstone_poong(String gemstone_poong) {
		this.gemstone_poong = Double.parseDouble(gemstone_poong);
	}

	public double getGemstone_wara() {
		return gemstone_wara;
	}

	public void setGemstone_wara(String gemstone_wara) {
		this.gemstone_wara = Double.parseDouble(gemstone_wara);
	}
	
	
	
}
