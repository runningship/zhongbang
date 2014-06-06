package org.bc.dietary.test.web;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.User;

public class TestInitXPath {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testInitDeptPath(){
		Department root = SimpDaoTool.getGlobalCommonDaoService().get(Department.class, 1);
		initDeptPath(root);
	}
	
	@Test
	public void testInitUserPath(){
		List<User> users = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, new String[]{}, null);
		for(User user: users){
			Department dept = SimpDaoTool.getGlobalCommonDaoService().get(Department.class, user.deptId);
			if(dept!=null){
				user.orgpath = dept.path+user.id;
				SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(user);
			}
		}
	}
	
	private void initDeptPath(Department dept){
		if(dept==null){
			return;
		}
		Department parent = dept.getParent();
		if(parent==null){
			dept.path = dept.id.toString();
		}else{
			dept.path = parent.path+dept.id.toString();
		}
		SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(dept);
		for(Department child : dept.getDirectChildren()){
			initDeptPath(child);
		}
	}
}
