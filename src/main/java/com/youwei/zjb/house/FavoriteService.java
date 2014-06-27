package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Favorite;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/fav/")
public class FavoriteService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Integer userId, Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po==null){
			Favorite fav = new Favorite();
			fav.houseId = houseId;
			fav.userId = userId;
			service.saveOrUpdate(fav);
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public void delete(Integer userId,Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po!=null){
			service.delete(po);
		}
	}
	
	@WebMethod
	public ModelAndView list(HouseQuery query){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user==null){
			user = service.get(User.class, 316);
		}
		StringBuilder hql = new StringBuilder("select h from House h,Favorite f where h.id=f.houseId and f.userId="+user.id);
		Page<House> page = new Page<House>();
		page.setCurrentPageNo(1);
		page = service.findPage(page, hql.toString());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
