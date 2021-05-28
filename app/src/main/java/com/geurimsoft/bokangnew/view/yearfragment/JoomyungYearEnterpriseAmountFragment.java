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
import com.geurimsoft.bokangnew.view.etc.EnterpriseYearStatsView;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.grms.data.GSDailyInOut;
import com.geurimsoft.grms.data.GSDailyInOutGroup;
import com.geurimsoft.socket.client.AES;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class JoomyungYearEnterpriseAmountFragment extends Fragment
{

	private LinearLayout yi_month_enterprise_amount_income_empty_layout, yi_month_enterprise_amount_release_empty_layout, yi_month_enterprise_amount_petosa_empty_layout;
	private LinearLayout yi_month_enterprise_amount_loading_indicator, yi_month_enterprise_amount_loading_fail;

	private TextView yi_month_enterprise_amount_date, yi_month_enterprise_amount_income_title, yi_month_enterprise_amount_release_title, yi_month_enterprise_amount_petosa_title;

	private YearEnterpriseAmountTask  monthEnterpriseAmountTask;

	public JoomyungYearEnterpriseAmountFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_enterprise_amount, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.yi_month_enterprise_amount_income_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_income_empty_layout);
		this.yi_month_enterprise_amount_release_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_release_empty_layout);
		this.yi_month_enterprise_amount_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_petosa_empty_layout);
		
		this.yi_month_enterprise_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_loading_indicator); 
		this.yi_month_enterprise_amount_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_loading_fail);
		
		this.yi_month_enterprise_amount_date = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_date); 
		this.yi_month_enterprise_amount_income_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_income_title); 
		this.yi_month_enterprise_amount_release_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_release_title); 
		this.yi_month_enterprise_amount_petosa_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_petosa_title);

		makeMonthEnterpriseAmountData(AppConfig.DAY_STATS_YEAR);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthEnterpriseAmountTask.cancel(true);
	}

	private void makeMonthEnterpriseAmountData(int _year)
	{

		String dateStr = _year + "년 입출고 현황";
		this.yi_month_enterprise_amount_date.setText(dateStr);

		monthEnterpriseAmountTask = new YearEnterpriseAmountTask(String.valueOf(_year), dateStr);
		monthEnterpriseAmountTask.execute();

	}
	
	private void setDisplay(GSDailyInOut data, String _date)
	{

		if (data == null || data.list == null)
		{
			Log.d(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : setDisplay() is data is null.");
			return;
		}

		try
		{

			GSDailyInOutGroup inputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK]);
			GSDailyInOutGroup outputGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE]);
			GSDailyInOutGroup slugeGroup = data.findByServiceType(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA]);

			String unit = getString(R.string.unit_lube);

			yi_month_enterprise_amount_income_empty_layout.removeAllViews();
			yi_month_enterprise_amount_release_empty_layout.removeAllViews();
			yi_month_enterprise_amount_petosa_empty_layout.removeAllViews();

			EnterpriseYearStatsView statsView = new EnterpriseYearStatsView(getActivity(), AppConfig.SITE_JOOMYUNG, AppConfig.STATE_AMOUNT, _date);

			if (inputGroup != null)
			{
				statsView.makeStatsView(yi_month_enterprise_amount_income_empty_layout, inputGroup, AppConfig.MODE_STOCK, AppConfig.STATE_AMOUNT);
				yi_month_enterprise_amount_income_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_STOCK] + "(" + AppConfig.changeToCommanString(inputGroup.totalUnit) + unit + ")");
			}

			if (outputGroup != null)
			{
				statsView.makeStatsView(yi_month_enterprise_amount_release_empty_layout, outputGroup, AppConfig.MODE_RELEASE, AppConfig.STATE_AMOUNT);
				yi_month_enterprise_amount_release_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_RELEASE] + "(" + AppConfig.changeToCommanString(outputGroup.totalUnit) + unit + ")");
			}

			if (slugeGroup != null)
			{
				statsView.makeStatsView(yi_month_enterprise_amount_petosa_empty_layout, slugeGroup, AppConfig.MODE_PETOSA, AppConfig.STATE_AMOUNT);
				yi_month_enterprise_amount_petosa_title.setText(AppConfig.MODE_NAMES[AppConfig.MODE_PETOSA] + "(" + AppConfig.changeToCommanString(slugeGroup.totalUnit) + unit + ")");
			}

		}
		catch(Exception ex)
		{
			Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}

	public class YearEnterpriseAmountTask extends AsyncTask<String, String, GSDailyInOut>
	{

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public YearEnterpriseAmountTask(String _queryDate, String _dateStr)
		{
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_enterprise_amount_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSDailyInOut doInBackground(String... params)
		{

			String department = AppConfig.SITE_JOOMYUNG + ",";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>YEAR_CUSTOMER_UNIT</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

//			Log.d(AppConfig.TAG, "YearEnterpriseAmountTask is called.");

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
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
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
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : onPostExecute() : result is null.");
				yi_month_enterprise_amount_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_enterprise_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result, queryDate);
			}
			
			yi_month_enterprise_amount_loading_indicator.setVisibility(View.GONE);

		}

	}

}
