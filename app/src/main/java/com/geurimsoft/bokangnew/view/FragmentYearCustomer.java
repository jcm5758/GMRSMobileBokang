/**
 * 연보 거래처별 수량
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
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroupNew;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FragmentYearCustomer extends Fragment
{

	private LinearLayout layoutYearCustomerInput, layoutYearCustomerOutput, layoutYearCustomerSluge;
	private LinearLayout layoutYearCustomerInputOutsideSource, layoutYearCustomerInputOutsideProduct;
	private LinearLayout layoutYearCustomerOutputOutsideSource, layoutYearCustomerOutputOutsideProduct;

	private TextView tvYearCustomerDate;

	private TextView tvYearCustomerInput, tvYearCustomerOutput, tvYearCustomerSluge;
	private TextView tvYearCustomerInputOutsideSource, tvYearCustomerInputOutsideProduct;
	private TextView tvYearCustomerOutputOutsideSource, tvYearCustomerOutputOutsideProduct;

	YearCustomerStatsView statsView;
	String unit;

	// 수량, 금액 타입
	private int stateType = GSConfig.STATE_AMOUNT;

	// 질의 내용
	private String qryContent = "Unit";

	public FragmentYearCustomer(int stateType, String qryContent)
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
		View v = inflater.inflate(R.layout.year_customer, container, false);
		return v;
	}

	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();

		this.layoutYearCustomerInput = (LinearLayout)view.findViewById(R.id.layoutYearCustomerInput);
		this.layoutYearCustomerOutput = (LinearLayout)view.findViewById(R.id.layoutYearCustomerOutput);
		this.layoutYearCustomerSluge = (LinearLayout)view.findViewById(R.id.layoutYearCustomerSluge);
		this.layoutYearCustomerInputOutsideSource = (LinearLayout)view.findViewById(R.id.layoutYearCustomerInputOutsideSource);
		this.layoutYearCustomerInputOutsideProduct = (LinearLayout)view.findViewById(R.id.layoutYearCustomerInputOutsideProduct);
		this.layoutYearCustomerOutputOutsideSource = (LinearLayout)view.findViewById(R.id.layoutYearCustomerOutputOutsideSource);
		this.layoutYearCustomerOutputOutsideProduct = (LinearLayout)view.findViewById(R.id.layoutYearCustomerOutputOutsideProduct);

		this.tvYearCustomerDate = (TextView)view.findViewById(R.id.tvYearCustomerDate);

		this.tvYearCustomerInput = (TextView)view.findViewById(R.id.tvYearCustomerInput);
		this.tvYearCustomerOutput = (TextView)view.findViewById(R.id.tvYearCustomerOutput);
		this.tvYearCustomerSluge = (TextView)view.findViewById(R.id.tvYearCustomerSluge);
		this.tvYearCustomerInputOutsideSource = (TextView)view.findViewById(R.id.tvYearCustomerInputOutsideSource);
		this.tvYearCustomerInputOutsideProduct = (TextView)view.findViewById(R.id.tvYearCustomerInputOutsideProduct);
		this.tvYearCustomerOutputOutsideSource = (TextView)view.findViewById(R.id.tvYearCustomerOutputOutsideSource);
		this.tvYearCustomerOutputOutsideProduct = (TextView)view.findViewById(R.id.tvYearCustomerOutputOutsideProduct);

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

			String dateStr = _year + "년  입출고 현황(단위:" + GSConfig.AMOUNT_NAMES[this.stateType] + ")";

			// 날짜 표시 텍스트뷰 설정
			this.tvYearCustomerDate.setText(dateStr);

			// 레이아웃 초기화
			this.layoutYearCustomerInput.removeAllViews();
			this.layoutYearCustomerOutput.removeAllViews();
			this.layoutYearCustomerSluge.removeAllViews();
			this.layoutYearCustomerInputOutsideSource.removeAllViews();
			this.layoutYearCustomerInputOutsideProduct.removeAllViews();
			this.layoutYearCustomerOutputOutsideSource.removeAllViews();
			this.layoutYearCustomerOutputOutsideProduct.removeAllViews();

			// 데이터 표 만들기
			this.statsView = new YearCustomerStatsView(getActivity(), GSConfig.CURRENT_BRANCH.getBranchID(), this.stateType, _year);

			// 데이터 요청하기
			this.getData(_year, this.qryContent, GSConfig.MODE_STOCK);
			this.getData(_year, this.qryContent, GSConfig.MODE_RELEASE);
			this.getData(_year, this.qryContent, GSConfig.MODE_PETOSA);
			this.getData(_year, this.qryContent, GSConfig.MODE_OUTSIDE_STOCK_SOURCE);
			this.getData(_year, this.qryContent, GSConfig.MODE_OUTSIDE_STOCK_PRODUCT);
			this.getData(_year, this.qryContent, GSConfig.MODE_OUTSIDE_RELEASE_SOURCE);
			this.getData(_year, this.qryContent, GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG("FragmentYearCustomerAmount", functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear, String qryContent, int serviceType)
	{

		String functionName = "getData()";

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
						parseData(response, serviceType);

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

		request.setShouldCache(false);
		requestQueue.add(request);

	}

	public void parseData(String msg, int serviceType)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			Gson gson = new Gson();
			GSDailyInOutGroupNew dataGroup = null;

			// 입고
			if (serviceType == GSConfig.MODE_STOCK)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerInput, dataGroup, GSConfig.MODE_STOCK, this.stateType);
					this.tvYearCustomerInput.setText(GSConfig.MODE_NAMES[GSConfig.MODE_STOCK] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerInput.setVisibility(View.GONE);
				}

			}
			// 출고
			else if (serviceType == GSConfig.MODE_RELEASE)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerOutput, dataGroup, GSConfig.MODE_RELEASE, this.stateType);
					this.tvYearCustomerOutput.setText(GSConfig.MODE_NAMES[GSConfig.MODE_RELEASE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerOutput.setVisibility(View.GONE);
				}

			}
			// 토사
			else if (serviceType == GSConfig.MODE_PETOSA)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerSluge, dataGroup, GSConfig.MODE_PETOSA, this.stateType);
					this.tvYearCustomerSluge.setText(GSConfig.MODE_NAMES[GSConfig.MODE_PETOSA] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerSluge.setVisibility(View.GONE);
				}

			}
			// 외부입고(원석)
			else if (serviceType == GSConfig.MODE_OUTSIDE_STOCK_SOURCE)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerInputOutsideSource, dataGroup, GSConfig.MODE_OUTSIDE_STOCK_SOURCE, this.stateType);
					this.tvYearCustomerInputOutsideSource.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_SOURCE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerInputOutsideSource.setVisibility(View.GONE);
				}

			}
			// 외부입고(제품)
			else if (serviceType == GSConfig.MODE_OUTSIDE_STOCK_PRODUCT)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerInputOutsideProduct, dataGroup, GSConfig.MODE_OUTSIDE_STOCK_PRODUCT, this.stateType);
					this.tvYearCustomerInputOutsideProduct.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_STOCK_PRODUCT] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerInputOutsideProduct.setVisibility(View.GONE);
				}

			}
			// 외부출고(원석)
			else if (serviceType == GSConfig.MODE_OUTSIDE_RELEASE_SOURCE)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerOutputOutsideSource, dataGroup, GSConfig.MODE_OUTSIDE_RELEASE_SOURCE, this.stateType);
					this.tvYearCustomerOutputOutsideSource.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_SOURCE] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerOutputOutsideSource.setVisibility(View.GONE);
				}

			}
			// 외부출고(제품)
			else if (serviceType == GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT)
			{

				dataGroup = gson.fromJson(msg, GSDailyInOutGroupNew.class);

				if (dataGroup != null)
				{
					statsView.makeStatsView(this.layoutYearCustomerOutputOutsideProduct, dataGroup, GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT, this.stateType);
					this.tvYearCustomerOutputOutsideProduct.setText(GSConfig.MODE_NAMES[GSConfig.MODE_OUTSIDE_RELEASE_PRODUCT] + "(" + GSConfig.changeToCommanString(dataGroup.TotalUnit) + GSConfig.AMOUNT_NAMES[this.stateType] + ")");
				}
				else
				{
					this.tvYearCustomerOutputOutsideProduct.setVisibility(View.GONE);
				}

			}

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

}
