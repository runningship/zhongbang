package com.youwei.zjb.util;

import java.util.List;

import net.sf.json.JSONObject;

import org.bc.sdak.utils.BeanUtil;

public class DebugHelper {

	public static void printResult(List<?> list){
		for(Object o : list){
			if(o instanceof JSONObject){
				System.out.println(o.toString());
			}else{
				System.out.println(BeanUtil.toString(o));
			}
		}
	}
}
