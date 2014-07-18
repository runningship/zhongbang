package com.youwei.zjb.house;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.BeanUtil;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.client.DaiKuanType;
import com.youwei.zjb.client.FuKuan;
import com.youwei.zjb.client.KeHuXingzhi;
import com.youwei.zjb.client.KeHuLaiYuan;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.PiYue;

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
			house.isdel = 0;
			service.saveOrUpdate(house);
			String py = DataHelper.toPinyin(house.quyu);
			if(StringUtils.isNotEmpty(py) && py.length()>0){
				house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
			}else{
				LogUtil.warning("生成房源编号失败,houseId="+house.id);
			}
			mv.data.put("msg", "发布成功");
			mv.data.put("result", 0);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(House house){
		ModelAndView mv = new ModelAndView();
		//检查，是否是重复房源.检查条件为,小区名+楼栋号+房号
		House po = service.getUniqueByParams(House.class, new String[]{"area","dhao","fhao"},new Object[]{house.area,house.dhao,house.fhao});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "小区名+楼栋号+房号 重复");
		}
		String py = DataHelper.toPinyin(house.quyu);
		if(StringUtils.isNotEmpty(py) && py.length()>0){
			house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
		}else{
			LogUtil.warning("生成房源编号失败,houseId="+house.id);
		}
		service.saveOrUpdate(house);
		mv.data.put("msg", "修改成功");
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView softDelete(Integer houseId){
		ModelAndView mv = new ModelAndView();
		if(houseId!=null){
			House po = service.get(House.class, houseId);
			if(po!=null){
				po.isdel= 1;
				service.saveOrUpdate(po);
			}
		}
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView recover(Integer houseId){
		ModelAndView mv = new ModelAndView();
		if(houseId==null){
			House po = service.get(House.class, houseId);
			if(po!=null){
				po.isdel= 0;
				service.saveOrUpdate(po);
			}
		}
		mv.data.put("msg", "恢复成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView physicalDelete(Integer houseId){
		ModelAndView mv = new ModelAndView();
		//是否需要权限
		if(houseId!=null){
			House po = service.get(House.class, houseId);
			if(po!=null){
				service.delete(po);
			}
		}
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMy(HouseQuery query ,Page<House> page){
//		User user = ThreadSession.getUser();
//		if(user==null){
//			ModelAndView mv = new ModelAndView();
//			mv.data.put("msg", "用户已经掉线");
//			return mv;
//		}
//		query.userId = user.id;
		query.userId = 396;
		return listAll(query ,page);
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
		mv.data.put("shenhe", ShenHe.toJsonArray());
		mv.data.put("zhuangxiu", ZhuangXiu.toJsonArray());
		mv.data.put("kehu", KeHuXingzhi.toJsonArray());
		mv.data.put("kehulaiyuan", KeHuLaiYuan.toJsonArray());
		mv.data.put("piyue", PiYue.toJsonArray());
		mv.data.put("fukuan", FuKuan.toJsonArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView listRent(HouseQuery query ,Page<House> page){
		if(query.jiaoyis==null){
			query.jiaoyis = new ArrayList<String>();
		}
		query.jiaoyis.add(JiaoYi.仅租.toString());
		query.jiaoyis.add(JiaoYi.出租.toString());
		query.jiaoyis.add(JiaoYi.租售.toString());
		return listAll(query , page);
	}
	
	@WebMethod
	public ModelAndView listSell(HouseQuery query ,Page<House> page){
		if(query.jiaoyis==null){
			query.jiaoyis = new ArrayList<String>();
		}
		query.jiaoyis.add(JiaoYi.仅售.toString());
		query.jiaoyis.add(JiaoYi.出售.toString());
		return listAll(query , page);
	}
	
	@WebMethod
	public ModelAndView listRecycle(HouseQuery query ,Page<House> page){
		query.isdel=1;
		return listAll(query , page);
	}
	
	@WebMethod
	public ModelAndView view(int houseId){
		User user = ThreadSession.getUser();
		if(user==null){
			user = service.get(User.class, 316);
		}
		ModelAndView mv = new ModelAndView();
		House house = service.get(House.class, houseId);
		List<House> list = new ArrayList<House>();
		list.add(house);
		mv.data.put("house", JSONHelper.toJSONArray(list));
		Favorite fav = service.getUniqueByParams(Favorite.class, new String[]{"userId" , "houseId"}, new Object[]{ user.id , houseId });
		mv.data.put("fav", fav==null ? 0:1);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listAll(HouseQuery query ,Page<House> page){
		List<Object> params = new ArrayList<Object>();
		System.out.println(BeanUtil.toString(query));
		StringBuilder hql = null;
		if(StringUtils.isNotEmpty(query.xpath)){
			hql = new StringBuilder(" select h from  House h  ,User u where h.userId=u.id and u.id is not null and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			hql = new StringBuilder(" select h  from House  h where 1=1 ");
		}
		if(query.xingzhi!=null){
			hql.append(" and h.xingzhi = ? ");
			params.add(String.valueOf(query.xingzhi.getCode()));
		}
		if(StringUtils.isNotEmpty(query.area)){
			hql.append(" and h.area like ?");
			params.add("%"+query.area+"%");
		}
		if(query.id!=null){
			hql.append(" and h.id = ?");
			params.add(query.id);
		}
		
		if(query.quyus!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.quyus.size();i++){
				hql.append(" h.quyu = ? ");
				if(i<query.quyus.size()-1){
					hql.append(" or ");
				}
				params.add(query.quyus.get(i));
			}
			hql.append(" )");
		}
		
		if(query.fangxing!=null){
			hql.append(" and ").append(query.fangxing.getQueryStr());
		}
		
		if(query.jiaoyis!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.jiaoyis.size();i++){
				hql.append(" h. jiaoyi = ? ");
				if(i<query.jiaoyis.size()-1){
					hql.append(" or ");
				}
				params.add(JiaoYi.valueOf(query.jiaoyis.get(i)).getCode());
			}
			hql.append(" )");
		}
		if(query.ztai!=null){
			hql.append(" and h.ztai = ?");
			params.add(String.valueOf(query.ztai.getCode()));
		}
		if(StringUtils.isNotEmpty(query.leibie)){
			hql.append(" and h.leibie = ? ");
			params.add(query.leibie);
		}
		if(query.sjiaStart!=null){
			hql.append(" and h.sjia>= ? ");
			params.add(query.sjiaStart);
		}
		if(query.sjiaEnd!=null){
			hql.append(" and h.sjia<= ? ");
			params.add(query.sjiaEnd);
		}
		
		if(query.dateType==null){
			query.dateType = DateType.首次录入日;
		}
		hql.append(buildDateHql(query.dateType,query.dateStart,DateSeparator.After,params));
		hql.append(buildDateHql(query.dateType,query.dateEnd, DateSeparator.Before , params));
		
		if(StringUtils.isNotEmpty(query.louxing)){
			hql.append(" and h.lxing= ? ");
			params.add(query.louxing);
		}
		if(query.mianjiStart!=null){
			hql.append(" and h.mianji>= ? ");
			params.add(query.mianjiStart);
		}
		if(query.mianjiEnd!=null){
			hql.append(" and h.mianji<= ? ");
			params.add(query.mianjiEnd);
		}
		if(query.lcengStart!=null){
			hql.append(" and h.lceng>= ? ");
			params.add(query.lcengStart);
		}
		if(query.lcengEnd!=null){
			hql.append(" and h.lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if(StringUtils.isNotEmpty(query.chaoxiang)){
			hql.append(" and h.chaoxiang= ? ");
			params.add(query.chaoxiang);
		}
		if(StringUtils.isNotEmpty(query.chanquan)){
			hql.append(" and h.chanquan like ?");
			params.add("%"+query.chanquan+"%");
		}
		
		if(query.userId!=null){
			hql.append(" and h.userId= ? ");
			params.add(query.userId);
		}
		
		hql.append(" and ( isdel= 0 or isdel is null) ");
		page = service.findPage(page, hql.toString(),params.toArray());
		ModelAndView mv = new ModelAndView();
		mv.data.put("page", JSONHelper.toJSON(page,DataHelper.dateSdf.toPattern()));
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
