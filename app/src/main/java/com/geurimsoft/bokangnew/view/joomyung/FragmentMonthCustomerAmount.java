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
import com.geurimsoft.bokangnew.data.GSConfig;
import com.google.gson.Gson;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentMonthCustomerAmount extends Fragment
{

	private LinearLayout month_enterprise_amount_loading_indicator, month_enterprise_amount_loading_fail;

	private LinearLayout month_enterprise_amount_income_empty_layout, month_enterprise_amount_release_empty_layout, month_enterprise_amount_petosa_empty_layout;
	private LinearLayout month_enterprise_amount_outside_income_empty_layout_source, month_enterprise_amount_outside_income_empty_layout_product;
	private LinearLayout month_enterprise_amount_outside_release_empty_layout_source, month_enterprise_amount_outside_release_empty_layout_product;

	private TextView month_enterprise_amount_date;
	private TextView month_enterprise_amount_income_title, month_enterprise_amount_release_title, month_enterprise_amount_petosa_title;
	private TextView month_enterprise_amount_outside_income_title_source, month_enterprise_amount_outside_income_title_product;
	private TextView month_enterprise_amount_outside_release_title_source, month_enterprise_amount_outside_release_title_product;

	private int iYear, iMonth;

	public FragmentMonthCustomerAmount() {}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.month_customer_amount, container, false);
		return v;
	}

	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();

		this.month_enterprise_amount_income_empty_layout = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_income_empty_layout);
		this.month_enterprise_amount_release_empty_layout = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_release_empty_layout);
		this.month_enterprise_amount_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_petosa_empty_layout);
		this.month_enterprise_amount_outside_income_empty_layout_source = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_outside_income_empty_layout_source);
		this.month_enterprise_amount_outside_income_empty_layout_product = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_outside_income_empty_layout_product);
		this.month_enterprise_amount_outside_release_empty_layout_source = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_outside_release_empty_layout_source);
		this.month_enterprise_amount_outside_release_empty_layout_product = (LinearLayout)view.findViewById(R.id.month_enterprise_amount_outside_release_empty_layout_product);

		this.month_enterprise_amount_date = (TextView)view.findViewById(R.id.month_enterprise_amount_date);

		this.month_enterprise_amount_income_title = (TextView)view.findViewById(R.id.month_enterprise_amount_income_title);
		this.month_enterprise_amount_release_title = (TextView)view.findViewById(R.id.month_enterprise_amount_release_title);
		this.month_enterprise_amount_petosa_title = (TextView)view.findViewById(R.id.month_enterprise_amount_petosa_title);
		this.month_enterprise_amount_outside_income_title_source = (TextView)view.findViewById(R.id.month_enterprise_amount_outside_income_title_source);
		this.month_enterprise_amount_outside_income_title_product = (TextView)view.findViewById(R.id.month_enterprise_amount_outside_income_title_product);
		this.month_enterprise_amount_outside_release_title_source = (TextView)view.findViewById(R.id.month_enterprise_amount_outside_release_title_source);
		this.month_enterprise_amount_outside_release_title_product = (TextView)view.findViewById(R.id.month_enterprise_amount_outside_release_title_product);

		makeMonthEnterpriseAmountData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);

	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	private void makeMonthEnterpriseAmountData(int _year, int _monthOfYear)
	{

		String functionName = "makeMonthAmountData()";

		try
		{

			String dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황";
//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + _year + "년 " + _monthOfYear + "월");

			this.month_enterprise_amount_date.setText(dateStr);

			String qryContent = "Unit";

			this.getData(_year, _monthOfYear, qryContent);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear, int searchMonth, String qryContent)
	{

		String functionName = "getData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "searchDate : " + searchDate + ", qryContent : " + qryContent);

		iYear = searchYear;
		iMonth = searchMonth;

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
						parseData(response);
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
				params.put("GSType", "MONTH_CUSTOMER");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"SearchYear\": " + searchYear + ", \"SearchMonth\": " + searchMonth + ", \"QryContent\" : \"" + qryContent + "\" }");
				return params;
			}
		};

		request.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
		requestQueue.add(request);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

	}

	public void parseData(String msg)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			GSDailyInOut dio = new GSDailyInOut();

			Gson gson = new Gson();
			GSDailyInOutGroup[] diog = gson.fromJson(msg, GSDailyInOutGroup[].class);

			dio.list = new ArrayList<>(Arrays.asList(diog));

			this.setDisplayData(dio);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void setDisplayData(GSDailyInOut data)
	{

		if (data == null)
		{
			Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + this.getClass().getName() + " : setDisplay() is data is null.");
			return;
		}

		try
		{

			String unit = getString(R.string.unit_lube);

			this.month_enterprise_amount_income_empty_layout.removeAllViews();
			this.month_enterprise_amount_release_empty_layout.removeAllViews();
			this.month_enterprise_amount_petosa_empty_layout.removeAllViews();
			this.month_enterprise_amount_outside_income_empty_layout_source.removeAllViews();
			this.month_enterprise_amount_outside_income_empty_layout_product.removeAllViews();
			this.month_enterprise_amount_outside_release_empty_layout_source.removeAllViews();
			this.month_enterprise_amount_outside_release_empty_layout_product.removeAllViews();

			GSDailyInOutGroup inputGroup = null;

			MonthCustomerStatsView statsView = new MonthCustomerStatsView(getActivity(), GSConfig.CURRENT_BRANCH.getBranchID(), GSConfig.STATE_AMOUNT, iYear, iMonth);

			//-----------------------------------------------------------------------------------------------------------
			// 입고
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_income_empty_layout, inputGroup, GSConfig.MODE_STOCK);
				month_enterprise_amount_income_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_income_title.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 출고
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_release_empty_layout, inputGroup, GSConfig.MODE_RELEASE);
				month_enterprise_amount_release_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_release_title.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부입고(원석)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_outside_income_empty_layout_source, inputGroup, GSConfig.MODE_STOCK);
				month_enterprise_amount_outside_income_title_source.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_outside_income_title_source.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부입고(제품)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_outside_income_empty_layout_product, inputGroup, GSConfig.MODE_STOCK);
				month_enterprise_amount_outside_income_title_product.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_outside_income_title_product.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부출고(원석)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_outside_release_empty_layout_source, inputGroup, GSConfig.MODE_RELEASE);
				month_enterprise_amount_outside_release_title_source.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_outside_release_title_source.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부출고(제품)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_outside_release_empty_layout_product, inputGroup, GSConfig.MODE_RELEASE);
				month_enterprise_amount_outside_release_title_product.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_outside_release_title_product.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 토사
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(month_enterprise_amount_petosa_empty_layout, inputGroup, GSConfig.MODE_PETOSA);
				month_enterprise_amount_petosa_title.setText(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + unit + ")");
			}
			else
			{
				month_enterprise_amount_petosa_title.setVisibility(View.GONE);
			}

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}

}
