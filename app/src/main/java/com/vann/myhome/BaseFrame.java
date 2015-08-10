package com.vann.myhome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BaseFrame extends Activity {
	
	public ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	/** 提示信息
	 * @param msg
	 */
	public void showMsg(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	/** 跳转到另一个界面
	 * @param cls
	 */
	public void openActivity(Class<?> cls){
		Intent _intent  = new Intent(this, cls);
		startActivity(_intent);
	}
	
	/**
	 *  显示进度条
	 */
	protected void showProgressDialog(){
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请稍后...");
		dialog.setIndeterminate(false);
		//是否可按会推荐取消
		dialog.setCancelable(true);
		dialog.show();
	}
	
	/**
	 * 关闭进度条
	 */
	protected void closeProgressDialog(){
		if(dialog != null){
			dialog.dismiss();
			dialog = null;
		}
	}
}
