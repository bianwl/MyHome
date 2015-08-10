package com.vann.myhome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class HomeOpenHelper extends SQLiteOpenHelper {

    /** 数据库名称 */
    public static final String DATABASE_NAME = "vannhome.db";
    /** 版本号 */
    public static final int DATABASE_VERSION = 1;
    /** 区域表创建语句 */
    public static final String CREATE_AREA = "create table if not exists areas("
            + "id integer primary key autoincrement,"
            + "areaId text,areaName text)";

    /** 设备表创建语句 */
    public static final String CREATE_DEVICE = "create table if not exists devices("
            + "id integer primary key autoincrement,"
            + "devId text,devName text,otherName text,devNum integer,"
            + "devIp text,devType text,devState text)";

    /** 场景表创建语句 */
    public static final String CREATE_SENCE = "create table if not exists sences("
            + "id integer primary key autoincrement,"
            + "senceId text,senceName text,senceType integer,areaId text,areaName text)";
    /** 区域配置表创建语句 */
    public static final String CREATE_AREA_DEVICE = "create table if not exists area_devices("
            + "id integer primary key autoincrement,"
            + "areaId text,devName text,selected text )";

    /** 场景配置表创建语句 */
    public static final String CREATE_SENCE_DEVICE = "create table if not exists sence_devices("
            + "id integer primary key autoincrement,"
            + "senceId text,senceType integer,areaId text,areaName text,devName text,devState text)";

    public HomeOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AREA);
        db.execSQL(CREATE_AREA_DEVICE);
        db.execSQL(CREATE_DEVICE);
        db.execSQL(CREATE_SENCE);
        db.execSQL(CREATE_SENCE_DEVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
