package com.pf.testgreendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pf.testgreendao.entity.User;
import com.pf.testgreendao.green.UserDao;
import com.pf.testgreendao.util.GreenDaoHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = GreenDaoHelper.getDaoSession().getUserDao();
    }

    /**
     * 添加
     *
     * @param view
     */
    public void add(View view) {
        // 传入null id就会自增
        userDao.insert(new User(null, "name1", "pwd1", "13800000000", 121));
        userDao.insert(new User(null, "name2", "pwd2", "13900000000", 122));
        userDao.insert(new User(null, "name3", "pwd3", "13500000000", 123));
        Log.i(TAG, "添加成功");
    }

    /**
     * 删除
     *
     * @param view
     */
    public void remove(View view) {
        userDao.deleteByKey((long) 1);
        Log.i(TAG, "删除成功");
    }

    /**
     * 修改
     *
     * @param view
     */
    public void update(View view) {
        List<User> userList = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq("name1"))
                .build()
                // 查询一个人，使用unique，查多人可以用list
                .list();
        if (null != userList && !userList.isEmpty()) {
            for (User user : userList) {
                user.setAge(user.getAge() + 1);
                userDao.update(user);
            }
            Log.i(TAG, "修改成功");
        } else {
            Log.i(TAG, "修改失败");
        }
    }

    /**
     * 查询
     *
     * @param view
     */
    public void select(View view) {
        for (User user : userDao.loadAll()) {
            Log.i(TAG, user.toString());
        }
        Log.i(TAG, "查询完成");
    }

    /**
     * 删除所有
     *
     * @param view
     */
    public void removeAll(View view) {
        userDao.deleteAll();
        Log.i(TAG, "删除全部");
    }
}