package com.geurimsoft.grms.data;

public class ReleaseData {
	
	private String account;
	private double release_25mm;
	private double release_19mm;
	private double release_13mm;
	private double release_sand;
	private double release_crushed_sand;
	
	public ReleaseData() {
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}


	public double getRelease_25mm() {
		return release_25mm;
	}

	public void setRelease_25mm(String release_25mm) {
		this.release_25mm = Double.parseDouble(release_25mm);
	}

	public double getRelease_19mm() {
		return release_19mm;
	}

	public void setRelease_19mm(String release_19mm) {
		this.release_19mm = Double.parseDouble(release_19mm);
	}

	public double getRelease_13mm() {
		return release_13mm;
	}

	public void setRelease_13mm(String release_13mm) {
		this.release_13mm = Double.parseDouble(release_13mm);
	}

	public double getRelease_sand() {
		return release_sand;
	}

	public void setRelease_sand(String release_sand) {
		this.release_sand = Double.parseDouble(release_sand);
	}

	public double getRelease_crushed_sand() {
		return release_crushed_sand;
	}

	public void setRelease_crushed_sand(String release_crushed_sand) {
		this.release_crushed_sand = Double.parseDouble(release_crushed_sand);
	}
	
	
}
