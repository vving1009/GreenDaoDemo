package com.pf.testgreendao;

import android.app.Application;

import com.pf.testgreendao.util.GreenDaoHelper;

/**
 * @author zhaopf
 * @version 1.0
 * @QQ 1308108803
 * @date 2018/1/15
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoHelper.initDataBase(this);
    }
}