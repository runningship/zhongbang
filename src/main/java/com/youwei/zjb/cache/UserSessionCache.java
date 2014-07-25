package com.youwei.zjb.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.utils.LogUtil;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.entity.UserSession;

public class UserSessionCache {

	public static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static Map<String,User> map = new HashMap<String , User>();
	public static User getUser(String sessionId){
		if(!map.containsKey(sessionId)){
			UserSession us = dao.getUniqueByKeyValue(UserSession.class, "sessionId", sessionId);
			if(us==null){
				return null;
			}
			User user = dao.get(User.class, us.userId);
			if(user!=null){
				map.put(sessionId, user);
			}else{
				LogUtil.warning("sessionId["+sessionId+"] 没有找到对应的用户信息");
			}
		}
		return map.get(sessionId);
	}
	
	public static void updateSession(String oldSessionId, String newSessionId){
		User user = map.remove(oldSessionId);
		if(user!=null){
			map.put(newSessionId, user);
		}
		dao.execute("update from UserSession set sessionId=? where sessionId=?", newSessionId,oldSessionId);
	}
	
	public static void putSession(String sessionId , Integer userId , String remoteIP){
		User user = dao.get(User.class, userId);
		if(user==null){
			return;
		}
		UserSession po = dao.getUniqueByParams(UserSession.class, new String[]{"sessionId" ,"userId"},  new Object[]{sessionId , userId});
		if(po==null){
			po = new UserSession();
			po.sessionId = sessionId;
			po.userId = userId;
			po.ip = remoteIP;
			po.addtime = new Date();
			dao.saveOrUpdate(po);
			map.put(sessionId, user);
		}
		//允许多处登录
		//dao.execute("delete from UserSession where userId=? and sessionId<>? ", userId, sessionId);
	}
	
	public static void removeUserSession(int userId){
		UserSession us = dao.getUniqueByKeyValue(UserSession.class, "userId", userId);
		if(us!=null){
			map.remove(us.sessionId);
			dao.delete(us);
			LogUtil.info("session[id="+us.sessionId+"] was removed for user[id="+us.userId+"]");
		}
	}
}
