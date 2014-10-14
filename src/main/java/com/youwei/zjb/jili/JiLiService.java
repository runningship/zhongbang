package com.youwei.zjb.jili;

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
import com.youwei.zjb.entity.User;
import com.youwei.zjb.jili.entity.JiLi;
import com.youwei.zjb.jili.entity.JiLiQuery;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/jili/")
public class JiLiService {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(JiLi jili){
		ModelAndView mv = new ModelAndView();
		dao.saveOrUpdate(jili);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(Integer id){
		ModelAndView mv = new ModelAndView();
		JiLi po = dao.get(JiLi.class, id);
		User u = dao.get(User.class, po.uid);
		mv.data = JSONHelper.toJSON(po,DataHelper.dateSdf.toPattern());
		mv.data.put("uname", u.uname);
		return mv;
	}
	
	@WebMethod
	public ModelAndView tongji(JiLiQuery query,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select jl.uid as uid, u.uname as uname ,u.orgpath as xpath, sum(jl.score) as total ,d.namea as dname,q.namea as qname from JiLi jl, User u ,Department d , Department q where jl.uid=u.id and d.id=u.deptId and q.id=d.fid");
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ?");
			params.add(query.xpath+"%");
		}
		hql.append(HqlHelper.buildDateSegment("jl.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("jl.addtime", query.addtimeEnd, DateSeparator.Before, params));
		hql.append(" group by jl.uid ");
		List<Map> list = dao.listAsMap(hql.toString(), params.toArray());
		mv.data.put("list", JSONHelper.toJSONArray(list , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(JiLiQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select jl.id as id, u.uname as uname ,d.namea as dname,q.namea as qname, jl.score as score , jl.addtime as addtime, jl.reason as reason from JiLi jl, User u , Department d,Department q where jl.uid=u.id and u.deptId=d.id and q.id=d.fid");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.reason)){
			hql.append(" and reason like ? ");
			params.add("%"+query.reason+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ?");
			params.add(query.xpath+"%");
		}
		hql.append(HqlHelper.buildDateSegment("jl.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("jl.addtime", query.addtimeEnd, DateSeparator.Before, params));
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(JiLi jili){
		ModelAndView mv = new ModelAndView();
		JiLi po = dao.get(JiLi.class, jili.id);
		if(po!=null){
			po.reason = jili.reason;
			po.score = jili.score;
			dao.saveOrUpdate(jili);
		}else{
			throw new GException(PlatformExceptionType.BusinessException, "数据不存在或已被删除");
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		JiLi po = dao.get(JiLi.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "数据不存在或已被删除");
		}
		dao.delete(po);
		return mv;
	}
}
