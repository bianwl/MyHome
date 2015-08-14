package com.vann.myhome.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.constants.CommonConstant;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域设备选择对话框
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class AreaParamDialogFragment extends DialogFragment {
    private HomeDB db;

    private AreaParamDevAdapter mAdapter;

    private EditText mAreaName;

    private ListView mListView;

    private TextView mTitle;

    private Button mOk;

    private Button mCancel;

    private int state;

    // 当前区域名称
    private String currentName;

    private AreaModel currentArea;

    public static AreaParamDialogFragment newInstance(AreaModel area, int type) {
        AreaParamDialogFragment df = new AreaParamDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("area", area);
        bundle.putInt("type", type);
        df.setArguments(bundle);
        return df;
    }

    public interface CallBackListener {
        void onFinish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initVariable();
        String title = "添加区域";
        if (!TextUtils.isEmpty(currentName)) {
            title = "编辑区域";
        }
        View view = inflater.inflate(R.layout.dialog_areaparam, container,
                false);
        initView(view);
        initListener();
        bindData();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTitle.setText(title);
        return view;
    }

    private void initVariable() {
        db = HomeDB.getIntance(getActivity());
        AreaModel area = getArguments().getParcelable("area");
        String areaId = "";
        if (area != null) {
            areaId = area.getAreaId();
            currentName = area.getName();
            currentArea = area;
        }

        int type = getArguments().getInt("type");
        if (type == CommonConstant.DIALOG_INSERT) {
            state = CommonConstant.DIALOG_INSERT;
            mAdapter = new AreaParamDevAdapter(db.loadDevices());
        } else {
            state = CommonConstant.DIALOG_EDIT;
            mAdapter = new AreaParamDevAdapter(db.loadAreaDevice(areaId, 2));
        }
    }

    private void initListener() {
        // 确定按钮监听
        mOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String areaName = mAreaName.getText().toString();
                if (TextUtils.isEmpty(areaName)) {
                    DialogUtil.createWarnDialog(getActivity(), "区域名称不能为空！");
                    return;
                } else if (isExists(areaName) && state == CommonConstant.DIALOG_INSERT) {
                    DialogUtil.createWarnDialog(getActivity(), areaName + "区域已存在,请重新命名！");
                    return;
                }
                if (currentArea == null) {
                    currentArea = new AreaModel();
                }
                currentArea.setName(areaName);
                String areaId = currentArea.getAreaId();
                DeviceModel dev = null;
                String devName = "";
                if (state == CommonConstant.DIALOG_INSERT) {// 添加区域配置
                    for (int i = 0; i < mAdapter.getList().size(); i++) {
                        dev = mAdapter.getList().get(i);
                        devName = dev.getName();
                        if (dev.isChecked()) {
                            db.saveAreaDevice(areaId, devName, "1");
                        } else {
                            db.saveAreaDevice(areaId, devName, "0");
                        }
                    }
                    db.saveArea(currentArea);

                } else if (state == CommonConstant.DIALOG_EDIT) {// 编辑区域配置
                    for (int j = 0; j < mAdapter.getList().size(); j++) {
                        dev = mAdapter.getList().get(j);
                        devName = dev.getName();
                        if (dev.isChecked()) {
                            db.updateAreaDevice(areaId, devName, "1");
                        } else {
                            db.updateAreaDevice(areaId, devName, "0");
                        }
                    }
                    db.updateArea(areaId, areaName);
                }
                CallBackListener listener = (CallBackListener) getActivity();
                listener.onFinish();
                dismiss();
            }
        });
        // 取消按钮监听
        mCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initView(View view) {
        mTitle = (TextView) view.findViewById(R.id.dialog_title);
        mAreaName = (EditText) view
                .findViewById(R.id.edt_dialog_areaparam_name);
        if (state == CommonConstant.DIALOG_EDIT) {
            mAreaName.setText(currentName);
        }
        mListView = (ListView) view.findViewById(R.id.lv_dialog_areaparam);
        mOk = (Button) view.findViewById(R.id.btn_ok);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void bindData() {
        mListView.setAdapter(mAdapter);
    }

    class AreaParamDevAdapter extends BaseAdapter {

        /**
         * 区域对应的设备列表
         */
        private List<DeviceModel> devices = new ArrayList<DeviceModel>();

        public List<DeviceModel> getList() {
            return devices;
        }

        public AreaParamDevAdapter(List<DeviceModel> devList) {
            devices = devList;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.dialog_areaparam_list_item, null);
                holder.imv_dev = (ImageView) convertView
                        .findViewById(R.id.imv_device);
                holder.tv_devName = (TextView) convertView
                        .findViewById(R.id.tv_devName);
                holder.otherName = (TextView) convertView
                        .findViewById(R.id.tv_otherName);
                holder.ck_selected = (CheckBox) convertView
                        .findViewById(R.id.ck_devSelect);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final DeviceModel dev = (DeviceModel) getItem(position);
            if (dev.getName().startsWith("k")) {
                holder.imv_dev.setImageResource(R.drawable.k_b);
            } else if (dev.getName().startsWith("t")) {
                holder.imv_dev.setImageResource(R.drawable.t_b);
            } else if (dev.getName().startsWith("c")) {
                holder.imv_dev.setImageResource(R.drawable.c_b);
            }
            holder.tv_devName.setText(dev.getName());
            holder.otherName.setText(dev.getOtherName());
            holder.ck_selected
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            dev.setChecked(isChecked);
                        }
                    });
            holder.ck_selected.setChecked(dev.isChecked());
            return convertView;
        }

    }

    class ViewHolder {
        ImageView imv_dev;
        TextView tv_devName;
        TextView otherName;
        CheckBox ck_selected;

    }

    private boolean isExists(String areaName) {
        for (AreaModel area : db.loadAreas()) {
            if (TextUtils.equals(areaName, area.getName())) {
                return true;
            }
        }
        return false;
    }

}
