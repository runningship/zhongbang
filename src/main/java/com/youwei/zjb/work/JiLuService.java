package com.youwei.zjb.work;

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
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Attachment;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.entity.JiLu;
import com.youwei.zjb.work.entity.Journal;

@Module(name="/jilu")
public class JiLuService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(JiLu jilu){
		ModelAndView mv = new ModelAndView();
		jilu.addtime = new Date();
		User user = ThreadSession.getUser();
		jilu.userId = user.id;
		dao.saveOrUpdate(jilu);
		mv.data.put("result", 0);
		mv.data.put("recordId", jilu.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(JiLuQuery query,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select u.uname as uname,q.namea as deptName,q.namea as quyu,j.title as title ,j.addtime as addtime "
				+ ",j.id as id , j.category as category from JiLu j, User u,Department d,Department q where j.userId=u.id and u.deptId=d.id and d.fid=q.id");
		if(query.category!=null){
			hql.append(" and j.category=?");
			params.add(query.category);
		}
		if(StringUtils.isNotEmpty(query.goin)){
			hql.append(" and j.goin like ? ");
			params.add("%"+query.goin+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeEnd, DateSeparator.Before, params));
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		JiLu jilu = dao.get(JiLu.class,id);
		List<Attachment> attachs = dao.listByParams(Attachment.class, new String[]{"bizType" , "recordId"}, new Object[]{"jilu" , id});
		
		mv.data.put("attachs", JSONHelper.toJSONArray(attachs));
		mv.data.put("jilu", JSONHelper.toJSON(jilu));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		JiLu po = dao.get(JiLu.class, id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
}
