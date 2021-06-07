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
import com.geurimsoft.bokangnew.data.GSConfig;

public class TotalTabActivity extends FragmentActivity {

	private FragmentTabHost tabHost;
	private LayoutInflater mInflater;
	
	private ActionBar actionBar;
	
	private SharedPreferences preferences;
	
	private int user_grade;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_tab_activity);
		
//		AppConfig.activities.add(TotalTabActivity.this);
		
		GSConfig.DAY_STATS_YEAR = 0;
		GSConfig.DAY_STATS_MONTH = 0;
		GSConfig.DAY_STATS_DAY = 0;
		
		preferences = getSharedPreferences("user_account", Context.MODE_PRIVATE);
		
		user_grade = preferences.getInt("user_grade", 0);
		
		actionBar= getActionBar();
		actionBar.setTitle("통합");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		this.mInflater = LayoutInflater.from(TotalTabActivity.this);
		
		tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		
		tabHost.setup(this, getSupportFragmentManager(), R.id.tab_content_layout);
		
//		tabHost.addTab(tabHost.newTabSpec("day").setIndicator("일보"), TotalDailyPagerFragment.class, null);
//		tabHost.addTab(tabHost.newTabSpec("month").setIndicator("월보"), TotalMonthPagerFragment.class, null);
//		tabHost.addTab(tabHost.newTabSpec("year").setIndicator("연보"), TotalYearPagerFragment.class, null);
	
		tabHost.setCurrentTab(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.total_menu, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if(id == android.R.id.home) {
			finish();
		} else if(id == R.id.yongin_site_change) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("user_department", 0);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(TotalTabActivity.this, YonginTabActivity.class);
			startActivity(intent);
			
			
		} else if(id == R.id.gwangju_site_change) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("user_department", 1);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(TotalTabActivity.this, GwangjuTabActivity.class);
			startActivity(intent);
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
}
