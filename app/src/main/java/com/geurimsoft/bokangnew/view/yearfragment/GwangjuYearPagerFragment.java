package com.geurimsoft.bokangnew.view.yearfragment;

import java.util.ArrayList;
import java.util.Calendar;

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
import com.geurimsoft.bokangnew.view.etc.YearDatePickerDialog;
import com.geurimsoft.bokangnew.conf.AppConfig;

public class GwangjuYearPagerFragment extends Fragment {
	
	private Calendar calendar = Calendar.getInstance();
	private int currentYear;
	
	private PagerTabStrip statsTabStrip;
	private ViewPager statsPager;
	private StatsPagerAdapter statsPagerAdapter;
	
	private ArrayList<Fragment> fragments;
	
	private int userGrade;
	
	private SharedPreferences pref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.currentYear =  calendar.get(Calendar.YEAR);
		
		pref = getActivity().getSharedPreferences("user_account", Context.MODE_PRIVATE);
		userGrade = pref.getInt("user_grade", 3);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.stats_pager_layout, container, false);
		
		if(AppConfig.DAY_STATS_YEAR == 0 || AppConfig.DAY_STATS_MONTH == 0 || AppConfig.DAY_STATS_DAY == 0) {
			AppConfig.DAY_STATS_YEAR = this.currentYear;
		}
		
		makeFragmentList(userGrade);
		
		setHasOptionsMenu(true);
		
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
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
		
		//this.statsPager.setCurrentItem(0, true);
	}
	
	
	private void makeFragmentList(int _userGrade) {
		
		fragments = new ArrayList<Fragment>();
		
		if(_userGrade <= 1) {
			fragments.add(new GwangjuYearAmountFragment());
			fragments.add(new GwangjuYearPriceFragment());
			fragments.add(new GwangjuYearGraphFragment());
		} else {
			fragments.add(new GwangjuYearAmountFragment());
			fragments.add(new GwangjuYearGraphFragment());
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		 inflater.inflate(R.menu.stats_menu, menu);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
		 	case R.id.stats_change_date_menu:
		    	   
		 		YearDatePickerDialog yearDatePickerDialog = new YearDatePickerDialog(getActivity(), AppConfig.DAY_STATS_YEAR, AppConfig.DAY_STATS_YEAR+10, new YearDatePickerDialog.DialogListner() {
					
					@Override
					public void OnConfirmButton(Dialog dialog, int selectYear) {
						// TODO Auto-generated method stub
						
						if(AppConfig.LIMIT_YEAR > selectYear || selectYear > currentYear) {
							Toast.makeText(getActivity(), getString(R.string.change_date_year_error), Toast.LENGTH_SHORT).show();
							return;
						} 
						
						
						if(AppConfig.DAY_STATS_YEAR != selectYear) {
//							calendar.set(Calendar.YEAR, selectYear);
							
							AppConfig.DAY_STATS_YEAR = selectYear;
						
							statsPagerAdapter.notifyDataSetChanged();
						}
						
						dialog.dismiss();
					}
				});
		 		yearDatePickerDialog.show();
		 		
		 		return true;
		 	default:
		 		return super.onOptionsItemSelected(item);
		 }
	}
	
	public class StatsPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES;

		public StatsPagerAdapter(FragmentManager fm) {
			
			super(fm);
			if(userGrade <= 1)
				TITLES = getResources().getStringArray(R.array.stats_year_tab_array1);
			else
				TITLES = getResources().getStringArray(R.array.stats_year_tab_array2);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
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
		public Fragment getItem(int position) {
			
			Fragment newFragment = null;
			
			newFragment =  fragments.get(position);
			return newFragment;
		}

		@Override
		public int getItemPosition(Object object) {
			// return super.getItemPosition(object);
			return POSITION_NONE;
		}
	}
}
