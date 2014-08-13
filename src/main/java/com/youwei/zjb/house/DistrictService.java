package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/areas/")
public class DistrictService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(District district){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException,"小区名不能为空");
		}
		District po = dao.getUniqueByKeyValue(District.class, "name", district.name);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"小区名重复");
		}
		district.pinyin=DataHelper.toPinyin(district.name);
		district.pyShort=DataHelper.toPinyinShort(district.name);
		dao.saveOrUpdate(district);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int areaId){
		ModelAndView mv = new ModelAndView();
		District area = dao.get(District.class, areaId);
		mv.data.put("area", JSONHelper.toJSON(area));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		District area = dao.get(District.class, id);
		if(area!=null){
			dao.delete(area);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(String search , Page<District> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from District where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(search)){
			search = "%"+search+"%";
			hql.append(" and (name like ?  or quyu like ? or pinyin like ? or pyShort like ?)");
			params.add(search);
			params.add(search);
			params.add(search);
			params.add(search);
		}
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("result", 0);
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView search(String area){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from District where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(area)){
			area = "%"+area+"%";
			hql.append(" and (name like ?  or quyu like ? or pinyin like ? or pyShort like ?)");
			params.add(area);
			params.add(area);
			params.add(area);
			params.add(area);
		}
		List<District> list = dao.listByParams(District.class, hql.toString(), params.toArray());
		if(list.isEmpty()){
			mv.returnText="empty";
		}else{
			StringBuilder tmp = new StringBuilder("<dl>");
			for(int i=0;i<list.size();i++){
				District d = list.get(i);
				tmp.append("<dd value='"+i+"' onclick='form_submit();' onmouseover="+"\"mo($(this).attr('value'));\""+"  ><strong style='color:#696;'>"+d.name+"</strong><small>&nbsp;</small><big>"+d.quyu+"</big></dd>");
			}
			tmp.append("</dl>");
			mv.returnText = tmp.toString();
		}
		
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView update(District district){
		ModelAndView mv = new ModelAndView();
		if(district.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "id不能为空");
		}
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException, "小区名不能为空");
		}
		District po = dao.get(District.class, district.id);
		district.pinyin=DataHelper.toPinyin(district.name);
		district.pyShort=DataHelper.toPinyinShort(district.name);
		dao.saveOrUpdate(district);
		//更新房源
		if(!po.name.equals(district.name) || !po.quyu.equals(district.quyu)){
			dao.execute("update House set area=? ,quyu=? where area=?", district.name ,district.quyu, po.name);
		}
		mv.data.put("msg", "保存成功");
		return mv;
	}
	
	
}
