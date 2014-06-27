package com.youwei.zjb.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.FetchType;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.DateSeparator;

public class HqlHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
	
	public static String getLazyHql(Class<?> entity){
		StringBuilder hql = new StringBuilder("select ");
		for(Field f: entity.getDeclaredFields()){
			Basic anno = f.getAnnotation(Basic.class);
			if(anno!=null && anno.fetch()==FetchType.LAZY){
				continue;
			}
			hql.append(f.getName()+" as "+ f.getName()).append(",");
		}
		String result = StringUtils.removeEnd(hql.toString(), ",");
		result = result+" from " +entity.getName();
		return result;
	}
	
	public static String getCommonAlis(Class<?> clazz , String alias){
		StringBuilder hql = new StringBuilder();
		for(Field f: clazz.getDeclaredFields()){
			Basic anno = f.getAnnotation(Basic.class);
			if(anno!=null && anno.fetch()==FetchType.LAZY){
				continue;
			}
			hql.append(alias).append(".").append(f.getName()+" as "+ f.getName()).append(",");
		}
		String result = StringUtils.removeEnd(hql.toString(), ",");
		return result;
	}
}
