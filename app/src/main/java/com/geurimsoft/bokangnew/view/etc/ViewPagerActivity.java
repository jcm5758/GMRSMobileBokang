package com.geurimsoft.bokangnew.view.etc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geurimsoft.bokangnew.R;

@SuppressLint("ValidFragment")
public class ViewPagerActivity extends FragmentActivity 
{
		
	 private final static int COUNT=4; // 표시할 페이지 수
	 private ViewPager mPager; //뷰 페이저
	  
	 @Override
	public void onCreate(Bundle savedInstanceState) 
	 {
	
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.page_layout);
		 
		 mPager = (ViewPager)findViewById(R.id.pager);
		 mPager.setAdapter(new BkFragmentAdapter(getSupportFragmentManager()));
	   
		 //무한 스크롤 구현 부분
		 mPager.setOnPageChangeListener(new OnPageChangeListener() 
		 {
	    
	   
			 @Override
			public void onPageSelected(int position) 
			 {
				 
				 if(position < COUNT) 
				 {	     
					 mPager.setCurrentItem(position + COUNT, false);  // 두번째 false 인자값은 위치이동 에니메이션을 꺼준다
	    
				 } 
				 else if (position >= COUNT*2)	// true 로 바꿔서 실행해보면 어떤얘긴지 단박에 알수 있다. 
				 {
					 mPager.setCurrentItem(position - COUNT, false);  
				 }
			 
			 }
	    
			 @Override
			public void onPageScrolled(int position, float positionOffest, int positionOffestPixel) { }	    
			 @Override
			public void onPageScrollStateChanged(int state) { }
	  
		 });
	 
	 }
	 
	 //FragmentPager 구현
	 private class BkFragmentAdapter extends FragmentPagerAdapter
	 {
	 
		 //생성자
		 public BkFragmentAdapter(FragmentManager fm) {super(fm);}
	 
		 /**
		  * 실제 뷰페이저에서 보여질 fragment를 반환.
		  * 일반 아답터(갤러리, 리스트뷰 등)의 getView와 같은 역할
		  * @param position - 뷰페이저에서 보여저야할 페이지 값( 0부터 )
		  * @return 보여질 fragment
		  */
	  
		 @Override public Fragment getItem(int position) 
		 {
			 return ArrayFragment.newInstance(position%COUNT);  
			 // 3배로 리턴된 페이지를 보여준다. [1 2 3 4] [1 2 3 4] [1 2 3 4] 이렇게 보여지는 원리..
		 }
	 
		 //뷰페이저에서 보여질 총 페이지 수
		 @Override public int getCount() { return COUNT*3; }  
		 // 보여줄 페이지의 갯수를 3배로 리턴한다. 
	 	
	 }
	 
	 //뷰 페이저의 페이지에 맞는 fragment를 생성하는 객체
	 private static class ArrayFragment extends Fragment 
	 {
	 
		 int mPosition; //뷰 페이저의 페이지 값
	 
		 //fragment 생성하는 static 메소드 뷰페이저의 position을 값을 받는다.
		 static ArrayFragment newInstance(int position) 
		 {
			 ArrayFragment f = new ArrayFragment(); //객체 생성
			 Bundle args = new Bundle();     		//해당 fragment에서 사용될 정보 담을 번들 객체
			 args.putInt("position", position);    	//포지션 값을 저장
			 f.setArguments(args);       			//fragment에 정보 전달.
			 return f;           					//fragment 반환
		 }
	 
		 //fragment가 만들어질 때
		 @Override
		 public void onCreate(Bundle savedInstanceState) 
		 {
			 super.onCreate(savedInstanceState);
			 mPosition = getArguments() != null ? getArguments().getInt("position") : 0; 
			 // 뷰페이저의 position값을  넘겨 받음
		 }
	 
		 //fragment의 UI 생성
		 @Override
		 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		 {
			 View v = inflater.inflate(R.layout.cctv_layout, container, false);
			 //미리 알고 있는 레이아웃을 inflate 한다.	 
	   
			 return v;
		 }
	 
	 }
	
}