package com.qilu.core.net.callback;


import android.os.Handler;

import com.qilu.core.app.ConfigKeys;
import com.qilu.core.app.Qilu;
import com.qilu.core.ui.loader.LoaderStyle;
import com.qilu.core.ui.loader.QiluLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private static final Handler HANDLER =Qilu.getHandler(); //用static声明Handler，可以防止内存泄漏。

    public RequestCallbacks(IRequest request, ISuccess success, IFailure failure, IError error, LoaderStyle loader_style) {
        REQUEST = request;
        SUCCESS = success;
        FAILURE = failure;
        ERROR = error;
        LOADER_STYLE = loader_style;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (SUCCESS != null) {
                    SUCCESS.onSuccess(response.body());
                }
            }
        } else {
            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }
        onRequestFinish();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }
        onRequestFinish();
    }
    private void onRequestFinish() {
        final long delayed = Qilu.getConfiguration(ConfigKeys.LOADER_DELAYED);
        if (LOADER_STYLE != null) {
            HANDLER.postDelayed(new Runnable() {
             @Override
                public void run() {
                    QiluLoader.stopLoading();
                }
            }, delayed);
        }
    }
}
