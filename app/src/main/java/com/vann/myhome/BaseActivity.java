package com.vann.myhome;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;

public class BaseActivity extends BaseFrame {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String vs = android.os.Build.VERSION.RELEASE.substring(0, 3);
		float version = Float.valueOf(vs);
		if (version > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyDeath().penaltyLog().build());
		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WifiManager manager = (WifiManager) getSystemService(this.WIFI_SERVICE);
		WifiManager.MulticastLock lock = manager
				.createMulticastLock("wifi test");
		lock.acquire();
		if (!manager.isWifiEnabled()) {
			manager.setWifiEnabled(true);
		}
	}

}
