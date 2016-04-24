package com.aling.hscache.test;

import org.junit.Test;

import com.ailing.hscache.factory.HsCacheFactory;
import com.aling.hscache.sample.HscahceSample;

import junit.framework.Assert;

public class HscacheTest {

	@Test
	public void testHascache(){
		String val = HsCacheFactory.get(HscahceSample.class, "123");
		Assert.assertEquals("123", val);
		val = HsCacheFactory.get(HscahceSample.class, "1234");
		Assert.assertEquals("1234", val);
	}
}
