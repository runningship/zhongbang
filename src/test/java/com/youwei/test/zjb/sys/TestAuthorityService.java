package com.youwei.test.zjb.sys;

import java.util.List;

import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.sys.Authority;
import com.youwei.zjb.sys.AuthorityService;
import com.youwei.zjb.util.DebugHelper;

public class TestAuthorityService {
	
	AuthorityService service = TransactionalServiceHelper.getTransactionalService(AuthorityService.class);
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testUpdate(){
		service.update(1, "error string");
	}
	
	@Test
	public void testGetSubMenus(){
		ModelAndView mv = service.getSubMenus(Authority.xt, 316);
		DebugHelper.printResult((List<?>) mv.data.get("menus"));
	}
	
}
