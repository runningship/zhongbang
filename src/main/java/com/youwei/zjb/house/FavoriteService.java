package com.youwei.zjb.house;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.entity.Favorite;

public class FavoriteService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Integer userId, Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po==null){
			service.saveOrUpdate(po);
		}
	}
	
	public void delete(Integer userId,Integer houseId){
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po!=null){
			service.delete(po);
		}
	}
}
