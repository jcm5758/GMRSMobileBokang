/**
 * 일보 메인
 *
 * 2021. 05. 28. 리뉴얼
 *
 * Written by jcm5758
 *
 */

package com.geurimsoft.bokangnew.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.apiserver.data.UserRightData;
import com.geurimsoft.bokangnew.data.GSBranch;
import com.geurimsoft.bokangnew.data.GSConfig;
import com.geurimsoft.bokangnew.view.util.DayDatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentDailyMain extends Fragment
{

	// 날짜 관련
	private Calendar calendar = Calendar.getInstance();
	private int currentYear, currentMonth, currentDay;

	// 사용자 권한
	private UserRightData urd = null;

	// 슬라이딩 구현용
	private PagerTabStrip statsTabStrip;
	private ViewPager statsPager;
	private StatsPagerAdapter statsPagerAdapter;

	// 세부 프로그먼트
	private ArrayList<Fragment> fragments;

	Context context;

	public FragmentDailyMain(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		String functionName = "onCreate()";

		// 현재 연월일 찾기
		this.currentYear = calendar.get(Calendar.YEAR);
		this.currentMonth = calendar.get(Calendar.MONTH) + 1;
		this.currentDay = calendar.get(Calendar.DATE);

//		Log.d(GSConfig.APP_DEBUG, GSConfig.LOG_MSG(getActivity().getClass().getName(), functionName) + " this.currentYear : " + this.currentYear + ", this.currentMonth : " + this.currentMonth + ", this.currentDay : " + this.currentDay );

		this.urd = GSConfig.CURRENT_USER.getUserRightDataByID(GSConfig.CURRENT_BRANCH.getBranchID());

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View v = inflater.inflate(R.layout.stats_pager_layout, container, false);

		this.context = container.getContext();

		// 전역변수에 날짜 정보 지정
		if(GSConfig.DAY_STATS_YEAR == 0 || GSConfig.DAY_STATS_MONTH == 0 || GSConfig.DAY_STATS_DAY == 0)
		{
			GSConfig.DAY_STATS_YEAR = this.currentYear;
			GSConfig.DAY_STATS_MONTH = this.currentMonth;
			GSConfig.DAY_STATS_DAY = this.currentDay;
		}

		// 프래그먼트 리스트 생성
		makeFragmentList();

		// 메뉴 설정
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
		this.statsTabStrip.setPadding(5, 5, 5, 5);

		this.statsPagerAdapter = new StatsPagerAdapter(getChildFragmentManager());
		this.statsPager.setAdapter(statsPagerAdapter);

	}

	/**
	 * 프래그먼트 리스트 만들기
	 * 일보 수량, 금액
	 */
	private void makeFragmentList()
	{
		
		fragments = new ArrayList<Fragment>();

		if (this.urd.isShowDayAmount())
			fragments.add(new FragmentDaily(GSConfig.STATE_AMOUNT, "Unit"));

		if (this.urd.isShowDayPrice())
			fragments.add(new FragmentDaily(GSConfig.STATE_PRICE, "TotalPrice"));

		if (this.urd.isShowDaySearch())
			fragments.add(new FragmentDailySearch());

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{

		int menuID = 0;

		if ( GSConfig.CURRENT_BRANCH.getBranchID() == 2)
			menuID = R.menu.stats_menu2;
		else
			menuID = R.menu.stats_menu;

		inflater.inflate(menuID, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{

			 case R.id.stats_change_branchkwangju:
				 return menuAction(0);

			 case R.id.stats_change_branchjoomyung:
				 return menuAction(1);

			 case R.id.stats_change_date_menu:

		 		DayDatePickerDialog dayDatePickerDialog = new DayDatePickerDialog(getActivity(), GSConfig.DAY_STATS_YEAR, GSConfig.DAY_STATS_YEAR+10,  GSConfig.DAY_STATS_MONTH, GSConfig.DAY_STATS_DAY, new DayDatePickerDialog.DialogListner() {

		 			@Override
					public void OnConfirmButton(Dialog dialog, int selectYear, int selectMonth, int selectDay) {

						if(GSConfig.LIMIT_YEAR > selectYear || selectYear > currentYear)
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_year_error), Toast.LENGTH_SHORT).show();
							return;
						}

						if(GSConfig.LIMIT_YEAR == selectYear && GSConfig.LIMIT_MONTH > selectMonth )
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_month_error), Toast.LENGTH_SHORT).show();
							return;
						}

						if( currentYear == selectYear  && selectMonth > currentMonth )
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_month_error), Toast.LENGTH_SHORT).show();
							return;
						}

						if(currentMonth ==  selectMonth && selectDay > currentDay )
						{
							Toast.makeText(getActivity(), getString(R.string.change_date_day_error), Toast.LENGTH_SHORT).show();
							return;
						}

						if(GSConfig.DAY_STATS_YEAR != selectYear || GSConfig.DAY_STATS_MONTH != selectMonth || GSConfig.DAY_STATS_DAY != selectDay)
						{

							GSConfig.DAY_STATS_YEAR = selectYear;
							GSConfig.DAY_STATS_MONTH = selectMonth;
							GSConfig.DAY_STATS_DAY = selectDay;

							statsPagerAdapter.notifyDataSetChanged();

						}

						dialog.dismiss();

					}

				});

		 		dayDatePickerDialog.show();
		 		
		 		return true;

		 	default:
		 		return super.onOptionsItemSelected(item);

		 }

	}

	/**
	 *
	 * 메뉴 중 지점 클릭시 이동 처리
	 *
	 * @param which 지점 순번(사용자 권한의 순번)
	 *
	 * @return
	 */
	public boolean menuAction(int which)
	{

		// 지점 로그인 정보가 없으면 패스
		if (GSConfig.CURRENT_USER.getUserRightData(which).getUr01() != 1)
		{
			Toast.makeText(context, "지점에 로그인 권한이 없습니다.", Toast.LENGTH_SHORT).show();
			return false;
		}

		// 사용자 권한 정보 리스트 찾아오기
		ArrayList<UserRightData> urData = GSConfig.CURRENT_USER.getUserright();

		// 현재 지점으로 설정
		GSConfig.CURRENT_BRANCH = new GSBranch(urData.get(which).getBranID(), urData.get(which).getBranName(), urData.get(which).getBranShortName());

		// 새로 옮겨갈 프래그먼트 설정
		Intent intent = new Intent(context, GSConfig.Activity_CLASS);
		intent.putExtra("branName", GSConfig.CURRENT_BRANCH.getBranchShortName());
		intent.putExtra("branID", GSConfig.CURRENT_BRANCH.getBranchID());

		// 프래그먼트로 이동
		startActivity(intent);

		// 현재 프래그먼트 종료
		getActivity().onBackPressed();

		return true;

	}

	public class StatsPagerAdapter extends FragmentPagerAdapter
	{

		private String[] TITLES;

		public StatsPagerAdapter(FragmentManager fm)
		{

			super(fm);

			if (urd.isShowDayAmount() && urd.isShowDayPrice() && urd.isShowDaySearch())
				TITLES = new String[] { "수량", "금액", "검색" };
			else if (urd.isShowDayAmount() && urd.isShowDayPrice() && !urd.isShowDaySearch())
				TITLES = new String[] { "수량", "금액" };
			else if (urd.isShowDayAmount() && !urd.isShowDayPrice() && !urd.isShowDaySearch())
				TITLES = new String[] { "수량" };

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
			return fragments.get(position);
		}

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

	}

}
