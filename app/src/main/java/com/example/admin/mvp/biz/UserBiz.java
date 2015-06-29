package com.example.admin.mvp.biz;

import android.util.Log;

import com.example.admin.mvp.base.BaseConfig;
import com.example.admin.mvp.model.User;
import com.example.admin.mvp.utils.JsonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2015/6/28.
 */
public class UserBiz implements IUserBiz
{

    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener)
    {
        //模拟子线程耗时操作
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                HashMap<String ,String> params = new HashMap<String, String>();
                params.put("name",username);
                params.put("password",password);
                params.put("method", "login");

                SyncHttpClient client = new SyncHttpClient();
                RequestParams requestParams = new RequestParams(params);
                client.get(BaseConfig.api+BaseConfig.AskType.getAllDriver, requestParams, new JsonHttpResponseHandler() {
                    //模拟登录成功
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {

                            String result = response.getString("result");
                            User user = (User) JsonUtils.setResult(result);
                            if(user!=null){
                                loginListener.loginSuccess(user);
                            }else {
                                loginListener.loginFailed();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                        // Pull out the first event on the public timeline
                        Log.i("fail", "success2 mission");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        Log.i("fail", "failing mission");
                        loginListener.loginFailed();
                    }
                });
            }
        }.start();
    }
}
