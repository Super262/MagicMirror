package com.qilu.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;
import com.qilu.core.delegates.QiluDelegate;
import com.qilu.core.ec.R;
import com.qilu.core.net.RestClient;


public class SignUpDelegate extends QiluDelegate implements View.OnClickListener {
    private TextInputEditText mPhone = null;
    private TextInputEditText mName = null;
    private TextInputEditText mPassword = null;
    private TextInputEditText mRePassword = null;
    private ISignListener mISignListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mPhone = $(R.id.edit_sign_up_phone);
        mName = $(R.id.edit_sign_up_name);
        mPassword = $(R.id.edit_sign_up_password);
        mRePassword = $(R.id.edit_sign_up_re_password);
        $(R.id.btn_sign_up).setOnClickListener(this);
        $(R.id.tv_link_sign_in).setOnClickListener(this);

    }
    private boolean checkForm() {
        final String phone = mPhone.getText().toString().trim();
        final String name = mName.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String rePassword = mRePassword.getText().toString().trim();

        boolean isPass = true;

        if (phone.length() != 11) {
            mPhone.setError("手机号码错误");
            isPass = false;
        }
        else {
            mPhone.setError(null);
        }

        if (name.isEmpty()) {
            mName.setError("用户名不能为空");
            isPass = false;
        }
        else {
            mName.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            mRePassword.setError("密码验证错误");
            isPass = false;
        } else {
            mRePassword.setError(null);
        }

        return isPass;
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_sign_up) {
            onClickSignUp();
        }
        else if (i == R.id.tv_link_sign_in) {
            onClickLink();
        }
    }
    private void onClickLink() {
        getSupportDelegate().startWithPop(new SignInDelegate());
    }
    private void onClickSignUp() {
        final String phone= mPhone.getText().toString();
        final String name= mName.getText().toString();
        final String pwd=mPassword.getText().toString();
        if (checkForm()) {
            RestClient.builder()
                    .url("/register")
                    .params("phone", phone)
                    .params("name", name)
                    .params("password", pwd)
                    .success(response -> SignHandler.onSignUp(response, mISignListener))
                    .failure(() -> Toast.makeText(getContext(),"网络错误",Toast.LENGTH_LONG).show())
                    .error((code, msg) -> {
                        if(code == 422){
                            Toast.makeText(getContext(), "重复注册，请登录", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "内部错误："+msg+"，Code："+code, Toast.LENGTH_LONG).show();
                        }
                    })
                    .loader(getContext())
                    .build()
                    .postRaw();
        }
    }

}
