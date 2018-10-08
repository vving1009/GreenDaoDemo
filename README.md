Android集成GreenDao

https://github.com/greenrobot/greenDAO

### 1、项目引入GreenDao

按照github的方式就可以

* 在项目中的build.gradle中


        buildscript {

            repositories {
                google()
                jcenter()
                mavenCentral()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.0.0'
                classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2'
            }
        }

* 在app模块下的build.gradle中

1、最上面引入插件

    apply plugin: 'org.greenrobot.greendao'

2、引入

    compile 'org.greenrobot:greendao:3.2.2'

3、配置

    // schemaVersion： 数据库schema版本，也可以理解为数据库版本号
    // daoPackage：设置DaoMaster、DaoSession、Dao包名
    // targetGenDir：设置DaoMaster、DaoSession、Dao目录
    // targetGenDirTest：设置生成单元测试目录
    // generateTests：设置自动生成单元测试用例
    greendao {
        schemaVersion 1
        daoPackage 'com.pf.testgreendao.green'
        targetGenDir 'src/main/java'
    }


### 2、注解说明

greenDao3之后是以注解式来创建表的，下面先放一些注解说明

1、常用注解

    @Entity     用于标识这是一个需要Greendao帮我们生成代码的bean

    @Id         标明主键，括号里可以指定是否自增

    @Property   用于设置属性在数据库中的列名（默认不写就是保持一致）

    @NotNull    非空

    @Transient  标识这个字段是自定义的不会创建到数据库表里

2、实体类注解

    @Entity(
            schema = "myschema",

            active = true,

            nameInDb = "AWESOME_USERS",

            indexes = {
                    @Index(value = "name DESC", unique = true)
            },

            createInDb = false
    )

    解释：

        schema      一个项目中有多个schema时 标明要让这个dao属于哪个schema

        active      标明是否支持实体类之间update，refresh，delete等操作

        nameInDb    就是写个存在数据库里的表名（不写默认是一致）

        indexes     定义索引，这里可跨越多个列

        CreateInDb  如果是有多个实体都关联这个表，可以把多余的实体里面设置为false避免重复创建（默认是true）

3、索引注解

    @Entity
    public class User {
        @Id
        private Long id;

        @Index(unique = true)
        private String name;
    }

    @Entity
    public class User {
        @Id
        private Long id;

        @Unique
        private String name;
    }

    @Index      通过这个字段建立索引

    @Unique     添加唯一约束，上面的括号里unique=true作用相同

4、关系注解

    @ToOne      将自己的一个属性与另一个表建立关联

    @ToMany     将自己的一个属性与多个表建立关联

### 3、使用greenDao

**首先创建一个User类**

    package com.pf.testgreendao.entity;

    import org.greenrobot.greendao.annotation.Entity;
    import org.greenrobot.greendao.annotation.Id;
    import org.greenrobot.greendao.annotation.NotNull;
    import org.greenrobot.greendao.annotation.Generated;

    /**
     * @author zhaopf
     * @version 1.0
     * @QQ 1308108803
     * @date 2018/1/15
     */
    @Entity
    public class User {

        /**
         * 主键，传入null id就会自增
         */
        @Id
        private Long id;
        @NotNull
        private String userName;
        @NotNull
        private String passWord;

        private int age;

        @Generated(hash = 652254006)
        public User(Long id, @NotNull String userName, @NotNull String passWord,
                int age) {
            this.id = id;
            this.userName = userName;
            this.passWord = passWord;
            this.age = age;
        }

        @Generated(hash = 586692638)
        public User() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", passWord='" + passWord + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

**之后build项目，会自动生成一个green文件夹(路径由build.gradle中daoPackage字段指定)**

<br/>

**我们需要对这个GDUser表进行操作就需要建一个帮助类**

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

        private static DaoMaster.DevOpenHelper devOpenHelper;
        private static SQLiteDatabase database;
        private static DaoMaster daoMaster;
        private static DaoSession daoSession;

        public static void initDataBase(Context context) {
            if (null == context) {
                throw new NullPointerException("context cannot be null.");
            }
            // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
            // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
            devOpenHelper = new DaoMaster.DevOpenHelper(context, "pf_db", null);
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

<br/>

**之后就可以进行增删改查操作了**

<br/>

    package com.pf.testgreendao;

    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;

    import com.pf.testgreendao.entity.User;
    import com.pf.testgreendao.green.UserDao;
    import com.pf.testgreendao.util.GreenDaoHelper;

    import java.util.Collections;
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
            userDao.insert(new User(null, "name1", "pwd1", 121));
            userDao.insert(new User(null, "name2", "pwd2", 122));
            userDao.insert(new User(null, "name3", "pwd3", 123));
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

### 4、方法总结

增：

    // 传入null id就会自增
    userDao.insert(new User(null, "name1", "pwd1", 121));

删：

    删除某一个：  userDao.deleteByKey((long) 1);
    删除所有：   userDao.deleteAll();

改：

    userDao.update(user);

查：

    查询的方法主要依靠下面2个

    userDao.queryRaw();

          queryRaw的参数是 String where, String... selectionArg

          示例：List<User> userList = userDao.queryRaw("where T.userName=?", "name1")

    userDao.queryBuilder();

          示例：
                List<User> userList = userDao.queryBuilder()
                                        .where(UserDao.Properties.UserName.eq(30))
                                        .build()
                                        .list();

### 5、小结

    1、@id在添加的时候可以传null，默认会自增

    2、List<User> userList = userDao.queryBuilder()
                      .where(UserDao.Properties.UserName.eq("name1"))
                      .build()
                      // 查询一个人，使用unique，查多人可以用list
                      .list();

        查询的时候最好用list接收
            如果有一条数据，那么直接userList.get(0)
            如果有多条数据，但是用了unique，就会挂掉

    3、删除方法没有返回值，所以当要删除某一个key的时候，这个key不存在，也不会报错