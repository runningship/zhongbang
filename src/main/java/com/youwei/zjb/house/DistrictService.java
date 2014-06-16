package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.District;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/areas/")
public class DistrictService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(District district){
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException, 1, "小区名不能为空");
		}
		District po = service.getUniqueByKeyValue(District.class, "name", district.name);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "小区名重复");
		}
		service.saveOrUpdate(district);
	}
	
	@WebMethod
	public ModelAndView list(String search , Page<District> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from District where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(search)){
			search = "%"+search+"%";
			hql.append(" and name like ?  or quyu like ? or pyShort like ?");
			params.add(search);
			params.add(search);
			params.add(search);
		}
		page = service.findPage(page, hql.toString(), params.toArray());
		mv.data.put("result", 0);
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	public void update(District district){
		if(district.id==null){
			return;
		}
		service.saveOrUpdate(district);
	}
	
	
}
