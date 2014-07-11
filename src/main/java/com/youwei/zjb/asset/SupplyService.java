package com.youwei.zjb.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.asset.entity.Asset;
import com.youwei.zjb.asset.entity.OfficeSupply;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;

/**
 * 办公用品
 */
@Module(name="/supply/")
public class SupplyService {

CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(OfficeSupply supply){
		ModelAndView mv = new ModelAndView();
		supply.addtime = new Date();
		supply.shenhe = 0;
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		supply.userId = user.id;
		supply.deptId = user.deptId;
		dao.saveOrUpdate(supply);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int supplyId){
		ModelAndView mv = new ModelAndView();
		OfficeSupply supply = dao.get(OfficeSupply.class , supplyId);
		mv.data.put("supply", JSONHelper.toJSON(supply));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<OfficeSupply> page, SupplyQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select o.id as id, o.djia as djia, o.zjia as zjia ,o.count as count, o.beizhu as beizhu,o.title as title, o.addtime as addtime,"
				+ " o.xgr as xgr, d.namea as deptName, q.namea as quyu from OfficeSupply o,Department d , Department q where d.id=o.deptId and q.id=d.fid");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.title)){
			hql.append(" and o.title like ?");
			params.add("%"+query.title+"%");
		}
		if(query.shenhe!=null){
			hql.append(" and o.shenhe=? ");
			params.add(query.shenhe);
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and d.path like ?");
			params.add(query.xpath+"%");
		}
		page = dao.findPage(page, hql.toString(), true , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(OfficeSupply supply){
		ModelAndView mv = new ModelAndView();
		OfficeSupply po = dao.get(OfficeSupply.class, supply.id);
		supply.shenhe = po.shenhe;
		supply.addtime = po.addtime;
		supply.userId = po.userId;
		supply.deptId = po.deptId;
		dao.saveOrUpdate(supply);
		return mv;
	}
	
	@WebMethod
	public ModelAndView shenhe(int supplyId , int shenhe){
		ModelAndView mv = new ModelAndView();
		OfficeSupply po = dao.get(OfficeSupply.class, supplyId);
		po.shenhe = shenhe;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		OfficeSupply supply = dao.get(OfficeSupply.class, id);
		if(supply!=null){
			dao.delete(supply);
		}
		return mv;
	}
}
