package com.youwei.zjb.work;

import java.util.ArrayList;
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
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/jilu")
public class JiLuService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(JiLuQuery query,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select u.uname as uname,q.namea as deptName,q.namea as quyu,j.title as title ,j.addtime as addtime "
				+ "from JiLu j, User u,Department d,Department q where j.userId=u.id and u.deptId=d.id and d.fid=q.id");
		if(query.category!=null){
			hql.append(" and j.category=?");
			params.add(query.category);
		}
		if(StringUtils.isNotEmpty(query.goin)){
			hql.append(" and j.goin like ? ");
			params.add("%"+query.goin+"%");
		}
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeEnd, DateSeparator.Before, params));
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
