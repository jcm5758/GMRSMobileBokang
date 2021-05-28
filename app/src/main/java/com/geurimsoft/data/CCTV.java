package com.geurimsoft.data;
/**
 *     <cctv>
		<name><![CDATA[1번카메라]]></name>        
        <cctvurl>rtsp://116.124.245.42:1935/live/ipcamera01.stream</cctvurl>
    </cctv>
 * @author mac
 *
 */
public class CCTV {

	private String cctvName;
	private String cctvUrl;
	public String getCctvName() {
		return cctvName;
	}
	public void setCctvName(String cctvName) {
		this.cctvName = cctvName;
	}
	public String getCctvUrl() {
		return cctvUrl;
	}
	public void setCctvUrl(String cctvUrl) {
		this.cctvUrl = cctvUrl;
	}
	
	
}
