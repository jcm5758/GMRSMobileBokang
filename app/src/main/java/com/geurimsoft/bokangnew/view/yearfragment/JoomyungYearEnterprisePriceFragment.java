package com.geurimsoft.bokangnew.view.yearfragment;

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
import com.geurimsoft.bokangnew.view.etc.EnterpriseYearStatsView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSDailyInOut;
import com.geurimsoft.bokangnew.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.client.AES;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class JoomyungYearEnterprisePriceFragment extends Fragment
{

	private LinearLayout yi_month_enterprise_price_income_empty_layout, yi_month_enterprise_price_release_empty_layout, yi_month_enterprise_price_petosa_empty_layout;
	private LinearLayout yi_month_enterprise_price_loading_indicator, yi_month_enterprise_price_loading_fail;

	private TextView yi_month_enterprise_price_date, yi_month_enterprise_price_income_title, yi_month_enterprise_price_release_title, yi_month_enterprise_price_petosa_title;

	private YearEnterprisePriceTask monthEnterprisePriceTask;

	public JoomyungYearEnterprisePriceFragment() { }
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_enterprise_price, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.yi_month_enterprise_price_income_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_income_empty_layout);
		this.yi_month_enterprise_price_release_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_release_empty_layout);
		this.yi_month_enterprise_price_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_petosa_empty_layout);
		
		this.yi_month_enterprise_price_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_loading_indicator); 
		this.yi_month_enterprise_price_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_price_loading_fail);
		
		this.yi_month_enterprise_price_date = (TextView)view.findViewById(R.id.yi_month_enterprise_price_date); 
		this.yi_month_enterprise_price_income_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_income_title); 
		this.yi_month_enterprise_price_release_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_release_title); 
		this.yi_month_enterprise_price_petosa_title = (TextView)view.findViewById(R.id.yi_month_enterprise_price_petosa_title);
		
		makeMonthEnterprisepriceData(GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthEnterprisePriceTask.cancel(true);
	}
	
	private void makeMonthEnterprisepriceData(int _year, int _monthOfYear)
	{

		String dateStr = _year + "년 " + "  입출고 현황";
		this.yi_month_enterprise_price_date.setText(dateStr);

		monthEnterprisePriceTask = new YearEnterprisePriceTask(String.valueOf(_year), dateStr);
		monthEnterprisePriceTask.execute();
		
	}
	
	private void setDisplay(GSDailyInOut data, String _date)
	{

		if (data == null)
			return;

		GSDailyInOutGroup inputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK]);
		GSDailyInOutGroup outputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE]);
		GSDailyInOutGroup slugeGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA]);

		String unit = getString(R.string.unit_won);

		yi_month_enterprise_price_income_empty_layout.removeAllViews();
		yi_month_enterprise_price_release_empty_layout.removeAllViews();
		yi_month_enterprise_price_petosa_empty_layout.removeAllViews();

		EnterpriseYearStatsView statsView = new EnterpriseYearStatsView(getActivity(), AppConfig.SITE_JOOMYUNG, AppConfig.STATE_PRICE, _date);

		statsView.makeStatsView(yi_month_enterprise_price_income_empty_layout, inputGroup, AppConfig.MODE_STOCK, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_income_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK] + "(" + AppConfig.changeToCommanString(inputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_release_empty_layout, outputGroup, AppConfig.MODE_RELEASE, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_release_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE] + "(" + AppConfig.changeToCommanString(outputGroup.totalUnit) + unit + ")");

		statsView.makeStatsView(yi_month_enterprise_price_petosa_empty_layout, slugeGroup, AppConfig.MODE_PETOSA, AppConfig.STATE_PRICE);
		yi_month_enterprise_price_petosa_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA] + "(" + AppConfig.changeToCommanString(slugeGroup.totalUnit) + unit + ")");

	}
	
	
	public class YearEnterprisePriceTask extends AsyncTask<String, String, GSDailyInOut>
	{

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public YearEnterprisePriceTask(String _queryDate, String _dateStr)
		{
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_enterprise_price_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSDailyInOut doInBackground(String... params)
		{

			String branchID = AppConfig.SITE_JOOMYUNG + ",";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>YEAR_CUSTOMER_MONEY</GCType><GCQuery>" + branchID + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

//			Log.d(GSConfig.APP_DEBUG, "YearEnterprisePriceTask is called.");

			Socket soc = null;
			BufferedReader in = null;
			BufferedWriter out = null;
			String str;
			GSDailyInOut parsedData = null;

			try
			{

				// 소켓 연결
				soc = new Socket(AppConfig.SERVER_IP, AppConfig.SERVER_PORT);

				// 서버로부터의 입력 모드
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

				// 서버로의 전송 모드
				out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));

				out.write(AES.encrypt(message, AppConfig.SOCKET_KEY) + "\n");
				out.flush();

				// 서버로부터의 입력 받아오기
				str = in.readLine();
				if (str != null)
				{
					responseMessage = AES.decrypt(str, AppConfig.SOCKET_KEY);
					parsedData = XmlConverter.parseDaily(responseMessage);
				}

				// 종료 메시지
				out.write("exit\n");
				out.flush();

				// 접속 끊기
				soc.close();

			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

			return parsedData;

		}

		@Override
		protected void onPostExecute(GSDailyInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : onPostExecute() : result is null.");
				yi_month_enterprise_price_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_enterprise_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result, queryDate);
			}
			
			yi_month_enterprise_price_loading_indicator.setVisibility(View.GONE);

		}

	}

}
