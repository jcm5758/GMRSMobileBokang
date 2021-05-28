/**
 * ApiReconnectDialog
 * 기능 : Api 호출에 이상이 있을 때 재연결 다이얼로그
 *
 *  2021. 05. 28 최초 생성
 *
 *  Writtend by jcm5758
 *
 */

package com.geurimsoft.bokangnew.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.geurimsoft.bokangnew.R;

public class ApiReconnectDialog extends Dialog
{

    private Button btnOk, btnCancel;
    private DialogListener dialogListener;

    /**
     * 생성자
     * @param context 호출한 context
     */
    public ApiReconnectDialog(@NonNull Context context)
    {

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_api_reconnect);

        setCancelable(false);

        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        if(getWindow() != null)
        {

            WindowManager.LayoutParams wm = getWindow().getAttributes();
            wm.copyFrom(getWindow().getAttributes());
            wm.width = WindowManager.LayoutParams.MATCH_PARENT;
            wm.height = WindowManager.LayoutParams.WRAP_CONTENT;

        }

        btnOk.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                dialogListener.onPositiveClicked();
                dismiss();

            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {

            /**
             * 취소 버튼 클릭
             * @param view 클릭한 버튼
             */
            @Override
            public void onClick(View view)
            {

                dialogListener.onNegativeClicked();
                dismiss();

            }

        });

    }

    /**
     * 다이얼로그 클릭 리스너 setter
     * @param dialogListener 다이얼로그 리스너
     */
    public void setDialogListener(DialogListener dialogListener)
    {
        this.dialogListener = dialogListener;
    }

}