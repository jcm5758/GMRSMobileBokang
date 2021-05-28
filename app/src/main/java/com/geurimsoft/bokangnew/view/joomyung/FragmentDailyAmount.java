/**
 * 일보 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 *  Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view.joomyung;

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
import com.geurimsoft.bokangnew.view.etc.StatsView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSDailyInOut;
import com.geurimsoft.bokangnew.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.client.SocketClient;

public class FragmentDailyAmount extends Fragment
{

	private LinearLayout income_empty_layout, release_empty_layout, petosa_empty_layout;
	private TextView stats_daily_date, daily_income_title, daily_release_title, daily_petosa_title;

	private int year, month, day;

	private LinearLayout loading_indicator, loading_fail;

	private DailyAmountTask dailyAmountTask;

	public FragmentDailyAmount() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.statviewdetaildaily, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();

		if(view == null)
			Log.e(AppConfig.TAG, "StatsDailyAmountFragment onResume View is null");
		
		this.income_empty_layout = (LinearLayout)view.findViewById(R.id.income_empty_layout);
		this.release_empty_layout = (LinearLayout)view.findViewById(R.id.release_empty_layout);
		this.petosa_empty_layout = (LinearLayout)view.findViewById(R.id.petosa_empty_layout);
		
		this.loading_indicator = (LinearLayout)view.findViewById(R.id.loading_indicator);
		this.loading_fail = (LinearLayout)view.findViewById(R.id.loading_fail);
		
		this.stats_daily_date = (TextView)view.findViewById(R.id.stats_daily_date);

		this.daily_income_title = (TextView) view.findViewById(R.id.daily_income_title);
		this.daily_release_title = (TextView) view.findViewById(R.id.daily_release_title);
		this.daily_petosa_title = (TextView) view.findViewById(R.id.daily_petosa_title);

		// 일일 입고/출고/토사 수량 조회
		// 연/월/일
		makeDailyAmountData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH,GSConfig.DAY_STATS_DAY);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		dailyAmountTask.cancel(true);
	}

    /**
     * 일일 입고/출고/토사 수량 조회
	 * @param _year				연도
	 * @param _monthOfYear		월
	 * @param _dayOfMonth		일
	 */
	public void makeDailyAmountData(int _year, int _monthOfYear, int _dayOfMonth)
	{

		String str = _year + "년 " + _monthOfYear + "월 " + _dayOfMonth + "일 입출고 현황";

//		Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : makeDailyAmountData() : " + str);
		
		if(stats_daily_date == null)
			Log.e(AppConfig.TAG, "StatsDailyAmountFragment makeDailyAmountData stats_daily_date is null");
		
		this.stats_daily_date.setText(str);

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear + ",";
		else
			queryDate += _monthOfYear + ",";

		if (_dayOfMonth < 10)
			queryDate += "0" + _dayOfMonth;
		else
			queryDate += _dayOfMonth;

		dailyAmountTask = new DailyAmountTask(queryDate);
		dailyAmountTask.execute();

	}
	
	private void setDisplayData(GSDailyInOut dio)
	{

//		Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : setDisplayData() is called.");

		if(getActivity() == null)
		{
			Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : Activity is null.");
			return;
		}

		if (dio == null)
		{
			Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : dio is null.");
			return;
		}

		income_empty_layout.removeAllViews();
		release_empty_layout.removeAllViews();
		petosa_empty_layout.removeAllViews();
		
		StatsView statsView = new StatsView(getActivity(), dio, 0);

		statsView.makeStockStatsView(income_empty_layout);
		GSDailyInOutGroup tempGroup = dio.findByServiceType("입고");
		if (tempGroup != null)
			daily_income_title.setText(tempGroup.getTitleUnit());
		
		statsView.makeReleaseStatsView(release_empty_layout);
		tempGroup = dio.findByServiceType("출고");
		if (tempGroup != null)
			daily_release_title.setText(tempGroup.getTitleUnit());

		statsView.makePetosaStatsView(petosa_empty_layout);
		tempGroup = dio.findByServiceType("토사");
		if (tempGroup != null)
			daily_petosa_title.setText(tempGroup.getTitleUnit());
		
	}

	/**
	 * 일일 입고/출고/토사 수량 조회 태스크
	 */
	public class DailyAmountTask extends AsyncTask<String, String, GSDailyInOut>
	{

		private String queryDate;
		
		public DailyAmountTask(String _queryDate)
		{
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			loading_indicator.setVisibility(View.VISIBLE);
		}

		@Override
		protected GSDailyInOut doInBackground(String... params)
		{

			String branchID = "3,";
			String messages =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>DAILY_UNIT</GCType><GCQuery>" + branchID + queryDate + "</GCQuery></GEURIMSOFT>\n";

			SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, messages, AppConfig.SOCKET_KEY);

			try
			{
				sc.start();
				sc.join();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			GSDailyInOut dio = null;
			
			String mss = sc.getReturnString();

			if(mss.equals("Fail"))
			{
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : Response is Fail.");
				return null;
			}

//			Log.d(AppConfig.TAG, "DEBUGGING : " + this.getClass().getName() + " : doInBackground() : " + mss);

            dio = XmlConverter.parseDaily(mss);
//			dio.print();

			return dio;

		}

		@Override
		protected void onPostExecute(GSDailyInOut result)
		{

			super.onPostExecute(result);
			
			if(result.equals("Fail"))
			{
				loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				loading_fail.setVisibility(View.GONE);
				setDisplayData(result);
			}
			
			loading_indicator.setVisibility(View.GONE);

		}

	}

}
