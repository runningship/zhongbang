package com.youwei.zjb.work;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.work.entity.Journal;
import com.youwei.zjb.work.entity.OutRecord;

public class OutService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(OutRecord out){
		if(out.userId==null || out.outTime==null || out.backTime==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "您填写的数据不完整");
		}
		
		String hql = "from OutRecord where userId=? and outTime>= ? and backTime<=? ";
		long count = dao.countHqlResult(hql, out.userId,out.outTime,out.backTime);
		if(count>=0){
			throw new GException(PlatformExceptionType.BusinessException, 2, "您在该时间段已经有外出记录了");
		}
		if(out.category==1){
			addOutBiz(out);
		}else{
			addOutHouse(out);
		}
	}
	
	public void delete(int id){
		OutRecord po = dao.getUnique(OutRecord.class, id);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	public void piyue(int id, String content){
		OutRecord po = dao.get(OutRecord.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "该记录已不存在");
		}
		po.reply=1;
		po.conts = content;
		dao.saveOrUpdate(po);
	}

	private void addOutBiz(OutRecord out) {
		dao.saveOrUpdate(out);
	}

	private void addOutHouse(OutRecord out) {
		if(StringUtils.isEmpty(out.clients)){
			//如果是带看需要客源信息
			throw new GException(PlatformExceptionType.BusinessException, 3, "请选择客源信息");
		}
		if(StringUtils.isEmpty(out.houses)){
			throw new GException(PlatformExceptionType.BusinessException, 4, "请选择房源信息");
		}
		List<User> clients = dao.listByParams(User.class, "from User where id in (?) ", (Object[])out.clients.split(","));
		List<House> houses = dao.listByParams(House.class, "from House where id in (?) ", (Object[])out.houses.split(","));
		for(User u : clients){
			out.clientInfo+=u.uname+" "+u.tel+",";
		}
		for(House h : houses){
			out.houseInfo+=h.quyu+" "+h.area+" "+h.dhao+h.fhao+",";
		}
		dao.saveOrUpdate(out);
	}
	
	public void list(OutQuery query ,Page<Journal> page){
		StringBuilder hql = new StringBuilder("from OutQuery where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(query.category!=null){
			hql.append(" and category=? ");
			params.add(query.category);
		}
		//默认只能看到自己的数据
		User user = ThreadSession.getUser();
		hql.append(" and uid = ?");
		params.add(user.id);
		
		page = dao.findPage(page, hql.toString(), params.toArray());
		
	}
}
