/**
 * Main Activity
 *
 * 2021. 05. 28 리뉴얼
 *
 * Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geurimsoft.bokangnew.apiserver.RetrofitService;
import com.geurimsoft.bokangnew.apiserver.RetrofitUtil;
import com.geurimsoft.bokangnew.apiserver.data.RequestData;
import com.geurimsoft.bokangnew.apiserver.data.UserInfo;
import com.geurimsoft.bokangnew.apiserver.data.UserRightData;
import com.geurimsoft.bokangnew.data.GSBranch;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.data.GSDailyInOut;
import com.geurimsoft.bokangnew.data.GSDailyInOutGroup;
import com.geurimsoft.bokangnew.dialog.ApiLoadingDialog;
import com.geurimsoft.bokangnew.dialog.ApiReconnectDialog;
import com.geurimsoft.bokangnew.dialog.DialogListener;
import com.geurimsoft.bokangnew.view.BackPressHandler;
import com.geurimsoft.bokangnew.view.etc.GwangjuTabActivity;
import com.geurimsoft.bokangnew.view.etc.JoomyungTabActivity;
import com.geurimsoft.bokangnew.view.etc.TotalTabActivity;
import com.geurimsoft.bokangnew.view.etc.YonginTabActivity;
import com.geurimsoft.bokangnew.conf.AppConfig;
import com.geurimsoft.bokangnew.data.CCTV;
import com.geurimsoft.bokangnew.data.Place;
import com.geurimsoft.bokangnew.data.XmlConverter;
import com.geurimsoft.bokangnew.data.GSUser;
import com.geurimsoft.bokangnew.client.SocketClient;
import com.geurimsoft.bokangnew.util.ItemXmlParser;
import com.geurimsoft.bokangnew.view.joomyung.FragmentDailyAmount;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppMain extends Activity
{

//	private RetrofitService service = RetrofitUtil.getService();
//	private Disposable disposable;

//	private ApiLoadingDialog loadingDialog;
//	private ApiReconnectDialog reconnectDialog;

	// User Layout 변수
	EditText edtLogin, edtPasswd;
	CheckBox chkAutoLogin;
	LinearLayout layoutlogin, btnlayout;
	Button btnlogin, change_site_btn; // add etc button
	ImageView ivMenu01, ivMenu02;

	TextView change_site_title;
	
	private boolean isLogin = true;
	public static ArrayList<Place> PLACE_LIST = null;
	public static int CURRENT_PLACE_INDEX = 0;
	public static ArrayList<CCTV> CURRENT_CCTV_LIST = null;
	
	private Context mContext;
	
	private SharedPreferences pref;
	
	private long backKeyPressedTime = 0;
	
	private Toast appFinishedToast;

	private BackPressHandler backPressHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		GSConfig.context = this;
		
		AppConfig.activities.add(AppMain.this);
		
		backPressHandler = new BackPressHandler(this);
		
		setUserInterface();

		// 자동 로그인 체크시
		autoCheck();

		// 앱 버전 확인
		//appVersionCheck();

	}

	/**
	 * 위젯 받아오기
	 */
	public void setUserInterface()
	{

//		reconnectDialog = new ApiReconnectDialog(this);
//		loadingDialog = new ApiLoadingDialog(this);

		pref = getSharedPreferences("user_account", Context.MODE_PRIVATE);

		// 아이디
		edtLogin = (EditText) this.findViewById(R.id.edtlogin);
		edtLogin.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		edtLogin.setPrivateImeOptions("defaultInputmode=english");

		// 비밀번호
		edtPasswd = (EditText) this.findViewById(R.id.edtpasswd);
		edtPasswd.setImeOptions(EditorInfo.IME_ACTION_DONE);
		edtPasswd.setPrivateImeOptions("defaultInputmode=english");

		// 자동 로그인
		chkAutoLogin = (CheckBox) this.findViewById(R.id.chkAutoLogin);

		// 로그인 버튼
		btnlogin = (Button) this.findViewById(R.id.btnlogin);
		
		// 이벤트 등록
		btnlogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtPasswd.getWindowToken(), 0);

				checkUser();

			}

		});

	}

	/**
	 * 자동 로그인 확인 시 기본 정보 저장
	 */
	private void autoCheck()
	{

		pref = getSharedPreferences("user_account", Context.MODE_PRIVATE);

		String sId = pref.getString("userID", null);
		String sPass = pref.getString("userPWD", null);

		boolean auto_chcek = pref.getBoolean("outo_chcek", false);

		chkAutoLogin.setChecked(auto_chcek);
		
		if (auto_chcek == true)
		{
			edtLogin.setText(sId);
			edtPasswd.setText(sPass);
		}
		else
		{
			SharedPreferences.Editor removeEditor = pref.edit();
			removeEditor.remove("userID");
			removeEditor.remove("userPWD");
			removeEditor.remove("outo_chcek");
			removeEditor.commit();
		}

	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	/**
	 * 로그인 입력 정보 확인
	 * @return
	 */
	public boolean checkUser()
	{

		String sId = edtLogin.getText().toString();
		String sPass = edtPasswd.getText().toString();

		if (sId == null || sId.trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (sPass == null || sPass.trim().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

		return LoginCheck(sId, sPass);

	}

	/**
	 * 로그인 권한 확인
	 * @param userID    	User ID
	 * @param userPWD		User PWD
	 * @return
	 */
	private boolean LoginCheck(String userID, String userPWD)
	{

		String functionName = "getLogin()";

		String url = GSConfig.API_SERVER_ADDR + "API";
		RequestQueue requestQueue = Volley.newRequestQueue(GSConfig.context);

		StringRequest request = new StringRequest(
				Request.Method.POST,
				url,
				//응답을 잘 받았을 때 이 메소드가 자동으로 호출
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "응답 -> " + response);
						parseData(userID, userPWD, response);
					}
				},
				//에러 발생시 호출될 리스너 객체
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "에러 -> " + error.getMessage());
					}
				}
		) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("GSType", "LOGIN");
				params.put("GSQuery", "{ \"UserID\" : \"" + userID + "\", \"UserPWD\" : \"" + userPWD + "\"}");
				return params;
			}
		};

		request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
		requestQueue.add(request);

		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "요청 보냄.");

		return true;

	}

	public void parseData(String userID, String userPWD, String msg)
	{

		String functionName = "parseData()";

		Gson gson = new Gson();

		UserInfo userInfo = gson.fromJson(msg, UserInfo.class);

		if (userInfo == null)
		{
			Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + "userInfo is null.");
			return;
		}

//		Log.e(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(this.getClass().getName(), functionName) + userInfo.getUserinfo().get(0).getName());

		GSConfig.CURRENT_USER = userInfo;

		if (GSConfig.CURRENT_USER.isUserInfoNull() || GSConfig.CURRENT_USER.isUserRightNull())
			return;

		SharedPreferences.Editor editor = pref.edit();
		editor.putString("userID", userID);
		editor.putString("userPWD", userPWD);
		editor.putBoolean("outo_chcek", chkAutoLogin.isChecked());
		editor.commit();

		Toast.makeText(getApplicationContext(),GSConfig.CURRENT_USER.getUserinfo().get(0).getName() + getString(R.string.login_success), Toast.LENGTH_SHORT).show();

		// 현장 선택
		showBranch();

	}


	/**
	 * 현장 선택
	 */
	private void showBranch()
	{

		if (GSConfig.CURRENT_USER == null || GSConfig.CURRENT_USER.isUserInfoNull() || GSConfig.CURRENT_USER.isUserRightNull())
			return;

		String functionName = "siteAlert()";

		ArrayList<UserRightData> urData = GSConfig.CURRENT_USER.getUserright();

		String[] commandArray = new String[ urData.size() ];

		for(int i = 0; i < urData.size(); i++)
		{
			commandArray[i] = urData.get(i).getBranName();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(getString(R.string.site_msg));
		builder.setItems(commandArray, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + "." + functionName + " : which : " + which);

				if (GSConfig.CURRENT_USER.getUserRightData(which).getUr01() != 1)
				{
					Toast.makeText(getApplicationContext(),"지점에 로그인 권한이 없습니다.", Toast.LENGTH_SHORT).show();
					return;
				}

				GSConfig.CURRENT_BRANCH = new GSBranch(urData.get(which).getBranID(), urData.get(which).getBranName(), urData.get(which).getBranShortName());

				Intent intent = new Intent(AppMain.this, GSConfig.Activity_LIST[which]);
				intent.putExtra("branName", GSConfig.CURRENT_BRANCH.getBranchShortName());

				startActivity(intent);

				dialog.dismiss();

			}

		});

		AlertDialog alert = builder.create();
		alert.show();

	}
	
	private void appVersionCheck()
	{
		new VersionCheckTask().execute();
	}
	
	public int getVersionCode(Context context)
	{
		 
		try
		{
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		}
		catch (NameNotFoundException e)
		{
			return 0;
		}

	}

	/**
	 * 앱 업데이트를 위해 Gooegle Market으로 이동 혹은 취소
	 */
	private void showUpdateDialog()
	{

		AlertDialog.Builder successDia = new AlertDialog.Builder(this);
		successDia.setMessage(this.getString(R.string.update_msg));

		successDia.setPositiveButton(this.getString(R.string.update_yesbutton), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.geurimsoft.bokangnew"));
				startActivity(intent);
			}
		});

		successDia.setNegativeButton(this.getString(R.string.update_canclebutton), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		successDia.show();

	}

//	/**
//	 * 뒤로가기 버튼 클릭시
//	 */
//	@Override
//	public void onBackPressed()
//	{
//
//		if(System.currentTimeMillis() > backKeyPressedTime + 2000)
//		{
//			backKeyPressedTime = System.currentTimeMillis();
//			appFinishedToast = Toast.makeText(this, getString(R.string.app_finished_msg), Toast.LENGTH_LONG);
//			appFinishedToast.show();
//			return;
//		}
//
//		if(System.currentTimeMillis() <= backKeyPressedTime + 2000)
//		{
//
//			if(AppConfig.activities.size() > 0)
//			{
//				for(int actIndex = 0; actIndex < AppConfig.activities.size(); actIndex++)
//					AppConfig.activities.get(actIndex).finish();
//			}
//
//			appFinishedToast.cancel();
//
//		}
//	}

	/**
	 * 앱 버전 확인
	 */
	public class VersionCheckTask extends AsyncTask<String, String, String>
	{

		private ProgressDialog progress;
		private String newVersionCode;

		public VersionCheckTask() {}

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();

			progress = new ProgressDialog(AppMain.this);
			progress.setMessage(getString(R.string.update_check));
			progress.show();

		}

		@Override
		protected String doInBackground(String... params)
		{

			try
			{

				URL url = new URL("http://" + AppConfig.SERVER_IP + "/bokang_new/app_version.txt");
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				
				if (conn != null)
				{

					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					
					if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
					{

						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						newVersionCode = br.readLine();
							
						br.close();
					}

					conn.disconnect();

				}

			}
			catch (Exception ex)
			{
				Log.e("BOKANG", "newVersionCode ex : "+ex.getMessage());
			}

			return newVersionCode;

		}

		@Override
		protected void onPostExecute(String result)
		{

			super.onPostExecute(result);
			
			progress.dismiss();
			
			int versionCode = getVersionCode(AppMain.this);

			// 서버 버전보다 오래된 것이면 구글 마켓으로 이동 물어보기
			if(versionCode < Integer.parseInt(result))
			{
				showUpdateDialog();
			}

		}

	}

}
