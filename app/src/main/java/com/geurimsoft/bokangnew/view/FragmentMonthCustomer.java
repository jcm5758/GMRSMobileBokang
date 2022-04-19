/**
 * 월보 거래처별 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 * Witten by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FragmentMonthCustomer extends Fragment
{

	private LinearLayout layoutMonthCustomerInput, layoutMonthCustomerOutput, layoutMonthCustomerSluge;
	private LinearLayout layoutMonthCustomerInputOutsideSource, layoutMonthCustomerInputOutsideProduct;
	private LinearLayout layoutMonthCustomerOutputOutsideSource, layoutMonthCustomerOutputOutsideProduct;

	private TextView tvMonthCustomerDate;
	private TextView tvMonthCustomerInput, tvMonthCustomerOutput, tvMonthCustomerSluge;
	private TextView tvMonthCustomerInputOutsideSource, tvMonthCustomerInputOutsideProduct;
	private TextView tvMonthCustomerOutputOutsideSource, tvMonthCustomerOutputOutsideProduct;

	// 수량, 금액 타입
	private int stateType = GSConfig.STATE_AMOUNT;

	// 질의 내용
	private String qryContent = "Unit";

	public FragmentMonthCustomer(int stateType, String qryContent)
	{
		this.stateType = stateType;
		this.qryContent = qryContent;
	}


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.month_customer, container, false);
		return v;
	}

	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();

		this.layoutMonthCustomerInput = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerInput);
		this.layoutMonthCustomerOutput = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerOutput);
		this.layoutMonthCustomerSluge = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerSluge);
		this.layoutMonthCustomerInputOutsideSource = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerInputOutsideSource);
		this.layoutMonthCustomerInputOutsideProduct = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerInputOutsideProduct);
		this.layoutMonthCustomerOutputOutsideSource = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerOutputOutsideSource);
		this.layoutMonthCustomerOutputOutsideProduct = (LinearLayout)view.findViewById(R.id.layoutMonthCustomerOutputOutsideProduct);

		this.tvMonthCustomerDate = (TextView)view.findViewById(R.id.tvMonthCustomerDate);

		this.tvMonthCustomerInput = (TextView)view.findViewById(R.id.tvMonthCustomerInput);
		this.tvMonthCustomerOutput = (TextView)view.findViewById(R.id.tvMonthCustomerOutput);
		this.tvMonthCustomerSluge = (TextView)view.findViewById(R.id.tvMonthCustomerSluge);
		this.tvMonthCustomerInputOutsideSource = (TextView)view.findViewById(R.id.tvMonthCustomerInputOutsideSource);
		this.tvMonthCustomerInputOutsideProduct = (TextView)view.findViewById(R.id.tvMonthCustomerInputOutsideProduct);
		this.tvMonthCustomerOutputOutsideSource = (TextView)view.findViewById(R.id.tvMonthCustomerOutputOutsideSource);
		this.tvMonthCustomerOutputOutsideProduct = (TextView)view.findViewById(R.id.tvMonthCustomerOutputOutsideProduct);

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

			String dateStr = _year + "년 " + _monthOfYear + "월 입출고 현황(단위:" + GSConfig.AMOUNT_NAMES[this.stateType] + ")";
//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "dateStr : " + dateStr);

			this.tvMonthCustomerDate.setText(dateStr);

			this.getData(_year, _monthOfYear);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear, int searchMonth)
	{

		String functionName = "getData()";

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "searchDate : " + searchDate + ", qryContent : " + qryContent);

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
						parseData(searchYear, searchMonth, response);
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

	public void parseData(int searchYear, int searchMonth, String msg)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			GSDailyInOut dio = new GSDailyInOut();

			Gson gson = new Gson();
			GSDailyInOutGroup[] diog = gson.fromJson(msg, GSDailyInOutGroup[].class);

			dio.list = new ArrayList<>(Arrays.asList(diog));

			this.setDisplayData(searchYear, searchMonth, dio);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void setDisplayData(int searchYear, int searchMonth, GSDailyInOut data)
	{

		if (data == null)
		{
			Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + this.getClass().getName() + " : setDisplay() is data is null.");
			return;
		}

		try
		{

			String unit = getString(R.string.unit_lube);

			this.layoutMonthCustomerInput.removeAllViews();
			this.layoutMonthCustomerOutput.removeAllViews();
			this.layoutMonthCustomerSluge.removeAllViews();
			this.layoutMonthCustomerInputOutsideSource.removeAllViews();
			this.layoutMonthCustomerInputOutsideProduct.removeAllViews();
			this.layoutMonthCustomerOutputOutsideSource.removeAllViews();
			this.layoutMonthCustomerOutputOutsideProduct.removeAllViews();

			GSDailyInOutGroup inputGroup = null;

			MonthCustomerStatsView statsView = new MonthCustomerStatsView(getActivity(), GSConfig.CURRENT_BRANCH.getBranchID(), this.stateType, searchYear, searchMonth);

			//-----------------------------------------------------------------------------------------------------------
			// 입고
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerInput, inputGroup, GSConfig.MODE_STOCK);
				this.tvMonthCustomerInput.setText(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerInput.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 출고
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerOutput, inputGroup, GSConfig.MODE_RELEASE);
				this.tvMonthCustomerOutput.setText(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerOutput.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부입고(원석)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerInputOutsideSource, inputGroup, GSConfig.MODE_OUTSIDE_STOCK_SOURCE);
				this.tvMonthCustomerInputOutsideSource.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerInputOutsideSource.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부입고(제품)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerInputOutsideProduct, inputGroup, GSConfig.MODE_OUTSIDE_STOCK_PRODUCT);
				this.tvMonthCustomerInputOutsideProduct.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerInputOutsideProduct.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부출고(원석)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerOutputOutsideSource, inputGroup, GSConfig.MODE_OUTSIDE_RELEASE_SOURCE);
				this.tvMonthCustomerOutputOutsideSource.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerOutputOutsideSource.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 외부출고(제품)
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerOutputOutsideProduct, inputGroup, GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT);
				this.tvMonthCustomerOutputOutsideProduct.setText( GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerOutputOutsideProduct.setVisibility(View.GONE);
			}

			//-----------------------------------------------------------------------------------------------------------
			// 토사
			//-----------------------------------------------------------------------------------------------------------

			inputGroup = data.findByServiceType( GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] );

			if (inputGroup != null && inputGroup.List.size() > 0)
			{
				statsView.makeStatsView(this.layoutMonthCustomerSluge, inputGroup, GSConfig.MODE_PETOSA);
				this.tvMonthCustomerSluge.setText(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(inputGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
			}
			else
			{
				this.tvMonthCustomerSluge.setVisibility(View.GONE);
			}

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}

}
