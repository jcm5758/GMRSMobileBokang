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
import android.util.Log;

public class DayNumericWheelAdapter extends AbstractWheelTextAdapter
{

    private int minValue;
    private int maxValue;
    
    public DayNumericWheelAdapter(Context context, int minValue, int maxValue)
    {

        super(context);

        this.minValue = minValue;
        this.maxValue = maxValue;

    }

    @Override
    public CharSequence getItemText(int index)
    {

        Log.d("", "DayNumericWheelAdapter.getItemText() is called.");

        if (index >= 0 && index < getItemsCount())
        {
            int value = minValue + index;
            return Integer.toString(value);// + "일";
        }

        return null;

    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

}
