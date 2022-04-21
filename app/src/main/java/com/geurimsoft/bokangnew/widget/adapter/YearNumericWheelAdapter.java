/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.geurimsoft.bokangnew.widget.adapter;

import android.content.Context;

/**
 * Numeric Wheel adapter.
 */
public class YearNumericWheelAdapter extends AbstractWheelTextAdapter
{

    private int minValue;
    private int maxValue;
    
    private String format;
    
    public YearNumericWheelAdapter(Context context, int minValue, int maxValue)
    {
        this(context, minValue, maxValue, null);
    }

    public YearNumericWheelAdapter(Context context, int minValue, int maxValue, String format)
    {

        super(context);
        
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;

    }

    @Override
    public CharSequence getItemText(int index)
    {

        if (index >= 0 && index < getItemsCount())
        {
            int value = minValue + index;
            return format != null ? String.format(format, value) : Integer.toString(value);// + "년";
        }

        return null;

    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

}
