package com.youwei.zjb.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.bc.sdak.utils.LogUtil;

import com.youwei.zjb.cache.UserSessionCache;

public class SessionHelper {
	public static void updateSession(HttpServletRequest req){
		if(req.getSession().isNew()==false){
			return;
		}
		String oldSessionId = "";
		for(Cookie coo : req.getCookies()){
			if("JSESSIONID".equals(coo.getName())){
				oldSessionId = coo.getValue();
				break;
			}
		}
		UserSessionCache.updateSession(oldSessionId, req.getSession().getId());
		LogUtil.info("old session ["+oldSessionId+"] to new session ["+req.getSession().getId()+"]");
	}
}
