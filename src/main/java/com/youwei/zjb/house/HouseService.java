package com.youwei.zjb.house;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.House;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/house/")
public class HouseService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(House house){
		ModelAndView mv = new ModelAndView();
		//检查，是否是重复房源.检查条件为,小区名+楼栋号+房号
		House po = service.getUniqueByParams(House.class, new String[]{"area","dhao","fhao"},new Object[]{house.area,house.dhao,house.fhao});
		if(po!=null){
			mv.data.put("msg", "同一个房源已经存在");
			mv.data.put("result", 2);
		}else{
			service.saveOrUpdate(house);
			mv.data.put("msg", "发布成功");
			mv.data.put("result", 0);
		}
		return mv;
	}
	
	public void softDelete(Integer houseId){
		if(houseId==null){
			return;
		}
		House po = service.get(House.class, houseId);
		if(po!=null){
			po.isdel= 1;
			service.saveOrUpdate(po);
		}
	}
	
	public void recover(Integer houseId){
		if(houseId==null){
			return;
		}
		House po = service.get(House.class, houseId);
		if(po!=null){
			po.isdel= 0;
			service.saveOrUpdate(po);
		}
	}
	public void physicalDelete(Integer houseId){
		//是否需要权限
		if(houseId==null){
			return;
		}
		House po = service.get(House.class, houseId);
		if(po!=null){
			service.delete(po);
		}
	}
	
	public ModelAndView listMy(HouseQuery query){
		User user = ThreadSession.getUser();
		if(user==null){
			ModelAndView mv = new ModelAndView();
			mv.data.put("msg", "用户已经掉线");
			return mv;
		}
		query.userId = user.id;
		return listAll(query);
	}
	
	@WebMethod
	public ModelAndView getQueryOptions(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("chaoxiang", ChaoXiang.toJsonArray());
		mv.data.put("datetype", DateType.toJsonArray());
		mv.data.put("fangxing", FangXing.toJsonArray());
		mv.data.put("xingzhi", HouseAttribute.toJsonArray());
		mv.data.put("leibie", HouseType.toJsonArray());
		mv.data.put("jiaoyi", JiaoYi.toJsonArray());
		mv.data.put("louxing", LouXing.toJsonArray());
		mv.data.put("quyu", QuYu.toJsonArray());
		mv.data.put("zhuangtai", State.toJsonArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView listAll(HouseQuery query){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = null;
		if(StringUtils.isNotEmpty(query.xpath)){
			hql = new StringBuilder(" select h from  House h  ,User u where h.userId=u.id and u.id is not null and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			hql = new StringBuilder("from House  where 1=1 ");
		}
		if(query.xingzhi!=null){
			hql.append(" and xingzhi = ? ");
			params.add(String.valueOf(query.xingzhi.getCode()));
		}
		if(StringUtils.isNotEmpty(query.quyu)){
			hql.append(" and quyu = ?");
			params.add(query.quyu);
		}
		if(query.fangxing!=null){
			hql.append(" and ").append(query.fangxing.getQueryStr());
		}
		if(query.jiaoyi!=null && !query.jiaoyi.isEmpty()){
			hql.append(" and ( ");
			for(int i=0;i<query.jiaoyi.size();i++){
				hql.append(" jiaoyi = ? ");
				if(i<query.jiaoyi.size()-1){
					hql.append(" or ");
				}
				params.add(query.jiaoyi.get(i).getCode());
			}
			hql.append(" )");
		}
		if(query.state!=null){
			
		}
		if(StringUtils.isNotEmpty(query.leibie)){
			hql.append(" and leibie = ? ");
			params.add(query.leibie);
		}
		if(query.sjiaStart!=null){
			hql.append(" and sjia>= ? ");
			params.add(query.sjiaStart);
		}
		if(query.sjiaEnd!=null){
			hql.append(" and sjia<= ? ");
			params.add(query.sjiaEnd);
		}
		
		if(query.dateType==null){
			query.dateType = DateType.首次录入日;
		}
		hql.append(buildDateHql(query.dateType,query.dateStart,DateSeparator.After,params));
		hql.append(buildDateHql(query.dateType,query.dateEnd, DateSeparator.Before , params));
		
		if(StringUtils.isNotEmpty(query.louxing)){
			hql.append(" and lxing= ? ");
			params.add(query.louxing);
		}
		if(query.mianjiStart!=null){
			hql.append(" and mianji>= ? ");
			params.add(query.mianjiStart);
		}
		if(query.mianjiEnd!=null){
			hql.append(" and mianji<= ? ");
			params.add(query.mianjiEnd);
		}
		if(query.lcengStart!=null){
			hql.append(" and lceng>= ? ");
			params.add(query.lcengStart);
		}
		if(query.lcengEnd!=null){
			hql.append(" and lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if(StringUtils.isNotEmpty(query.chaoxiang)){
			hql.append(" and chaoxiang= ? ");
			params.add(query.chaoxiang);
		}
		if(StringUtils.isNotEmpty(query.chanquan)){
			hql.append(" and chanquan like ?");
			params.add("%"+query.chanquan+"%");
		}
		
		if(query.userId!=null){
			hql.append(" and userId= ? ");
			params.add(query.userId);
		}
		
		hql.append(" and ( isdel= 0 or isdel is null) ");
		
		Page<House> page = new Page<House>();
//		page.setCurrentPageNo(1);
		page.setPageSize(3);
		page = service.findPage(page, hql.toString(),params.toArray());
		List<House> houses = page.getResult();
		ModelAndView mv = new ModelAndView();
		mv.data.put("houses", JSONHelper.toJSONArray(houses));
		return mv;
	}
	
	private String buildDateHql(DateType dateType,String dateStr,DateSeparator sep,List<Object> params){
		if(StringUtils.isNotEmpty(dateStr)){
			if(DateType.建房年代==dateType){
				params.add(Integer.valueOf(dateStr));
				if(sep==DateSeparator.Before){
					return " and " + dateType.getField()+"<=?";
				}else{
					return " and " + dateType.getField()+">=?";
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdf.parse(dateStr);
				params.add(date);
				if(sep==DateSeparator.Before){
					return " and "+dateType.getField()+" <=?";
				}else{
					return " and "+dateType.getField()+" >=?";
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
}
