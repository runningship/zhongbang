package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.entity.Client;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/client")
public class ClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Client client){
		client.addtime = new Date();
		dao.saveOrUpdate(client);
	}
	
	public void update(Client client){
		dao.saveOrUpdate(client);
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, id);
		if(client!=null){
			dao.delete(client);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	public void assign(Integer clientId,Integer userId){
		Client client = dao.get(Client.class, clientId);
		if(client==null){
			return;
		}
		if(userId==null){
			return;
		}
		client.salesman = userId;
		dao.saveOrUpdate(client);
	}
	
	@WebMethod
	public ModelAndView list(ClientQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select c.id as id,c.lxr as lxr,c.tel as tel, c.mianjiFrom as mianjiFrom ,c.mianjiTo as mianjiTo ,c.jiageFrom as jiageFrom,"
				+ "c.jiageTo as jiageTo,c.loucengFrom as loucengFrom,c.loucengTo as loucengTo,c.addtime as addtime,c.zhongyaos as kehu ,u.uname as uname "
				+ "from Client c,User u where u.id=c.salesman and u.id is not null ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			
		}
		if(query.chuzu!=null){
			hql.append(" and c.chuzu=? ");
			params.add(query.chuzu);
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and c.tel like ?");
			params.add("%"+query.tel+"%");
		}
		if(query.youxiao!=null){
			hql.append(" and c.valid=? ");
			params.add(query.youxiao);
		}
		if(StringUtils.isNotEmpty(query.quyus)){
			String[] quyus = query.quyus.split(",");
			if(quyus.length>0){
				hql.append(" and (1=0 ");
				for(String quyu : query.quyus.split(",")){
					hql.append(" or c.quyu like ? ");
					params.add("%"+quyu+"%");
				}
				hql.append(")");
			}
		}
		if(StringUtils.isNotEmpty(query.kehulaiyuan)){
			hql.append(" and c.source like ?");
			params.add("%"+query.kehulaiyuan+"%");
		}
		
		if(query.kehuxingzhi!=null){
			hql.append(" and c.zhongyaos = ?");
			params.add(String.valueOf(query.kehuxingzhi.getCode()));
		}
		
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(StringUtils.isNotEmpty(query.orderBy)){
			hql.append(" order by ").append(query.orderBy);
		}
		if(StringUtils.isNotEmpty(query.order)){
			hql.append(query.order);
		}
		
		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteBatch(List<Object> ids){
		List<Integer> params = new ArrayList<Integer>();
		ModelAndView mv = new ModelAndView();
		mv.data.put("result", 0);
		if(ids.isEmpty()){
			return mv;
		}
		StringBuilder hql = new StringBuilder("delete from Client where id in (-1");
		for(Object id : ids){
			hql.append(",").append("?");
			params.add(Integer.valueOf(id.toString()));
		}
		hql.append(")");
		dao.execute(hql.toString(), params.toArray());
		return mv;
	}
}
