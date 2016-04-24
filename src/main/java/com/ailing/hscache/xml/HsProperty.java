/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.xml;


/**
 *@FileName  Property.java
 *@Date  16-4-13 下午5:30
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsProperty {
    private String name;
    private String value;
    private HsCacheRef ref;
    private String type;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setRef(HsCacheRef ref) {
        this.ref = ref;
    }

    public HsCacheRef getRef() {
        return ref;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
