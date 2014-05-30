package com.youwei.zjb;

import com.youwei.zjb.entity.User;

public class ThreadSession {

	private static final ThreadLocal<User> SESSION_MAP = new ThreadLocal<User>();
    
    private ThreadSession() {  
    }  
  
      
    public static User get() {  
        return SESSION_MAP.get(); 
    }  
  
    public static void set(User user) {  
        SESSION_MAP.set(user);
    }  
}
