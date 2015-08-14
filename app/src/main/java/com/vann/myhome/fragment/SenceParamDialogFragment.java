package com.vann.myhome.fragment;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vann.myhome.R;
import com.vann.myhome.adapter.SenceParamDevAdapter;
import com.vann.myhome.constants.CommonConstant;
import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.model.SenceModel;
import com.vann.myhome.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景配置设备选择设置
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SenceParamDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button mOk;
    private Button mCancel;
    private EditText mName;
    private ListView mListView;
    private RadioButton rbAll;
    private RadioButton rbArea;
    private TextView mTitle;

    private HomeDB db;

    private SenceParamDevAdapter mAdapter;

    private SenceModel sence;

    private AreaModel area;

    /**
     * 当前窗口类别
     */
    private int mState;
    /**
     * 场景类别 0：区域 1：全局
     */
    private int mStyle;
    /**
     * 选中的设备Name集合
     */
    private List<String> devices = new ArrayList<String>();

    public static SenceParamDialogFragment newInstance(SenceModel sence,
                                                       AreaModel area, int state) {
        Bundle bd = new Bundle();
        bd.putParcelable("sence", sence);
        bd.putInt("dialog_state", state);
        bd.putParcelable("area", area);
        SenceParamDialogFragment spf = new SenceParamDialogFragment();
        spf.setArguments(bd);
        return spf;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_senceparam, container,
                false);
        mOk = (Button) view.findViewById(R.id.idOk);
        mCancel = (Button) view.findViewById(R.id.idCancel);
        mListView = (ListView) view.findViewById(R.id.devList);
        rbAll = (RadioButton) view.findViewById(R.id.allArea);
        rbArea = (RadioButton) view.findViewById(R.id.area);
        mName = (EditText) view.findViewById(R.id.edt_senceNme);
        mTitle = (TextView) view.findViewById(R.id.dialog_title);
        initVariable();
        // 初始化监听
        initListerner();
        // 初始化数据
        initData();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    private void initVariable() {
        int state = getArguments().getInt("dialog_state");
        sence = getArguments().getParcelable("sence");
        area = getArguments().getParcelable("area");
        sence.setAreaId(area.getAreaId());
        sence.setAreaName(area.getName());
        int type = area.getStyle();
        db = HomeDB.getIntance(getActivity());
        if (state == CommonConstant.DIALOG_EDIT) {
            mState = CommonConstant.DIALOG_EDIT;
            mTitle.setText("编辑场景");
            mName.setText(sence.getName());

        } else if (state == CommonConstant.DIALOG_INSERT) {
            mState = CommonConstant.DIALOG_INSERT;
            mTitle.setText("添加场景");
        }

        if (type == CommonConstant.SENCE_ALL) {
            mStyle = CommonConstant.SENCE_ALL;
            rbAll.setChecked(true);
            rbArea.setChecked(false);
        } else if (type == CommonConstant.SENCE_AREA) {
            mStyle = CommonConstant.SENCE_AREA;
            rbAll.setChecked(false);
            rbArea.setChecked(true);
        }
        sence.setType(mStyle);
    }

    private void initListerner() {
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initData() {
        String sql = "select * from sence_devices where senceId =?";
        Cursor cursor = db.rawQuery(sql, new String[]{sence.getSenceId()});
        if (cursor.moveToFirst()) {
            do {
                String devName = cursor.getString(cursor
                        .getColumnIndex("devName"));
                devices.add(devName);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        mAdapter = new SenceParamDevAdapter(getActivity(), devices, db,
                sence.getSenceId());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idOk:
                String name = mName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    DialogUtil.createWarnDialog(getActivity(), "场景名称不能为空！");
                    return;
                } else if (isExists(name) && mState == CommonConstant.DIALOG_INSERT) {
                    DialogUtil.createWarnDialog(getActivity(), name + "场景已存在，请重新命名！");
                    return;
                }
                if (mState == CommonConstant.DIALOG_EDIT) {
                    updateSenceParam(name);
                } else if (mState == CommonConstant.DIALOG_INSERT) {
                    sence.setName(name);
                    db.saveSence(sence);
                    saveSenceParam(name);
                }
                getDialog().dismiss();
                CallBackListener listener = (CallBackListener) getActivity();
                listener.onFinish();
                break;
            default:
                getDialog().dismiss();
                break;
        }
    }

    /**
     * 更新场景配置
     */
    private void updateSenceParam(String senceName) {
        String sId = sence.getSenceId();
        ContentValues values = new ContentValues();
        values.put("senceName", senceName);
        db.updateData("sences", values, "senceId=?", new String[]{sId});
        String sql = "delete from sence_devices where senceId=?";
        db.execSql(sql, new String[]{sId});
        saveSenceParam(senceName);
    }

    /**
     * 保存场景配置
     */
    private void saveSenceParam(String senceName) {
        if (mAdapter.getResults().isEmpty()) {
            return;
        }
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        devices.addAll(mAdapter.getResults());
        for (DeviceModel dev : devices) {
            ContentValues values = new ContentValues();
            values.put("devName", dev.getName());
            values.put("devState", dev.getState());
            values.put("senceId", sence.getSenceId());
            values.put("senceType", mStyle);
            values.put("areaId", area.getAreaId());
            values.put("areaName", area.getName());
            db.insertData("sence_devices", values);
        }
    }

    public interface CallBackListener {
        void onFinish();
    }

    private boolean isExists(String sName) {
        for (SenceModel sence : db.loadSences(area.getAreaId())) {
            if (TextUtils.equals(sName, sence.getName())) {
                return true;
            }
        }
        return false;
    }
}
