package com.qilu.core.net;

import com.qilu.core.util.storage.QiluPreference;

import java.util.List;
import java.util.WeakHashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RestService {
    @GET
    Call<String> get(@Url String url, @QueryMap WeakHashMap<String, String> params);

    @GET
    Call<String> getWithToken(@Header("Authorization") String token, @Url String url,
                              @QueryMap WeakHashMap<String, String> params);

    @GET
    Call<String> getNoParams(@Url String url);

    @GET
    Call<String> getNoParamsWithToken(@Header("Authorization") String token, @Url String url);

    @Multipart
    @POST
    Call<String> postRaw(@Url String url, @PartMap WeakHashMap<String, RequestBody> params);

    @Multipart
    @POST
    Call<String> postRawWithToken(@Header("Authorization") String token, @Url String url,
                                  @PartMap WeakHashMap<String, RequestBody> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> uploadWithToken(@Header("Authorization") String token, @Url String url,
                                 @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> postWithFiles(@Url String url, @PartMap WeakHashMap<String, RequestBody> params,
                               @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST
    Call<String> postWithFilesWithToken(@Header("Authorization") String token,
                                        @Url String url,
                                        @PartMap WeakHashMap<String, RequestBody> params,
                                        @Part List<MultipartBody.Part> parts);
}
