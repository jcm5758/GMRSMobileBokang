package com.geurimsoft.bokangnew.view.monthfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.view.etc.MonthDatePickerDialog;
import com.geurimsoft.bokangnew.conf.AppConfig;

import java.util.ArrayList;
import java.util.Calendar;

public class JoomyungMonthPagerFragment extends Fragment
{
	
	private Calendar calendar = Calendar.getInstance();
	private int currentYear, currentMonth;
	
	private PagerTabStrip statsTabStrip;
	private ViewPager statsPager;
	private StatsPagerAdapter statsPagerAdapter;
	
	private ArrayList<Fragment> fragments;
	
	private SharedPreferences pref;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		
		this.currentYear =  calendar.get(Calendar.YEAR);
		this.currentMonth = calendar.get(Calendar.MONTH) + 1;
		
		pref = getActivity().getSharedPreferences("user_account", Context.MODE_PRIVATE);
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View v = inflater.inflate(R.layout.stats_pager_layout, container, false);
		
		if(GSConfig.DAY_STATS_YEAR == 0 || GSConfig.DAY_STATS_MONTH == 0 ||GSConfig.DAY_STATS_DAY == 0)
		{
			GSConfig.DAY_STATS_YEAR = this.currentYear;
			GSConfig.DAY_STATS_MONTH = this.currentMonth;
		}
		
		makeFragmentList();
		
		setHasOptionsMenu(true);
		
		return v;

	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		getActivity().invalidateOptionsMenu();
		
		View view = this.getView();
		
		this.statsTabStrip = (PagerTabStrip)view.findViewById(R.id.stats_tab);
		this.statsPager = (ViewPager)view.findViewById(R.id.stats_pager);
		
		this.statsTabStrip.setDrawFullUnderline(false);
		this.statsTabStrip.setTabIndicatorColor(Color.WHITE);
		this.statsTabStrip.setBackgroundColor(Color.GRAY);
		this.statsTabStrip.setNonPrimaryAlpha(0.5f);
		this.statsTabStrip.setTextSpacing(25);
		this.statsTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		this.statsTabStrip.setTextColor(Color.WHITE);
		this.statsTabStrip.setPadding(10, 10, 10, 10);

		this.statsPagerAdapter = new StatsPagerAdapter(getChildFragmentManager());
		this.statsPager.setAdapter(statsPagerAdapter);

	}

	private void makeFragmentList()
	{
		
		fragments = new ArrayList<Fragment>();
		
		fragments.add(new JoomyungMonthAmountFragment());
		fragments.add(new JoomyungMonthPriceFragment());
		fragments.add(new JoomyungMonthEnterpriseAmountFragment());
		fragments.add(new JoomyungMonthEnterprisePriceFragment());
		fragments.add(new JoomyungMonthGraphFragment());

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		 inflater.inflate(R.menu.stats_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		 switch (item.getItemId())
		 {

		 	case R.id.stats_change_date_menu:
		    	   
		 		MonthDatePickerDialog monthDatePickerDialog = new MonthDatePickerDialog(getActivity(), GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_YEAR+10,  GSConfig.DAY_STATS_MONTH, new MonthDatePickerDialog.DialogListner() {
					
					@Override
					public void OnConfirmButton(Dialog dialog, int selectYear, int selectMonth) {

						if(AppConfig.LIMIT_YEAR > selectYear || selectYear > currentYear)
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_year_error), Toast.LENGTH_SHORT).show();
							return;
						} 
						
						if(AppConfig.LIMIT_YEAR == selectYear && AppConfig.LIMIT_MONTH > selectMonth )
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_month_error), Toast.LENGTH_SHORT).show();
							return;
						}
						
						if( currentYear == selectYear  && selectMonth > currentMonth )
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_month_error), Toast.LENGTH_SHORT).show();
							return;
						}

						if(GSConfig.DAY_STATS_YEAR != selectYear || GSConfig.DAY_STATS_MONTH != selectMonth)
						{
							GSConfig.DAY_STATS_YEAR = selectYear;
							GSConfig.DAY_STATS_MONTH = selectMonth;
							statsPagerAdapter.notifyDataSetChanged();
						}
						
						dialog.dismiss();

					}
				});

		 		monthDatePickerDialog.show();
		 		
		 		return true;

		 	default:
		 		return super.onOptionsItemSelected(item);

		 }

	}
	
	public class StatsPagerAdapter extends FragmentPagerAdapter
	{

		private final String[] TITLES;
		
		public StatsPagerAdapter(FragmentManager fm)
		{
			super(fm);
			TITLES = getResources().getStringArray(R.array.stats_month_tab_array1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			if (position != TITLES.length)
				super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment newFragment = null;
			newFragment =  fragments.get(position);
			return newFragment;
		}

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

	}

}
