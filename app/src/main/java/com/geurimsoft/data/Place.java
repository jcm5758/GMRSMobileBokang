package com.geurimsoft.data;

import java.util.ArrayList;

/*
 * 
 *  <place>
		<name><![CDATA[A현장]]></name>        
        <cctvlistfile>cctvlist.xml</cctvlistfile>
    </place>
    
 * */
public class Place {
	private String placeName;
	private String cctvListFile;
	private ArrayList<CCTV> cctvList;
	
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getCctvListFile() {
		return cctvListFile;
	}
	public void setCctvListFile(String cctvListFile) {
		this.cctvListFile = cctvListFile;
	}
	public ArrayList<CCTV> getCctvList() {
		return cctvList;
	}
	public void setCctvList(ArrayList<CCTV> cctvList) {
		this.cctvList = cctvList;
	}
	
}
