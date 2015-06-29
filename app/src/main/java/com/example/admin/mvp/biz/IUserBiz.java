package com.example.admin.mvp.biz;

/**
 * Created by admin on 2015/6/28.
 */
public interface IUserBiz
{
    public void login(String username, String password, OnLoginListener loginListener);
}
