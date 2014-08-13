package com.youwei.zjb.house;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.entity.UserAuthority;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.PiYue;

@Module(name="/house/")
public class HouseService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	@WebMethod
	public ModelAndView add(House house){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		District area = service.getUniqueByKeyValue(District.class, "name", house.area);
		if(area==null){
			throw new GException(PlatformExceptionType.BusinessException,"楼盘在楼盘字典中不存在，您可以先在楼盘字典中添加该楼盘");
		}
		//检查，是否是重复房源.检查条件为,小区名+楼栋号+房号
		House po = service.getUniqueByParams(House.class, new String[]{"area","dhao","fhao"},new Object[]{house.area,house.dhao,house.fhao});
		if(po!=null){
			mv.data.put("msg", "同一个房源已经存在");
			mv.data.put("result", 2);
		}else{
			house.isdel = 0;
			house.dateadd = new Date();
			house.userId = user.id;
			house.deptId = user.deptId;
			if(house.sjia ==null){
				house.sjia=0f;
			}
			if(house.mianji!=null && house.mianji!=0){
				int jiage = (int) (house.sjia*10000/house.mianji);
				house.djia = (float) jiage;
			}
			service.saveOrUpdate(house);
			String py = DataHelper.toPinyin(house.quyu);
			if(StringUtils.isNotEmpty(py) && py.length()>0){
				house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
				service.saveOrUpdate(house);
			}else{
				LogUtil.warning("生成房源编号失败,houseId="+house.id);
			}
			mv.data.put("msg", "发布成功");
			mv.data.put("result", 0);
		}
		
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了房源["+house.area+"]";
		operService.add(OperatorType.房源记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(House house){
		
		ModelAndView mv = new ModelAndView();
		String py = DataHelper.toPinyin(house.quyu);
		if(StringUtils.isNotEmpty(py) && py.length()>0){
			house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
		}else{
			LogUtil.warning("生成房源编号失败,houseId="+house.id);
		}
		House po = service.get(House.class, house.id);
		String gjStr = DataHelper.compareHouse(po, house);
		house.isdel = po.isdel;
		house.dateadd = po.dateadd;
//		house.userId = po.userId;
		house.deptId = po.deptId;
		if(house.mianji!=null && house.mianji!=0){
			int jiage = (int) (house.sjia*10000/house.mianji);
			house.djia = (float) jiage;
		}
		service.saveOrUpdate(house);
		User user = ThreadSession.getUser();
		if(StringUtils.isNotEmpty(gjStr)){
		GenJin gj = new GenJin();
			gj.userId = user.id;
			gj.bianhao = house.houseNumber;
			gj.area = house.area +house.dhao+"#"+house.fhao;
			gj.addtime = new Date();
			gj.sh=1;
			gj.conts = gjStr;
			gj.hid = po.id;
			service.saveOrUpdate(gj);
		}
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了房源["+house.houseNumber+"]";
		operService.add(OperatorType.房源记录, operConts);
		mv.data.put("msg", "修改成功");
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateXingzhi(int hid, String xingzhi){
		ModelAndView mv = new ModelAndView();
		House house = service.get(House.class, hid);
		house.xingzhi = xingzhi;
		service.saveOrUpdate(house);
		User user = ThreadSession.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了房屋性质["+house.houseNumber+"]";
		operService.add(OperatorType.房源记录, operConts);
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
				User user = ThreadSession.getUser();
				po.isdel= 1;
				po.datedel = new Date();
				po.userdel = user.uname;
				service.saveOrUpdate(po);
				
				String operConts = "["+user.Department().namea+"-"+user.uname+ "] 删除了房源["+po.area+"]";
				operService.add(OperatorType.房源记录, operConts);
			}
		}
		
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView softDeleteBatch(List<Object> ids){
		List<Integer> params = new ArrayList<Integer>();
		ModelAndView mv = new ModelAndView();
		mv.data.put("result", 0);
		if(ids.isEmpty()){
			return mv;
		}
		StringBuilder hql = new StringBuilder("delete from House where id in (-1");
		for(Object id : ids){
			hql.append(",").append("?");
			params.add(Integer.valueOf(id.toString()));
		}
		hql.append(")");
		service.execute(hql.toString(), params.toArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView physicalDeleteBatch(List<Object> ids){
		List<Integer> params = new ArrayList<Integer>();
		ModelAndView mv = new ModelAndView();
		mv.data.put("result", 0);
		if(ids.isEmpty()){
			return mv;
		}
		StringBuilder hql = new StringBuilder("delete from House where id in (-1");
		StringBuilder gjHql = new StringBuilder("delete from GenJin where hid in (-1");
		for(Object id : ids){
			hql.append(",").append("?");
			gjHql.append(",").append("?");
			params.add(Integer.valueOf(id.toString()));
		}
		hql.append(")");
		gjHql.append(")");
		service.execute(hql.toString(), params.toArray());
		service.execute(gjHql.toString(), params.toArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView recover(Integer houseId){
		ModelAndView mv = new ModelAndView();
		if(houseId!=null){
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
				service.execute("delete from GenJin where hid=?", po.id);
			}
		}
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMy(HouseQuery query ,Page<House> page){
		User user = ThreadSession.getUser();
		query.userId = user.id;
		return listAll(query ,page);
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		House house = service.get(House.class, id);
		User ywy = service.get(User.class,house.userId);
		mv.data.put("house", JSONHelper.toJSON(house));
		mv.data.getJSONObject("house").put("ywy", ywy.uname);
		return mv;
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
		query.jiaoyis.add(JiaoYi.租售.toString());
		return listAll(query , page);
	}
	
	@WebMethod
	public ModelAndView listRecycle(HouseQuery query ,Page<House> page){
		query.isdel=1;
		return listAll(query , page);
	}
	
	
	@WebMethod
	public ModelAndView view(String authParent , int houseId){
		User user = ThreadSession.getUser();
		ModelAndView mv = new ModelAndView();
		House house = service.get(House.class, houseId);
		List<House> list = new ArrayList<House>();
		list.add(house);
		mv.data.put("house", JSONHelper.toJSONArray(list));
		User fbr = service.get(User.class, house.userId);
		if(fbr!=null){
			Department dept = fbr.Department();
			Department quyu = dept.Parent();
			String fbrStr = quyu.namea+" "+dept.namea + " "+fbr.uname;
			mv.data.put("fbr", fbrStr);
			mv.data.getJSONArray("house").getJSONObject(0).put("ywy", fbr.uname);
		}
		Favorite fav = service.getUniqueByParams(Favorite.class, new String[]{"userId" , "houseId"}, new Object[]{ user.id , houseId });
		mv.data.put("fav", fav==null ? 0:1);
		if(user.id.equals(house.userId)){
			mv.data.put("showTel", "true");
		}
		for(UserAuthority ua :  user.Authorities()){
			if((authParent+"_xingzhi").equals(ua.name)){
				mv.data.put("showTel", "true");
				break;
			}
		}
		return mv;
	}
	
	private void buildQuery(StringBuilder hql,HouseQuery query , List<Object> params){
		hql.append(" select h, u.uname as fbr,d.namea as dname from  House h  ,User u,Department d where h.userId=u.id  and u.deptId=d.id");
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
//		else{
//			hql.append(" select h  from House  h where 1=1 ");
//		}
		if(query.xingzhi!=null){
			hql.append(" and h.xingzhi = ? ");
			params.add(String.valueOf(query.xingzhi.getCode()));
		}
		if(StringUtils.isNotEmpty(query.area)){
			hql.append(" and h.area like ?");
			params.add("%"+query.area+"%");
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and (h.tel like ? or h.fortel like ? or h.fordlrtel like ?)");
			params.add("%"+query.tel+"%");
			params.add("%"+query.tel+"%");
			params.add("%"+query.tel+"%");
		}
		if(StringUtils.isNotEmpty(query.houseNumber)){
			hql.append(" and h.houseNumber like ?");
			params.add("%"+query.houseNumber+"%");
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
		if(query.zjiaStart!=null){
			hql.append(" and h.zjia>= ? ");
			params.add(query.zjiaStart);
		}
		if(query.zjiaEnd!=null){
			hql.append(" and h.zjia<= ? ");
			params.add(query.zjiaEnd);
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
		if(StringUtils.isNotEmpty(query.dhao)){
			hql.append(" and h.dhao like ?");
			params.add("%"+query.dhao+"%");
		}
		
		if(StringUtils.isNotEmpty(query.fhao)){
			hql.append(" and h.fhao like ?");
			params.add("%"+query.fhao+"%");
		}
		
		if(query.userId!=null){
			hql.append(" and h.userId= ? ");
			params.add(query.userId);
		}
		if(query.isdel==null){
			hql.append(" and ( isdel= 0 or isdel is null) ");
		}
		if(query.isdel!=null){
			hql.append(" and h.isdel=?");
			params.add(query.isdel);
		}
	}
	@WebMethod
	public ModelAndView export(HouseQuery query ,Page<House> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		buildQuery(hql,query,params);
		page = service.findPage(page,hql.toString(), true, params.toArray());
		JSONObject json = JSONHelper.toJSON(page);
		JSONArray data = json.getJSONArray("data");
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File("d:" + File.separator + "barcode.xls")) ;
			 WritableSheet sheet = workbook.createSheet("房源", 0) ;
			 sheet.addCell(new Label(0, 0, "编号"));
			 sheet.addCell(new Label(1, 0, "类别"));
			 sheet.addCell(new Label(2, 0, "区域"));
			 sheet.addCell(new Label(3, 0, "楼盘名称"));
			 sheet.addCell(new Label(4, 0, "室厅卫阳"));
			 sheet.addCell(new Label(5, 0, "楼层"));
			 sheet.addCell(new Label(6, 0, "面积"));
			 sheet.addCell(new Label(7, 0, "单价"));
			 sheet.addCell(new Label(8, 0, "总价"));
			 sheet.addCell(new Label(9, 0, "装潢"));
			 sheet.addCell(new Label(10, 0, "年代"));
			 sheet.addCell(new Label(11, 0, "发布时间"));
			 sheet.addCell(new Label(12, 0, "发布人"));
			 sheet.addCell(new Label(13, 0, "性质"));
			 sheet.addCell(new Label(14, 0, "状态"));
			 
//			 for(int index=0;index<list.size();index++){
//				 House h = list.get(index);
//				 sheet.addCell(new Label(0, index+1, h.houseNumber));
//				 sheet.addCell(new Label(1, index+1, h.leibie));
//				 sheet.addCell(new Label(2, index+1, h.quyu));
//				 sheet.addCell(new Label(3, index+1, h.area));
//				 sheet.addCell(new Label(4, index+1, h.hxf+"/"+h.hxt+"/"+h.hxw+"/"+h.hxy));
//				 sheet.addCell(new Label(5, index+1, h.lceng==null? "":h.lceng.toString()));
//				 sheet.addCell(new Label(6, index+1, h.mianji==null? "":h.mianji.toString()));
//				 sheet.addCell(new Label(7, index+1, h.djia==null? "":h.djia.toString()));
//				 sheet.addCell(new Label(8, index+1, h.sjia==null? "":h.sjia.toString()));
//				 sheet.addCell(new Label(9, index+1, h.zhuangxiu==null? "":h.zhuangxiu.toString()));
//				 sheet.addCell(new Label(10, index+1, h.dateyear==null? "":h.dateyear.toString()));
//				 sheet.addCell(new Label(11, index+1, h.dateadd==null? "":h.dateadd.toString()));
//				 sheet.addCell(new Label(12, index+1, h.forlxr==null? "":h.forlxr.toString()));
//				 sheet.addCell(new Label(13, index+1, h.xingzhi==null? "":h.xingzhi.toString()));
//				 sheet.addCell(new Label(14, index+1, h.ztai==null? "":h.ztai.toString()));
//			 }
			 workbook.write();
			 workbook.close();
		} catch (IOException  | WriteException e) {
			e.printStackTrace();
		}
		return mv;
	}
	@WebMethod
	public ModelAndView listAll(HouseQuery query ,Page<House> page){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		buildQuery(hql,query,params);
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
//		hql.append(" and ( isdel= 0 or isdel is null) ");
		page.mergeResult = true;
		page = service.findPage(page, hql.toString(), true , params.toArray());
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		for(UserAuthority ua :  user.Authorities()){
			if((query.authParent+"_xingzhi").equals(ua.name)){
				mv.data.put("shy", "true");
				break;
			}
		}
		mv.data.put("page", JSONHelper.toJSON(page));
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
