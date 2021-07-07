package com.qw.list.repository.entities;

import java.io.Serializable;

public class Video implements Serializable {
    private long id;
    private String pic;
    private String src;
    private String fire_value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSrc() {

        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFire_value() {
        return fire_value;
    }

    public void setFire_value(String fire_value) {
        this.fire_value = fire_value;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", pic='" + pic + '\'' +
                ", src='" + src + '\'' +
                ", fire_value='" + fire_value + '\'' +
                '}';
    }
}
