package com.qilu.core.net;

import android.content.Context;

import com.qilu.core.net.callback.IError;
import com.qilu.core.net.callback.IFailure;
import com.qilu.core.net.callback.IRequest;
import com.qilu.core.net.callback.ISuccess;
import com.qilu.core.net.callback.RequestCallbacks;
import com.qilu.core.ui.loader.LoaderStyle;
import com.qilu.core.ui.loader.QiluLoader;
import com.qilu.core.util.storage.QiluPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public final class RestClient {

    private final WeakHashMap<String, String> PARAMS;
    private final String URL;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    private final String FILE;
    private final WeakHashMap<String, String> FILES;

    private final Context CONTEXT;
    private final LoaderStyle LOADER_STYLE;

    private final String TOKEN;

    RestClient(String url,
               WeakHashMap<String, String> params,
               IRequest request,
               ISuccess success,
               IFailure failure,
               IError error,
               WeakHashMap<String, String> files,
               String file,
               Context context,
               LoaderStyle loaderStyle) {
        this.URL = url;
        this.PARAMS = params;

        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;

        // upload
        this.FILES = files;
        this.FILE = file;

        // loader
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;

        this.TOKEN = "Bearer "+QiluPreference.getCustomAppProfile("token");
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (LOADER_STYLE != null) {
            QiluLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;

            case GET_WITH_TOKEN:
                call = service.getWithToken(TOKEN, URL, PARAMS);
                break;

            case GET_NO_PARAMS:
                call = service.getNoParams(URL);
                break;

            case GET_NO_PARAMS_WITH_TOKEN:
                call = service.getNoParamsWithToken(TOKEN, URL);
                break;

            case POST_RAW:
                WeakHashMap<String, RequestBody> tempParams1 = new WeakHashMap<>();
                for (String key : PARAMS.keySet()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), PARAMS.get(key) == null ? "" : PARAMS.get(key));
                    tempParams1.put(key, requestBody);
                }
                call = service.postRaw(URL, tempParams1);
                break;

            case POST_RAW_WITH_TOKEN:
                WeakHashMap<String, RequestBody> tempParams2 = new WeakHashMap<>();
                for (String key : PARAMS.keySet()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), PARAMS.get(key) == null ? "" : PARAMS.get(key));
                    tempParams2.put(key, requestBody);
                }
                call = service.postRawWithToken(TOKEN, URL, tempParams2);
                break;

            case POST_WITH_FILES:
                WeakHashMap<String, RequestBody> requestBodyMap1 = new WeakHashMap<>();
                List<MultipartBody.Part> partList1 = new ArrayList<>();
                for (WeakHashMap.Entry<String, String> entry : PARAMS.entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), val);
                    requestBodyMap1.put(key, body);
                }
                for (WeakHashMap.Entry<String, String> entry : FILES.entrySet()) {
                    String key = entry.getKey();
                    File val = new File(entry.getValue());
                    RequestBody body = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), val);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(key, val.getName(), body);
                    partList1.add(part);
                }
                call = service.postWithFiles(URL, requestBodyMap1, partList1);
                break;

            case POST_WITH_FILES_WITH_TOKEN:
                WeakHashMap<String, RequestBody> requestBodyMap2 = new WeakHashMap<>();
                List<MultipartBody.Part> partList2 = new ArrayList<>();
                for (WeakHashMap.Entry<String, String> entry : PARAMS.entrySet()) {
                    String key = entry.getKey();
                    String val = entry.getValue();
                    RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), val);
                    requestBodyMap2.put(key, body);
                }
                for (WeakHashMap.Entry<String, String> entry : FILES.entrySet()) {
                    String key = entry.getKey();
                    File val = new File(entry.getValue());
                    RequestBody body = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), val);
                    MultipartBody.Part part = MultipartBody.Part.createFormData(key, val.getName(), body);
                    partList2.add(part);
                }
                call = service.postWithFilesWithToken(TOKEN, URL, requestBodyMap2, partList2);
                break;

            case UPLOAD:
                File file1 = new File(FILE);
                final RequestBody requestFile1 =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file1);
                final MultipartBody.Part body1 =
                        MultipartBody.Part.createFormData("file", file1.getName(), requestFile1);
                call = service.upload(URL,body1);
                break;

            case UPLOAD_WITH_TOKEN:
                File file2 = new File(FILE);
                final RequestBody requestFile2 =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file2);
                final MultipartBody.Part body2 =
                        MultipartBody.Part.createFormData("file", file2.getName(), requestFile2);
                call = service.uploadWithToken(TOKEN, URL,body2);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallback());
        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    public final void get() {
        request(HttpMethod.GET);
    }
    public final void getWithToken() {
        request(HttpMethod.GET_WITH_TOKEN);
    }

    public final void getNoParams() {
        request(HttpMethod.GET_NO_PARAMS);
    }
    public final void getNoParamsWithToken() {
        request(HttpMethod.GET_NO_PARAMS_WITH_TOKEN);
    }

    public final void postRaw() {
        request(HttpMethod.POST_RAW);
    }
    public final void postRawWithToken() {
        request(HttpMethod.POST_RAW_WITH_TOKEN);
    }

    public final void postWithFiles() {
        request(HttpMethod.POST_WITH_FILES);
    }
    public final void postWithFilesWithToken() {
        request(HttpMethod.POST_WITH_FILES_WITH_TOKEN);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }
    public final void uploadWithToken() {
        request(HttpMethod.UPLOAD_WITH_TOKEN);
    }
}
