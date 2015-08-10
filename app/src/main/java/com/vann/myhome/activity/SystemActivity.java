package com.vann.myhome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.adapter.GridViewAdapter;

/**
 * 系统设置窗口
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SystemActivity extends BaseActivity {

    private GridViewAdapter mAdapter;

    private GridView mGridView;

    private Integer[] datas = {R.drawable.more_qy, R.drawable.more_sb,
            R.drawable.more_m3, R.drawable.mainnav01, R.drawable.ftp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        initView();
        // 初始化监听
        initListener();
        // 绑定数据
        bindData();

    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gdMain);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.system_config);
    }

    private void initListener() {
        // GridView监听
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (datas[position]) {
                    case R.drawable.more_qy:
                        openActivity(AreaParamActivity.class);
                        break;
                    case R.drawable.more_sb:
                        openActivity(DeviceParamActivity.class);
                        break;
                    case R.drawable.more_m3:
                        openActivity(PanelParamActivity.class);
                        break;
                    case R.drawable.mainnav01:
                        openActivity(SenceParamActivity.class);
                        break;
                    case R.drawable.ftp:
                        showMsg("ftp");
                        break;
                }
            }
        });
    }

    private void bindData() {
        mAdapter = new GridViewAdapter(this, datas);
        mGridView.setAdapter(mAdapter);
    }
}
