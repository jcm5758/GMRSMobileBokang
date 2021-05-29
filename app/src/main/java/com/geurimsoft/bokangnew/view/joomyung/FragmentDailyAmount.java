/**
 * 일보 수량
 *
 * 2021. 05. 28. 리뉴얼
 *
 *  Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view.joomyung;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.apiserver.RetrofitService;
import com.geurimsoft.bokangnew.apiserver.RetrofitUtil;
import com.geurimsoft.bokangnew.apiserver.data.RequestData;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.dialog.ApiLoadingDialog;
import com.geurimsoft.bokangnew.dialog.ApiReconnectDialog;
import com.geurimsoft.bokangnew.dialog.DialogListener;
import com.geurimsoft.bokangnew.util.GSUtil;
import com.geurimsoft.bokangnew.view.etc.StatsView;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSDailyInOut;
import com.geurimsoft.bokangnew.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.client.SocketClient;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentDailyAmount extends Fragment
{


	private RetrofitService service = RetrofitUtil.getService();
	private Disposable disposable;
	private ApiLoadingDialog loadingDialog;
	private ApiReconnectDialog reconnectDialog;

	private LinearLayout income_empty_layout, release_empty_layout, petosa_empty_layout;
	private TextView stats_daily_date, daily_income_title, daily_release_title, daily_petosa_title;

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

		String functionName = "onResume()";
		
		View view = this.getView();

		if(view == null)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + " StatsDailyAmountFragment onResume View is null");
			return;
		}
		
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
	 *
	 * @param _year				연도
	 * @param _monthOfYear		월
	 * @param _dayOfMonth		일
	 */
	public void makeDailyAmountData(int _year, int _monthOfYear, int _dayOfMonth)
	{

		String functionName = "makeDailyAmountData()";

		reconnectDialog = new ApiReconnectDialog(GSConfig.context);
		loadingDialog = new ApiLoadingDialog(GSConfig.context);

		try
		{

			String str = _year + "년 " + _monthOfYear + "월 " + _dayOfMonth + "일 입출고 현황";
			Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + _year + "년 " + _monthOfYear + "월 " + _dayOfMonth + "일");

//		if(stats_daily_date == null)
//			Log.e(GSConfig.APP_DEBUG, "StatsDailyAmountFragment makeDailyAmountData stats_daily_date is null");

			this.stats_daily_date.setText(str);

			String queryDate = GSUtil.makeStringFromDate(_year, _monthOfYear, _dayOfMonth);
			String qryContent = "Unit";

			this.getData(queryDate, qryContent);

//			dailyAmountTask = new DailyAmountTask(queryDate);
//			dailyAmountTask.execute();

		}
		catch(Exception ex)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + ex.toString());
			return;
		}

	}
	
	private void setDisplayData(GSDailyInOut dio)
	{

		if(getActivity() == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : Activity is null.");
			return;
		}

		if (dio == null)
		{
			Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : setDisplayData() : dio is null.");
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

	private void getData(String serchDate, String qryContent)
	{

		String functionName = "getData()";

		HashMap<String, String> map = new HashMap<>();
		map.put("branchID", String.valueOf(GSConfig.CURRENT_BRANCH.getBranchID()));
		map.put("serchDate", serchDate);
		map.put("qryContent", qryContent);

		RequestData jData = new RequestData("DAY", map);

		this.disposable = service.apiLogin(jData)

		.retryWhen(throwableObservable -> throwableObservable

				.zipWith(Observable.range(1, 2), (throwable, count) -> count)

				.flatMap(count -> {

					Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : connect retry " + count + " times");

					if (count < 2) {
						Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : server reconnecting...");
						return Observable.timer(GSConfig.API_RECONNECT, TimeUnit.SECONDS);
					}

					Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : server disconnected...");

					return Observable.error(new Exception());

				})
		)
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.doOnSubscribe(disposable1 -> {
			loadingDialog.show();
		})
		.doOnTerminate(() -> {
			loadingDialog.dismiss();
		})
		.subscribe(

				item -> {

					Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : status : " + item.getStatus());

				},
				e -> {

					Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : onError : " + e);

					reconnectDialog.setDialogListener(new DialogListener()
					{

						@Override
						public void onPositiveClicked()
						{
							getData(serchDate, qryContent);
						}

						@Override
						public void onNegativeClicked()
						{
							loadingDialog.dismiss();
						}

					});

					reconnectDialog.show();

				},
				() -> {
					Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : onComplete");
				}

		);

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
				Log.e(GSConfig.APP_DEBUG, "ERROR : " + this.getClass().getName() + " : doInBackground() : Response is Fail.");
				return null;
			}

            dio = XmlConverter.parseDaily(mss);

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
