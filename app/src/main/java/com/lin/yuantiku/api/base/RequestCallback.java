package com.lin.yuantiku.api.base;

/**
 * Created by zhanglin on 2016/5/17.
 */
public interface RequestCallback<T> {

    void onSuccess(T entity);

    void onFail(String errorMessage);

    void onCookieExpired();
}
