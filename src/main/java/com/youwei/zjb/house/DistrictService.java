package com.youwei.zjb.house;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.entity.District;

public class DistrictService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(District district){
		if(StringUtils.isEmpty(district.name)){
			//TODO
			return;
		}
		District po = service.getUniqueByKeyValue(District.class, "name", district.name);
		if(po==null){
			service.saveOrUpdate(district);
		}
	}
	
	public void update(District district){
		if(district.id==null){
			return;
		}
		service.saveOrUpdate(district);
	}
	
}
