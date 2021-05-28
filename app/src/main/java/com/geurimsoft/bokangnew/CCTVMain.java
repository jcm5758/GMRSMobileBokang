package com.geurimsoft.bokangnew;


import java.util.ArrayList;

import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.CCTV;
import com.geurimsoft.data.Place;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class CCTVMain extends Activity {

	//User Interface
 	LinearLayout cctvLayout01,cctvLayout04;
 	
	//layout01
 	TextView tvVideoTitle;
 	VideoView vvL1;
 	
	//layout04
 	TextView tvL4Title01,tvL4Title02,tvL4Title03,tvL4Title04 ;
 	VideoView vvL401, vvL402, vvL403, vvL404;
	
	Uri uri1, uri2, uri3, uri4;

	//Button
	ImageButton btnLeftMove, btnRightMove;
	
	//TopButton
	Button btnTopHome, btnTopSetting, btnSelCCTV, btnSelOneDisplay, btnSelFourDisplay, btnSearch;
	
	
	// Media Controller
	MediaController mc;

	
	//화면을 구성하는 내용 
	int currentViewType = AppConfig.LAYOUT_TYPE_4;
	
	//화면의 순서
	
	int currentDisplayIndex = 0;
	int currentDisplay4Index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cctv_view_layout);
		
		AppConfig.activities.add(CCTVMain.this);
		
		if(AppMain.CURRENT_CCTV_LIST == null){
			AppMain.CURRENT_CCTV_LIST = AppMain.PLACE_LIST.get(AppMain.CURRENT_PLACE_INDEX).getCctvList();
		}
		setUserInterface();
		setVideoView();

	}

	/**
	 * 화면을 구성하는 디자인 
	 */
	public void setUserInterface(){
		//VideoView
		vvL1	= (VideoView)this.findViewById(R.id.vvL1);
		vvL401= (VideoView)this.findViewById(R.id.vvL401);
		vvL402= (VideoView)this.findViewById(R.id.vvL402);
		vvL403= (VideoView)this.findViewById(R.id.vvL403);
		vvL404= (VideoView)this.findViewById(R.id.vvL404);
		
		
		//Title 
		tvVideoTitle = (TextView)this.findViewById(R.id.tvVideoTitle);
		
		tvL4Title01 = (TextView)this.findViewById(R.id.tvL4Title01);
		tvL4Title02 = (TextView)this.findViewById(R.id.tvL4Title02);
		tvL4Title03 = (TextView)this.findViewById(R.id.tvL4Title03);
		tvL4Title04 = (TextView)this.findViewById(R.id.tvL4Title04);
		
		cctvLayout01 	= (LinearLayout)this.findViewById(R.id.cctvlayout01);
		cctvLayout04	= (LinearLayout)this.findViewById(R.id.cctvlayout04);
	 
		cctvLayout01.setVisibility(View.GONE);
		cctvLayout04.setVisibility(View.VISIBLE);
		
		btnLeftMove	= (ImageButton)this.findViewById(R.id.btnLeftMove);
		btnRightMove	= (ImageButton)this.findViewById(R.id.btnRightMove);
		btnLeftMove.setOnClickListener(clickListener);
		btnRightMove.setOnClickListener(clickListener);
		
		btnTopHome		= (Button)this.findViewById(R.id.btntophome);
		btnTopSetting	= (Button)this.findViewById(R.id.btntopsetting);
		btnSelCCTV		= (Button)this.findViewById(R.id.btnselcctv);
		btnSelOneDisplay= (Button)this.findViewById(R.id.btnSelOnedisplay);
		btnSelFourDisplay= (Button)this.findViewById(R.id.btnSelFourDisplay);
		btnSearch		= (Button)this.findViewById(R.id.btnSearch);
		
		
		btnTopHome.setOnClickListener(clickListener);
		btnTopSetting.setOnClickListener(clickListener);
		btnSelCCTV.setOnClickListener(clickListener);
		btnSelOneDisplay.setOnClickListener(clickListener);
		btnSelFourDisplay.setOnClickListener(clickListener);
		btnSearch.setOnClickListener(clickListener);

		
	}

	/**
	 * 화면을 교환하기 위한 내용 
	 * @param viewType
	 */
	public void setView(int viewType ){
		this.currentViewType = viewType;
		cctvLayout01.setVisibility(View.GONE);
 		cctvLayout04.setVisibility(View.GONE);
 		
		if(this.currentViewType ==AppConfig.LAYOUT_TYPE_4 ){
			cctvLayout04.setVisibility(View.VISIBLE);
		}else{
			cctvLayout01.setVisibility(View.VISIBLE);
		}
		setVideoView();
	}
	
	public void changePlace(){
		
		
	}
	public void changeView(int changeIndex){
		
		if(this.currentViewType ==AppConfig.LAYOUT_TYPE_4 ){
			chagneFourView(changeIndex);
		}else{
			chageOneView(changeIndex);
		}
	}
	
	public void chageOneView(int changeIndex){
		int maxIdx =AppMain.PLACE_LIST.get(AppMain.CURRENT_PLACE_INDEX).getCctvList().size();
		
		if( changeIndex < 0 && this.currentDisplayIndex > 0 ){
			this.currentDisplayIndex = this.currentDisplayIndex + changeIndex;
		}else if( changeIndex > 0 && changeIndex < maxIdx){
			this.currentDisplayIndex = this.currentDisplayIndex + changeIndex;
		}
		setVideoView();
		
	}
	public void chagneFourView(int changeIndex){
		int maxIdx =AppMain.PLACE_LIST.get(AppMain.CURRENT_PLACE_INDEX).getCctvList().size();
		
		
		int totalPage = maxIdx / 4;
		if(maxIdx % 4 > 0 ){
			totalPage = totalPage +1;
		}
		Toast.makeText(getApplicationContext(),"totalPage-"+totalPage+"changeIndex-"+changeIndex+", this.currentDisplay4Index-"+ this.currentDisplay4Index, Toast.LENGTH_SHORT).show();
		if( changeIndex < 0 && this.currentDisplay4Index > 0 ){
			this.currentDisplay4Index = this.currentDisplay4Index + changeIndex;
		}else if( changeIndex > 0 && currentDisplay4Index < totalPage ){
			this.currentDisplay4Index = this.currentDisplay4Index + changeIndex;
		}
		setVideoView();
	}
	
	public void setVideoView(){
 	 	vvL1.stopPlayback();
	 	vvL401.stopPlayback();
		vvL402.stopPlayback();
	 	vvL403.stopPlayback();
	 	vvL404.stopPlayback();
	 	
	 	vvL401.setVisibility(View.GONE);
	 	vvL402.setVisibility(View.GONE);
	 	vvL403.setVisibility(View.GONE);
	 	vvL404.setVisibility(View.GONE);
	 	vvL1.setVisibility(View.GONE);
	 	
		if(this.currentViewType ==AppConfig.LAYOUT_TYPE_4 ){
			vvL401.setVisibility(View.VISIBLE);
		 	vvL402.setVisibility(View.VISIBLE);
		 	vvL403.setVisibility(View.VISIBLE);
		 	vvL404.setVisibility(View.VISIBLE);
		 	
		 	CCTV thisCCTV = null;
		 	if(AppMain.CURRENT_CCTV_LIST.size() > this.currentDisplay4Index*4){
			 	thisCCTV = AppMain.CURRENT_CCTV_LIST.get(this.currentDisplay4Index*4);
			 	tvL4Title01.setText(thisCCTV.getCctvName());
				vvL401.setMediaController(mc);
				Uri uri1 = Uri.parse(thisCCTV.getCctvUrl());
				vvL401.setVideoURI(uri1);
				vvL401.start();	
		 	}

		 	if(AppMain.CURRENT_CCTV_LIST.size() > this.currentDisplay4Index*4+1){
			 	thisCCTV = AppMain.CURRENT_CCTV_LIST.get(this.currentDisplay4Index*4+1);
			 	tvL4Title02.setText(thisCCTV.getCctvName());
				vvL402.setMediaController(mc);
				Uri uri2 = Uri.parse(thisCCTV.getCctvUrl());
				vvL402.setVideoURI(uri2);
				vvL402.start();	
		 	}
		 	if(AppMain.CURRENT_CCTV_LIST.size() > this.currentDisplay4Index*4+2){
			 	thisCCTV = AppMain.CURRENT_CCTV_LIST.get(this.currentDisplay4Index*4+2);
			 	tvL4Title03.setText(thisCCTV.getCctvName());
				vvL403.setMediaController(mc);
				Uri uri2 = Uri.parse(thisCCTV.getCctvUrl());
				vvL403.setVideoURI(uri2);
				vvL403.start();	
		 	}
		 	if(AppMain.CURRENT_CCTV_LIST.size() > this.currentDisplay4Index*4+3){
			 	thisCCTV = AppMain.CURRENT_CCTV_LIST.get(this.currentDisplay4Index*4+3);
			 	tvL4Title04.setText(thisCCTV.getCctvName());
				vvL404.setMediaController(mc);
				Uri uri2 = Uri.parse(thisCCTV.getCctvUrl());
				vvL404.setVideoURI(uri2);
				vvL404.start();	
		 	}
			
		}else{
		 	vvL1.setVisibility(View.VISIBLE);

			CCTV thisCCTV = AppMain.CURRENT_CCTV_LIST.get(this.currentDisplayIndex);

			tvVideoTitle.setText(thisCCTV.getCctvName());
			vvL1.setMediaController(mc);
			Uri uri1 = Uri.parse(thisCCTV.getCctvUrl());
			vvL1.setVideoURI(uri1);
			vvL1.start();	
		}
	}
	public ArrayList<String> getPlaceList(){
		ArrayList<String> thisList = new ArrayList<String>();
		for(int idx =0; idx < AppMain.PLACE_LIST.size() ; idx++){
			Place thisPlace = AppMain.PLACE_LIST.get(idx);
			thisList.add(thisPlace.getPlaceName()); 
		}
		return thisList;
	}
	
	public ArrayList<String> getCCTVList(){
		ArrayList<String> thisList = new ArrayList<String>();
		for(int idx =0; idx < AppMain.CURRENT_CCTV_LIST.size() ; idx++){
			CCTV thisCCTV = AppMain.CURRENT_CCTV_LIST.get(idx);
			thisList.add(thisCCTV.getCctvName()); 
		}
		return thisList;
	}
	public void callSelCCTVPopup(View v){
		PopupWindow popup = new PopupWindow(v);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  
        View popupview = inflater.inflate(R.layout.popup1, null);

        Spinner spPlace = (Spinner)popupview.findViewById(R.id.spPlace);
        ArrayAdapter<String> adapterSp;
        adapterSp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getPlaceList());
        spPlace.setAdapter(adapterSp);
        
        ListView popupListView = (ListView)popupview.findViewById(R.id.popupListView);
        ArrayAdapter<String> adapterList;
        adapterList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getCCTVList());
        popupListView.setAdapter(adapterList);
        
        
        popup.setContentView(popupview); 
        popup.setWindowLayoutMode(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(popupview, Gravity.CENTER,0,0);
        popup.showAsDropDown(v);

	}
	View.OnClickListener clickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.btntophome){
				Intent intent = new Intent(CCTVMain.this, AppMain.class);
				startActivity(intent);
			}else if(v.getId() == R.id.btntopsetting){
				Intent intent = new Intent(CCTVMain.this, AppSetting.class);
				startActivity(intent);				
			}else if(v.getId() == R.id.btnSearch){
				Intent intent = new Intent(CCTVMain.this, StatView01.class);
				startActivity(intent);				
			}else if(v.getId() == R.id.btnselcctv){
				callSelCCTVPopup(v);
			} else if(v.getId() == R.id.btnSelOnedisplay){	
				setView(AppConfig.LAYOUT_TYPE_1);				
			} else if(v.getId() == R.id.btnSelFourDisplay){
				setView(AppConfig.LAYOUT_TYPE_4);
			}else if (v.getId() == R.id.btnLeftMove){ //이
				changeView(-1);
			}else if(v.getId() == R.id.btnRightMove){
				changeView(1);				
			}
		}
	};
	/**
	 *  문자열 URL로부터   Uri객체를 생성하는 함ㄴ수 
	 * @param strUrl
	 * @return
	 */
	public Uri getUri(String strUrl){
		Uri thisUri = null;
		thisUri = Uri.parse(strUrl);
		return thisUri;
	}
}
