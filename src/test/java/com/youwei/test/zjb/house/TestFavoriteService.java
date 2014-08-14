package com.youwei.test.zjb.house;

import java.util.List;

import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.FavoriteService;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.house.entity.House;

public class TestFavoriteService {

	FavoriteService service = new FavoriteService();
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testAdd(){
		service.add(169);
	}
	
	private void printResult(List<House> list){
		for(House h : list){
			System.out.println(BeanUtil.toString(h));
		}
	}
}
