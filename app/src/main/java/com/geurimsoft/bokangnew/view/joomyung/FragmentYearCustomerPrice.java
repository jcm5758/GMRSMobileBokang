package com.geurimsoft.bokangnew.view.joomyung;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroupNew;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentYearCustomerPrice extends Fragment
{

	private LinearLayout yi_month_enterprise_loading_indicator, yi_month_enterprise_loading_fail;

	private LinearLayout yi_month_enterprise_income_layout, yi_month_enterprise_release_layout, yi_month_enterprise_petosa_layout;
	private LinearLayout yi_month_enterprise_income_outside_source_layout, yi_month_enterprise_income_outside_product_layout;
	private LinearLayout yi_month_enterprise_release_outside_source_layout, yi_month_enterprise_release_outside_product_layout;

	private TextView yi_month_enterprise_date;
	private TextView yi_month_enterprise_income_title, yi_month_enterprise_release_title, yi_month_enterprise_petosa_title;
	private TextView yi_month_enterprise_income_outside_source_title, yi_month_enterprise_income_outside_product_title;
	private TextView yi_month_enterprise_release_outside_source_title, yi_month_enterprise_release_outside_product_title;

	private int iYear;

	EnterpriseYearStatsView statsView;
	String unit;

	public FragmentYearCustomerPrice() {}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_enterprise_amount, container, false);
		return v;
	}

	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();

		this.yi_month_enterprise_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_loading_indicator);
		this.yi_month_enterprise_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_loading_fail);

		this.yi_month_enterprise_income_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_income_layout);
		this.yi_month_enterprise_release_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_release_layout);
		this.yi_month_enterprise_petosa_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_petosa_layout);
		this.yi_month_enterprise_income_outside_source_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_income_outside_source_layout);
		this.yi_month_enterprise_income_outside_product_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_income_outside_product_layout);
		this.yi_month_enterprise_release_outside_source_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_release_outside_source_layout);
		this.yi_month_enterprise_release_outside_product_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_release_outside_product_layout);

		this.yi_month_enterprise_date = (TextView)view.findViewById(R.id.yi_month_enterprise_date);

		this.yi_month_enterprise_income_title = (TextView)view.findViewById(R.id.yi_month_enterprise_income_title);
		this.yi_month_enterprise_release_title = (TextView)view.findViewById(R.id.yi_month_enterprise_release_title);
		this.yi_month_enterprise_petosa_title = (TextView)view.findViewById(R.id.yi_month_enterprise_petosa_title);
		this.yi_month_enterprise_income_outside_source_title = (TextView)view.findViewById(R.id.yi_month_enterprise_income_outside_source_title);
		this.yi_month_enterprise_income_outside_product_title = (TextView)view.findViewById(R.id.yi_month_enterprise_income_outside_product_title);
		this.yi_month_enterprise_release_outside_source_title = (TextView)view.findViewById(R.id.yi_month_enterprise_release_outside_source_title);
		this.yi_month_enterprise_release_outside_product_title = (TextView)view.findViewById(R.id.yi_month_enterprise_release_outside_product_title);

		this.makeData(GSConfig.DAY_STATS_YEAR);

	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	private void makeData(int _year)
	{

		String functionName = "makeData()";

		try
		{

			String dateStr = _year + "년  입출고 현황";

			this.unit = getString(R.string.unit_won);

			// 날짜 표시 텍스트뷰 설정
			this.yi_month_enterprise_date.setText(dateStr);

			// 레이아웃 초기화
			this.yi_month_enterprise_income_layout.removeAllViews();
			this.yi_month_enterprise_release_layout.removeAllViews();
			this.yi_month_enterprise_petosa_layout.removeAllViews();
			this.yi_month_enterprise_income_outside_source_layout.removeAllViews();
			this.yi_month_enterprise_income_outside_product_layout.removeAllViews();
			this.yi_month_enterprise_release_outside_source_layout.removeAllViews();
			this.yi_month_enterprise_release_outside_product_layout.removeAllViews();

			// 데이터 표 만들기
			this.statsView = new EnterpriseYearStatsView(getActivity(), GSConfig.CURRENT_BRANCH.getBranchID(), GSConfig.STATE_PRICE, iYear);

			this.getData(_year, "TotalPrice", GSConfig.MODE_STOCK);
			this.getData(_year, "TotalPrice", GSConfig.MODE_RELEASE);
			this.getData(_year, "TotalPrice", GSConfig.MODE_PETOSA);
			this.getData(_year, "TotalPrice", GSConfig.MODE_OUTSIDE_STOCK_SOURCE);
			this.getData(_year, "TotalPrice", GSConfig.MODE_OUTSIDE_STOCK_PRODUCT);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear, String qryContent, int serviceType)
	{

		String functionName = "getData()";

		iYear = searchYear;

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

//						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);

						Gson gson = new Gson();
						GSDailyInOutGroupNew dataGroup = null;

						// 입고
						if (serviceType == GSConfig.MODE_STOCK)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_income_layout, dataGroup, GSConfig.MODE_STOCK, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_income_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_income_title.setVisibility(View.GONE);
							}

						}
						// 출고
						else if (serviceType == GSConfig.MODE_RELEASE)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_release_layout, dataGroup, GSConfig.MODE_RELEASE, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_release_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_release_title.setVisibility(View.GONE);
							}

						}
						// 토사
						else if (serviceType == GSConfig.MODE_PETOSA)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_petosa_layout, dataGroup, GSConfig.MODE_PETOSA, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_petosa_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_petosa_title.setVisibility(View.GONE);
							}

						}
						// 외부입고(원석)
						else if (serviceType == GSConfig.MODE_OUTSIDE_STOCK_SOURCE)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_income_outside_source_layout, dataGroup, GSConfig.MODE_OUTSIDE_STOCK_SOURCE, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_income_outside_source_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_income_outside_source_title.setVisibility(View.GONE);
							}

						}
						// 외부입고(제품)
						else if (serviceType == GSConfig.MODE_OUTSIDE_STOCK_PRODUCT)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_income_outside_product_layout, dataGroup, GSConfig.MODE_OUTSIDE_STOCK_PRODUCT, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_income_outside_product_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_income_outside_product_title.setVisibility(View.GONE);
							}

						}
						// 외부출고(원석)
						else if (serviceType == GSConfig.MODE_OUTSIDE_RELEASE_SOURCE)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_release_outside_source_layout, dataGroup, GSConfig.MODE_OUTSIDE_RELEASE_SOURCE, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_release_outside_source_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_release_outside_source_title.setVisibility(View.GONE);
							}

						}
						// 외부출고(제품)
						else if (serviceType == GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT)
						{

							dataGroup = gson.fromJson(response, GSDailyInOutGroupNew.class);

							if (dataGroup != null)
							{
								statsView.makeStatsView(yi_month_enterprise_release_outside_product_layout, dataGroup, GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT, GSConfig.STATE_AMOUNT);
								yi_month_enterprise_release_outside_product_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + unit + ")");
							}
							else
							{
								yi_month_enterprise_release_outside_product_title.setVisibility(View.GONE);
							}

						}

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
				params.put("GSType", "YEAR_CUSTOMER");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"SearchYear\": " + searchYear + ", \"QryContent\" : \"" + qryContent + "\",  \"ServiceType\" : " + serviceType + " }");
				return params;
			}
		};

		request.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//		request.setRetryPolicy(new DefaultRetryPolicy(20000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
		requestQueue.add(request);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

	}

}
