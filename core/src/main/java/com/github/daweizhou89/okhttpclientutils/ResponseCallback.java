package com.github.daweizhou89.okhttpclientutils;

/**
 * Created by daweizhou89 on 16/8/4.
 */
public interface ResponseCallback {

    /***
     * 请求成功
     * @param url
     * @param response
     */
    void onSuccess(String url, String response);

    /***
     * 请求失败
     * @param url
     * @param throwable
     */
    void onFailure(String url, Throwable throwable);
}
