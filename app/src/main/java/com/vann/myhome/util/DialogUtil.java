package com.vann.myhome.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.vann.myhome.R;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class DialogUtil {
    /**
     * 创建警告对话框
     *
     * @param context
     * @param msg
     */
    public static void createWarnDialog(Context context, String msg) {
        new AlertDialog.Builder(context).setTitle(R.string.warnning)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

}
