package com.youwei.zjb.user;

import java.util.List;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.User;

public class UserHelper {

	public static List<User> getUserWithAuthority(String authName){
		String hql = "select u from User u, RoleAuthority ra where u.roleId=ra.roleId and ra.name='"+authName+"'";
		String hql2 = "select u from User u, UserAuthority ra where u.id=ra.userId and ra.name='"+authName+"'";
		List<User> list1 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql);
		List<User> list2 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql2);
		for(User u : list2){
			if(hasUser(list1 ,u)){
				continue;
			}
			list1.add(u);
		}
		return list1;
	}

	private static boolean hasUser(List<User> list, User u) {
		for(User u1 : list){
			if(u.id.equals(u1.id)){
				return true;
			}
		}
		return false;
	}
	
}
