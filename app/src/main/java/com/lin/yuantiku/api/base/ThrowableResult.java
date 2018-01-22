package com.lin.yuantiku.api.base;

/**
 * Created by zhanglin on 2016/5/17.
 */
public class ThrowableResult extends Throwable {

    private int errorCode;
    private String errorMsg;

    public ThrowableResult(String detailMessage) {
        super(detailMessage);
        this.errorMsg = detailMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ThrowableResult setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ThrowableResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
