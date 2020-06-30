package com.qilu.core.app;


import com.alibaba.fastjson.JSONObject;
import com.qilu.core.util.storage.QiluPreference;

public class AccountManager {

    private enum SignTag {
        SIGN_TAG
    }

    //保存用户登录状态，登录后调用
    public static void setSignState(String phone,String pwd,String response,boolean state) {
        if(state){
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            String token = data.getString("token");
            String userID = data.getString("userID");
            QiluPreference.addCustomAppProfile("password",pwd);
            QiluPreference.addCustomAppProfile("phone",phone);
            QiluPreference.addCustomAppProfile("userID",userID);
            QiluPreference.addCustomAppProfile("token", token);

        }
        QiluPreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    private static boolean isSignIn() {
        return QiluPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}
