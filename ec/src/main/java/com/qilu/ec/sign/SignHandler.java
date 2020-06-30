package com.qilu.ec.sign;

import com.alibaba.fastjson.JSONObject;
import com.qilu.core.app.AccountManager;

public class SignHandler {

    public static void onSignIn(String phone,String pwd,String response, ISignListener signListener) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        String code = jsonObject.getString("code");
        if(code.equals("200")){
            //已经注册并登录成功了
            AccountManager.setSignState(phone,pwd,response,true);
            signListener.onSignInSuccess();
        }
        else{
            AccountManager.setSignState(phone,pwd,response,false);
            signListener.onSignInFailure(code);
        }
    }


    public static void onSignUp(String response, ISignListener signListener) {
        JSONObject jsonObject = JSONObject.parseObject(response);
        String code = jsonObject.getString("code");
        if(code.equals("201")){
            signListener.onSignUpSuccess();
        }
        else{
            signListener.onSignUpFailure(code);
        }
    }
}