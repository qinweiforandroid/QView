package com.qw.widget.list.section;

import java.io.Serializable;

/**
 * Created by qinwei on 2017/7/13.
 */

public class SectionData<G, T> implements Serializable {
    public T t;
    public G header;
    public boolean isHeader;
    public int index;

    public SectionData(G header, int index) {
        this.header = header;
        this.index = index;
        this.isHeader = true;
    }

    public SectionData(T t) {
        this.isHeader = false;
        this.t = t;
        this.header = null;
    }
}
