package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.entity.Client;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.util.HqlHelper;

public class ClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Client client){
		
	}
	
	public void update(Client client){
		
	}
	
	public void delete(Integer id){
		
	}
	
	public List<Client> list(ClientQuery query){
		StringBuilder hql = null;
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isEmpty(query.xpath)){
			hql = new StringBuilder("from Client where 1=1 ");
		}else{
			hql = new StringBuilder("from Client c,User u where u.id=c.salesman and u.id is not null and u.orgpath like ? ");
			params.add(query.xpath);
		}
		if(query.chuzu!=null){
			hql.append(" and chuzu=? ");
			params.add(query.chuzu);
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and tel like ?");
			params.add("%"+query.tel+"%");
		}
		if(query.valid!=null){
			hql.append(" and valid=? ");
			params.add(query.valid);
		}
		if(StringUtils.isNotEmpty(query.quyus)){
			String[] quyus = query.quyus.split(",");
			if(quyus.length>0){
				hql.append(" and (1=0 ");
				for(String quyu : query.quyus.split(",")){
					hql.append(" or quyu like ? ");
					params.add("%"+quyu+"%");
				}
				hql.append(")");
			}
		}
		if(StringUtils.isNotEmpty(query.source)){
			hql.append(" and source like ?");
			params.add("%"+query.source+"%");
		}
		
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(StringUtils.isNotEmpty(query.orderBy)){
			hql.append(" order by ").append(query.orderBy);
		}
		if(StringUtils.isNotEmpty(query.order)){
			hql.append(query.order);
		}
		
		Page<Client> page = new Page<Client>();
		page.setCurrentPageNo(1);
		page.setPageSize(40);
		page = dao.findPage(page, hql.toString(),params.toArray());
		List<Client> list = page.getResult();
		return list;
	}
	
	public void delete(List<Integer> ids){
		String hql = "delete from Client where id in (?)";
		dao.execute(hql, ids.toArray());
	}
}
