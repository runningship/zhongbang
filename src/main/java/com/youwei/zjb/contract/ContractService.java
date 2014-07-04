package com.youwei.zjb.contract;

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

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.contract.entity.Contract;
import com.youwei.zjb.contract.entity.ContractProcessClass;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.user.RuQiTuJin;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/contract")
public class ContractService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<Contract> page , ContractQuery query){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select c from Contract c,User u where u.id=c.userId ");
		if(query.chushou!=null){
			hql.append(" and c.flag = ? ");
			params.add(query.chushou);
		}
		if(StringUtils.isNotEmpty(query.addr)){
			hql.append(" and c.addr like ? ");
			params.add("%"+query.addr+"%");
		}
		if(StringUtils.isNotEmpty(query.bianhao)){
			hql.append(" and c.bianhao like ? ");
			params.add("%"+query.bianhao+"%");
		}
		if(StringUtils.isNotEmpty(query.name)){
			hql.append(" and (c.lxr_f like ? or c.lxr_k like ?)");
			params.add("%"+query.name+"%");
			params.add("%"+query.name+"%");
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and (c.tel_f like ? or c.tel_k like ?)");
			params.add("%"+query.tel+"%");
			params.add("%"+query.tel+"%");
		}
		if(query.zjiaStart!=null){
			hql.append(" and c.zjia>=?");
			params.add(query.zjiaStart);
		}
		if(query.zjiaEnd!=null){
			hql.append(" and c.zjia<=?");
			params.add(query.zjiaEnd);
		}
		hql.append(HqlHelper.buildDateSegment("c.signdate", query.dateStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("c.signdate", query.dateEnd, DateSeparator.Before, params));
		if(query.proClass!=null){
			hql.append(" and c.proid=?");
			params.add(query.proClass);
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView proClassList(int claid){
		ModelAndView mv = new ModelAndView();
		List<ContractProcessClass> proClassList = dao.listByParams(ContractProcessClass.class, "from ContractProcessClass where claid=? order by ordera",claid);
		mv.data.put("proClassList", JSONHelper.toJSONArray(proClassList));
		return mv;
	}
	
	@WebMethod(name="buzhou/delete")
	public ModelAndView deleteProcessClass(int pcId){
		ModelAndView mv = new ModelAndView();
		ContractProcessClass po = dao.get(ContractProcessClass.class, pcId);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "步骤已不存在");
		}
		dao.delete(po);
		return mv;
	}
	
	@WebMethod(name="buzhou/get")
	public ModelAndView getProcessClass(int pcId){
		ModelAndView mv = new ModelAndView();
		ContractProcessClass po = dao.get(ContractProcessClass.class, pcId);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "步骤已不存在");
		}
		mv.data.put("buzhou",JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod(name="buzhou/add")
	public ModelAndView addContractProcessClass(ContractProcessClass proClass){
		ModelAndView mv = new ModelAndView();
		ContractProcessClass po = dao.getUniqueByKeyValue(ContractProcessClass.class, "title", proClass.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "步骤名称重复");
		}
		po = dao.getUniqueByKeyValue(ContractProcessClass.class, "ordera", proClass.ordera);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "步骤序号重复");
		}
		dao.saveOrUpdate(proClass);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod(name="buzhou/update")
	public ModelAndView updateContractProcessClass(ContractProcessClass proClass){
		ModelAndView mv = new ModelAndView();
		if(proClass.id==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "id不能为空");
		}
		dao.saveOrUpdate(proClass);
		mv.data.put("msg", "添加成功");
		return mv;
	}
}
