package com.youwei.zjb.house;

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
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/genjin")
public class GenJinService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(GenJin gj){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user==null){
			gj.userId = 316;
		}else{
			gj.userId = user.id;
		}
		if(gj!=null){
			gj.addtime = new Date();
			service.saveOrUpdate(gj);
		}
		mv.data.put("msg", "保存成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		if(id!=null){
			GenJin po = service.get(GenJin.class, id);
			if(po!=null){
				service.delete(po);
			}
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView review(Integer id, Integer sh){
		ModelAndView mv = new ModelAndView();
		if(id!=null){
			if(sh!=0 && sh!=1){
				throw new GException(PlatformExceptionType.BusinessException, "无效的参数");
			}
			GenJin po = service.get(GenJin.class, id);
			if(po!=null){
				po.sh = sh;
				service.saveOrUpdate(po);
			}
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(GenJinQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder(" select gj.id as id,gj.hid as houseId,gj.conts as conts,gj.addtime as addtime,gj.sh as sh,gj.chuzu as chuzu ,gj.area as area, gj.bianhao as bianhao, "
				+ "u.uname as uname,dept.namea as deptName from  GenJin gj  ,User u,Department dept where gj.userId=u.id and u.id is not null and dept.id=u.deptId");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		
		if(query.houseId!=null){
			hql.append(" and hid=? ");
			params.add(query.houseId);
		}
		
		if(query.sh!=null){
			hql.append(" and gj.sh=? ");
			params.add(query.sh.getCode());
		}
		
		if(StringUtils.isNotEmpty(query.area)){
			hql.append(" and area like ?");
			params.add("%"+query.area+"%");
		}
		
		if(StringUtils.isNotEmpty(query.bianhao)){
			hql.append(" and bianhao like ?");
			params.add("%"+query.bianhao+"%");
		}
		hql.append(HqlHelper.buildDateSegment("gj.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("gj.addtime", query.addtimeEnd, DateSeparator.Before, params));
		
		hql.append(" order by gj.id ");
		page = service.findPage(page, hql.toString(), true,params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
