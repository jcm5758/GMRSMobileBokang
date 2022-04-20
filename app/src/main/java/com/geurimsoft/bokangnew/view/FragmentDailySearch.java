/**
 * 일보 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 *  Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOut;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.util.GSUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentDailySearch extends Fragment
{

	private TextView tvDailySearchTitle;
	private Spinner spCustomerName, spCustomerSiteName, spProduct;
	private Button btSearch;

	private ArrayList customerList = new ArrayList();
	private ArrayList customerSiteList = new ArrayList();
	private ArrayList productList = new ArrayList();

	private String currentCustomerName = "";
	private String currentCustomerSiteName = "";
	private String currentProduct = "";

	public FragmentDailySearch() { }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.daily_search, container, false);
		return v;
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		String functionName = "onResume()";
		
		View view = this.getView();

		if(view == null)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " View is null");
			return;
		}

		this.tvDailySearchTitle = (TextView)view.findViewById(R.id.tvDailySearchTitle);
		this.spCustomerName = (Spinner) view.findViewById(R.id.spCustomerName);
		this.spCustomerSiteName = (Spinner) view.findViewById(R.id.spCustomerSiteName);
		this.spProduct = (Spinner) view.findViewById(R.id.spProduct);
		this.btSearch = (Button) view.findViewById(R.id.btSearch);

		// 거래처명 선택 시
		this.spCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{

				// 선택한 거래처명 찾기
				currentCustomerName = (String)customerList.get(i);
				Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), "spCustomerName().onItemSelected()") + currentCustomerName);

				// 현장명 리스트 채우기
				if (!currentCustomerName.equals("거래처명을 선택하세요"))
					initCustomerSiteData(currentCustomerName);

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}

		});

		// 현장명 선택 시
		this.spCustomerSiteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				currentCustomerSiteName = (String)customerSiteList.get(i);
				Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), "spCustomerSiteName().onItemSelected()") + currentCustomerSiteName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}

		});

		// 품명 선택 시
		this.spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				currentProduct = (String)productList.get(i);
				Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), "spCustomerSiteName().onItemSelected()") + currentProduct);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}

		});

		// 검색 버튼 선택시
		this.btSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view)
			{
				getData(currentCustomerName, currentCustomerSiteName, currentProduct);
			}

		});

		// 거래처명 조회
		initCustomerData();

		// 품명 조회
		initProductData();

	}

	public void parseData(String msg)
	{

		String functionName = "parseData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);


	}

	/**
	 * 거래처명 리스트 조회하고 스피너 채우기
	 */
	public void initCustomerData()
	{

		String functionName = "initCustomerData()";

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response)
					{

//						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);

						Gson gson = new Gson();
						String[] customerNames = gson.fromJson(response, String[].class);

						customerList = new ArrayList();

						for(String s : customerNames)
						{
							customerList.add(s);
						}

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, customerList);
						spCustomerName.setAdapter(adapter);

					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "SEARCH_CUSTOMER");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + "}");
				return params;
			}
		};

//		request.setRetryPolicy(new DefaultRetryPolicy(
//				0,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

	}

	/**
	 * 입력받은 거래처명으로 현장명 조회하고 스피너 채우기
	 * @param customerName	거래처명
	 */
	public void initCustomerSiteData(String customerName)
	{

		String functionName = "initCustomerSiteData()";

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response)
					{

						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);

						Gson gson = new Gson();
						String[] customerSiteNames = gson.fromJson(response, String[].class);

						customerSiteList = new ArrayList();

						for(String s : customerSiteNames)
						{
							customerSiteList.add(s);
						}

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, customerSiteList);
						spCustomerSiteName.setAdapter(adapter);

					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "SEARCH_CUSTOMERSITE");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"CustomerName\": \"" + customerName + "\" }");
				return params;
			}
		};

//		request.setRetryPolicy(new DefaultRetryPolicy(
//				0,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

	}

	/**
	 * 품명 조회하고 스피너 채우기
	 */
	public void initProductData()
	{

		String functionName = "initProductData()";

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response)
					{

						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);

						Gson gson = new Gson();
						String[] products = gson.fromJson(response, String[].class);

						productList = new ArrayList();

						for(String s : products)
						{
							productList.add(s);
						}

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, productList);
						spProduct.setAdapter(adapter);

					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "SEARCH_PRODUCT");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + " }");
				return params;
			}
		};

//		request.setRetryPolicy(new DefaultRetryPolicy(
//				0,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

	}

	/**
	 *
	 * 거래처 단가 조회 하기
	 *
	 * @param customerName			거래처명
	 * @param customerSiteName		현장명
	 * @param product				품명
	 */
	private void getData(String customerName, String customerSiteName, String product)
	{

		String functionName = "getData()";

		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "customerName : "
				+ customerName + ", customerSiteName : " + customerSiteName+ ", product : " + product);

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);
//						parseData(response);
					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "SEARCH_CUSTOMERPRICE");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID()
						+ ", \"CustomerName\": \"" + customerName + "\", \"CustomerSiteName\" : \"" + customerSiteName + "\", \"Product\" : \"" + product + "\" }");
				return params;
			}
		};

//		request.setRetryPolicy(new DefaultRetryPolicy(
//				0,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

	}

}
