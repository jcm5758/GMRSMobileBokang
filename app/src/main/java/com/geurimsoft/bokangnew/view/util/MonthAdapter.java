package com.geurimsoft.bokangnew.view.util;

import android.content.Context;

import com.geurimsoft.bokangnew.widget.adapter.YearNumericWheelAdapter;

import junit.framework.Assert;

import java.util.Locale;

public class MonthAdapter extends YearNumericWheelAdapter {

    public MonthAdapter(Context context) {
        super(context, 1, 12);
    }

    @Override
    public CharSequence getItemText(int index) {

        if (index >= 0 && index < getItemsCount()) {

            int value = 1 + index;

            return String.valueOf(value);

//            switch (value) {
//                case 1:
//                    return "1월";
//                case 2:
//                    return "2월";
//                case 3:
//                    return "3월";
//                case 4:
//                    return "4월";
//                case 5:
//                    return "5월";
//                case 6:
//                    return "6월";
//                case 7:
//                    return "7월";
//                case 8:
//                    return "8월";
//                case 9:
//                    return "9월";
//                case 10:
//                    return "10월";
//                case 11:
//                    return "11월";
//                case 12:
//                    return "12월";
//            }

        }

        return null;

    }

}
