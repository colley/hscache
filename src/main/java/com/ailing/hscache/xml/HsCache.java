/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.xml;

import java.util.ArrayList;
import java.util.List;


/**
 *@FileName  Cache.java
 *@Date  16-4-13 下午5:33
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsCache {
    public static final String SCHEDULER_VAR = "SCHEDULER_VAR";
    private List<HsProperty> list = new ArrayList<HsProperty>();
    private String id;
    private String init = "true";
    private boolean remote = false;
    private int expired = -1;
    private boolean springFactory;

    public boolean isSpringFactory() {
        return springFactory;
    }

    public void setSpringFactory(boolean springFactory) {
        this.springFactory = springFactory;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInit() {
        return this.init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public void addProperty(HsProperty property) {
        this.list.add(property);
    }

    public HsProperty[] getPropertys() {
        return (HsProperty[]) this.list.toArray(new HsProperty[0]);
    }
}
