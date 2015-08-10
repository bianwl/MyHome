package com.vann.myhome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备实体
 *
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class DeviceModel {

    private int id;
    /**
     * 设备id
     */
    private String devId;
    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String otherName;

    private int devNum;

    private String devIp;
    /**
     * 状态
     */
    private String state;
    /**
     * 类型 k：开关，t：调节亮度按钮 c:窗帘
     */
    private String type;
    /**
     * 是否选中，0为false,1为true 用于场景、区域设置
     */
    private boolean checked = false;

    private List<AreaModel> children = new ArrayList<AreaModel>();

    public int getId() {
        return this.id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public int getDevNum() {
        return devNum;
    }

    public void setDevNum(int devNum) {
        this.devNum = devNum;
    }

    public String getDevIp() {
        return devIp;
    }

    public void setDevIp(String devIp) {
        this.devIp = devIp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setChildren(List<AreaModel> children) {
        this.children = children;
    }

    public List<AreaModel> getChildren() {
        return this.children;
    }

    public boolean removeChild(AreaModel area) {
        if (children != null) {
            return children.remove(area);
        }
        return false;
    }

    public boolean addChild(AreaModel area) {
        if (children != null) {
            children.add(area);
        }
        return false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return this.checked;
    }
}
