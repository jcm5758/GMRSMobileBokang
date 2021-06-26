package com.geurimsoft.bokangnew.view.kwangju;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.geurimsoft.bokangnew.data.GSDataStatisticYearChart;
import com.geurimsoft.bokangnew.data.GSMonthInOut;
import com.google.gson.Gson;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.HashMap;
import java.util.Map;

public class FragmentYearGraph extends Fragment
{

	private LinearLayout stats_year_graph_container, stats_year_graph_loading_indicator,
		stats_year_graph_loading_fail;

	public FragmentYearGraph() { }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.year_graph, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.stats_year_graph_container = (LinearLayout)view.findViewById(R.id.container_layout);
		this.stats_year_graph_loading_indicator = (LinearLayout)view.findViewById(R.id.chart_loading_indicator);
		this.stats_year_graph_loading_fail = (LinearLayout)view.findViewById(R.id.chart_loading_fail);

		makeYearGraphData(GSConfig.DAY_STATS_YEAR);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	private void makeYearGraphData(int _year)
	{

		String functionName = "makeYearGraphData()";

		try
		{

			String dateStr = _year + "년  입출고 현황";
			String qryContent = "Unit";

			this.getData(_year, qryContent, dateStr);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}

	private void getData(int searchYear, String qryContent, String dateStr)
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
						parseData(response, dateStr);
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
				params.put("GSQuery", "{ \"branchID\" : " + GSConfig.CURRENT_BRANCH.getBranchID() + ", \"searchYear\": " + searchYear + ", \"qryContent\" : \"" + qryContent + "\" }");
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

	public void parseData(String msg, String dateStr)
	{

		String functionName = "parseData()";
//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + msg);

		try
		{

			Gson gson = new Gson();

			GSMonthInOut data = gson.fromJson(msg, GSMonthInOut.class);

			this.setDisplayData(data, dateStr);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}
	
	private void setDisplayData(GSMonthInOut data, String _date)
	{
		
		stats_year_graph_container.removeAllViews();

		GSDataStatisticYearChart sy = new GSDataStatisticYearChart(getActivity(), _date, data);
		final XYMultipleSeriesRenderer multiRenderer = sy.getRenderer();
		final XYMultipleSeriesDataset dataset = sy.getDataSet();

		View mChartLine = ChartFactory.getTimeChartView(getActivity(), dataset, multiRenderer, "dd-mmm-yyyy");
		stats_year_graph_container.addView(mChartLine);
		
	}

}
