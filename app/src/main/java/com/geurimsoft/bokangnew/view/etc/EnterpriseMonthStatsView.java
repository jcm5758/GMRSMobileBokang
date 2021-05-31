package com.geurimsoft.bokangnew.view.etc;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.StAdapter;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutDetail;
import com.geurimsoft.bokangnew.apiserver.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.data.GSMonthInOut;
import com.geurimsoft.bokangnew.client.SocketClient;

public class EnterpriseMonthStatsView
{

	private Activity mActivity;
	private int branchID;
	private int statsType;
	private String date;

	private Context mContext;

	private LinearLayout stock_layout, release_layout, petosa_layout;


	/**
	 * 월별-거래처별-일별 통계
	 * @param _activity		Acitivity
	 * @param branchID		지점 ID
	 * @param statsType	Unit / Money
	 * @param _date			검색일
	 */
	public EnterpriseMonthStatsView(Activity _activity, int branchID, int statsType, String _date)
	{

		this.mContext = _activity;
		this.mActivity = _activity;
		this.branchID = branchID;
		this.statsType = statsType;
		this.date = _date;

	}

	/**
	 * 뷰 생성
	 * @param _layout
	 * @param group
	 * @param serviceType
	 */
	public void makeStatsView(LinearLayout _layout, GSDailyInOutGroup group, final int serviceType, final int statsType)
	{

		String functionName = "makeStatsView()";

		try
		{

			if (group == null)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : makeStatsView() : group is null.");
				return;
			}

			int header_count = group.headerCount;
			String[] header_titles = group.header;
			int recordCount = group.recordCount;

			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " header_count : " + header_count);
			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " header_titles.length : " + header_titles.length);
			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " recordCount : " + recordCount);

			group.print();

			ArrayList<GSDailyInOutDetail> detailList = group.list;

			if(detailList == null || detailList.size() == 0)
			{
				Log.d(GSConfig.APP_DEBUG, "DEBUGGING : " + this.getClass().getName() + " : makeStatsView() : detailList is null.");
				return;
			}

			// 레이아웃 파라미터 지정
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			LinearLayout header_layout = new LinearLayout(mContext);
			header_layout.setLayoutParams(params);
			header_layout.setOrientation(LinearLayout.HORIZONTAL);

			// 헤더 설정
			for(int header_index = 0; header_index < header_count; header_index++)
			{
				TextView title_textview = makeMenuTextView(mContext, header_titles[header_index], "#ffffff", Gravity.CENTER);
				header_layout.addView(title_textview);
			}
Log.d("Babo" , "----------------------------------------------------------------check001");
			_layout.addView(header_layout);

			TextView stock_item_textview;


Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " detailList.size() : " + detailList.size());

			for(int stock_index = 0; stock_index < detailList.size(); stock_index++)
			{

				Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " stock_index : " + stock_index);

				GSDailyInOutDetail detail = detailList.get(stock_index);
				String[] stock_items = detail.getStringValues(statsType);

				Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " stock_index : 001");

				LinearLayout stock_row_layout = new LinearLayout(mContext);

				stock_row_layout.setLayoutParams(params);
				stock_row_layout.setOrientation(LinearLayout.HORIZONTAL);

				for(int i = 0; i < stock_items.length; i++)
				{

					Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " i : " + i);

					int gravity = 0;

					if(i == 0)
					{

						gravity = Gravity.CENTER;

						if(stock_index == recordCount - 1)
							stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
						else
						{

							stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);
							stock_item_textview.setTag(stock_items[i]);

							stock_item_textview.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									String name = (String) v.getTag();
									new EnterpriseDetailTask(name, branchID, statsType, serviceType, date).execute();
								}
							});

						}

						stock_row_layout.addView(stock_item_textview);

					}
					else
					{

						gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

						if(stock_index == recordCount - 1)
							stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
						else
							stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);

						stock_row_layout.addView(stock_item_textview);

					}

				}

				_layout.addView(stock_row_layout);

			}
Log.d("Babo" , "----------------------------------------------------------------check100");
			// 레이아웃 지정
			if (serviceType == GSConfig.MODE_STOCK)
				this.stock_layout = _layout;
			else if (serviceType == GSConfig.MODE_RELEASE)
				this.release_layout = _layout;
			else if (serviceType == GSConfig.MODE_PETOSA)
				this.petosa_layout = _layout;

		}
		catch(Exception ex)
		{
			Log.d(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplay() : " + ex.toString());
			return;
		}

	}
	
	private TextView makeMenuTextView(Context context, String str, String color, int gravity)
	{
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.menu_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor(color));
		tv.setTextSize(13);
		tv.setText(str);
		
		return tv;

	}
	
	private TextView makeRowTextView(Context context, String str, int gravity)
	{
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.row_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor("#000000"));
		tv.setTextSize(13);
		tv.setText(str);
		
		return tv;

	}

	/**
	 * 거래처별 상세 조회
	 */
	public class EnterpriseDetailTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;

		// 거래처 full name
		private String customerName;

		// 지점 ID
		private int branchID;

		// Unit / Money
		private int statsType;

		// 입고 / 출고 / 토사
		private int serviceType;

		private CustomProgressDialog progressDialog;

		public EnterpriseDetailTask(String customerName, int branchID, int statsType, int serviceType, String _queryDate)
		{
			this.queryDate = _queryDate;
			this.customerName = customerName;
			this.branchID = branchID;
			this.statsType = statsType;
			this.serviceType = serviceType;
		}

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();
			
			progressDialog = new CustomProgressDialog(mContext);
			progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			progressDialog.show();

		}

		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String query = this.branchID + "," + this.serviceType + "," + queryDate + "," + this.customerName;
			String message = null;

			if (this.statsType == GSConfig.STATE_AMOUNT)
				message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_CUSTOMER_DAY_UNIT</GCType><GCQuery>" + query + "</GCQuery></GEURIMSOFT>\n";
			else if (this.statsType == GSConfig.STATE_PRICE)
				message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_CUSTOMER_DAY_MONEY</GCType><GCQuery>" + query + "</GCQuery></GEURIMSOFT>\n";

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
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : Returned XML is null.");
				return null;
			}
			
			GSMonthInOut data = XmlConverter.parseMonth(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSMonthInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
				showErrorDialog();
			else
				showEnterprisePopup(result, this.queryDate, this.customerName, this.statsType, this.serviceType);
			
			progressDialog.dismiss();

		}

	}
	
	private PopupWindow popupWindow;
    private int mWidthPixels, mHeightPixels; 
	
	private void showEnterprisePopup(GSMonthInOut data, String queryDate, String customerName, int statsType, int serviceType)
	{

		WindowManager w = mActivity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);

		mWidthPixels = metrics.widthPixels;
		mHeightPixels = metrics.heightPixels;

		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
		{

			try
			{
				mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
				mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
			}
			catch (Exception ignored) {}

		}

		// 상태바와 메뉴바의 크기를 포함
		if (Build.VERSION.SDK_INT >= 17)
		{

			try
			{
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				mWidthPixels = realSize.x;
				mHeightPixels = realSize.y;
			}
			catch (Exception ignored) {}

		}

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.enterprise_popup, null);

		popupWindow = new PopupWindow(layout, mWidthPixels-20, LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

		TextView popup_date = (TextView)layout.findViewById(R.id.popup_date);
		LinearLayout popup_header_container = (LinearLayout)layout.findViewById(R.id.popup_header_container);

		ListView popup_listview = (ListView)layout.findViewById(R.id.popup_listview);
		popup_listview.setDividerHeight(0);

		Button popup_close_btn = (Button)layout.findViewById(R.id.popup_close_btn);

		popup_header_container.removeAllViews();

		String[] dates = date.split(",");

		String statsTypeStr = "";

		if(statsType == GSConfig.STATE_AMOUNT)
		{
			statsTypeStr = "(단위:루베)";
		}
		else
		{
			statsTypeStr = "(단위:천원)";
		}

		String modeStr = GSConfig.MODE_NAMES[serviceType] + " 현황";

		String dateStr = dates[0] + "년 " + dates[1] + "월 "+ customerName + "\n" + modeStr + statsTypeStr;
		popup_date.setText(dateStr);

		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(mActivity, data, statsType);
		statsHeaderAndFooterView.makeHeaderView(popup_header_container);

		StAdapter adapter = new StAdapter(mActivity, data, statsType);

		View foot = View.inflate(mActivity, R.layout.stats_foot, null);
		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

		statsHeaderAndFooterView.makeFooterView(footer_layout);
		popup_listview.addFooterView(foot);
		popup_listview.setAdapter(adapter);

		popup_close_btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				popupWindow.dismiss();
			}

		});

	}
	
	private void showErrorDialog()
	{

		AlertDialog.Builder errorDialog = new AlertDialog.Builder(mContext);
		errorDialog.setMessage(mContext.getString(R.string.loding_fail_msg));
		errorDialog.setPositiveButton(mContext.getString(R.string.dialog_confirm_button), new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}

		});
		errorDialog.show();

	}

}
