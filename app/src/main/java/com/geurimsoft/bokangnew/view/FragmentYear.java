/**
 * 연보 수량
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
import android.widget.ListView;
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
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.GSMonthInOut;
import com.geurimsoft.bokangnew.data.StAdapter;
import com.geurimsoft.bokangnew.view.util.StatsHeaderAndFooterView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FragmentYear extends Fragment
{

	private TextView tvYearDate;
	private LinearLayout layoutYearHeader;
	private ListView lvYearListView;

	// 수량, 금액 타입
	private int stateType = GSConfig.STATE_AMOUNT;

	// 질의 내용
	private String qryContent = "Unit";

	public FragmentYear(int stateType, String qryContent)
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
		View v = inflater.inflate(R.layout.year_basic, container,false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();

		this.tvYearDate = (TextView)view.findViewById(R.id.tvYearDate);
		this.layoutYearHeader = (LinearLayout)view.findViewById(R.id.layoutYearHeader);
		this.lvYearListView = (ListView)view.findViewById(R.id.lvYearListView);
		this.lvYearListView.setDividerHeight(0);

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

			String dateStr = _year + "년 입출고 현황(단위:" + GSConfig.AMOUNT_NAMES[this.stateType] + ")";
//			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "dateStr " + dateStr);

			this.tvYearDate.setText(dateStr);

			this.getData(_year);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear)
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
				params.put("GSType", "YEAR");
				params.put("GSQuery", "{ \"BranchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"SearchYear\": " + searchYear + ", \"QryContent\" : \"" + qryContent + "\" }");
				return params;
			}
		};

		request.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		request.setShouldCache(false);
		requestQueue.add(request);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

	}

	public void parseData(String msg)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			Gson gson = new Gson();

			GSMonthInOut data = gson.fromJson(msg, GSMonthInOut.class);

			this.setDisplayData(data);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}
	
	private void setDisplayData(GSMonthInOut data)
	{

		try
		{

			this.layoutYearHeader.removeAllViews();

			StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), data, this.stateType);
			statsHeaderAndFooterView.makeHeaderView(this.layoutYearHeader);

			StAdapter adapter = new StAdapter(getActivity(), data, this.stateType);

			View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
			LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

			statsHeaderAndFooterView.makeFooterView(footer_layout);
			this.lvYearListView.addFooterView(foot);
			this.lvYearListView.setAdapter(adapter);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}

}
