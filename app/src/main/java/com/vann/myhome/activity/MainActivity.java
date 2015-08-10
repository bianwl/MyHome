package com.vann.myhome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.adapter.GridViewAdapter;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class MainActivity extends BaseActivity {
    private GridView gdMain;

    private GridViewAdapter adapter;

    private Button mBack;

    private Integer[] datas = { R.drawable.mainnav01, R.drawable.mainnav02,
            R.drawable.mainnav03, R.drawable.mainnav04, R.drawable.mainnav05,
            R.drawable.mainnav06 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gdMain = (GridView) findViewById(R.id.gdMain);
        mBack = (Button) findViewById(R.id.back);
        mBack.setVisibility(View.INVISIBLE);
        gdMain.setScrollbarFadingEnabled(false);
        adapter = new GridViewAdapter(MainActivity.this, datas);
        gdMain.setAdapter(adapter);
        initListener();
    }

    private void initListener() {
        gdMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (datas[position]) {
                    case R.drawable.mainnav01:
                        openActivity(SenceActivity.class);
                        break;
                    case R.drawable.mainnav02:
                        openActivity(AreaActivity.class);
                        break;
                    case R.drawable.mainnav03:
                        showMsg("mainnav03");
                        break;
                    case R.drawable.mainnav04:
                        showMsg("mainnav04");
                        break;
                    case R.drawable.mainnav05:
                        openActivity(SystemActivity.class);
                        break;
                    case R.drawable.mainnav06:
                        showMsg("敬请期待！");
                        break;

                }

            }
        });
    }
}
