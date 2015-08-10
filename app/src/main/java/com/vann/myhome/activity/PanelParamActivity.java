package com.vann.myhome.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;

/**
 * 面板设置
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class PanelParamActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_param);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.panel_param);
    }
}
