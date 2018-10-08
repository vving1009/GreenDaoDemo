package com.pf.testgreendao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pf.testgreendao.green.DaoMaster;
import com.pf.testgreendao.green.UserDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库升级使用
 */

public class DbUpgradeOpenHelper extends DaoMaster.OpenHelper {

    public DbUpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DbUpgradeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, UserDao.class);
        //MigrationHelper2.migrate(db, UserDao.class);
    }
}
