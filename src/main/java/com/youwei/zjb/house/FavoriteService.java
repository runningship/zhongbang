package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Favorite;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.User;

public class FavoriteService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Integer userId, Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po==null){
			Favorite fav = new Favorite();
			fav.houseId = houseId;
			fav.userId = userId;
			service.saveOrUpdate(fav);
		}
	}
	
	public void delete(Integer userId,Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po!=null){
			service.delete(po);
		}
	}
	
	public List<House> list(HouseQuery query){
		User user = ThreadSession.getUser();
		if(user==null){
			return new ArrayList<House>();
		}
		StringBuilder hql = new StringBuilder("select h from House h,Favorite f where h.id=f.houseId and f.userId="+user.id);
		Page<House> page = new Page<House>();
		page.setCurrentPageNo(1);
		page = service.findPage(page, hql.toString());
		List<House> houses = page.getResult();
		return houses;
	}
}
