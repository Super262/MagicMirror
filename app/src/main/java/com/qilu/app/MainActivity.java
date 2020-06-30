package com.qilu.app;

import android.Manifest;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.qilu.core.activities.ProxyActivity;
import com.qilu.core.app.AccountManager;
import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.util.storage.ImageHistoryHelper;
import com.qilu.core.util.storage.QiluPreference;
import com.qilu.core.util.storage.UserCollectionHelper;
import com.qilu.ec.launcher.LauncherDelegate;
import com.qilu.ec.main.EcBottomDelegate;
import com.qilu.ec.sign.ISignListener;
import com.qilu.ec.sign.SignInDelegate;
import com.qilu.ui.launcher.ILauncherListener;
import com.qilu.ui.launcher.OnLauncherFinishTag;

import java.io.File;

public class MainActivity extends ProxyActivity implements ISignListener,ILauncherListener {
    @Override
    public QiluDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.requestPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Util.requestPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW);
        Util.requestPermission(this, Manifest.permission.WAKE_LOCK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorECampus));
        }
    }

    @Override
    public void post(Runnable runnable) {

    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        getSupportDelegate().startWithPop(new EcBottomDelegate());
    }

    @Override
    public void onSignInFailure(String code) {
        if(code.equals("400")){
            Toast.makeText(this, "手机号或密码错误", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "内部错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTokenExpired() {
        AccountManager.setSignState("","","",false);
        QiluPreference.clearAppPreferences();
        deleteDatabase(UserCollectionHelper.TABLE_NAME);
        deleteDatabase(ImageHistoryHelper.TABLE_NAME);
        getSupportDelegate().pop();
        getSupportDelegate().startWithPop(new SignInDelegate());
    }

    @Override
    public void onSignUpSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        getSupportDelegate().startWithPop(new SignInDelegate());
    }

    @Override
    public void onSignUpFailure(String code) {
        if(code.equals("422")){
            Toast.makeText(this, "重复注册，请登录", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "内部错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch(tag){
            case SIGNED:
                Toast.makeText(this,"已登录",Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new EcBottomDelegate());
                break;
            case NOT_SIGNED:
                Toast.makeText(this,"未登录",Toast.LENGTH_LONG).show();
                getSupportDelegate().startWithPop(new SignInDelegate());
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
