package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.entity.GenJin;
import com.youwei.zjb.util.HqlHelper;


public class GenJinService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(GenJin gj){
		if(gj!=null){
			service.saveOrUpdate(gj);
		}
	}
	
	public void delete(Integer houseId){
		if(houseId!=null){
			GenJin po = service.get(GenJin.class, houseId);
			if(po!=null){
				service.delete(po);
			}
		}
	}
	
	public void review(Integer houseId, Integer sh){
		if(houseId!=null){
			if(sh!=0 || sh!=1){
				//无效的参数
				return;
			}
			GenJin po = service.get(GenJin.class, houseId);
			if(po!=null){
				po.sh = sh;
				service.delete(po);
			}
		}
	}
	
	public List<GenJin> list(GenJinQuery query){
		StringBuilder hql = null;
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.xpath)){
			hql = new StringBuilder(" select gj from  GenJin gj  ,User u where gj.userId=u.id and u.id is not null and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			hql =  new StringBuilder("from GenJin gj where 1=1 ");
		}
		
		if(query.houseId!=null){
			hql.append(" and hid=? ");
			params.add(query.houseId);
		}
		
		if(query.review!=null){
			hql.append(" and sh=? ");
			params.add(query.review);
		}
		
		if(StringUtils.isNotEmpty(query.area)){
			hql.append(" and area like ?");
			params.add("%"+query.area+"%");
		}
		
		if(StringUtils.isNotEmpty(query.bianhao)){
			hql.append(" and bianhao like ?");
			params.add("%"+query.bianhao+"%");
		}
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("addtime", query.addtimeEnd, DateSeparator.Before, params));
		
		hql.append(" order by gj.id ");
		Page<GenJin> page = new Page<GenJin>();
		page.setCurrentPageNo(1);
		page.setPageSize(40);
		page = service.findPage(page, hql.toString(),params.toArray());
		List<GenJin> list = page.getResult();
		return list;
	}
}
