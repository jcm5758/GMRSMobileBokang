package com.geurimsoft.bokangnew.view.joomyung;

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
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSDataStatisticMonthChart;
import com.geurimsoft.bokangnew.data.GSMonthInOut;
import com.geurimsoft.bokangnew.client.SocketClient;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public class FragmentMonthGraph extends Fragment
{

	private LinearLayout stats_month_graph_container, stats_month_graph_loading_indicator, stats_month_graph_loading_fail;
	private MonthGraphTask monthGraphTask;

	public FragmentMonthGraph() { }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.month_graph, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.stats_month_graph_container = (LinearLayout)view.findViewById(R.id.container_layout);
		this.stats_month_graph_loading_indicator = (LinearLayout)view.findViewById(R.id.chart_loading_indicator);
		this.stats_month_graph_loading_fail = (LinearLayout)view.findViewById(R.id.chart_loading_fail);

		makeMonthGraphData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthGraphTask.cancel(true);
	}
	
	private void makeMonthGraphData(int _year, int _monthOfYear)
	{

		String dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황";

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear;
		else
			queryDate += _monthOfYear;
		
		monthGraphTask = new MonthGraphTask(queryDate, dateStr);
		monthGraphTask.execute();
		
	}
	
	private void setDisplay(GSMonthInOut data, String _date)
	{

		try
		{

			stats_month_graph_container.removeAllViews();
			GSDataStatisticMonthChart sy = new GSDataStatisticMonthChart(getActivity(), _date, data);
			final XYMultipleSeriesRenderer multiRenderer = sy.getRenderer();
			final XYMultipleSeriesDataset dataset = sy.getDataSet();

			View mChartLine = ChartFactory.getTimeChartView(getActivity(), dataset, multiRenderer, "dd-mmm-yyyy");
			stats_month_graph_container.addView(mChartLine);

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}
	
	public class MonthGraphTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public MonthGraphTask(String _queryDate, String _dateStr)
		{
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			stats_month_graph_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String department = "3,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_UNIT</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try
			{

				SocketClient sc = new SocketClient(GSConfig.API_SERVER_ADDR, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);

				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();
				
			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : RETURNED XML is null.");
				return null;
			}

			GSMonthInOut data = XmlConverter.parseMonth(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSMonthInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				stats_month_graph_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				stats_month_graph_loading_fail.setVisibility(View.GONE);
				setDisplay(result, dateStr);
			}
			
			stats_month_graph_loading_indicator.setVisibility(View.GONE);

		}

	}

}
