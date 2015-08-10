package com.vann.myhome.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.adapter.AreaDevAdapter;
import com.vann.myhome.adapter.ImageAdapter;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.fragment.AreaSenceDialogFragment;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.widget.GalleryFlow;

import java.util.ArrayList;
import java.util.List;

/** /**

 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class AreaActivity extends BaseActivity {
    // 控件
    private TextView title;
    private Button selectSence;
    private ListView lv_area;
    private GalleryFlow mGalleryFlow;

    private AreaModel currentArea;

    // GalleryFlow的适配器
    private ImageAdapter imgAdapter;
    // 设备列表适配器
    private AreaDevAdapter mAdapter;

    private HomeDB db;

    private List<AreaModel> areas = new ArrayList<AreaModel>();

    private List<DeviceModel> devices = new ArrayList<DeviceModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_area);
        db = HomeDB.getIntance(this);
        initView();
        initListener();
        initData();
        setSelection();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.tvTitle);
        selectSence = (Button) findViewById(R.id.plus);
        selectSence.setVisibility(View.VISIBLE);
        lv_area = (ListView) findViewById(R.id.lv_area);
        mGalleryFlow = (GalleryFlow) findViewById(R.id.gf_area);
    }

    private void initListener() {
        // 场景选择按钮
        selectSence.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogFragment();

            }
        });
        // GalleryFlow 监听
        mGalleryFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentArea = areas.get(position);
                setSelection();
            }
        });
    }


    private void showDialogFragment(){
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        AreaSenceDialogFragment fr = AreaSenceDialogFragment.newInstance(currentArea);
        fr.show(tr, "AreaSenceDialogFragment");
    }
    private void initData() {
        areas.clear();
        areas.addAll(db.loadAreas());
        imgAdapter = new ImageAdapter(this, areas.toArray());
        mGalleryFlow.setAdapter(imgAdapter);
        if (!areas.isEmpty()) {
            currentArea = areas.get(0);
        }

    }

    private void setSelection() {
        devices.clear();
        if (currentArea != null) {
            title.setText(currentArea.getName());
            for (DeviceModel dev : db.loadAreaDevice(currentArea.getAreaId(),1)) {
                devices.add(dev);
            }
        }
        mAdapter = new AreaDevAdapter(this,devices);
        lv_area.setAdapter(mAdapter);
    }
}
