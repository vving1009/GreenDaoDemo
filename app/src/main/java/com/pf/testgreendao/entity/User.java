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
    @NotNull
    private String phone;

    private int age;

    @Generated(hash = 1461646461)
    public User(Long id, @NotNull String userName, @NotNull String passWord,
            @NotNull String phone, int age) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.phone = phone;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}