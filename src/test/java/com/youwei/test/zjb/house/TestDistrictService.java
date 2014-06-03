package com.youwei.test.zjb.house;

import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.District;
import com.youwei.zjb.house.DistrictService;

public class TestDistrictService {

	DistrictService service = new DistrictService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testUpdate(){
		District dis = SimpDaoTool.getGlobalCommonDaoService().get(District.class, 367);
		dis.uid=-1;
		service.update(dis);
	}
}
