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

	/** ��ʾ��Ϣ
	 * @param msg
	 */
	public void showMsg(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	/** ��ת����һ������
	 * @param cls
	 */
	public void openActivity(Class<?> cls){
		Intent _intent  = new Intent(this, cls);
		startActivity(_intent);
	}
	
	/**
	 *  ��ʾ�����
	 */
	protected void showProgressDialog(){
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("���Ժ�...");
		dialog.setIndeterminate(false);
		//�Ƿ�ɰ����˼�ȡ��
		dialog.setCancelable(true);
		dialog.show();
	}
	
	/**
	 *  �رս����
	 */
	protected void closeProgressDialog(){
		if(dialog != null){
			dialog.dismiss();
			dialog = null;
		}
	}
}
