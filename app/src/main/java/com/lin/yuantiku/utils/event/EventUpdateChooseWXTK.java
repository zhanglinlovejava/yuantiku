package com.lin.yuantiku.utils.event;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class EventUpdateChooseWXTK {
    public int index;
    public boolean isChoosed;
    public String body;


    public EventUpdateChooseWXTK(int index, boolean isChoosed, String body) {
        this.index = index;
        this.isChoosed = isChoosed;
        this.body = body;
    }
}
