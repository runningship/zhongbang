package com.youwei.zjb.work;

import java.util.ArrayList;
import java.util.Date;
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
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.entity.JiLu;
import com.youwei.zjb.work.entity.Journal;
import com.youwei.zjb.work.entity.PYItem;

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
		//产生一个空的批阅项
		PYItem py = new PYItem();
		py.category = jilu.category;
		py.conts = "";
		py.uid = user.id;
		py.uname = user.uname;
		py.addtime = new Date();
		py.bizId = jilu.id;
		py.finish=0;
		dao.saveOrUpdate(py);
		mv.data.put("result", 0);
		mv.data.put("recordId", jilu.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(JiLu jilu){
		ModelAndView mv = new ModelAndView();
		if(jilu.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录已不存在");
		}
		JiLu po = dao.get(JiLu.class, jilu.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录已不存在");
		}
		po.title=jilu.title;
		po.conts = jilu.conts;
		po.starttime = jilu.starttime;
		po.endtime = jilu.endtime;
		po.goin = jilu.goin;
		dao.saveOrUpdate(po);
		mv.data.put("result", 0);
		mv.data.put("recordId", po.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(JiLuQuery query,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select py.finish as py, u.uname as uname,d.namea as deptName,q.namea as quyu,j.title as title ,j.addtime as addtime , j.starttime as starttime "
				+ ",j.id as id , j.category as category ,j.pingji as pingji from JiLu j,PYItem py, User u,Department d,Department q where j.userId=u.id and py.bizId=j.id and u.deptId=d.id and d.fid=q.id and py.uid=? ");
		params.add(ThreadSession.getUser().id);
		if(query.category!=null){
			hql.append(" and j.category=? and py.category=? ");
			params.add(query.category);
			params.add(query.category);
		}
		if(StringUtils.isNotEmpty(query.pingji)){
			hql.append(" and j.pingji=?");
			params.add(query.pingji);
		}
		if(query.finish!=null){
			hql.append(" and py.finish=?");
			params.add(query.finish);
		}
		if(StringUtils.isNotEmpty(query.goin)){
			hql.append(" and j.goin like ? ");
			params.add("%"+query.goin+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		if(query.category==1){
			hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeStart, DateSeparator.After, params));
			hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeEnd, DateSeparator.Before, params));
		}else{
			hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeStart, DateSeparator.After, params));
			hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeEnd, DateSeparator.Before, params));
		}
		page.orderBy = "j.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		if(query.category!=null && (query.category==2 || query.category==3)){
			mv.data.put("page", JSONHelper.toJSON(page , DataHelper.monthSdf.toPattern()));
		}else{
			mv.data.put("page", JSONHelper.toJSON(page));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView piyue(int id, String contb , String pingji ){
		JiLu po = dao.get(JiLu.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "该记录已不存在");
		}
		User user = ThreadSession.getUser();
		//批阅项
		PYItem pypo = dao.getUniqueByParams(PYItem.class, new String[]{"category" , "bizId","uid"}, new Object[]{po.category , po.id , user.id});
		if(pypo!=null){
			pypo.conts = contb;
			pypo.finish=1;
			dao.saveOrUpdate(pypo);
		}else{
			PYItem py = new PYItem();
			py.category = po.category;
			py.conts = contb;
			py.uid = user.id;
			py.uname = user.uname;
			py.addtime = new Date();
			py.bizId = po.id;
			py.finish = 1;
			dao.saveOrUpdate(py);
		}
		if(pingji!=null){
			po.pingji = pingji;
		}
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		JiLu jilu = dao.get(JiLu.class,id);
		List<Attachment> attachs = dao.listByParams(Attachment.class, new String[]{"bizType" , "recordId"}, new Object[]{"jilu" , id});
		List<PYItem> pyList = dao.listByParams(PYItem.class, "from PYItem where bizId=? and category=?", id,jilu.category);
		PYItem myPy = new PYItem();
		for(PYItem item : pyList){
			if(item.uid.equals(ThreadSession.getUser().id)){
				myPy = item;
				break;
			}
		}
		pyList.remove(myPy);
		mv.data.put("myPy", JSONHelper.toJSON(myPy));
		mv.data.put("pyList", JSONHelper.toJSONArray(pyList));
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
