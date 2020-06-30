package com.qilu.ec.sign;

public interface ISignListener {

    void onSignInSuccess();

    void onSignInFailure(String code);

    void onTokenExpired();

    void onSignUpSuccess();

    void onSignUpFailure(String code);
}