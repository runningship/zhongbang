package com.youwei.zjb.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.feedback.entity.FeedBack;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/feedback/")
public class FeedBackService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select SubString(fb.conts,1,20) as conts ,fb.addtime as addtime , u.uname as uname, d.namea as deptName from FeedBack fb, User u, "
				+ "Department d where fb.userId=u.id and u.deptId=d.id");
		List<Object> params = new ArrayList<Object>();
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(FeedBack fb){
		if(fb.conts==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "请先填写反馈已经");
		}
		fb.addtime = new Date();
		fb.userId = ThreadSession.getUser().id;
		dao.saveOrUpdate(fb);
		return new ModelAndView();
	}
}
