package com.youwei.test.zjb.admin;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.Page;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.admin.AdminQuery;
import com.youwei.zjb.admin.AdminService;
import com.youwei.zjb.admin.entity.AdminClass;
import com.youwei.zjb.admin.entity.ProcessClass;
import com.youwei.zjb.util.DebugHelper;

public class TestAdminService {

	AdminService service = new AdminService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testAdminClassList(){
		ModelAndView mv = service.listAdminClass(new Page<Map>());
		List<AdminClass> list = (List<AdminClass>) mv.data.get("data");
		DebugHelper.printResult(list);
	}
	
	@Test
	public void testListTable(){
		ModelAndView mv = service.listTable(new AdminQuery() , new Page<Map>());
		List<?> list = (List<?>) mv.data.get("data");
		DebugHelper.printResult(list);
	}
	
	@Test
	public void testListProcessClass(){
		ModelAndView mv = service.listProcessClass(2);
		List<?> list = (List<?>) mv.data.get("data");
		DebugHelper.printResult(list);
	}
	
	@Test
	public void testListProcess(){
		ModelAndView mv = service.listProcess(29);
		List<?> list = (List<?>) mv.data.get("data");
		DebugHelper.printResult(list);
	}
	
	@Test
	public void testAddAdminClass(){
		ProcessClass pc = new ProcessClass();
		service.addProcessClass(pc);
	}
}
