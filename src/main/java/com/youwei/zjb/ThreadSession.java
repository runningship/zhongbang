package com.youwei.zjb;

import com.youwei.zjb.entity.User;

import javax.servlet.http.HttpServletRequest;
public class ThreadSession {

	private static final ThreadLocal<User> User= new ThreadLocal<User>();
	
	private static final ThreadLocal<HttpServletRequest> HttpServletRequest = new ThreadLocal<HttpServletRequest>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
    }  
  
    public static void setSuperAdminr(boolean sup) {  
    	superAdmin.set(sup);
    }
    
    public static User getUser() {  
        return User.get(); 
    }  
  
    public static void setUser(User user) {  
        User.set(user);
    }
    
    public static HttpServletRequest getHttpServletRequest(){
    	return HttpServletRequest.get();
    }
    
    public static void setHttpServletRequest(HttpServletRequest request){
    	HttpServletRequest.set(request);
    }
    
    public static String getIp(){
    	HttpServletRequest req = ThreadSession.getHttpServletRequest();
    	String ip = req.getHeader("x-forwarded-for");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}
		return ip;
    }
}
