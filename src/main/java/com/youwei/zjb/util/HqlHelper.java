package com.youwei.zjb.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.FetchType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.utils.LogUtil;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.house.DateType;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.JiaoYi;

public class HqlHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static String buildDateSegment(String fieldName,String dateStr,DateSeparator sep,List<Object> params){
		if(StringUtils.isEmpty(dateStr)){
			return "";
		}
		try {
//			Date date = sdf.parse(dateStr);
//			params.add(date);
//			if(sep==DateSeparator.Before){
//				return " and " + fieldName + " <= ? ";
//			}else{
//				return " and " + fieldName + " >= ? ";
//			}
			Date date;
			try{
				date = sdf.parse(dateStr);
			}catch(ParseException ex){
				date = DataHelper.monthSdf.parse(dateStr);
			}
			if(sep==DateSeparator.Before){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
				params.add(cal.getTime());
				return " and " + fieldName + " < ? ";
			}else{
				params.add(date);
				return " and " + fieldName + " >= ? ";
			}
		} catch (ParseException e) {
			LogUtil.log(Level.WARN, "时间查询错误，dateStr="+dateStr, e);
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
	
	public static void buildQuery(StringBuilder hql, HouseQuery query,
			List<Object> params) {

		if (StringUtils.isNotEmpty(query.xpath)) {
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath + "%");
		}
		// else{
		// hql.append(" select h  from House  h where 1=1 ");
		// }
		if (query.xingzhi != null) {
			hql.append(" and h.xingzhi = ? ");
			params.add(String.valueOf(query.xingzhi.getCode()));
		}
		if (StringUtils.isNotEmpty(query.area)) {
			hql.append(" and h.area like ?");
			params.add("%" + query.area + "%");
		}
		if (StringUtils.isNotEmpty(query.tel)) {
			hql.append(" and (h.tel like ? or h.fortel like ? or h.fordlrtel like ?)");
			params.add("%" + query.tel + "%");
			params.add("%" + query.tel + "%");
			params.add("%" + query.tel + "%");
		}
		if (StringUtils.isNotEmpty(query.houseNumber)) {
			hql.append(" and h.houseNumber like ?");
			params.add("%" + query.houseNumber + "%");
		}
		if (query.id != null) {
			hql.append(" and h.id = ?");
			params.add(query.id);
		}

		if (query.quyus != null) {
			hql.append(" and ( ");
			for (int i = 0; i < query.quyus.size(); i++) {
				hql.append(" h.quyu = ? ");
				if (i < query.quyus.size() - 1) {
					hql.append(" or ");
				}
				params.add(query.quyus.get(i));
			}
			hql.append(" )");
		}

		if (query.fangxing != null) {
			hql.append(" and ").append(query.fangxing.getQueryStr());
		}

		if (query.jiaoyis != null) {
			hql.append(" and ( ");
			for (int i = 0; i < query.jiaoyis.size(); i++) {
				hql.append(" h. jiaoyi = ? ");
				if (i < query.jiaoyis.size() - 1) {
					hql.append(" or ");
				}
				params.add(JiaoYi.valueOf(query.jiaoyis.get(i)).getCode());
			}
			hql.append(" )");
		}
		if (query.ztai != null) {
			hql.append(" and h.ztai = ?");
			params.add(String.valueOf(query.ztai.getCode()));
		}
		if (StringUtils.isNotEmpty(query.leibie)) {
			hql.append(" and h.leibie = ? ");
			params.add(query.leibie);
		}
		if (query.sjiaStart != null) {
			hql.append(" and h.sjia>= ? ");
			params.add(query.sjiaStart);
		}
		if (query.sjiaEnd != null) {
			hql.append(" and h.sjia<= ? ");
			params.add(query.sjiaEnd);
		}
		if (query.zjiaStart != null) {
			hql.append(" and h.zjia>= ? ");
			params.add(query.zjiaStart);
		}
		if (query.zjiaEnd != null) {
			hql.append(" and h.zjia<= ? ");
			params.add(query.zjiaEnd);
		}
		if (query.dateType == null) {
			query.dateType = DateType.首次录入日;
		}
		hql.append(buildDateHql(query.dateType, query.dateStart,
				DateSeparator.After, params));
		hql.append(buildDateHql(query.dateType, query.dateEnd,
				DateSeparator.Before, params));

		if (StringUtils.isNotEmpty(query.louxing)) {
			hql.append(" and h.lxing= ? ");
			params.add(query.louxing);
		}
		if (query.mianjiStart != null) {
			hql.append(" and h.mianji>= ? ");
			params.add(query.mianjiStart);
		}
		if (query.mianjiEnd != null) {
			hql.append(" and h.mianji<= ? ");
			params.add(query.mianjiEnd);
		}
		if (query.lcengStart != null) {
			hql.append(" and h.lceng>= ? ");
			params.add(query.lcengStart);
		}
		if (query.lcengEnd != null) {
			hql.append(" and h.lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if (StringUtils.isNotEmpty(query.chaoxiang)) {
			hql.append(" and h.chaoxiang= ? ");
			params.add(query.chaoxiang);
		}
		if (StringUtils.isNotEmpty(query.chanquan)) {
			hql.append(" and h.chanquan like ?");
			params.add("%" + query.chanquan + "%");
		}
		if (StringUtils.isNotEmpty(query.dhao)) {
			hql.append(" and h.dhao like ?");
			params.add("%" + query.dhao + "%");
		}

		if (StringUtils.isNotEmpty(query.fhao)) {
			hql.append(" and h.fhao like ?");
			params.add("%" + query.fhao + "%");
		}

		if (query.userId != null) {
			hql.append(" and h.userId= ? ");
			params.add(query.userId);
		}
		if (query.isdel == null) {
			hql.append(" and ( isdel= 0 or isdel is null) ");
		}
		if (query.isdel != null) {
			hql.append(" and h.isdel=?");
			params.add(query.isdel);
		}
	}

	private static String buildDateHql(DateType dateType,String dateStr,DateSeparator sep,List<Object> params){
		if(StringUtils.isNotEmpty(dateStr)){
			if(DateType.建房年代==dateType){
				params.add(Integer.valueOf(dateStr));
				if(sep==DateSeparator.Before){
					return " and " + dateType.getField()+"<=?";
				}else{
					return " and " + dateType.getField()+">=?";
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdf.parse(dateStr);
				params.add(date);
				if(sep==DateSeparator.Before){
					return " and "+dateType.getField()+" <=?";
				}else{
					return " and "+dateType.getField()+" >=?";
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
}
