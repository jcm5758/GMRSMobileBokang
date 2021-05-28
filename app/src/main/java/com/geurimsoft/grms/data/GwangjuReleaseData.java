package com.geurimsoft.grms.data;

public class GwangjuReleaseData {
	
	private String account;
	private double release_25mm;
	private double release_gangsa;
	private double release_crush;
	private double release_honhab;
	private double release_13mm;
	
	public GwangjuReleaseData() {
		
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

	public double getRelease_crush() {
		return release_crush;
	}

	public void setRelease_crush(String release_crush) {
		this.release_crush = Double.parseDouble(release_crush);
	}

	public double getRelease_gangsa() {
		return release_gangsa;
	}

	public void setRelease_gangsa(String release_gangsa) {
		this.release_gangsa = Double.parseDouble(release_gangsa);
	}

	public double getRelease_honhab() {
		return release_honhab;
	}

	public void setRelease_honhab(String release_honhab) {
		this.release_honhab = Double.parseDouble(release_honhab);
	}

	public double getRelease_13mm() {
		return release_13mm;
	}

	public void setRelease_13mm(String release_13mm) {
		this.release_13mm = Double.parseDouble(release_13mm);
	}

}
