package com.youwei.zjb.work;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Attachment;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.entity.Journal;
import com.youwei.zjb.work.entity.OutRecord;

@Module(name="/out/")
public class OutService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(OutRecord out){
		ModelAndView mv = new ModelAndView();
		out.userId = ThreadSession.getUser().id;
		if(out.outTime==null || out.backTime==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "您填写的数据不完整");
		}
		out.reply = 0;
		String hql = "from OutRecord where userId=? and outTime>= ? and backTime<=? ";
		long count = dao.countHqlResult(hql, out.userId,out.outTime,out.backTime);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, 2, "您在该时间段已经有外出记录了");
		}
		if(out.category==1){
			addOutBiz(out);
		}else{
			addOutHouse(out);
		}
		mv.data.put("recordId", out.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		OutRecord out = dao.get(OutRecord.class,id);
		List<Attachment> attachs = dao.listByParams(Attachment.class, new String[]{"bizType" , "recordId"}, new Object[]{"out" , id});
		mv.data.put("attachs", JSONHelper.toJSONArray(attachs));
		mv.data.put("out", JSONHelper.toJSON(out));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		OutRecord po = dao.get(OutRecord.class, id);
		if(po!=null){
			dao.delete(po);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView piyue(int id, String conts , int integral){
		OutRecord po = dao.get(OutRecord.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "该记录已不存在");
		}
		po.reply=1;
		po.conts = conts;
		po.integral = integral;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}

	private void addOutBiz(OutRecord out) {
		dao.saveOrUpdate(out);
		
	}
	
	@WebMethod
	public ModelAndView upateOutRecord(int id, String onCont){
		OutRecord po = dao.get(OutRecord.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "该记录已不存在");
		}
		po.onCont = onCont;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}

	private void addOutHouse(OutRecord out) {
		if(StringUtils.isEmpty(out.clients)){
			//如果是带看需要客源信息
			throw new GException(PlatformExceptionType.BusinessException, 3, "请选择客源信息");
		}
		if(StringUtils.isEmpty(out.houses)){
			throw new GException(PlatformExceptionType.BusinessException, 4, "请选择房源信息");
		}
//		List<User> clients = dao.listByParams(User.class, "from User where id in (?) ", (Object[])out.clients.split(","));
//		List<House> houses = dao.listByParams(House.class, "from House where id in (?) ", (Object[])out.houses.split(","));
//		for(User u : clients){
//			out.clientInfo+=u.uname+" "+u.tel+",";
//		}
//		for(House h : houses){
//			out.houseInfo+=h.quyu+" "+h.area+" "+h.dhao+h.fhao+",";
//		}
		dao.saveOrUpdate(out);
	}
	
	@WebMethod
	public ModelAndView list(OutQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select o.id as id,d.namea as deptName ,q.namea as quyu, u.uname as uname, o.clientInfo as client ,o.houseInfo as house, "
				+ "o.outTime as outTime, o.backTime as backTime ,o.outCont as outCont,o.reply as reply from OutRecord o , User u , Department d,Department q where o.userId=u.id and u.deptId=d.id and q.id=d.fid");
		List<Object> params = new ArrayList<Object>();
		hql.append(HqlHelper.buildDateSegment("o.outTime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("o.outTime", query.addtimeEnd, DateSeparator.Before, params));
		if(query.category!=null){
			hql.append(" and o.category=? ");
			params.add(query.category);
		}
		if(query.kanfang!=null){
			hql.append(" and o.outHouse=?");
			params.add(query.kanfang);
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		//默认只能看到自己的数据
//		User user = ThreadSession.getUser();
//		hql.append(" and uid = ?");
//		params.add(user.id);
		
		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		DataHelper.fillDefaultValue(page.getResult(), "reply", PiYue.待批阅.getCode());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
