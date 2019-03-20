package com.gst.myviewstudy.ui.bean;

/**
 * author: GuoSongtao on 2018/2/8 15:49
 * email: 157010607@qq.com
 */

public class ViewInfo {
    private String name;
    private int nameTag;//

    public ViewInfo() {
    }

    public ViewInfo(String name) {
        this.name = name;
    }

    public ViewInfo(String name, int nameTag) {
        this.name = name;
        this.nameTag = nameTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameTag() {
        return nameTag;
    }

    public void setNameTag(int nameTag) {
        this.nameTag = nameTag;
    }
}
