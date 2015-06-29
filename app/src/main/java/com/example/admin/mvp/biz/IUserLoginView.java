package com.example.admin.mvp.biz;

import com.example.admin.mvp.model.User;

/**
 * Created by admin on 2015/6/28.
 */
public interface IUserLoginView
{
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError();

}
