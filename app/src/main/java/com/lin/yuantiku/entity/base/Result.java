package com.lin.yuantiku.entity.base;

/**
 * Created by zhanglin on 2017/7/25.
 */
public class Result<T> {

    /**
     * 0 表示请求成功
     **/
    public int error_code;
    public String error_msg;
    public long server_time;
    public T data;

    @Override
    public String toString() {
        return "Result{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", server_time=" + server_time +
                ", data=" + data +
                '}';
    }
}
