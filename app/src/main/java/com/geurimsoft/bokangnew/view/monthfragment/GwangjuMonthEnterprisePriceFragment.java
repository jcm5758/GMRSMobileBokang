package com.geurimsoft.bokangnew.view.monthfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.StatsData;
import com.geurimsoft.bokangnew.client.SocketClient;

public class GwangjuMonthEnterprisePriceFragment extends Fragment{

	private LinearLayout gj_month_enterprise_price_income_empty_layout, gj_month_enterprise_price_release_empty_layout, gj_month_enterprise_price_petosa_empty_layout; 
	private LinearLayout gj_month_enterprise_price_loading_indicator, gj_month_enterprise_price_loading_fail;
	
	private TextView gj_month_enterprise_price_date, gj_month_enterprise_price_income_title, gj_month_enterprise_price_release_title, gj_month_enterprise_price_petosa_title;
	
	private MonthEnterprisePriceTask monthEnterprisePriceTask;
	
	public GwangjuMonthEnterprisePriceFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.gj_month_enterprise_price, container, false);

		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		View view = this.getView();
		
		this.gj_month_enterprise_price_income_empty_layout = (LinearLayout)view.findViewById(R.id.gj_month_enterprise_price_income_empty_layout);
		this.gj_month_enterprise_price_release_empty_layout = (LinearLayout)view.findViewById(R.id.gj_month_enterprise_price_release_empty_layout);
		this.gj_month_enterprise_price_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.gj_month_enterprise_price_petosa_empty_layout);
		
		this.gj_month_enterprise_price_loading_indicator = (LinearLayout)view.findViewById(R.id.gj_month_enterprise_price_loading_indicator); 
		this.gj_month_enterprise_price_loading_fail = (LinearLayout)view.findViewById(R.id.gj_month_enterprise_price_loading_fail);
		
		this.gj_month_enterprise_price_date = (TextView)view.findViewById(R.id.gj_month_enterprise_price_date); 
		this.gj_month_enterprise_price_income_title = (TextView)view.findViewById(R.id.gj_month_enterprise_price_income_title); 
		this.gj_month_enterprise_price_release_title = (TextView)view.findViewById(R.id.gj_month_enterprise_price_release_title); 
		this.gj_month_enterprise_price_petosa_title = (TextView)view.findViewById(R.id.gj_month_enterprise_price_petosa_title);
		
		
		makeMonthEnterprisepriceData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		monthEnterprisePriceTask.cancel(true);
	}
	
	private void makeMonthEnterprisepriceData(int _year, int _monthOfYear) {

		String dateStr = _year+"년 "+ _monthOfYear+"월  입출고 현황";
		this.gj_month_enterprise_price_date.setText(dateStr);
		
		String queryDate = _year+","+_monthOfYear;
		
		monthEnterprisePriceTask = new MonthEnterprisePriceTask(queryDate, dateStr);
		monthEnterprisePriceTask.execute();
		
	}
	
	private void setDisplay(StatsData statsData, String _date) {
		
//		String unit = getString(R.string.unit_won);
//
//		gj_month_enterprise_price_income_empty_layout.removeAllViews();
//		gj_month_enterprise_price_release_empty_layout.removeAllViews();
//		gj_month_enterprise_price_petosa_empty_layout.removeAllViews();
//
//		EnterpriseStatsView statsView = new EnterpriseStatsView(getActivity(), statsData, AppConfig.SITE_GWANGJU, AppConfig.STATE_PRICE, _date);
//		statsView.makeStockStatsView(gj_month_enterprise_price_income_empty_layout);
//		gj_month_enterprise_price_income_title.setText("입고"+"("+statsData.getStock_total()+unit+")");
//
//		statsView.makeReleaseStatsView(gj_month_enterprise_price_release_empty_layout);
//		gj_month_enterprise_price_release_title.setText("출고"+"("+statsData.getRelease_total()+unit+")");
//
//		statsView.makePetosaStatsView(gj_month_enterprise_price_petosa_empty_layout);
//		gj_month_enterprise_price_petosa_title.setText("폐토사"+"("+statsData.getPetosa_total()+unit+")");
	}
	
	
	public class MonthEnterprisePriceTask extends AsyncTask<String, String, StatsData> {

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public MonthEnterprisePriceTask(String _queryDate, String _dateStr) {
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			gj_month_enterprise_price_loading_indicator.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		protected StatsData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department = "1,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_month_company_money</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try {
				SocketClient sc = new SocketClient(AppConfig.SERVER_IP,
						AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);
				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();
				
			} catch (Exception e) {
				Log.e(GSConfig.APP_DEBUG, "StatViewDetailDaily.makeDailyData() : " + e.toString());
				return null;
			}

			StatsData statsData = null;
			
			if (responseMessage == null || responseMessage.equals("Fail")) {
				Log.d(GSConfig.APP_DEBUG, "Returned xml is null.");
				return statsData;
			}
			
//			statsData = XmlConverter.parseStatsInfo(responseMessage);
			
			try {
				Thread.sleep(10);
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return statsData;
		}

		@Override
		protected void onPostExecute(StatsData result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			
			if(result == null) {
				gj_month_enterprise_price_loading_fail.setVisibility(View.VISIBLE);
			} else {
				gj_month_enterprise_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result, queryDate);
			}
			
			gj_month_enterprise_price_loading_indicator.setVisibility(View.GONE);
		}
	}
}
