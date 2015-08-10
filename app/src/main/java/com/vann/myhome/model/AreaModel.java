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
public class AreaModel implements Parcelable {

    /** id */
    private int id;
    /** 区域id */
    private String areaId;
    /** 名称 */
    private String name;
    /**类别 0:区域，1：全局*/
    private int  style = CommonConstant.SENCE_AREA;

    /**image*/
    private Object image;

    /** 区域下的设备集合 */
    private List<DeviceModel> children = new ArrayList<DeviceModel>();

    public AreaModel() {
        areaId = UUID.randomUUID().toString();
    }

    public AreaModel(Parcel parcel) {
        id = parcel.readInt();
        areaId = parcel.readString();
        name = parcel.readString();
        style = parcel.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int pid){
        id = pid;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }



    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public void setChildren(List<DeviceModel> children) {
        this.children = children;
    }

    public List<DeviceModel> getChildren() {
        return this.children;
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
        dest.writeString(name);
        dest.writeInt(style);
    }

    public final static Parcelable.Creator<AreaModel> CREATE = new Parcelable.Creator<AreaModel>() {

        @Override
        public AreaModel createFromParcel(Parcel source) {
            return new AreaModel(source);
        }

        @Override
        public AreaModel[] newArray(int size) {
            return new AreaModel[size];
        }
    };
}
