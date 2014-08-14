package com.youwei.test.zjb.house;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.bc.sdak.Page;
import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.ChaoXiang;
import com.youwei.zjb.house.DateType;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.HouseAttribute;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.HouseService;
import com.youwei.zjb.house.HouseType;
import com.youwei.zjb.house.JiaoYi;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.entity.House;

public class TestHouseService {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	@Test
	public void testListByQuyu(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.quyus.add("蜀山区");
		ModelAndView mv = hs.listAll(hq , new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByXinzhi(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.xingzhi = HouseAttribute.公盘;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByFangXing(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.fangxing = FangXing.房1到2;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByJiaoYi(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
//		hq.jiaoyis=JiaoYi.租售+","+JiaoYi.出售;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}

	@Test
	public void testListByLeibie(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.leibie = HouseType.商铺.toString();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBySJia(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.sjiaStart = 50f;
		hq.sjiaEnd = 60f;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByLouXing(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.louxing = LouXing.小高层.toString();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByMianJi(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.mianjiStart=50f;
		hq.mianjiEnd=60f;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByLouCeng(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.lcengStart=5;
		hq.lcengEnd=6;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByChaoXiang(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.chaoxiang= ChaoXiang.东.toString();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateweituo(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateType = DateType.委托日期;
		hq.dateStart = "2013-09-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydategenjin(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateType = DateType.最后跟进日;
		hq.dateStart = "2013-09-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateadd(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateType = DateType.首次录入日;
		hq.dateStart = "2013-03-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateyear(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateType = DateType.建房年代;
		hq.dateStart = "2013";
		hq.dateEnd = "2013";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByXPath(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.xpath="185";
		hq.quyus.add("包河区");
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	private void printResult(ModelAndView mv){
		List<House> houses = (List<House>) mv.data.get("houses");
		for(House house : houses){
			System.out.println(BeanUtil.toString(house));
		}
	}
}
