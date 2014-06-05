package com.youwei.zjb.util;

import java.util.List;

import org.bc.sdak.utils.BeanUtil;

public class DebugHelper {

	public static void printResult(List<?> list){
		for(Object o : list){
			System.out.println(BeanUtil.toString(o));
		}
	}
}
