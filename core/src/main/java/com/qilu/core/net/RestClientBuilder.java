package com.qilu.core.net;

import android.content.Context;

import com.qilu.core.net.callback.IError;
import com.qilu.core.net.callback.IFailure;
import com.qilu.core.net.callback.IRequest;
import com.qilu.core.net.callback.ISuccess;
import com.qilu.core.ui.loader.LoaderStyle;
import java.util.WeakHashMap;

public final class RestClientBuilder {

    private final WeakHashMap<String, String> PARAMS = new WeakHashMap<>();
    private String mUrl = null;
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private Context mContext = null;
    private LoaderStyle mLoaderStyle = null;

    // upload
    private String mFile = null;
    private WeakHashMap<String, String> mFiles = new WeakHashMap<>();


    RestClientBuilder() {
    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, String> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, String value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder file(String file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String key, String val) {
        this.mFiles.put(key, val);
        return this;
    }


    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final RestClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final RestClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
        return this;
    }


    public final RestClient build() {
        return new RestClient(mUrl, PARAMS,
                mIRequest, mISuccess, mIFailure,
                mIError, mFiles, mFile, mContext,
                mLoaderStyle);
    }
}
