package com.youwei.zjb.user;

import java.util.List;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.entity.UserAuthority;

public class UserHelper {

	public static List<User> getUserWithAuthority(String authName , String dataScope , User target){
		String data_qy = dataScope+"_quyu";
		String quyu = target.Department().Parent().path;
		String hql = "select u from User u, RoleAuthority ra,RoleAuthority ra2 where u.roleId=ra.roleId and u.roleId=ra2.roleId and u.flag=0 and u.sh=1 and u.orgpath like '"+quyu+"%' and ra.name='"+data_qy+"' and ra2.name='"+authName+"'";
		String hql2 = "select u from User u, UserAuthority ra , UserAuthority ra2 where u.id=ra.userId and u.id=ra2.userId and u.flag=0 and u.sh=1 and u.orgpath like '"+quyu+"%' and ra.name='"+data_qy+"' and ra2.name='"+authName+"'";
		List<User> list1 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql);
		List<User> list2 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql2);
		for(User u : list2){
			if(hasUser(list1 ,u)){
				continue;
			}
			list1.add(u);
		}
		List<User> list3 = getUserWithAuthority2(authName , dataScope+"_all");
		for(User u : list3){
			if(hasUser(list1 ,u)){
				continue;
			}
			list1.add(u);
		}
		return list1;
	}
	
	private static List<User> getUserWithAuthority2(String authName , String dataScope){
		String hql = "select u from User u, RoleAuthority ra , RoleAuthority ra2 where u.roleId=ra.roleId and u.roleId=ra2.roleId and u.flag=0 and u.sh=1 and ra.name='"+authName+"' and ra2.name='"+dataScope+"'";
		String hql2 = "select u from User u, UserAuthority ra , UserAuthority ra2 where u.id=ra.userId and u.id=ra2.userId and u.flag=0 and u.sh=1 and ra.name='"+authName+"' and ra2.name='"+dataScope+"'";
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

	public static String getDataScope(User user , String dataPrefix){
		String code=user.orgpath;
		for(UserAuthority ra :  user.Authorities()){
			System.out.println(ra.name);
			if((dataPrefix+"_data_dept").equals(ra.name)){
				code = user.Department().path;
			}
			if((dataPrefix+"_data_quyu").equals(ra.name)){
				code = String.valueOf(user.Department().Parent().path);
			}
			if((dataPrefix+"_data_all").equals(ra.name)){
				code = "";
			}
		}
		return code;
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
