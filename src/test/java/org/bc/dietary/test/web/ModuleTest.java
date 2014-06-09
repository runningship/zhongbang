package org.bc.dietary.test.web;

import org.bc.sdak.Page;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.Client;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.District;
import com.youwei.zjb.entity.FanKui;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.HouseRent;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.entity.AuthCode;
import com.youwei.zjb.sys.entity.OperRecord;
import com.youwei.zjb.sys.entity.PC;

public class ModuleTest {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testHouseRent(){
		Page<HouseRent> page = new Page<HouseRent>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from HouseRent");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testHouse(){
		Page<House> page = new Page<House>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from House");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testGenJin(){
		Page<GenJin> page = new Page<GenJin>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from GenJin");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testDistrict(){
		Page<District> page = new Page<District>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from District");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testFanKui(){
		Page<FanKui> page = new Page<FanKui>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from FanKui");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testOperRecord(){
		Page<OperRecord> page = new Page<OperRecord>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from OperRecord");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testDepartment(){
		Page<Department> page = new Page<Department>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from Department");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testPC(){
		Page<PC> page = new Page<PC>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testRole(){
		Page<Role> page = new Page<Role>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from Role");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testUser(){
		Page<User> page = new Page<User>();
		page.setCurrentPageNo(2);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from User");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testAuthCode(){
		Page<AuthCode> page = new Page<AuthCode>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from AuthCode");
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testClient(){
		Page<Client> page = new Page<Client>();
		page.setCurrentPageNo(1);
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from Client");
		System.out.println(page.getResult().size());
	}
}
