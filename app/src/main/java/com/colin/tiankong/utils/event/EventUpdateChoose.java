package com.colin.tiankong.utils.event;

import com.colin.tiankong.entity.ChooseItem;

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
