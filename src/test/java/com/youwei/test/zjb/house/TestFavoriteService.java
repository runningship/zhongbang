package com.youwei.test.zjb.house;

import java.util.List;

import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.FavoriteService;

public class TestFavoriteService {

	FavoriteService service = new FavoriteService();
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testAdd(){
		service.add(207, 169);
	}
	
	@Test
	public void testQuery(){
		User user = new User();
		user.id = 207;
		ThreadSession.setUser(user);
		ModelAndView mv = service.list(null);
//		printResult(list);
	}
	
	private void printResult(List<House> list){
		for(House h : list){
			System.out.println(BeanUtil.toString(h));
		}
	}
}
