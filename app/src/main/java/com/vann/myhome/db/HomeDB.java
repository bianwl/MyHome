package com.vann.myhome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.vann.myhome.model.AreaModel;
import com.vann.myhome.model.DeviceModel;
import com.vann.myhome.model.SenceModel;
import com.vann.myhome.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class HomeDB {
    public static final String TAG = "HomeDB";

    private SQLiteDatabase db;
    private static HomeDB homeDB;
    private Context mContext;

    /**
     * 构造方法私有化
     *
     * @param context
     */
    private HomeDB(Context context) {
        HomeOpenHelper dbHelp = new HomeOpenHelper(context);
        db = dbHelp.getReadableDatabase();
        mContext = context;
    }

    /**
     * 获取homeDB实例
     *
     * @param context
     * @return
     */
    public synchronized static HomeDB getIntance(Context context) {
        if (homeDB == null) {
            homeDB = new HomeDB(context);
        }
        return homeDB;
    }

    /**
     * 存储Device实例到数据库
     *
     * @param dev
     */
    public void saveDevice(DeviceModel dev) {
        if (dev != null) {
            ContentValues values = new ContentValues();
            values.put("devName", dev.getName());
            values.put("devId", dev.getDevId());
            values.put("devNum", dev.getDevNum());
            values.put("otherName", dev.getOtherName());
            values.put("devIp", dev.getDevIp());
            values.put("devState", dev.getState());
            values.put("devType", dev.getType());
            db.insert("devices", null, values);
        }
    }

    /**
     * 清空设备表信息
     */
    public void clearAllDevices() {
        String sql = "delete from devices";
        db.execSQL(sql);
    }

    /**
     * 获取所有设备信息
     *
     * @return
     */
    public List<DeviceModel> loadDevices() {
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        Cursor cursor = db.query("devices", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DeviceModel dev = new DeviceModel();
                dev.setDevId(cursor.getString(cursor
                        .getColumnIndexOrThrow("devId")));
                dev.setName(cursor.getString(cursor.getColumnIndex("devName")));
                dev.setOtherName(cursor.getString(cursor
                        .getColumnIndex("otherName")));
                dev.setDevIp(cursor.getString(cursor.getColumnIndex("devIp")));
                dev.setDevNum(cursor.getInt(cursor.getColumnIndex("devNum")));
                dev.setState(cursor.getString(cursor.getColumnIndex("devState")));
                dev.setType(cursor.getString(cursor.getColumnIndex("devType")));
                devices.add(dev);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return devices;
    }

    /**
     * 存储区域实例到数据库
     *
     * @param area
     */
    public void saveArea(AreaModel area) {
        if (area != null) {
            ContentValues values = new ContentValues();
            values.put("areaId", area.getAreaId());
            values.put("areaName", area.getName());
            db.insert("areas", null, values);
        }
    }

    public void updateArea(String areaId, String areaName) {
        ContentValues values = new ContentValues();
        values.put("areaName", areaName);
        db.update("areas", values, "areaId=?", new String[]{areaId});
    }

    /**
     * 获取所有区域信息
     *
     * @return
     */
    public List<AreaModel> loadAreas() {
        List<AreaModel> areas = new ArrayList<AreaModel>();
        Cursor cursor = db.query("areas", null, null, null, null, null, null);
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                AreaModel area = new AreaModel();
                area.setAreaId(cursor.getString(cursor.getColumnIndex("areaId")));
                area.setName(cursor.getString(cursor.getColumnIndex("areaName")));
                Bitmap icon = ImageUtil.ImgIcon(mContext,
                        "room" + String.valueOf(i % 3));
                area.setImage(icon);
                areas.add(area);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return areas;
    }

    /**
     * 查询区域信息
     *
     * @param areaId
     * @return
     */
    public Cursor queryArea(String areaId) {
        String sql = "select * from areas where areaId=?";
        return db.rawQuery(sql, new String[]{areaId});
    }

    /**
     * 存储场景实例到数据库
     *
     * @param sence
     */
    public void saveSence(SenceModel sence) {
        if (sence != null) {
            ContentValues values = new ContentValues();
            values.put("senceId", sence.getSenceId());
            values.put("senceName", sence.getName());
            values.put("areaId", sence.getAreaId());
            values.put("areaName", sence.getAreaName());
            values.put("senceType", sence.getType());
            db.insert("sences", null, values);
        }
    }

    public void updateData(String table, ContentValues values,
                           String whereClause, String[] whereArgs) {
        db.update(table, values, whereClause, whereArgs);
    }

    /**
     * 插入记录
     *
     * @param table
     * @param values
     */
    public void insertData(String table, ContentValues values) {
        db.insert(table, null, values);
    }

    /**
     * 获取所有场景
     *
     * @return
     */
    public List<SenceModel> loadSences() {
        List<SenceModel> sences = new ArrayList<SenceModel>();
        Cursor cursor = db.query("sences", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                SenceModel sence = new SenceModel();
                sence.setSenceId(cursor.getString(cursor.getColumnIndex("senceId")));
                sence.setName(cursor.getString(cursor.getColumnIndex("senceName")));
                sence.setAreaId(cursor.getString(cursor.getColumnIndex("areaId")));
                sence.setAreaName(cursor.getString(cursor
                        .getColumnIndex("areaName")));
                sence.setType(cursor.getInt(cursor.getColumnIndex("senceType")));
                sences.add(sence);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return sences;
    }

    /**
     * 获取区域场景
     *
     * @param areaId
     * @return
     */
    public List<SenceModel> loadSences(String areaId) {
        List<SenceModel> sences = new ArrayList<SenceModel>();
        Cursor cursor = db.query("sences", null, "areaId=?",
                new String[]{areaId}, null, null, null);
        Log.e(TAG, cursor.getCount() + "");
        while (cursor.moveToNext()) {
            SenceModel sence = new SenceModel();
            sence.setSenceId(cursor.getString(cursor.getColumnIndex("senceId")));
            sence.setName(cursor.getString(cursor.getColumnIndex("senceName")));
            sence.setAreaId(cursor.getString(cursor.getColumnIndex("areaId")));
            sence.setAreaName(cursor.getString(cursor
                    .getColumnIndex("areaName")));
            sence.setType(cursor.getInt(cursor.getColumnIndex("senceType")));
            sences.add(sence);
        }
        if (cursor != null) {
            cursor.close();
        }
        return sences;
    }

    /**
     * 添加区域配置信息记录
     *
     * @param areaName
     * @param devName
     * @param selected
     */
    public void saveAreaDevice(String areaId, String devName, String selected) {
        ContentValues values = new ContentValues();
        values.put("areaId", areaId);
        values.put("devName", devName);
        values.put("selected", selected);
        db.insert("area_devices", null, values);
    }

    /**
     * @param sql
     * @param bindArgs
     */
    public void execSql(String sql, Object[] bindArgs) {
        db.execSQL(sql, bindArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        return db.query(table, columns, selection, selectionArgs, groupBy,
                having, orderBy);
    }

    /**
     * 更新区域配置
     *
     * @param areaName
     * @param devName
     * @param selected
     */
    public void updateAreaDevice(String areaId, String devName, String selected) {
        ContentValues values = new ContentValues();
        values.put("selected", selected);
        String[] args = {areaId, devName};
        db.update("area_devices", values, "areaId=? and devName =?", args);
    }

    /**
     * 查询记录
     *
     * @param areaName
     * @param devName
     * @return
     */
    public Cursor queryAreaDevice(String areaId, String devName) {
        String sql = "select * from area_devices where areaId=? and devName=?";
        return db.rawQuery(sql, new String[]{areaId, devName});
    }

    /**
     * @param areaId
     * @param isSelect 0:未选中的，1：选中的，2：全部
     * @return
     */
    public List<DeviceModel> loadAreaDevice(String areaId, int isSelect) {
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        String sql = "";
        String[] args = null;
        if (isSelect == 2) {
            sql = "select * from area_devices where areaId=?";
            args = new String[]{areaId};
        } else {
            sql = "select * from area_devices where areaId=? and selected=?";
            args = new String[]{areaId, String.valueOf(isSelect)};
        }

        Cursor cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()) {
            do {
                String devName = cursor.getString(cursor
                        .getColumnIndex("devName"));
                String selected = cursor.getString(cursor
                        .getColumnIndex("selected"));
                DeviceModel device = getDevice(devName);
                if (TextUtils.equals(selected, "0")) {
                    device.setChecked(false);
                } else {
                    device.setChecked(true);
                }
                devices.add(device);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return devices;
    }

    /**
     * 获取场景下的设备配置信息
     *
     * @param senceId
     * @return
     */
    public List<DeviceModel> loadSenceDevices(String senceId) {
        List<DeviceModel> devices = new ArrayList<DeviceModel>();
        String sql = "select * from sence_devices where senceId =?";
        Cursor cursor = db.rawQuery(sql, new String[]{senceId});
        if (cursor.moveToFirst()) {
            do {
                String devId = cursor.getString(cursor.getColumnIndex("devId"));
                DeviceModel device = getDevice(devId);
                devices.add(device);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return devices;
    }

    public DeviceModel getDevice(String devName) {
        DeviceModel device = null;
        for (DeviceModel dev : loadDevices()) {
            if (TextUtils.equals(devName, dev.getName())) {
                device = dev;
                break;
            }
        }
        return device;
    }

    public SenceModel getSence(String sid) {
        SenceModel sence = null;
        for (SenceModel s : loadSences()) {
            if (TextUtils.equals(sid, s.getSenceId())) {
                sence = s;
                break;
            }
        }
        return sence;
    }
}
