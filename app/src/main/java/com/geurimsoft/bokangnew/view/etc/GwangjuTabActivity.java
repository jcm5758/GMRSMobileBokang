package com.geurimsoft.bokangnew.view.etc;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.view.dailyfragment.GwangjuDailyPagerFragment;
import com.geurimsoft.bokangnew.view.monthfragment.GwangjuMonthPagerFragment;
import com.geurimsoft.bokangnew.view.yearfragment.GwangjuYearPagerFragment;
import com.geurimsoft.bokangnew.conf.AppConfig;

public class GwangjuTabActivity extends FragmentActivity {

	private FragmentTabHost tabHost;
	private LayoutInflater mInflater;
	
	private ActionBar actionBar;
	
	private SharedPreferences preferences;
	
	private int user_grade;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_tab_activity);
		
		AppConfig.activities.add(GwangjuTabActivity.this);
		
		AppConfig.DAY_STATS_YEAR = 0;
		AppConfig.DAY_STATS_MONTH = 0;
		AppConfig.DAY_STATS_DAY = 0;
		
		preferences = getSharedPreferences("user_account", Context.MODE_PRIVATE);
		
		user_grade = preferences.getInt("user_grade", 0);
		
		actionBar= getActionBar();
		actionBar.setTitle("통계(광주)");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		this.mInflater = LayoutInflater.from(GwangjuTabActivity.this);
		
		tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		
		tabHost.setup(this, getSupportFragmentManager(), R.id.tab_content_layout);
		
		if(user_grade >= 2) {
			tabHost.addTab(tabHost.newTabSpec("day").setIndicator("일보"), GwangjuDailyPagerFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("month").setIndicator("월보"), GwangjuMonthPagerFragment.class, null);
		} else {
			tabHost.addTab(tabHost.newTabSpec("day").setIndicator("일보"), GwangjuDailyPagerFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("month").setIndicator("월보"), GwangjuMonthPagerFragment.class, null);
			tabHost.addTab(tabHost.newTabSpec("year").setIndicator("연보"), GwangjuYearPagerFragment.class, null);
		}
	
		tabHost.setCurrentTab(0);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.gwangju_menu, menu);
		
		SharedPreferences preferences = getSharedPreferences("user_account", Context.MODE_PRIVATE);
		int grade = preferences.getInt("user_grade", 0);
		if(grade <= 1)
			menu.findItem(R.id.total_site_change).setVisible(true);
		else
			menu.findItem(R.id.total_site_change).setVisible(false);
			
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if(id == android.R.id.home) {
			finish();
		} else if(id == R.id.gwangju_site_change) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("user_department", 0);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(GwangjuTabActivity.this, YonginTabActivity.class);
			startActivity(intent);
			
			
		} else if(id == R.id.total_site_change) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("user_department", 2);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(GwangjuTabActivity.this, TotalTabActivity.class);
			startActivity(intent);
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
}
