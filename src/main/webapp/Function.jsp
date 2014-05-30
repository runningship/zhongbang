<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%!

public Object getRequestValue(HttpServletRequest req,String key){
	Object value = null;
	String[] nameArr = key.split("\\.");
	for(int i=0;i<nameArr.length;i++){
		if(i==0){
			value = req.getAttribute(nameArr[i]);
		}else{
			value = getValue(value,nameArr[i]);	
		}
	}
	if(value==null){
		return "";
	}
	return value;
}

public Object getValue(Object obj, String field){
	try{
		Field f = obj.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.get(obj);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return null;
}
%>
