package com.geurimsoft.bokangnew;

import com.geurimsoft.conf.AppConfig;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ViewPagerActivity2 extends Activity 
{
	
	private ViewPager mPager;												//뷰 페이저
	private Button btnTopHome, btnSelCctv, btnTopSetting;
	MediaController mc;
	CCTVPagerAdapter cctvPagerAdapter ; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);
        setUserInterface();
    }
    
    public void setUserInterface()
    {    	
	
    	mc = new MediaController(this);  

    	btnTopHome = (Button)this.findViewById(R.id.btntophome);
    	btnSelCctv= (Button)this.findViewById(R.id.btnselcctv);
    	btnTopSetting= (Button)this.findViewById(R.id.btntopsetting);
    	
    	btnTopHome.setOnClickListener(clickListener);
    	btnSelCctv.setOnClickListener(clickListener);
    	btnTopSetting.setOnClickListener(clickListener);

        mPager = (ViewPager)findViewById(R.id.pager);
        cctvPagerAdapter = (new CCTVPagerAdapter(getApplicationContext(), AppConfig.LAYOUT_TYPE_1));
        mPager.setAdapter(cctvPagerAdapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener() 
        {
		
        	@Override
			public void onPageScrollStateChanged(int arg0) 
        	{
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) 
			{
			}
			
			@Override
			public void onPageSelected(int arg0) 
			{
			
				View thisView = (View)cctvPagerAdapter.instantiateItem(mPager, arg0);
				VideoView currVideoView =(VideoView)thisView.findViewById(R.id.videoView);
				currVideoView.setMediaController(mc);
				Uri uri1 = Uri.parse("rtsp://116.124.245.42:1935/live/ipcamera01.stream");
				currVideoView.setVideoURI(uri1);
				currVideoView.start();
			}
			
        });
        
    }
    
    /**
     * 상단 메뉴에 대한 
     */
	private OnClickListener clickListener = new OnClickListener()	//클릭 이벤트 객체 
	{		
		
		@Override
		public void onClick(View v) 
		{
			
			if(v.getId() == R.id.btntopcctv)
			{				
			}
			else if(v.getId() == R.id.btnselcctv)
			{				
			}
			else if(v.getId() == R.id.btntopsetting)
			{	
			}
			
		}
		
	};
	    
    /*
     * Pager 아답터 구현
     */
    private class CCTVPagerAdapter extends PagerAdapter
    {
    
    	private LayoutInflater mInflater;
    	private int layoutType =0;
    	
    	public CCTVPagerAdapter( Context con, int layoutType) 
    	{
			super();
			mInflater = LayoutInflater.from(con);
			this.layoutType = layoutType;
		}
    	
    	@Override 
    	public int getCount() 
    	{ 
    		return 2; 
    	}	//여기서는 2개만 할 것이다.
    	
    	//뷰페이저에서 사용할 뷰객체 생성/등록
    	@Override 
    	public Object instantiateItem(View pager, int position) 
    	{
    	
    		View v = null;
    		
    		if(layoutType == AppConfig.LAYOUT_TYPE_1)
    		{
        	
        		v = mInflater.inflate(R.layout.cctv_layout, null);
        		TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
        		tvTitle.setText("CCTV-"+position);
        		
        		((ViewPager)pager).addView(v, position);
    			
    		}
    		else if(layoutType == AppConfig.LAYOUT_TYPE_4)
    		{
        	
    			v = mInflater.inflate(R.layout.cctv_layout4, null);
        		TextView tvL4Title01 = (TextView)v.findViewById(R.id.tvL4Title01);
        		TextView tvL4Title02 = (TextView)v.findViewById(R.id.tvL4Title02);
        		TextView tvL4Title03 = (TextView)v.findViewById(R.id.tvL4Title03);
        		TextView tvL4Title04 = (TextView)v.findViewById(R.id.tvL4Title04);
        		
        		tvL4Title01.setText("CCTV-"+position+0);
        		tvL4Title02.setText("CCTV-"+position+1);
        		tvL4Title03.setText("CCTV-"+position+2);
        		tvL4Title04.setText("CCTV-"+position+3);
        		
    		}
    		
    		return v;
    		
    	}
    	
    	//뷰 객체 삭제.
		@Override 
		public void destroyItem(View pager, int position, Object view) 
		{
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
		@Override 
		public boolean isViewFromObject(View view, Object obj) 
		{ 
			return view == obj; 
		}
		
		@Override 
		public void finishUpdate(View arg0) 
		{			
		}
		
		@Override 
		public void restoreState(Parcelable arg0, ClassLoader arg1) 
		{			
		}
		
		@Override 
		public Parcelable saveState() 
		{ 
			return null; 
		}
		
		@Override 
		public void startUpdate(View arg0) 
		{			
		}
		
    }
    
}