package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/house/fav/")
public class FavoriteService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Integer houseId){
		User user = ThreadSession.getUser();
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{user.id,houseId});
		if(po==null){
			Favorite fav = new Favorite();
			fav.houseId = houseId;
			fav.userId = user.id;
			service.saveOrUpdate(fav);
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(Integer houseId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{user.id,houseId});
		if(po!=null){
			service.delete(po);
		}
		mv.data.put("msg", "已取消关注");
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(HouseQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
		page.mergeResult = true;
		hql.append(" select h, u.uname as fbr,d.namea as dname from  House h ,Favorite f  ,User u,Department d where h.id=f.houseId and f.userId=? and h.userId=u.id  and u.deptId=d.id");
//		StringBuilder hql = new StringBuilder("select h from House h,Favorite f where h.id=f.houseId and f.userId="+user.id);
		params.add(user.id);
		HqlHelper.buildQuery(hql, query, params);
		page = service.findPage(page, hql.toString(), true , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
