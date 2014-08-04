package com.youwei.test.zjb.im;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.im.IMService;
import com.youwei.zjb.im.entity.Contact;
import com.youwei.zjb.im.entity.Message;
import com.youwei.zjb.util.DebugHelper;

public class TestIMService {

	IMService service = new IMService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testGetContact(){
		ModelAndView mv = service.getContacts(316);
		DebugHelper.printResult((List<?>) mv.data.get("data"));
	}
	
	@Test
	public void testAddMessage(){
		Message dbMsg = new Message();
		dbMsg.sendtime = new Date();
		dbMsg.content = "test";
		dbMsg.senderId = 123;
		dbMsg.receiverId = 456;
		dbMsg.receiverType=1;
		dbMsg.hasRead=0;
		SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(dbMsg);
	}
}
