/*
 * Copyright (c) 2015-2016 by colley
 * All rights reserved.
 */
package com.ailing.hscache.xml;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;


/**
 *@FileName  XMLHelper.java
 *@Date  16-4-13 下午5:58
 *@author Ma Yuanchao
 *@version 1.0
 */
public class XMLHelper {
    private static transient Log log = LogFactory.getLog(XMLHelper.class);
    private static XMLHelper instance = null;
    private static HsCaches caches = null;
    private static Boolean isInit = Boolean.FALSE;
    private static Boolean isCacheInit = Boolean.FALSE;

    public static XMLHelper getInstance() throws Exception {
        if (isInit.equals(Boolean.FALSE)) {
            synchronized (isInit) {
                if (isInit.equals(Boolean.FALSE)) {
                    isInit = Boolean.TRUE;
                }
            }

            instance = new XMLHelper();
        }

        return instance;
    }

    public HsCaches getCaches() {
        if (caches == null) {
            synchronized (isCacheInit) {
                if (isCacheInit.equals(Boolean.FALSE)) {
                    try {
                        caches = createCaches();
                        isCacheInit = Boolean.TRUE;
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        return caches;
    }

    private static HsCaches createCaches() throws Exception {
        HsCaches objDaos = null;
        String cacheFileName = "system/cache/cache.xml";
        if (!StringUtils.isBlank(System.getProperty("JC.cache.filename"))) {
            cacheFileName = System.getProperty("JC.cache.filename").trim();
            log.warn("Use the Specified cache.xml file:" + cacheFileName);
        }

        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(cacheFileName);
        if (input == null) {
            throw new Exception(cacheFileName + " not exits!");
        }

        Digester digester = new Digester();

        digester.setValidating(false);
        digester.addObjectCreate("caches", HsCaches.class.getName());
        digester.addSetProperties("caches");

        digester.addObjectCreate("caches/quartz", HsQuartz.class.getName());
        digester.addSetProperties("caches/quartz");

        digester.addObjectCreate("caches/quartz/property", HsProperty.class.getName());
        digester.addSetProperties("caches/quartz/property");

        digester.addObjectCreate("caches/cache", HsCache.class.getName());
        digester.addSetProperties("caches/cache");

        digester.addObjectCreate("caches/cache/property", HsProperty.class.getName());
        digester.addSetProperties("caches/cache/property");

        digester.addSetNext("caches/quartz", "setQuartz", HsQuartz.class.getName());
        digester.addSetNext("caches/quartz/property", "addProperty", HsProperty.class.getName());

        digester.addSetNext("caches/cache", "addCache", HsCache.class.getName());
        digester.addSetNext("caches/cache/property", "addProperty", HsProperty.class.getName());

        objDaos = (HsCaches) digester.parse(input);

        return objDaos;
    }

    public static void main(String[] args) throws Exception {
    }
}
