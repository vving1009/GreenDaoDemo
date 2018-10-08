package com.pf.testgreendao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pf.testgreendao.green.DaoMaster;
import com.pf.testgreendao.green.DaoSession;

/**
 * @author zhaopf
 * @version 1.0
 * @QQ 1308108803
 * @date 2018/1/15
 */
public class GreenDaoHelper {

    private static DbUpgradeOpenHelper devOpenHelper;
    private static SQLiteDatabase database;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static void initDataBase(Context context) {
        if (null == context) {
            throw new NullPointerException("context cannot be null.");
        }
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        //devOpenHelper = new DaoMaster.DevOpenHelper(context, "pf_db", null);
        devOpenHelper = new DbUpgradeOpenHelper(context, "pf_db", null);
        database = devOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}