package com.youwei.zjb.oa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.oa.entity.Attence;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa/attence/")
public class AttenceService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Attence attence){
		ModelAndView mv =new ModelAndView();
		User user = ThreadSession.getUser();
		attence.userId = user.id;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Date end = cal.getTime();
		List<Attence> list = dao.listByParams(Attence.class, "from Attence where userId=? and type=? and checktime>=? and checktime<=?", user.id , attence.type, start , end);
		if(list==null || list.isEmpty()){
			attence.checktime = new Date();
			dao.saveOrUpdate(attence);
		}else{
			Attence po = list.get(0);
			if(po.type==2 || po.type==4){
				po.checktime = new Date();
				dao.saveOrUpdate(po);
			}
		}
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMy(){
		ModelAndView mv =new ModelAndView();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Date end = cal.getTime();
		User user = ThreadSession.getUser();
		List<Attence> list = dao.listByParams(Attence.class, "from Attence where userId=? and checktime>=? and checktime<=?", user.id ,  start , end);
		mv.data.put("attences", JSONHelper.toJSONArray(list));
		return mv;
	}
}
