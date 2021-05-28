/**
 * ApiLoadingDialog
 * 기능 : Api 호출 중일 때 다이얼로그
 *
 * 2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 */

package com.geurimsoft.bokangnew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.data.GSConfig;

public class ApiLoadingDialog extends Dialog
{

    /**
     * 생성자
     * @param context 호출한 context
     */
    public ApiLoadingDialog(@NonNull Context context)
    {

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_api_loading);

        setCancelable(false);

        Log.d(GSConfig.APP_DEBUG, this.getClass().getName() + " : ApiLoadingDialog()");

    }

}