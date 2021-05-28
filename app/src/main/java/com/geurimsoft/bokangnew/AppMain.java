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

public class AppMain extends Activity
{

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		AppConfig.activities.add(AppMain.this);
		
		this.mContext = this;
		
		setUserInterface();

		// 자동 로그인 체크시
		autoCheck();

		// CCTV 정보 가져오기
		getServerContents();

		// 앱 버전 확인
		appVersionCheck();

	}

	/**
	 * 위젯 받아오기
	 */
	public void setUserInterface()
	{

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

		// 로그인 레이아웃
		layoutlogin = (LinearLayout) this.findViewById(R.id.layoutlogin);
		btnlayout = (LinearLayout) this.findViewById(R.id.btnlayout);
		
		// 로그인 버튼
		btnlogin = (Button) this.findViewById(R.id.btnlogin);
		
		// 이미지 뷰
		ivMenu01 = (ImageView) this.findViewById(R.id.ivMenu01);
		ivMenu02 = (ImageView) this.findViewById(R.id.ivMenu02);

		change_site_btn = (Button) this.findViewById(R.id.change_site_btn);
		change_site_title = (TextView) this.findViewById(R.id.change_site_title);

		change_site_btn.setVisibility(View.INVISIBLE);
		change_site_title.setVisibility(View.INVISIBLE);

		// 이벤트 등록
		btnlogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtPasswd.getWindowToken(), 0);

				if (checkUser())
				{
					changeView(false);
				}

			}

		});

		ivMenu01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				pref = getSharedPreferences("user_account", Context.MODE_PRIVATE);
				int department = pref.getInt("user_department", 0);

				if(department != 1)
					return;

				PackageManager packageManager = getPackageManager();

				try
				{
					packageManager.getApplicationInfo("com.samsung.ipolis", PackageManager.GET_META_DATA);

					Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.samsung.ipolis");
					startActivity(intent);

				}
				catch (Exception e)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.samsung.ipolis"));
					startActivity(intent);
				}

			}

		});

		ivMenu02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d(AppConfig.TAG, "R.id.ivMenu02 is clicked.");

				pref = getSharedPreferences("user_account", Context.MODE_PRIVATE);
				int branchID = pref.getInt("branch_id", 0);

//				Log.d(AppConfig.TAG, "BranchID : " + branchID);

				if(branchID == 1)
				{
					Intent intent = new Intent(AppMain.this, YonginTabActivity.class);
					startActivity(intent);
				}
				else if(branchID == 2)
				{
					Intent intent = new Intent(AppMain.this, GwangjuTabActivity.class);
					startActivity(intent);
				}
				else if(branchID == 3)
				{
					Intent intent = new Intent(AppMain.this, JoomyungTabActivity.class);
					startActivity(intent);
				}
				else if(branchID == 4)
				{
					Intent intent = new Intent(AppMain.this, TotalTabActivity.class);
					startActivity(intent);
				}

			}

		});

		// 현장 선택 버튼 이벤트
		change_site_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d(AppConfig.TAG, "R.id.change_site_btn is clicked.");

				int branchID = pref.getInt("branch_id", 0);

				if(branchID == 1)
				{
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt("branch_id", 1);
					editor.commit();
					change_site_title.setText(getString(R.string.site_yo));
				}
				else if(branchID == 2)
				{
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt("branch_id", 2);
					editor.commit();
					change_site_title.setText(getString(R.string.site_gw));
				}
				else if(branchID == 3)
				{
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt("branch_id", 3);
					editor.commit();
					change_site_title.setText(getString(R.string.site_ju));
				}
				else if(branchID == 4)
				{
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt("branch_id", 4);
					editor.commit();
					change_site_title.setText(getString(R.string.site_total));
				}

			}

		});

		this.changeView(isLogin);

	}

	/**
	 * 자동 로그인 확인 시 기본 정보 저장
	 */
	private void autoCheck()
	{

		pref = getSharedPreferences("user_account", Context.MODE_PRIVATE);

		String sId = pref.getString("sId", null);
		String sPass = pref.getString("sPass", null);

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
			removeEditor.remove("sId");
			removeEditor.remove("sPass");
			removeEditor.remove("outo_chcek");
			removeEditor.commit();
		}

	}
	
	@Override
	protected void onResume()
	{

		super.onResume();
		
//		int site = pref.getInt("user_department", 0);
//
//		if(site == 0)
//			change_site_title.setText(getString(R.string.site_yo));
//		else if(site == 1)
//			change_site_title.setText(getString(R.string.site_gw));
//		else if(site == 2)
//			change_site_title.setText(getString(R.string.site_total));

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
			alert(getString(R.string.login_error_id_title), getString(R.string.login_error_id_msg));
			return false;
		}

		if (sPass == null || sPass.trim().length() == 0)
		{
			alert(getString(R.string.login_error_pw_title), getString(R.string.login_error_pw_msg));
			return false;
		}

		return LoginCheck(sId, sPass);

	}

	/**
	 * 로그인 권한 확인
	 * @param sId    	User ID
	 * @param sPass		User PWD
	 * @return
	 */
	private boolean LoginCheck(String sId, String sPass)
	{
		
		try
		{

			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>LOGIN</GCType><GCQuery>"+ sId + "," + sPass + "</GCQuery></GEURIMSOFT>\n";

			SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);

			sc.start();
			sc.join();

			String msg = sc.getReturnString();

			if (msg == null || msg.equals(""))
			{
				Log.d(AppConfig.TAG, "Returned xml is null.");
				return false;
			}

			if (msg.equals("Fail"))
			{
				Log.d(AppConfig.TAG, "Id & pwd is wrong");
				alert("Login Fail", getString(R.string.login_error_idpw));
				return false;
			}

			GSUser user = XmlConverter.parseUserInfo(msg);
			
			if (user == null)
			{

				Toast.makeText(getApplicationContext(), getString(R.string.login_error_idpw2), Toast.LENGTH_SHORT);
				Log.d(AppConfig.TAG, "user is null.");
				
				return false;
				
			}

			SharedPreferences.Editor editor = pref.edit();
			editor.putString("sId", sId);
			editor.putString("sPass", sPass);
			editor.putBoolean("outo_chcek", chkAutoLogin.isChecked());
			editor.commit();

			Toast.makeText(getApplicationContext(),user.getName() + getString(R.string.login_success), Toast.LENGTH_SHORT).show();

			// 현장 선택
			siteAlert();

			return true;

		}
		catch (Exception e)
		{
			Log.e(AppConfig.TAG, "checkLogin() : " + e.toString());
			return false;
		}

	}
	/**
	 * 알람 다이얼로그
	 * 
	 * @param title
	 * @param mesg
	 */
	public void alert(String title, String mesg)
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(AppMain.this);
		alert.setPositiveButton(title, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
			}
		});

		alert.setMessage(mesg);
		alert.show();

	}

	/**
	 * 현장 선택
	 */
	private void siteAlert()
	{
		
//		String[] commandArray = null;
//
//		commandArray = getResources().getStringArray(R.array.super_grade_site_array);
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setCancelable(false);
//		builder.setTitle(getString(R.string.site_msg));
//		builder.setItems(commandArray, new DialogInterface.OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//
//				if(which == 0)
//				{
//
//					SharedPreferences.Editor editor = pref.edit();
//					editor.putInt("branch_id", 1);
//					editor.commit();
//
//					dialog.dismiss();
//
//					change_site_title.setText(getString(R.string.site_yo));
//
//				}
//				else if(which == 1)
//				{
//
//					SharedPreferences.Editor editor = pref.edit();
//					editor.putInt("branch_id", 2);
//					editor.commit();
//					dialog.dismiss();
//
//					change_site_title.setText(getString(R.string.site_gw));
//
//				}
//				else if(which == 2)
//				{
//
//					SharedPreferences.Editor editor = pref.edit();
//					editor.putInt("branch_id", 3);
//					editor.commit();
//					dialog.dismiss();
//
//					change_site_title.setText(getString(R.string.site_ju));
//
//				}
//				else if(which == 3)
//				{
//
//					SharedPreferences.Editor editor = pref.edit();
//					editor.putInt("branch_id", 4);
//					editor.commit();
//					dialog.dismiss();
//
//					change_site_title.setText(getString(R.string.site_total));
//
//				}
//
//				dialog.dismiss();
//
//			}
//
//		});
//
//		AlertDialog alert = builder.create();
//		alert.show();

		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("branch_id", 3);
		editor.commit();

	}
	
	/*
	 * 서버로부터 컨텐츠를 가지고 온다.
	 */
	public void getServerContents()
	{

		PLACE_LIST = ItemXmlParser.getPlaceList(getApplicationContext(), AppConfig.XML_DIR + "/" + AppConfig.PLACE_LIST_FILE);

		for (int idx = 0; idx < PLACE_LIST.size(); idx++)
		{

			Place thisPlace = PLACE_LIST.get(idx);
			ArrayList<CCTV> thisCCTVList = ItemXmlParser.getCCTVList(getApplicationContext(), AppConfig.XML_DIR + "/"
							+ thisPlace.getCctvListFile());
			thisPlace.setCctvList(thisCCTVList);

		}

	}

	/**
	 * selLayout - true : 로그인 화면 false : 메뉴화면
	 * 
	 * @param selLayout
	 */
	private void changeView(boolean selLayout)
	{

		isLogin = selLayout;

		if (selLayout)
		{
			layoutlogin.setVisibility(View.VISIBLE);
			btnlayout.setVisibility(View.GONE);
		}
		else
		{
			layoutlogin.setVisibility(View.GONE);
			btnlayout.setVisibility(View.VISIBLE);
		}

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

	/**
	 * 뒤로가기 버튼 클릭시
	 */
	@Override
	public void onBackPressed()
	{
		
		if(System.currentTimeMillis() > backKeyPressedTime + 2000)
		{
			backKeyPressedTime = System.currentTimeMillis();
			appFinishedToast = Toast.makeText(this, getString(R.string.app_finished_msg), Toast.LENGTH_LONG);
			appFinishedToast.show();
			return;
		}
		
		if(System.currentTimeMillis() <= backKeyPressedTime + 2000)
		{
			
			if(AppConfig.activities.size() > 0)
			{
				for(int actIndex = 0; actIndex < AppConfig.activities.size(); actIndex++)
					AppConfig.activities.get(actIndex).finish();
			}
			
			appFinishedToast.cancel();
			
		}
	}

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
