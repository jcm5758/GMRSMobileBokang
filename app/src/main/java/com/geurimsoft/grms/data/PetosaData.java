package com.geurimsoft.grms.data;

public class PetosaData {
	private String account;
	private double petosa;
	
	public PetosaData() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getPetosa() {
		return petosa;
	}

	public void setPetosa(String petosa) {
		this.petosa = Double.parseDouble(petosa);
	}
	
	
}
