package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Client;
import com.youwei.zjb.entity.ClientGenJin;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.JiaoYi;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/client/")
public class ClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	
	@WebMethod
	public ModelAndView add(Client client){
		ModelAndView mv = new ModelAndView();
		Client po = dao.getUniqueByKeyValue(Client.class, "tel", client.tel);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "重复的电话号码，重复添加客源量吗?");
		}
		User user = ThreadSession.getUser();
		client.addtime = new Date();
		client.djr = user.id;
		dao.saveOrUpdate(client);
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了编号为["+client.id+"]客源["+client.lxr+"]";
		operService.add(OperatorType.客源记录, operConts);
		return mv;
	}
	
	@WebMethod(name="genjin/add")
	public ModelAndView addGenJin(ClientGenJin genjin){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class,genjin.clientId);
		if(client==null){
			throw new GException(PlatformExceptionType.BusinessException, "客源已经被删除或不存在");
		}
		genjin.addtime = new Date();
		genjin.userId = ThreadSession.getUser().id;
		dao.saveOrUpdate(genjin);
		client.gjtime = new Date();
		dao.saveOrUpdate(client);
		return mv;
	}
	
	@WebMethod(name="genjin/list")
	public ModelAndView listGenJin(int clientId){
		ModelAndView mv = new ModelAndView();
		String hql = "select g.conts as conts , u.uname as uname, g.addtime as addtime from ClientGenJin g ,User u where g.clientId=? and  u.id=g.userId order by g.addtime desc";
		List<Map> list = dao.listAsMap(hql,clientId);
		mv.data.put("genjins", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int clientId){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, clientId);
		User salesman = dao.get(User.class, client.salesman);
		mv.data.put("client", JSONHelper.toJSON(client));
		if(salesman!=null){
			mv.data.getJSONObject("client").put("salesmanDeptId",salesman.deptId);
			mv.data.getJSONObject("client").put("salesmanName",salesman.uname);
			mv.data.getJSONObject("client").put("salesman",salesman.id);
			mv.data.getJSONObject("client").put("salesmanQuyu",salesman.Department().Parent().id);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Client client){
		ModelAndView mv = new ModelAndView();
		Client po = dao.get(Client.class, client.id);
		client.addtime = po.addtime;
//		client.valid = po.valid;
		client.chuzu = po.chuzu;
		client.djr = po.djr;
		client.salesman = po.salesman;
		dao.saveOrUpdate(client);
		User user = ThreadSession.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了编号为["+client.id+"]客源["+client.lxr+"]";
		operService.add(OperatorType.客源记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, id);
		if(client!=null){
			dao.delete(client);
			User user = ThreadSession.getUser();
			String operConts = "["+user.Department().namea+"-"+user.uname+ "] 删除了编号为["+client.id+"]客源["+client.lxr+"]";
			operService.add(OperatorType.客源记录, operConts);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView matchHouse(int clientId,int chuzu , Page<House> page){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, clientId);
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from House where  isdel=0 ");
		if(StringUtils.isNotEmpty(client.area)){
			String[] areas = client.area.split(",");
			hql.append(" and (");
			for(int i=0;i<areas.length;i++){
				hql.append("area like ? ");
				if(i<areas.length-1){
					hql.append(" or ");
				}
				params.add("%"+areas[i]+"%");
			}
			hql.append(")");
		}
		if(client.mianjiFrom!=null){
			hql.append(" and mianji>=?");
			params.add(client.mianjiFrom);
		}
		if(client.mianjiTo!=null){
			hql.append(" and mianji<=?");
			params.add(client.mianjiTo);
		}
		if(client.jiageFrom!=null){
			hql.append(" and sjia>=?");
			params.add(client.jiageFrom);
		}
		if(client.jiageTo!=null){
			hql.append(" and sjia<=?");
			params.add(client.jiageTo);
		}
		HouseQuery query = new HouseQuery();
		query.jiaoyis = new ArrayList<String>();
		if(chuzu==0){
			query.jiaoyis.add(JiaoYi.仅售.toString());
			query.jiaoyis.add(JiaoYi.出售.toString());
			query.jiaoyis.add(JiaoYi.租售.toString());
		}else{
			query.jiaoyis.add(JiaoYi.仅租.toString());
			query.jiaoyis.add(JiaoYi.出租.toString());
			query.jiaoyis.add(JiaoYi.租售.toString());
		}
		if(query.jiaoyis!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.jiaoyis.size();i++){
				hql.append(" jiaoyi = ? ");
				if(i<query.jiaoyis.size()-1){
					hql.append(" or ");
				}
				params.add(JiaoYi.valueOf(query.jiaoyis.get(i)).getCode());
			}
			hql.append(" )");
		}
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView assign(Integer clientId,Integer userId){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, clientId);
		if(client==null){
			throw new GException(PlatformExceptionType.BusinessException, "客户不能为空");
		}
		if(userId==null){
			throw new GException(PlatformExceptionType.BusinessException,"业务员不能为空");
		}
		client.salesman = userId;
		dao.saveOrUpdate(client);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(ClientQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select c.salesman as salesman, c.id as id,c.lxr as lxr,c.tel as tel, c.mianjiFrom as mianjiFrom ,c.mianjiTo as mianjiTo ,c.jiageFrom as jiageFrom,"
				+ "c.jiageTo as jiageTo,c.loucengFrom as loucengFrom,c.loucengTo as loucengTo,c.addtime as addtime,c.gjtime as gjtime,c.zhongyaos as kehu ,u.uname as uname "
				+ "from Client c,User u where u.id=c.salesman and u.id is not null ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			
		}
		if(query.chuzu!=null){
			hql.append(" and c.chuzu=? ");
			params.add(query.chuzu);
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and c.tel like ?");
			params.add("%"+query.tel+"%");
		}
		if(query.youxiao!=null){
			hql.append(" and c.valid=? ");
			params.add(query.youxiao);
		}
		if(StringUtils.isNotEmpty(query.quyus)){
			String[] quyus = query.quyus.split(",");
			if(quyus.length>0){
				hql.append(" and (1=0 ");
				for(String quyu : query.quyus.split(",")){
					hql.append(" or c.quyuas like ? ");
					params.add("%"+quyu+"%");
				}
				hql.append(")");
			}
		}
		if(StringUtils.isNotEmpty(query.kehulaiyuan)){
			hql.append(" and c.source like ?");
			params.add("%"+query.kehulaiyuan+"%");
		}
		
		if(query.kehuxingzhi!=null){
			hql.append(" and c.zhongyaos = ?");
			params.add(String.valueOf(query.kehuxingzhi.getCode()));
		}
		
		hql.append(HqlHelper.buildDateSegment("c.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("c.addtime", query.addtimeEnd, DateSeparator.Before, params));
//		if(StringUtils.isNotEmpty(query.orderBy)){
//			hql.append(" order by ").append(query.orderBy);
//		}
//		if(StringUtils.isNotEmpty(query.order)){
//			hql.append(query.order);
//		}
		page.orderBy="addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteBatch(List<Object> ids){
		List<Integer> params = new ArrayList<Integer>();
		ModelAndView mv = new ModelAndView();
		mv.data.put("result", 0);
		if(ids.isEmpty()){
			return mv;
		}
		StringBuilder hql = new StringBuilder("delete from Client where id in (-1");
		for(Object id : ids){
			hql.append(",").append("?");
			params.add(Integer.valueOf(id.toString()));
		}
		hql.append(")");
		dao.execute(hql.toString(), params.toArray());
		return mv;
	}
}
