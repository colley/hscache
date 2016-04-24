/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache;


/**
 *@FileName  HsCacheException.java
 *@Date  16-4-17 下午12:40
 *@author Ma Yuanchao
 *@version 1.0
 */
public class HsCacheException extends Exception {
    /*** serial id */
    private static final long serialVersionUID = 1L;

    public HsCacheException() {
        super("HsCacheException 异常");
    }

    /**
     *
     * @param e
     */
    public HsCacheException(String e) {
        super(e);
    }

    /**
     *
     * @param er
     * @param e
     */
    public HsCacheException(String er, Throwable e) {
        super(er, e);
    }

    public HsCacheException(Throwable e) {
        super(e);
    }
}
