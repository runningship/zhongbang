package com.youwei.zjb.house;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.cache.ConfigService;
import com.youwei.zjb.client.FuKuan;
import com.youwei.zjb.client.KeHuLaiYuan;
import com.youwei.zjb.client.KeHuXingzhi;
import com.youwei.zjb.entity.Config;
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
import com.youwei.zjb.util.HqlHelper;
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
//			house.userId = user.id;
			house.deptId = user.deptId;
			if(house.sjia ==null){
				house.sjia=0f;
			}
			if(house.mianji!=null && house.mianji!=0){
				int jiage = (int) (house.sjia*10000/house.mianji);
				house.djia = (float) jiage;
			}
			try{
				house.fbrId = user.id;
				house.fbr = user.uname;
			}catch(Exception ex){
				LogUtil.log(Level.WARN, "业务员信息不正确,forlxr="+house.fbr, ex);
			}
			service.saveOrUpdate(house);
			Config quyu = service.getUniqueByParams(Config.class, new String[]{"type","name"},new Object[]{"quyu" , house.quyu});
			house.quyuCode = quyu.pyShort;
			house.houseNumber = quyu.pyShort+"-"+house.id;
			service.saveOrUpdate(house);
//			String py = DataHelper.toPinyin(house.quyu);
//			if(StringUtils.isNotEmpty(py) && py.length()>0){
//				house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
//				service.saveOrUpdate(house);
//			}else{
//				LogUtil.warning("生成房源编号失败,houseId="+house.id);
//			}
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
		if(house.userId==null){
			throw new GException(PlatformExceptionType.BusinessException,"请先选择业务员");
		}
		User user = ThreadSession.getUser();
		Config quyu = service.getUniqueByParams(Config.class, new String[]{"type","name"},new Object[]{"quyu" , house.quyu});
		
		House po = service.get(House.class, house.id);
		po.quyuCode = quyu.pyShort;
		po.houseNumber = quyu.pyShort+"-"+house.id;
		po.jiaoyi = house.jiaoyi;
		po.quyu = house.quyu;
		po.area = house.area;
		po.dhao = house.dhao;
		po.fhao = house.fhao;
		po.lxing = house.lxing;
		po.hxf = house.hxf;
		po.hxt = house.hxt;
		po.hxw = house.hxw;
		po.hxy = house.hxy;
		po.dateyear = house.dateyear;
		po.lceng = house.lceng;
		po.zceng = house.zceng;
		po.mianji = house.mianji;
		po.zjia = house.zjia;
		po.sjia = house.sjia;
		po.ztai = house.ztai;
		po.xingzhi = house.xingzhi;
		po.leibie = house.leibie;
		po.zhuangxiu = house.zhuangxiu;
		po.chaoxiang = house.chaoxiang;
		po.tuijie = house.tuijie;
		po.tudizheng = house.tudizheng;
		po.tel = house.tel;
		po.userId = house.userId;
		po.chanquan = house.chanquan;
		po.lxr = house.lxr;
		po.fortel = house.fortel;
		po.beizhu = house.beizhu;
		po.fordlr = house.fordlr;
		po.fordlrtel = house.fordlrtel;
		service.saveOrUpdate(po);
//		String py = DataHelper.toPinyin(house.quyu);
//		if(StringUtils.isNotEmpty(py) && py.length()>0){
//			house.houseNumber=  py.toUpperCase().charAt(0)+"-" + house.id;
//		}else{
//			LogUtil.warning("生成房源编号失败,houseId="+house.id);
//		}
		
		String gjStr = DataHelper.compareHouse(po, house);
		house.isdel = po.isdel;
		house.dateadd = po.dateadd;
//		house.userId = po.userId;
		house.deptId = po.deptId;
		if(house.mianji!=null && house.mianji!=0){
			int jiage = (int) (house.sjia*10000/house.mianji);
			house.djia = (float) jiage;
		}
//		try{
//			User ywy = service.get(User.class, Integer.valueOf(house.fbrId));
//			house.userId = ywy.id;
//			house.fbr = user.uname;
//		}catch(Exception ex){
//			LogUtil.log(Level.WARN, "业务员信息不正确,forlxr="+house.fbr, ex);
//		}
		service.saveOrUpdate(house);
		
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
		mv.data = JSONHelper.toJSON(house);
//		mv.data.put("house", JSONHelper.toJSON(house));
		User forlxr = service.get(User.class, house.fbrId);
		if(forlxr!=null){
			Department dept = service.get(Department.class, forlxr.deptId);
			Department quyu = dept.Parent();
			mv.data.put("forlxr_qid", quyu.id);
			mv.data.put("forlxr_did", dept.id);
		}
//		mv.data.getJSONObject("house").put("ywy", ywy.uname);
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
//		mv.data.put("quyu", QuYu.toJsonArray());
		mv.data.put("quyu", JSONHelper.toJSONArray(ConfigService.getConfigList("quyu")));
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
//		User fbr = service.get(User.class, house.fbrId);
		//把业务员当作发布人
		User fbr = service.get(User.class, house.userId);
		User ywy = service.get(User.class, house.userId);
		mv.data.getJSONArray("house").getJSONObject(0).put("ywy", ywy.uname);
		if(fbr!=null){
			Department dept = fbr.Department();
			Department quyu = dept.Parent();
			String fbrStr = quyu.namea+" "+dept.namea + " "+fbr.uname;
			mv.data.put("fbr", fbrStr);
//			mv.data.getJSONArray("house").getJSONObject(0).put("ywy", fbr.uname);
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
	
	@WebMethod
	public ModelAndView export(HouseQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		if("chushou".equals(query.nav)){
			if(query.jiaoyis==null){
				query.jiaoyis = new ArrayList<String>();
			}
			query.jiaoyis.add(JiaoYi.仅售.toString());
			query.jiaoyis.add(JiaoYi.出售.toString());
			query.jiaoyis.add(JiaoYi.租售.toString());
		}else if("chuzu".equals(query.nav)){
			if(query.jiaoyis==null){
				query.jiaoyis = new ArrayList<String>();
			}
			query.jiaoyis.add(JiaoYi.仅租.toString());
			query.jiaoyis.add(JiaoYi.出租.toString());
			query.jiaoyis.add(JiaoYi.租售.toString());
		}
		if("fav".equals(query.nav)){
			hql.append(" select h, u.uname as fbr,d.namea as dname from  House h ,Favorite f  ,User u,Department d where h.id=f.houseId and f.userId=? and h.userId=u.id  and u.deptId=d.id");
//			hql = new StringBuilder("select h from House h,Favorite f where h.id=f.houseId and f.userId="+ThreadSession.getUser().id);
			params.add(ThreadSession.getUser().id);
		}else{
			hql = new StringBuilder(" select h, u.uname as fbr,d.namea as dname from  House h  ,User u,Department d where h.userId=u.id  and u.deptId=d.id");
		}
		mv.isStream = true;
		mv.contentType="application/octet-stream";
		HqlHelper.buildQuery(hql,query,params);
		page.setPageSize(-1);
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
//		hql.append(" and ( isdel= 0 or isdel is null) ");
		page.mergeResult = true;
		page = service.findPage(page,hql.toString(), true, params.toArray());
		JSONObject json = JSONHelper.toJSON(page);
		JSONArray data = json.getJSONArray("data");
		try {
			 HttpServletResponse resp = ThreadSession.getHttpServletResponse();
			 resp.setContentType("application/octet-stream");
			 ServletOutputStream out = resp.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(out) ;
			resp.addHeader("Content-Disposition", "attachment;filename=" + new String("房源列表.xls".getBytes("utf-8"),"ISO-8859-1"));
			 WritableSheet sheet = workbook.createSheet("房源", 0) ;
			 sheet.addCell(new Label(0, 0, "编号"));
			 sheet.addCell(new Label(1, 0, "类别"));
			 sheet.addCell(new Label(2, 0, "区域"));
			 sheet.addCell(new Label(3, 0, "楼盘名称"));
			 sheet.addCell(new Label(4, 0, "室厅卫阳"));
			 sheet.addCell(new Label(5, 0, "楼层"));
			 sheet.addCell(new Label(6, 0, "面积"));
			 sheet.addCell(new Label(7, 0, "单价"));
			 sheet.addCell(new Label(8, 0, "总价(万)"));
			 sheet.addCell(new Label(9, 0, "装潢"));
			 sheet.addCell(new Label(10, 0, "年代"));
			 sheet.addCell(new Label(11, 0, "发布时间"));
			 sheet.addCell(new Label(12, 0, "发布人"));
			 sheet.addCell(new Label(13, 0, "性质"));
			 sheet.addCell(new Label(14, 0, "状态"));
			 sheet.addCell(new Label(15, 0, "房主号码"));
			 
			 for(int index=0;index<data.size();index++){
				 Map h = data.getJSONObject(index);
				 sheet.addCell(new Label(0, index+1, String.valueOf(h.get("houseNumber"))));
				 sheet.addCell(new Label(1, index+1, String.valueOf(h.get("leibie"))));
				 sheet.addCell(new Label(2, index+1, String.valueOf(h.get("quyu"))));
				 sheet.addCell(new Label(3, index+1, String.valueOf(h.get("area"))));
				 sheet.addCell(new Label(4, index+1, String.valueOf(h.get("hxf")+"/"+h.get("hxt")+"/"+h.get("hxw")+"/"+h.get("hxy"))));
				 sheet.addCell(new Label(5, index+1, h.get("lceng")==null? "":String.valueOf(h.get("lceng").toString())));
				 sheet.addCell(new Label(6, index+1, h.get("mianji")==null? "":String.valueOf(h.get("mianji").toString())));
				 sheet.addCell(new Label(7, index+1, h.get("djia")==null? "":String.valueOf(h.get("djia").toString())));
				 sheet.addCell(new Label(8, index+1, h.get("sjia")==null? "":String.valueOf(h.get("sjia").toString())));
				 ZhuangXiu zhuangxiu = ZhuangXiu.parse(String.valueOf(h.get("zhuangxiu")));
				 sheet.addCell(new Label(9, index+1, zhuangxiu==null? "":zhuangxiu.toString()));
				 sheet.addCell(new Label(10, index+1, h.get("dateyear")==null? "":String.valueOf(h.get("dateyear").toString())));
				 sheet.addCell(new Label(11, index+1, h.get("dateadd")==null? "":String.valueOf(h.get("dateadd").toString())));
				 sheet.addCell(new Label(12, index+1, h.get("forlxr")==null? "":String.valueOf(h.get("forlxr").toString())));
				 HouseAttribute xingzhi = HouseAttribute.parse(String.valueOf(h.get("xingzhi")));
				 sheet.addCell(new Label(13, index+1, xingzhi==null? "":xingzhi.toString()));
				 State ztai = State.parse(String.valueOf(h.get("ztai")));
				 sheet.addCell(new Label(14, index+1, ztai==null? "":ztai.toString()));
				 sheet.addCell(new Label(15, index+1,h.get("tel")==null?"": h.get("tel").toString()));
			 }
			 workbook.write();
			 workbook.close();
			 
//	        resp.addHeader("Content-Length", "" + workbook.);
//			FileInputStream ins = FileUtils.openInputStream(file);
//			IOUtils.copy(ins, out);
//			IOUtils.closeQuietly(ins);
		} catch (IOException  | WriteException e) {
			e.printStackTrace();
		}
		return mv;
	}
	@WebMethod
	public ModelAndView listAll(HouseQuery query ,Page<House> page){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select h, u.uname as ywy,d.namea as dname from  House h  ,User u,Department d where h.userId=u.id  and u.deptId=d.id");
		HqlHelper.buildQuery(hql,query,params);
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
	
}
