package com.geurimsoft.bokangnew.view.yearfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.StatsListData;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.client.SocketClient;

public class YonginYearGraphFragment extends Fragment{

	private LinearLayout stats_year_graph_container, stats_year_graph_loading_indicator,
		stats_year_graph_loading_fail;
	
	private yearGraphTask graphTask;
	
	public YonginYearGraphFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.year_graph, container, false);

		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		View view = this.getView();
		
		this.stats_year_graph_container = (LinearLayout)view.findViewById(R.id.container_layout);
		this.stats_year_graph_loading_indicator = (LinearLayout)view.findViewById(R.id.chart_loading_indicator);
		this.stats_year_graph_loading_fail = (LinearLayout)view.findViewById(R.id.chart_loading_fail);
		
		
		makeYearGraphData(GSConfig.DAY_STATS_YEAR);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		graphTask.cancel(true);
	}
	
	private void makeYearGraphData(int _year) {

		String dateStr = _year + "년  입출고 현황";
		String queryDate = String.valueOf(_year);
		
		graphTask = new yearGraphTask(queryDate, dateStr);
		graphTask.execute();
		
	}
	
	private void setDisplay(StatsListData _result, String _date) {
		
//		stats_year_graph_container.removeAllViews();
//		GSDataStatisticYearChart sy = new GSDataStatisticYearChart(getActivity(), _date, _result);
//		final XYMultipleSeriesRenderer multiRenderer = sy.getRenderer();
//		final XYMultipleSeriesDataset dataset = sy.getDataSet();
//
//		View mChartLine = ChartFactory.getTimeChartView(getActivity(), dataset, multiRenderer, "dd-mmm-yyyy");
//		stats_year_graph_container.addView(mChartLine);
		
	}
	
	public class yearGraphTask extends AsyncTask<String, String, StatsListData> {

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public yearGraphTask(String _queryDate, String _dateStr) {
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			stats_year_graph_loading_indicator.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected StatsListData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department = "0,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_year</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";
			
			responseMessage = null;

			try {
				SocketClient sc = new SocketClient(AppConfig.SERVER_IP,
						AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);
				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();

			} catch (Exception e) {
				Log.e(AppConfig.TAG, "StatViewDetailDaily.makeDailyData() : " + e.toString());
				return null;
			}

			StatsListData data = null;
			
			if (responseMessage == null || responseMessage.equals("Fail")) {
				Log.d(AppConfig.TAG, "Returned xml is null.");
				return data;
			}
			
			data = XmlConverter.parseStatsListInfo(responseMessage);
			
			try {
				Thread.sleep(10);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return data;
		}

		@Override
		protected void onPostExecute(StatsListData result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			
			if(result == null) {
				stats_year_graph_loading_fail.setVisibility(View.VISIBLE);
			} else {
				stats_year_graph_loading_fail.setVisibility(View.GONE);
				setDisplay(result, dateStr);
			}
			
			stats_year_graph_loading_indicator.setVisibility(View.GONE);
		}
	}
}
