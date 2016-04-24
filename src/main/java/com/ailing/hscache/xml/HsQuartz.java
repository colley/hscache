/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.xml;

import java.util.ArrayList;
import java.util.List;


/**
 *@FileName  Quartz.java
 *@Date  16-4-13 下午5:34
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsQuartz {
    private List<HsProperty> list = new ArrayList<HsProperty>();
    private boolean readonly;

    public void addProperty(HsProperty property) {
        this.list.add(property);
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public HsProperty[] getPropertys() {
        return (HsProperty[]) this.list.toArray(new HsProperty[0]);
    }
}
