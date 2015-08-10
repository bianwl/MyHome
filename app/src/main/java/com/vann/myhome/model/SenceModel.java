package com.vann.myhome.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.vann.myhome.constants.CommonConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SenceModel implements Parcelable {
    private int id;
    private String senceId;
    private String name;
    private String areaId;
    private String areaName;
    private int type = CommonConstant.SENCE_AREA; // 1:全局 ,0:区域
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private List<DeviceModel> children = new ArrayList<DeviceModel>();

    public SenceModel(){
        senceId = UUID.randomUUID().toString();
    }

    public SenceModel(Parcel parce){
        id = parce.readInt();
        senceId= parce.readString();
        name = parce.readString();
        areaId =parce.readString();
        areaName = parce.readString();
    }

    public int getId() {
        return id;
    }

    public String getSenceId() {
        return senceId;
    }

    public void setSenceId(String senceId) {
        this.senceId = senceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceModel> getChildren() {
        return children;
    }

    public void setChildren(List<DeviceModel> children) {
        this.children = children;
    }



    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public boolean removeChild(DeviceModel dev) {
        if (children != null) {
            return children.remove(dev);
        }
        return false;
    }

    public boolean addChild(DeviceModel dev) {
        if (children != null) {
            return children.add(dev);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(areaId);
        dest.writeString(areaName);
        dest.writeString(senceId);
        dest.writeString(name);
    }

    public final static Parcelable.Creator<SenceModel> CREATE = new Parcelable.Creator<SenceModel>() {

        @Override
        public SenceModel createFromParcel(Parcel source) {
            return new SenceModel(source);
        }

        @Override
        public SenceModel[] newArray(int size) {
            return new SenceModel[size];
        }
    };
}
