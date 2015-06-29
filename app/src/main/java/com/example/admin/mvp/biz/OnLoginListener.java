package com.example.admin.mvp.biz;

import com.example.admin.mvp.model.User;

/**
 * Created by admin on 2015/6/28.
 */
public interface OnLoginListener
{
    void loginSuccess(User user);

    void loginFailed();
}
