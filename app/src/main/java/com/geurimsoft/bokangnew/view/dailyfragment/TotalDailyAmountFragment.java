package com.geurimsoft.bokangnew.view.dailyfragment;

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
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.view.etc.StatsTotalView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.StatsTotalData;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.client.SocketClient;

public class TotalDailyAmountFragment extends Fragment {
	
	private LinearLayout total_income_empty_layout, total_release_yongin_empty_layout, total_release_gwangju_empty_layout, total_petosa_empty_layout;
	private TextView total_stats_daily_date, total_daily_income_title, total_daily_release_title, total_daily_petosa_title;
	
	private LinearLayout total_loading_indicator, total_loading_fail;
	
	private DailyAmountTask dailyAmountTask;
	
	public TotalDailyAmountFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.totalviewdetaildaily, container, false);
		
		return v;
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		View view = this.getView();
		if(view == null)
			Log.e("BOKANG", "StatsDailyAmountFragment onResume View is null");
		
		this.total_income_empty_layout = (LinearLayout)view.findViewById(R.id.total_income_empty_layout);
		this.total_release_yongin_empty_layout = (LinearLayout)view.findViewById(R.id.total_release_yongin_empty_layout);
		this.total_release_gwangju_empty_layout = (LinearLayout)view.findViewById(R.id.total_release_gwangju_empty_layout);
		this.total_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.total_petosa_empty_layout);
		
		this.total_loading_indicator = (LinearLayout)view.findViewById(R.id.total_loading_indicator);
		this.total_loading_fail = (LinearLayout)view.findViewById(R.id.total_loading_fail);
		
		this.total_stats_daily_date = (TextView)view.findViewById(R.id.total_stats_daily_date);
		
		this.total_daily_income_title = (TextView) view.findViewById(R.id.total_daily_income_title);
		this.total_daily_release_title = (TextView) view.findViewById(R.id.total_daily_release_title);
		this.total_daily_petosa_title = (TextView) view.findViewById(R.id.total_daily_petosa_title);
		
		makeDailyAmountData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH,GSConfig.DAY_STATS_DAY);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dailyAmountTask.cancel(true);
	}
	
	public void makeDailyAmountData(int _year, int _monthOfYear, int _dayOfMonth) {
		String str = _year+"년 "+ _monthOfYear+"월 "+ _dayOfMonth+"일 입출고 현황";
		
		if(total_stats_daily_date == null)
			Log.e("BOKANG", "StatsDailyAmountFragment makeDailyAmountData stats_daily_date is null");
		
		this.total_stats_daily_date.setText(str);
		
		String queryDate = _year+","+_monthOfYear+","+ _dayOfMonth;
		
		dailyAmountTask = new DailyAmountTask(queryDate);
		dailyAmountTask.execute();
	}
	
	private void setDisplayData(StatsTotalData statsTotalData) {
		
		if(getActivity() == null)
			return;
		
		this.total_income_empty_layout.removeAllViews();
		this.total_release_yongin_empty_layout.removeAllViews();
		this.total_release_gwangju_empty_layout.removeAllViews();
		this.total_petosa_empty_layout.removeAllViews();
		
		StatsTotalView statsTotalView = new StatsTotalView(getActivity(), statsTotalData);
		
		this.total_daily_income_title.setText("입고"+"("+statsTotalData.getStock_several()+"대 : "+statsTotalData.getStock_total()+"루베"+")");
		this.total_daily_release_title.setText("출고"+"("+statsTotalData.getRelease_several()+"대 : "+statsTotalData.getRelease_total()+"루베"+")");
		this.total_daily_petosa_title.setText("폐토사"+"("+statsTotalData.getPetosa_several()+"대 : "+statsTotalData.getPetosa_total()+"루베"+")");
		
		statsTotalView.makeStockStatsView(total_income_empty_layout);
		statsTotalView.makeReleaseYonginStatsView(total_release_yongin_empty_layout);
		statsTotalView.makeReleaseGwangjuStatsView(total_release_gwangju_empty_layout);
		statsTotalView.makePetosaStatsView(total_petosa_empty_layout);
	}
	
	public class DailyAmountTask extends AsyncTask<String, String, StatsTotalData> {

		private String queryDate;
		private String[] totalData;
		
		public DailyAmountTask(String _queryDate) {
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			total_loading_indicator.setVisibility(View.VISIBLE);
		
		}
		@Override
		protected StatsTotalData doInBackground(String... params) {
			// TODO Auto-generated method stub
			String department ="2,";
			String messages =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_day</GCType><GCQuery>"+department+queryDate+"</GCQuery></GEURIMSOFT>\n";

			
			SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, messages, AppConfig.SOCKET_KEY);
			try {
				sc.start();
				sc.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			StatsTotalData statsTotalData = null;
			String mss = sc.getReturnString();

			if(mss.equals("Fail")) {
				return statsTotalData;
			}
			
			statsTotalData = XmlConverter.parseStatsTotalInfo(mss);
		
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return statsTotalData;
		}

		@Override
		protected void onPostExecute(StatsTotalData result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			
			if(result == null) {
				total_loading_fail.setVisibility(View.VISIBLE);
			} else {
				total_loading_fail.setVisibility(View.GONE);
				setDisplayData(result);
			}
			
			total_loading_indicator.setVisibility(View.GONE);
		}
	}
}
