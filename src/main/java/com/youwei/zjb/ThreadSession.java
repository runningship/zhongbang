package com.youwei.zjb;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.youwei.zjb.entity.User;
public class ThreadSession {


	private static final ThreadLocal<HttpSession> HttpSession = new ThreadLocal<HttpSession>();
	
	private static final ThreadLocal<HttpServletResponse> Resp = new ThreadLocal<HttpServletResponse>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
    }  
  
    public static void setSuperAdminr(boolean sup) {  
    	superAdmin.set(sup);
    }
    
    public static HttpSession getHttpSession(){
    	return HttpSession.get();
    }
    
    public static void setHttpSession(HttpSession session){
    	HttpSession.set(session);
    }
    
    public static HttpServletResponse getHttpServletResponse(){
    	return Resp.get();
    }
    
    public static void setHttpSession(HttpServletResponse resp){
    	Resp.set(resp);
    }
    
    public static User getUser(){
    	HttpSession session = HttpSession.get();
    	if(session==null){
    		return null;
    	}
    	if(session.getAttribute("user")!=null){
    		return (User)session.getAttribute("user");
    	}
    	return null;
    }
    public static String getIp(){
    	HttpSession session = HttpSession.get();
    	return (String)session.getAttribute("ip");
    }

}
