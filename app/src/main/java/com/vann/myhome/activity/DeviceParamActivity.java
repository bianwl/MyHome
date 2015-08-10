package com.vann.myhome.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.adapter.DevParamListViewAdapter;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.util.SocketUtil;
import com.vann.myhome.util.UdpCallBackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备配置窗口
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class DeviceParamActivity extends BaseActivity {
    private ListView mListView;

    private ImageButton mButton;

    private DevParamListViewAdapter adapter;

    private HomeDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_param);
        db = HomeDB.getIntance(this);
        initView();
        initListener();
        new downLoadTask().execute();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lvDeviceParam);
        mButton = (ImageButton) findViewById(R.id.refresh);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.device_param);

    }

    private void initListener() {
        // 搜索设备事件
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new downLoadTask().execute();
            }
        });

    }

    class downLoadTask extends AsyncTask<Void, Void, List<DeviceModel>> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(List<DeviceModel> result) {
            closeProgressDialog();
            adapter = new DevParamListViewAdapter(DeviceParamActivity.this,
                    result);
            mListView.setAdapter(adapter);
        }

        @Override
        protected List<DeviceModel> doInBackground(Void... params) {
            if (db.loadDevices().size() > 0) {
                return queryDevices();
            } else {
                return getDevicesFromServer();
            }
        }
    }

    private List<DeviceModel> queryDevices() {
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        if (db.loadDevices().size() > 0) {
            for (DeviceModel dev : db.loadDevices()) {
                devices.add(dev);
            }
        }
        return devices;
    }

    private List<DeviceModel> getDevicesFromServer() {
        final List<DeviceModel> results = new ArrayList<DeviceModel>();
        SocketUtil.receive("$vnm,ask,", new UdpCallBackListener() {

            @Override
            public void onFinish(List<String> response) {
                boolean result = SocketUtil.handleDeviceResponse(db, response);
                if (result) {
                    for (DeviceModel dev : queryDevices()) {
                        results.add(dev);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
        return results;
    }
}
