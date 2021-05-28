package com.geurimsoft.bokangnew.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.geurimsoft.bokangnew.conf.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

public class XmlConverter 
{

	/**
	 * 로그인 정보 파싱
	 * @param json data
	 * @return GSUser
	 */
	public static GSUser parseUserInfo(String json)
	{

		try
		{

			if (json == null || json.equals(""))
			{
				Log.d(GSConfig.APP_DEBUG, "XmlConverter.parseUserInfo() : Returned JSON is null");
				return null;
			}

//			Log.d(GSConfig.APP_DEBUG, json);

			GSUser user = new GSUser();
			List<GSBranch> branchList = new ArrayList();

			JSONObject jsonObject = new JSONObject(json);
			user.setID(jsonObject.getInt("id"));
			user.setName(jsonObject.getString("name"));

			JSONArray listArray = jsonObject.getJSONArray("branchList");

			for(int i = 0 ; i < listArray.length() ; i++)
			{

				JSONObject listObject = listArray.getJSONObject(i);

				GSBranch branch = new GSBranch();

				branch.setBranchID(listObject.getInt("branch_id"));
				branch.setBranchName(listObject.getString("name"));
				branch.setDbName(listObject.getInt("db_name"));
				branch.setBranchType(listObject.getInt("branch_type"));
				branch.setAddMode_type(listObject.getInt("addMode_type"));
				branch.setAuth_day(new int[]{ 1, 1, 1, 1 });
				branch.setAuth_month(new int[]{ 1, 1, 1});
				branch.setAuth_year(new int[]{ 1 });

				branchList.add(branch);

			}

//			Log.d(GSConfig.APP_DEBUG, "Size of branchList : " + branchList.size());

			user.setBranchList(branchList);

			return user;

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, "XmlConverter.parseUserInfo() : " + ex.toString());
			return null;
		}
		
	}

	/**
	 * 일일 입고/출고/토사 수량 파싱
	 * @param xml
	 * @return
	 */
	public static GSDailyInOut parseDaily(String xml)
	{

		GSDailyInOut dio = new GSDailyInOut();

		if (xml == null || xml.equals(""))
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + XmlConverter.class.getName() + " : parseDaily() : XML is null.");
			return null;
		}

//		Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + XmlConverter.class.getName() + " : parseDaily() : XML : " + xml);

		Gson gson = new Gson();

		GSDailyInOutGroup[] diog = gson.fromJson(xml, GSDailyInOutGroup[].class);

		if (diog == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + XmlConverter.class.getName() + " : parseDaily() : dio is null.");
			return null;
		}

//		Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + XmlConverter.class.getName() + " : parseDaily() : Length of dio : " + diog.length);

		dio.list = new ArrayList<>(Arrays.asList(diog));

		return dio;

	}

	/**
	 * 월별 입고/출고/토사 수량 파싱
	 * @param xml
	 * @return
	 */
	public static GSMonthInOut parseMonth(String xml)
	{

//		Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + XmlConverter.class.getName() + " : parseMonth() is called.");

		if (xml == null || xml.equals(""))
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + XmlConverter.class.getName() + " : parseMonth() : XML is null.");
			return null;
		}

//		Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + XmlConverter.class.getName() + " : parseMonth() : XML : " + xml);

		Gson gson = new Gson();

		GSMonthInOut data = gson.fromJson(xml, GSMonthInOut.class);

		if (data == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + XmlConverter.class.getName() + " : parseMonth() : dio is null.");
			return null;
		}

		return data;

	}


	public static StatsListData parseStatsListInfo(String xml)
	{
		
		StatsListData statsListData = new StatsListData();
		
		if (xml == null || xml.equals(""))
		{
			return null;
		}	
		
		String[] temp = xml.split("[|]");
		
		// 헤더 정보 =======================================================
		int header_count = Integer.parseInt(temp[0]);  		
		String[] header_titles = new String[header_count];
		
		int index = 0;
		int header_titles_index = 0;
		for(index = 1; index < header_count + 1; index++) {
			header_titles[header_titles_index] = temp[index];
			header_titles_index++;
		}
		
		statsListData.setHeader_count(header_count);
		statsListData.setHeader_titles(header_titles);
		
		//입고 정보===========================================================
		int item_row_count = Integer.parseInt(temp[index]);
		statsListData.setItem_count(item_row_count);
		
		ArrayList<String[]> item_list = new ArrayList<String[]>();
		
		int item_count = 0;
		int item_index = 0;	
		
		index = index + 1;
		
		for(int i = 0; i < item_row_count; i++) {
			
			String[] items = new String[header_count];
			
			for(item_index = index; item_index < header_count + 2 + (header_count * (i+1)); item_index++) {
				
				items[item_count] = temp[item_index];
				item_count++;
			}
			
			index = item_index;
			if(i < item_row_count - 1)
				item_list.add(items);
			else
				statsListData.setTotal_items(items);
			
			item_count = 0;
		}
		statsListData.setItems_list(item_list);
		
		return statsListData;
	}
	
	public static StatsTotalData parseStatsTotalInfo(String xml) {
		
		StatsTotalData statsTotalData = new StatsTotalData();
		
		if (xml == null || xml.equals(""))
		{
			return null;
		}	
		
		String[] temp = xml.split("[|]");
		
		// 입고 헤더 정보 =======================================================
		int stock_header_count = Integer.parseInt(temp[0]);  		
		String[] stock_header_titles = new String[stock_header_count];
		
		int index = 0;
		int stock_header_titles_index = 0;
		for(index = 1; index < stock_header_count + 1; index++) {
			stock_header_titles[stock_header_titles_index] = temp[index];
			stock_header_titles_index++;
		}
		
		statsTotalData.setStock_header_count(stock_header_count);
		statsTotalData.setStock_header_titles(stock_header_titles);
		
		//입고 정보===========================================================
		int stock_count = Integer.parseInt(temp[index]);
		statsTotalData.setStock_count(stock_count);
		
		ArrayList<String[]> stock_list = new ArrayList<String[]>();
		
		int stock_item_count = 0;
		int stock_item_index = 0;	
		
		index = index + 1;
		
		for(int i = 0; i < stock_count; i++) {
			
			String[] items = new String[stock_header_count];
			
			for(stock_item_index = index; stock_item_index < stock_header_count + 2 + (stock_header_count * (i+1)); stock_item_index++) {
				
				items[stock_item_count] = temp[stock_item_index];
				stock_item_count++;
			}
			
			index = stock_item_index;
			if(i < stock_count - 1)
				stock_list.add(items);
			else
				statsTotalData.setStock_total_items(items);
			
			stock_item_count = 0;
		}
		statsTotalData.setStock_list(stock_list);
		statsTotalData.setStock_total(temp[index]);
		
		index = index + 1;
		statsTotalData.setStock_several(temp[index]);
		
		index = index + 1;
		
		//용인 출고 헤더 정보=============================================================
		int release_yi_header_count = Integer.parseInt(temp[index]);  		
		String[] release_yi_header_titles = new String[release_yi_header_count];
		
		index = index + 1;
		int release_yi_index = index + release_yi_header_count;
		
		int release_yi_header_titles_index = 0;
		for(; index < release_yi_index; index++) {
			release_yi_header_titles[release_yi_header_titles_index] = temp[index];
			release_yi_header_titles_index++;
		}
		
		statsTotalData.setRelease_yi_header_count(release_yi_header_count);
		statsTotalData.setRelease_yi_header_titles(release_yi_header_titles);
		
		//용인 출고  정보=============================================================
		int release_yi_count = Integer.parseInt(temp[index]);
		statsTotalData.setRelease_yi_count(release_yi_count);
		
		ArrayList<String[]> release_yi_list = new ArrayList<String[]>();
		
		int release_yi_item_count = 0;
		int release_yi_item_index = 0;	
				
		index = index + 1;
		release_yi_index = release_yi_index + 1;
		
		for(int i = 0; i < release_yi_count; i++) {
			
			String[] release_yi_item = new String[release_yi_header_count];
			
			for(release_yi_item_index = index; release_yi_item_index < release_yi_index + (release_yi_header_count * (i+1)); release_yi_item_index++) {
				release_yi_item[release_yi_item_count] = temp[release_yi_item_index];
				release_yi_item_count++;
			}
					
			index = release_yi_item_index;
			release_yi_list.add(release_yi_item);
					
			release_yi_item_count = 0;
		}
		statsTotalData.setRelease_yi_list(release_yi_list);
		
		
		//광주 출고 헤더 정보=============================================================
		int release_gj_header_count = Integer.parseInt(temp[index]);  		
		String[] release_gj_header_titles = new String[release_gj_header_count];
		
		index = index + 1;
		int release_gj_index = index + release_gj_header_count;
		
		int release_gj_header_titles_index = 0;
		for(; index < release_gj_index; index++) {
			release_gj_header_titles[release_gj_header_titles_index] = temp[index];
			release_gj_header_titles_index++;
		}
		
		statsTotalData.setRelease_gj_header_count(release_gj_header_count);
		statsTotalData.setRelease_gj_header_titles(release_gj_header_titles);
		
		//광주 출고  정보=============================================================
		int release_gj_count = Integer.parseInt(temp[index]);
		statsTotalData.setRelease_gj_count(release_gj_count);
		
		ArrayList<String[]> release_gj_list = new ArrayList<String[]>();
		
		int release_gj_item_count = 0;
		int release_gj_item_index = 0;	
				
		index = index + 1;
		release_gj_index = release_gj_index + 1;
		
		for(int i = 0; i < release_gj_count; i++) {
			
			String[] release_gj_item = new String[release_gj_header_count];
			
			for(release_gj_item_index = index; release_gj_item_index < release_gj_index + (release_gj_header_count * (i+1)); release_gj_item_index++) {
				release_gj_item[release_gj_item_count] = temp[release_gj_item_index];
				release_gj_item_count++;
			}
					
			index = release_gj_item_index;
			release_gj_list.add(release_gj_item);
					
			release_gj_item_count = 0;
		}
		statsTotalData.setRelease_gj_list(release_gj_list);
		
		//출고 합계  정보=============================================================
		statsTotalData.setRelease_total(temp[index]);
		
		index = index + 1;
		statsTotalData.setRelease_several(temp[index]);
		
		//페토사 헤더 정보===============================================================
		index = index + 1;
		int petosa_header_count = Integer.parseInt(temp[index]);
		String[] petosa_header_titles = new String[petosa_header_count];

		index = index + 1;
		int petosa_index = index + petosa_header_count;

		int petosa_header_titles_index = 0;
		for (; index < petosa_index; index++) {
			petosa_header_titles[petosa_header_titles_index] = temp[index];
			petosa_header_titles_index++;
		}
		statsTotalData.setPetosa_header_count(petosa_header_count);
		statsTotalData.setPetosa_header(petosa_header_titles);

		// 페토사 정보===============================================================
		int petosa_count = Integer.parseInt(temp[index]);
		statsTotalData.setPetosa_count(petosa_count);

		ArrayList<String[]> petosa_list = new ArrayList<String[]>();

		int petosa_item_count = 0;
		int petosa_item_index = 0;

		index = index + 1;
		petosa_index = petosa_index + 1;

		for (int i = 0; i < petosa_count; i++) {

			String[] petosa_item = new String[petosa_header_count];

			for (petosa_item_index = index; petosa_item_index < petosa_index+ (petosa_header_count * (i + 1)); petosa_item_index++) {
				petosa_item[petosa_item_count] = temp[petosa_item_index];
				petosa_item_count++;
			}

			index = petosa_item_index;
			if(i < petosa_count - 1)
				petosa_list.add(petosa_item);
			else {
				statsTotalData.setPetosa_total_items(petosa_item);
				statsTotalData.setPetosa_total(petosa_item[petosa_item.length-1]);
			}
			
			petosa_item_count = 0;
		}
		statsTotalData.setPetosa_list(petosa_list);
		
		statsTotalData.setPetosa_total(temp[index]);
		
		index = index + 1;
		statsTotalData.setPetosa_several(temp[index]);

		return statsTotalData;
	}
}
