package com.youwei.zjb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.persistence.Column;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.WebParam;

public class ServletHelper {

	private static Map<String,Object> getData(HttpServletRequest req) {
		Map<String,Object> map = new HashMap<String,Object>();
		Enumeration<String> names = req.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String val = req.getParameter(key);
			map.put(key, val);
		}
		return map;
	}
	
	public static Object[] buildParamters(CtMethod cm,HttpServletRequest req) {
		Map<String, Object> data = ServletHelper.getData(req);
		MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        CtClass[] pTypes = null;
		try {
			pTypes = cm.getParameterTypes();
		} catch (NotFoundException e1) {
			//this should neven happen.
			LogUtil.log(Level.SEVERE, "This should not happen at runtime,probably code issue.", e1);
			return new Object[]{};
		}
        Object[] values = new Object[pTypes.length];
        String paramName = "";
        for(int i=0;i<pTypes.length;i++){
        	try {
        		paramName = attr.variableName(i + pos);
        		Object obj = data.get(paramName);
        		String typeName = pTypes[i].getName();
//				Class<?> pt = Class.forName(pTypes[i].getName());
        		if("int".equals(typeName) || "java.lang.Integer".equals(typeName)){
        			if(obj==null){
        				throw new GException(BusinessExceptionType.ParameterMissingError,"parameter "+paramName+" is missing");
        			}
        			obj = Integer.valueOf(String.valueOf(obj));
        		}else if("long".equals(typeName) || "java.lang.Long".equals(typeName)){
        			if(obj==null){
        				throw new GException(BusinessExceptionType.ParameterMissingError,"parameter "+paramName+" is missing");
        			}
        			obj = Long.valueOf(String.valueOf(obj));
        		}else if("float".equals(typeName) || "java.lang.Float".equals(typeName)){
        			if(obj==null){
        				throw new GException(BusinessExceptionType.ParameterMissingError,"parameter "+paramName+" is missing");
        			}
        			obj = Float.valueOf(String.valueOf(obj));
        		}else if("double".equals(typeName) || "java.lang.Double".equals(typeName)){
        			if(obj==null){
        				throw new GException(BusinessExceptionType.ParameterMissingError,"parameter "+paramName+" is missing");
        			}
        			obj = Double.valueOf(String.valueOf(obj));
        		}else if("char".equals(typeName) || "java.lang.Character".equals(typeName)){
        			if(obj==null){
        				throw new GException(BusinessExceptionType.ParameterMissingError,"parameter "+paramName+" is missing");
        			}
        			obj = String.valueOf(obj).charAt(0);
        		}else if("java.lang.String".equals(typeName)){
        			if(obj==null){
        				obj="";
        			}else{
        				obj = String.valueOf(obj);
        			}
        		}else{
					obj = Class.forName(pTypes[i].getName()).newInstance();
					setValue(obj,data);
				}
        		values[i] = obj;
			}catch(ClassNotFoundException ex){
				throw new GException(BusinessExceptionType.MethodParameterError,"parameter ("+pTypes[i].getName()
						+ " " + paramName+") of method ("+cm.getDeclaringClass().getName()+"."+cm.getName()
						+") is not support,method type should be primary type or vo",ex);
			}catch (InstantiationException | IllegalAccessException ex) {
				LogUtil.log(Level.SEVERE, "please check code or deployment.", ex);
				return new Object[]{};
			}
        }
        return values;
	}
	
	private static void setValue(Object obj, Map<String, Object> data) {
		
		for(Field f : obj.getClass().getDeclaredFields()){
			String pname=f.getName();
			WebParam wparam = f.getAnnotation(WebParam.class);
			if(wparam!=null && StringUtils.isNotEmpty(wparam.name())){
				pname = wparam.name();
			}
			if(!data.containsKey(pname)){
				Column column = f.getAnnotation(Column.class);
				if(column!=null && column.nullable()==false){
					throw new GException(BusinessExceptionType.ParameterMissingError,pname + " is required");
				}
			}
			f.setAccessible(true);
			try {
				if("float".equals(f.getType().getName()) || "java.lang.Float".equals(f.getType().getName())){
					Object tmp = data.get(pname);
					try{
						if(tmp!=null){
							f.set(obj, Float.valueOf(String.valueOf(tmp)));
						}
					}catch(Exception ex){
						throw new GException(BusinessExceptionType.ParameterMissingError,"无效的数据["+pname+"="+tmp+"],必须是数字类型");
					}
				}else if("int".equals(f.getType().getName()) || "java.lang.Integer".equals(f.getType().getName())){
					Object tmp = data.get(pname);
					try{
						if(tmp!=null){
							f.set(obj, Integer.valueOf(String.valueOf(tmp)));
						}
					}catch(Exception ex){
						throw new GException(BusinessExceptionType.ParameterMissingError,"无效的数据["+pname+"="+tmp+"],必须是数字类型");
					}
				}else{
					f.set(obj, data.get(pname));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LogUtil.warning("set value for "+obj.getClass().getName()+"."+pname+" failed.("+e.getMessage()+")");
			}
		}
	}
	
	public static ModelAndView call(Object manager, String method , Object[] data) throws InvocationTargetException {
		try {
			for(Method m : manager.getClass().getDeclaredMethods()){
				if(!m.getName().equals(method)){
					continue;
				}
				Object result = m.invoke(manager,data);
				if(result instanceof ModelAndView){
					return (ModelAndView) result;
				}
			}
			throw new GException(BusinessExceptionType.MethodReturnTypeError,manager.getClass().getName()+"."+method+" not found");
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException ex) {
			throw new GException(BusinessExceptionType.ModuleInvokeError, "",ex);
		}
	}

	public static void fillMV(ServletRequest req, ModelAndView mv) {
		for(Object key : mv.data.keySet()){
			req.setAttribute(String.valueOf(key), mv.data.get(key));
		}
	}
	
	public static String getModule(String path){
		if(StringUtils.isEmpty(path)){
			return "";
		}
		path = StringUtils.removeEnd(path, "/");
		return path;
//		return path.substring(path.lastIndexOf("/"));
	}
	
//	public static String getMethod(String path){
//		if(StringUtils.isEmpty(path)){
//			return "";
//		}
//		path = StringUtils.substringBefore(path, "?");
//		//path = StringUtils.removeEnd(path, "/");
//		return path.substring(path.lastIndexOf("/")+1);
//	}
}
