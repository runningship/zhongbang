package com.youwei.test.zjb.user;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.UserService;
import com.youwei.zjb.util.DebugHelper;

public class TestUserService {

	UserService us = new UserService();
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void TestGetUserTree(){
	}
}
