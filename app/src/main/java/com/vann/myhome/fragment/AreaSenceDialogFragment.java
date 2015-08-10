package com.vann.myhome.fragment;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.model.SenceModel;
import com.vann.myhome.util.SocketUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域场景控制
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class AreaSenceDialogFragment extends DialogFragment {

    private GridView mGd;
    private Button close;
    private HomeDB db;

    private GdAdapter mAdapter;

    private AreaModel currentArea;

    private List<String> sences = new ArrayList<String>();

    public static AreaSenceDialogFragment newInstance(AreaModel area) {
        Bundle bd = new Bundle();
        bd.putParcelable("area", area);
        AreaSenceDialogFragment fragment = new AreaSenceDialogFragment();
        fragment.setArguments(bd);
        return fragment;
    }

    public AreaSenceDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("区域场景");
        View view = inflater.inflate(R.layout.dialog_area_sence, container,
                false);
        db = HomeDB.getIntance(getActivity());
        mGd = (GridView) view.findViewById(R.id.gd_areaSence);
        close = (Button) view.findViewById(R.id.btnClose);
        initListener();
        initData();
        mAdapter = new GdAdapter();
        mGd.setAdapter(mAdapter);
        return view;
    }

    private void initData() {
        Bundle bundle = getArguments();
        currentArea = bundle.getParcelable("area");
        String areaId = currentArea.getAreaId();
        Cursor cursor = db.query("sences", null, "areaId=?",
                new String[]{areaId}, null, null, null);
        sences.clear();
        if (cursor.moveToFirst()) {
            do {
                String senceName = cursor.getString(cursor
                        .getColumnIndex("senceName"));
                sences.add(senceName);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void initListener() {
        // 按钮监听
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        // 九宫格监听
        mGd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String sName = sences.get(position);
                String sid = null;
                for (SenceModel s : db.loadSences(currentArea.getAreaId())) {
                    if (TextUtils.equals(sName, s.getName())) {
                        sid = s.getSenceId();
                        break;
                    }
                }
                Cursor cursor = db.query("sence_devices", null, "senceId=?",
                        new String[]{sid}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String devState = cursor.getString(cursor
                                .getColumnIndex("devState"));
                        String devName = cursor.getString(cursor
                                .getColumnIndex("devName"));
                        DeviceModel dev = db.getDevice(devName);
                        SocketUtil.send(dev, devState);
                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        });

    }

    class ViewHolder {
        ImageView imv;
        TextView title;
    }

    class GdAdapter extends BaseAdapter {

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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.gridview_item, null);
                holder = new ViewHolder();
                holder.imv = (ImageView) convertView
                        .findViewById(R.id.imb_sence);
                holder.title = (TextView) convertView
                        .findViewById(R.id.tvSence);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imv.setBackgroundResource(R.drawable.room0);
            String senceName = sences.get(position);
            holder.title.setText(senceName);

            return convertView;
        }

    }
}
