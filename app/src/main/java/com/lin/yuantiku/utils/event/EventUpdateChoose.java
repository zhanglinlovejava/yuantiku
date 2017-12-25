package com.lin.yuantiku.utils.event;

import com.lin.yuantiku.entity.ChooseItem;

import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class EventUpdateChoose {
    public List<ChooseItem> list;

    public EventUpdateChoose(List<ChooseItem> list) {
        this.list = list;
    }
}
