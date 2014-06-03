package com.youwei.test.zjb.client;

import java.util.List;

import org.bc.sdak.utils.BeanUtil;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.client.ClientQuery;
import com.youwei.zjb.client.ClientService;
import com.youwei.zjb.entity.Client;

public class TestClientService {

	ClientService service = new ClientService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testQueryByQuyu(){
		ClientQuery query = new ClientQuery();
		query.quyus="蜀山区,瑶海区";
		List<Client> result = service.list(query);
		printResult(result);
	}
	
	private void printResult(List<?> list){
		for(Object o : list){
			System.out.println(BeanUtil.toString(o));
		}
	}
}
