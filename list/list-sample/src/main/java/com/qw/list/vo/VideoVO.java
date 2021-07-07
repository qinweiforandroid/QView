package com.qw.list.vo;

import java.io.Serializable;

/**
 * Created by qinwei on 2017/7/13.
 */

public class VideoVO implements Serializable {
    public String name;
    public String icon;
    public String url;

    public VideoVO(String name) {
        this.name = name;
    }
}
