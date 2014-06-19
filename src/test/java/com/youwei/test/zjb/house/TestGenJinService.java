package com.youwei.test.zjb.house;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.Page;
import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.house.GenJinQuery;
import com.youwei.zjb.house.GenJinService;
import com.youwei.zjb.util.DebugHelper;

public class TestGenJinService {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}

	@Test
	public void testQueryByDate(){
		GenJinQuery query = new GenJinQuery();
		query.addtimeStart = "2013-08-21";
		query.addtimeEnd = "2013-08-23";
		GenJinService service = new GenJinService();
		ModelAndView mv = service.list(query , new Page<Map>());
		DebugHelper.printResult(mv.data.getJSONObject("page").getJSONArray("data"));
	}
	
	@Test
	public void testQueryByArea(){
		GenJinQuery query = new GenJinQuery();
		query.area="太阳城";
		GenJinService service = new GenJinService();
		ModelAndView mv = service.list(query , new Page<Map>());
		DebugHelper.printResult(mv.data.getJSONObject("page").getJSONArray("data"));
	}
	
	@Test
	public void testQueryByXpath(){
		GenJinQuery query = new GenJinQuery();
		query.xpath="169";
		GenJinService service = new GenJinService();
		ModelAndView mv = service.list(query , new Page<Map>());
		DebugHelper.printResult(mv.data.getJSONObject("page").getJSONArray("data"));
	}
	
	@Test
	public void testAdd(){
		GenJinService service = new GenJinService();
		GenJin gj = new GenJin();
		gj.area="蜀鑫雅苑";
		gj.bianhao = "S-123";
		gj.conts="测试";
		gj.addtime = new Date();
		service.add(gj);
		
	}
	
	private void printResult(List<GenJin> list){
		for(GenJin gj : list){
			System.out.println(BeanUtil.toString(gj));
		}
	}
}
