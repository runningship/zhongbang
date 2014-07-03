package com.youwei.zjb.user;

import java.util.List;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.User;

public class UserHelper {

	public static List<User> getUserWithAuthority(String authName){
		String hql = "select u from User u, RoleAuthority ra where u.roleId=ra.roleId and ra.name='"+authName+"'";
		return SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql);
	}
	
}
