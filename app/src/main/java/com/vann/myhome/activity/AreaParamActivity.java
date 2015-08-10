package com.vann.myhome.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.fragment.AreaParamDialogFragment;
import com.vann.myhome.model.AreaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域配置窗口
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class AreaParamActivity extends BaseActivity implements AreaParamDialogFragment.CallBackListener {

    public static final String TAG = "AreaParamActivity";

    /**
     * 添加区域
     */
    private ImageButton mPlusArea;
    /**
     * 区域列表
     */
    private ListView mListView;

    private TextView mTv;

    private AreaListAdapter mAdapter;

    private HomeDB db;

    private List<AreaModel> areas = new ArrayList<AreaModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_param);
        db = HomeDB.getIntance(this);
        // 初始化控件
        initView();
        // 初始化监听
        initListener();
        // 绑定数据
        bindData();
        Log.i(TAG, "***oncreate()****");

    }

    private void initView() {
        mPlusArea = (ImageButton) findViewById(R.id.imgBtnPlus);
        mListView = (ListView) findViewById(R.id.lvArea);
        mTv = (TextView) findViewById(R.id.tvPlusType);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.area_param);
        mTv.setText("区域");
    }

    private void initListener() {

        mPlusArea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAreaParamDialog(null, 0);
            }
        });

    }

    private void bindData() {
        areas.clear();
        mAdapter = new AreaListAdapter();
        for (AreaModel area : db.loadAreas()) {
            areas.add(area);
        }
        mListView.setAdapter(mAdapter);
    }

    private void showAreaParamDialog(AreaModel area, int type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AreaParamDialogFragment fg = AreaParamDialogFragment.newInstance(area,
                type);
        fg.show(ft, "AreaParamDialogFragment");

    }

    class ViewHolder {
        TextView tvArea;
        ImageButton imgEdit;
        ImageButton imgDel;
    }

    private class AreaListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return areas.size();
        }

        @Override
        public Object getItem(int position) {
            return areas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(
                        R.layout.area_list_item, null);
                holder.tvArea = (TextView) convertView
                        .findViewById(R.id.tvName);
                holder.imgEdit = (ImageButton) convertView
                        .findViewById(R.id.imgBtnEdit);
                holder.imgDel = (ImageButton) convertView
                        .findViewById(R.id.imgBtnDel);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AreaModel area = areas.get(position);
            holder.tvArea.setText(area.getName());
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showAreaParamDialog(area, 1);
                }
            });
            holder.imgDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String sql = "delete from area_devices where  areaId=?";
                    String sql2 = "delete from areas where areaId =?";
                    db.execSql(sql, new String[]{area.getAreaId()});
                    db.execSql(sql2, new String[]{area.getAreaId()});
                    areas.remove(area);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

    }

    @Override
    public void onFinish() {
        bindData();
        mAdapter.notifyDataSetChanged();
    }

}
