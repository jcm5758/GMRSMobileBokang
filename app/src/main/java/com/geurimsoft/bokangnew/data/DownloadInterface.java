package com.geurimsoft.bokangnew.data;

import java.util.ArrayList;

public interface DownloadInterface {
	public void downloadComplete(boolean result, String resultMesg, ArrayList resultList, int contentType);
}
