package com.youwei.zjb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.DateSeparator;

public class HqlHelper {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static String buildDateSegment(String fieldName,String dateStr,DateSeparator sep,List<Object> params){
		if(StringUtils.isEmpty(dateStr)){
			return "";
		}
		try {
			Date date = sdf.parse(dateStr);
			params.add(date);
			if(sep==DateSeparator.Before){
				return " and " + fieldName + " <= ? ";
			}else{
				return " and " + fieldName + " >= ? ";
			}
		} catch (ParseException e) {
			//TODO
			return "";
		}
	}
}
