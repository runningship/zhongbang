package com.youwei.zjb.util;

import java.util.List;
import java.util.Map;

public class DataHelper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void fillDefaultValue(List<Map> list , String key,Object defValue){
		for(Map map : list){
			if(map.get(key)==null){
				map.put(key, defValue);
			}
		}
	}
}
