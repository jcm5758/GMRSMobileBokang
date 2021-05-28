/**
 * 용인 현장 메인 탭
 */
package com.geurimsoft.bokangnew;

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

import com.geurimsoft.bokangnew.dailyfragment.JoomyungDailyPagerFragment;
import com.geurimsoft.bokangnew.monthfragment.JoomyungMonthPagerFragment;
import com.geurimsoft.bokangnew.yearfragment.JoomyungYearPagerFragment;
import com.geurimsoft.conf.AppConfig;

public class JoomyungTabActivity extends FragmentActivity
{

	private FragmentTabHost tabHost;
	private LayoutInflater mInflater;
	
	private ActionBar actionBar;
	
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.stats_tab_activity);
		
		AppConfig.activities.add(JoomyungTabActivity.this);
		
		AppConfig.DAY_STATS_YEAR = 0;
		AppConfig.DAY_STATS_MONTH = 0;
		AppConfig.DAY_STATS_DAY = 0;
		
		this.preferences = getSharedPreferences("user_account", Context.MODE_PRIVATE);

		this.actionBar= getActionBar();
		this.actionBar.setTitle("통계(주명)");
		this.actionBar.setHomeButtonEnabled(true);
		this.actionBar.setDisplayShowHomeEnabled(false);
		this.actionBar.setDisplayHomeAsUpEnabled(true);
		
		this.mInflater = LayoutInflater.from(JoomyungTabActivity.this);

		this.tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		this.tabHost.setup(this, getSupportFragmentManager(), R.id.tab_content_layout);
		this.tabHost.addTab(tabHost.newTabSpec("day").setIndicator("일보"), JoomyungDailyPagerFragment.class, null);
		this.tabHost.addTab(tabHost.newTabSpec("month").setIndicator("월보"), JoomyungMonthPagerFragment.class, null);
		this.tabHost.addTab(tabHost.newTabSpec("year").setIndicator("연보"), JoomyungYearPagerFragment.class, null);
		this.tabHost.setCurrentTab(0);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
//		getMenuInflater().inflate(R.menu.yongin_menu, menu);
//		menu.findItem(R.id.total_site_change).setVisible(false);
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		int id = item.getItemId();
		
		if(id == android.R.id.home)
		{
			finish();
		}
		else if(id == R.id.yongin_site_change)
		{

			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("branch_id", 2);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(JoomyungTabActivity.this, GwangjuTabActivity.class);
			startActivity(intent);
			
		}
		else if(id == R.id.total_site_change)
		{

			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt("branch_id", 4);
			editor.commit();
			
			finish();
			
			Intent intent = new Intent(JoomyungTabActivity.this, TotalTabActivity.class);
			startActivity(intent);
			
		}

		return super.onOptionsItemSelected(item);

	}
	
}
