package com.vann.myhome.util;

import android.text.TextUtils;

import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.DeviceModel;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class Utility {
    /**
     * 将服务器上返回的设备信息存储到数据库中
     *
     * @param db
     * @param data
     * @param ip
     */
    public static void handleDevice(HomeDB db, String data, String ip) {
        if (data == null) {
            return;
        }
        if (data.contains("$vnm,") && data.contains("0")) {
            String[] devices = data.split(",");
            String devCode = devices[1];
            String devType = devCode.substring(0, 1);
            for (int i = 1; i < 4; i++) {
                String state = devices[i + 1];
                if (!TextUtils.equals(state, "00n")) {
                    DeviceModel dev = new DeviceModel();
                    dev.setDevIp(ip);
                    dev.setDevId(devCode);
                    dev.setDevNum(i);
                    dev.setName(devCode + i);
                    dev.setOtherName(devCode + i);
                    dev.setType(devType);
                    dev.setState(state);
                    if (!isExist(db, dev)) {
                        db.saveDevice(dev);
                    }
                }
            }
        }
    }


    /**
     * 判断设备信息表中是否已经存在该设备
     *
     * @param dev
     * @return
     */
    public static boolean isExist(HomeDB db, DeviceModel dev) {
        if (dev == null) {
            return true;
        }
        for (DeviceModel d : db.loadDevices()) {
            if (TextUtils.equals(dev.getName(), d.getName())) {
                return true;
            }
        }
        return false;
    }
}
