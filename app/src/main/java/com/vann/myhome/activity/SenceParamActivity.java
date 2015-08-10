package com.vann.myhome.activity;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vann.myhome.BaseActivity;
import com.vann.myhome.R;
import com.vann.myhome.adapter.ImageAdapter;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.fragment.SenceParamDialogFragment;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.SenceModel;
import com.vann.myhome.util.ImageUtil;
import com.vann.myhome.widget.GalleryFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景配置
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SenceParamActivity extends BaseActivity implements View.OnClickListener, SenceParamDialogFragment.CallBackListener {

    private ListView mListView;

    private ImageButton mPlus;

    private TextView mTv;

    private GalleryFlow mGallery;

    private AreaModel currentArea;

    // 场景集合
    private List<SenceModel> sences = new ArrayList<SenceModel>();

    private List<AreaModel> areas = new ArrayList<AreaModel>();

    private SenceAdapter mAdapter;

    private ImageAdapter imgAdapter;

    private HomeDB db;
    /**
     * 全局
     */
    private AreaModel tempArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sence_param);
        initVariable();
        initView();
        initListener();
        initData();
        setSelection();
    }

    private void initVariable() {
        db = HomeDB.getIntance(this);
        mAdapter = new SenceAdapter();
        tempArea = new AreaModel();
        Cursor cursor = db.rawQuery("select * from sences where areaName=?", new String[]{"全局"});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tempArea.setAreaId(cursor.getString(cursor.getColumnIndex("areaId")));
            tempArea.setId(cursor.getInt(cursor.getColumnIndex("id")));
        }
        if (cursor != null) {
            cursor.close();
        }
        tempArea.setStyle(1);
        tempArea.setName(getString(R.string.allArea));
        Bitmap icon = ImageUtil.ImgIcon(this, "room1");
        tempArea.setImage(icon);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lvSence);
        mPlus = (ImageButton) findViewById(R.id.imgBtnPlus);
        mTv = (TextView) findViewById(R.id.tvPlusType);
        mTv.setText(R.string.sence);
        mGallery = (GalleryFlow) findViewById(R.id.idGallery);
        TextView mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitle.setText(R.string.sence_param);

    }

    public void initListener() {
        mPlus.setOnClickListener(this);
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentArea = areas.get(position);
                setSelection();
            }
        });
    }

    private void initData() {
        areas.clear();
        for (AreaModel area : db.loadAreas()) {
            areas.add(area);
        }
        currentArea = tempArea;
        areas.add(0, currentArea);
        imgAdapter = new ImageAdapter(this, areas.toArray());
        mGallery.setAdapter(imgAdapter);
    }

    class ViewHolder {
        TextView tvArea;
        ImageButton imgEdit;
        ImageButton imgDel;
    }

    class SenceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sences.size();
        }

        @Override
        public Object getItem(int position) {
            return sences.get(position);
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
            final SenceModel sence = sences.get(position);
            holder.tvArea.setText(sence.getName());
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showSenceParamDialog(sence, currentArea, 1);
                }
            });
            holder.imgDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String sql = "delete from sence_devices where  senceId=?";
                    String sql2 = "delete from sences where senceId =?";
                    db.execSql(sql, new String[]{sence.getSenceId()});
                    db.execSql(sql2, new String[]{sence.getSenceId()});
                    sences.remove(sence);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

    }

    private void showSenceParamDialog(SenceModel sence, AreaModel area, int state) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SenceParamDialogFragment spf = SenceParamDialogFragment.newInstance(sence, area, state);
        spf.show(ft, "SenceParamDialogFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnPlus:
                SenceModel sence = new SenceModel();
                showSenceParamDialog(sence, currentArea, 0);
                break;

            default:
                break;
        }
    }

    private void setSelection() {
        sences.clear();
        for (SenceModel s : db.loadSences(currentArea.getAreaId())) {
            sences.add(s);
        }
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinish() {
        setSelection();
    }

}
