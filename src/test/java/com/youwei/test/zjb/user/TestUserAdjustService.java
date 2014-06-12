package com.youwei.test.zjb.user;

import java.util.List;
import java.util.Map;

import org.bc.sdak.Page;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.user.UserAdjustService;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.util.DebugHelper;

public class TestUserAdjustService {

	UserAdjustService service = new UserAdjustService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testList(){
		ModelAndView mv = service.list("",new Page<Map>());
		DebugHelper.printResult((List<?>) mv.data.get("data"));
	}
	
	@Test
	public void testAdd(){
		UserAdjust ua = new UserAdjust();
		ua.oldDeptId=222;
		ua.newDeptId = 223;
		ua.oldRoleId = 1;
		ua.newRoleId =1;
		ua.jiaojie = "test";
		ua.reason = "test";
		ua.userId = 316;
		service.add(ua);
	}
}
