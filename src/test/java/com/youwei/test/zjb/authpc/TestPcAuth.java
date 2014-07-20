package com.youwei.test.zjb.authpc;

import java.util.Date;
import java.util.Map;

import junit.framework.Assert;

import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.sys.PCQuery;
import com.youwei.zjb.sys.PcService;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.util.DebugHelper;
import com.youwei.zjb.util.SecurityHelper;

public class TestPcAuth {
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testAdd(){
		PcService pas = new PcService();
		PC pc = new PC();
		pc.addtime = new Date();
		pc.authCode = "123445";
		//0551zbfc8880
		try{
			pas.add(pc);
		}catch(GException ex){
			Assert.assertEquals(PlatformExceptionType.MachineCodeEmpty, ex.getType());
		}
		
		pc.baseboard="1111111";
		
		try{
			pas.add(pc);
		}catch(GException ex){
			Assert.assertEquals(PlatformExceptionType.AuthCodeError, ex.getType());
		}
		
		pc.authCode = "0551zbfc8880";
		pas.add(pc);
		PC obj = SimpDaoTool.getGlobalCommonDaoService().get(PC.class,pc.id);
		Assert.assertNotNull(obj);
		SimpDaoTool.getGlobalCommonDaoService().delete(obj);
	}
	
	@Test
	public void testValidatePC(){
		PC pc = new PC();
		pc.baseboard="";
		Assert.assertFalse(SecurityHelper.validate(pc));
		pc.deptId=86;
		Assert.assertFalse(SecurityHelper.validate(pc));
		
		pc.baseboard="e0cb4e9f9aad";
		Assert.assertTrue(SecurityHelper.validate(pc));
	}
	
	@Test
	public void testAuthorizedList(){
		PcService pas = new PcService();
		Page<Map> page = new Page<Map>();
		page.setCurrentPageNo(1);
		ModelAndView mv = pas.list(page, new PCQuery());
	}
}
