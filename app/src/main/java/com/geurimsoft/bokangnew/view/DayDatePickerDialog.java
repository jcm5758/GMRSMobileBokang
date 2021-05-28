package com.geurimsoft.bokangnew.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;


public class DayDatePickerDialog extends Dialog implements OnClickListener{

	private DayWheelDatePicker dayWheelDatePicker;
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	
	private Button confirm_button;
	
	private DialogListner dialogListner;
	
	private int selectYear;
	private int selectMonth;
	private int selectDay;
	
	private int maxYear;
	
	public DayDatePickerDialog(Context context, int _currentYear, int _maxYear, int _currentMonth, int _currentDay, DialogListner _dialogListner) {
		super(context);
		// TODO Auto-generated constructor stub
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.day_datepicker_dialog);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		LayoutParams params = getWindow().getAttributes();
	    params.width = LayoutParams.MATCH_PARENT;
	    params.height = LayoutParams.WRAP_CONTENT;
	    
	    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	    
		this.currentYear = _currentYear;
		this.currentMonth = _currentMonth;
		this.currentDay = _currentDay;
				
		this.dialogListner = _dialogListner;
		this.maxYear = _maxYear;
		
		setInterface();
		
	}
	
	private void setInterface() {
		this.confirm_button = (Button)findViewById(R.id.confirm_button);
		this.confirm_button.setOnClickListener(this);
		
		this.dayWheelDatePicker =  (DayWheelDatePicker)findViewById(R.id.day_datepicker);
		
		this.dayWheelDatePicker.setVisibleItems(8);
		this.dayWheelDatePicker.setMinMaxYears(AppConfig.LIMIT_YEAR, maxYear);
		
		this.dayWheelDatePicker.setYear(currentYear);
		this.dayWheelDatePicker.setMonth(currentMonth);
		this.dayWheelDatePicker.setDay(currentDay);
		
		this.selectYear = currentYear;
		this.selectMonth = currentMonth;
		this.selectDay = currentDay;
		
		this.dayWheelDatePicker.addDateChangedListener(new DayWheelDatePicker.IDateChangedListener() {


			@Override
			public void onChanged(DayWheelDatePicker sender, int oldDay, int oldMonth, int oldYear, int day, int month, int year) {
				// TODO Auto-generated method stub
				selectYear = year;
				selectMonth = month;
				selectDay = day;
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.confirm_button :
			dialogListner.OnConfirmButton(DayDatePickerDialog.this, selectYear, selectMonth, selectDay);
			break;
//			
		}
	}
	
	public interface DialogListner {
		public void OnConfirmButton(Dialog dialog, int selectYear, int selectMonth, int selectDay);

	}
}
