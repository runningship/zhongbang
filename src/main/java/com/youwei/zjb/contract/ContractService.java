package com.youwei.zjb.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.client.DaiKuanType;
import com.youwei.zjb.contract.entity.Contract;
import com.youwei.zjb.contract.entity.ContractProcess;
import com.youwei.zjb.contract.entity.ContractProcessClass;
import com.youwei.zjb.contract.entity.YongJin;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.HouseType;
import com.youwei.zjb.sys.entity.Qzy;
import com.youwei.zjb.user.RuQiTuJin;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/contract")
public class ContractService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView initAdd(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("yongtu", HouseType.toJsonArray());
		mv.data.put("daikuan_lx", DaiKuanType.toJsonArray());
		mv.data.put("qzy", JSONHelper.toJSONArray(dao.listAsMap("select q.userId as userId , u.uname as name from Qzy q, User u where q.userId=u.id")));
		return mv;
	}
	
	@WebMethod
	public ModelAndView initEdit(int contractId){
		ModelAndView mv = initAdd();
		Contract contract = dao.get(Contract.class, contractId);
		mv.data.put("contract", JSONHelper.toJSON(contract));
		if(contract.ywDeptId==null){
			if(contract.ywUserId!=null){
				User user = dao.get(User.class, contract.ywUserId);
				contract.ywDeptId = user.deptId;
				dao.saveOrUpdate(contract);
			}
		}
		if(contract.ywDeptId!=null){
			mv.data.getJSONObject("contract").put("quyu", dao.get(Department.class, contract.ywDeptId).getParent().id);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView view(int contractId){
		ModelAndView mv = initEdit(contractId);
		Contract contract = dao.get(Contract.class, contractId);
		List<ContractProcess> processList = dao.listByParams(ContractProcess.class, new String[]{"contractId"}, new Object[]{ contract.id });
		mv.data.put("actions", JSONHelper.toJSONArray(processList));
		//佣金收费
		List<YongJin> yongjins = dao.listByParams(YongJin.class, "from YongJin where contractId = ? and flag=1", contractId);
		mv.data.put("yongjins", JSONHelper.toJSONArray(yongjins));
		//代收费
		List<YongJin> yongjinProxys = dao.listByParams(YongJin.class, "from YongJin where contractId = ? and flag=3", contractId);
		mv.data.put("yongjinProxys", JSONHelper.toJSONArray(yongjinProxys));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Contract contract){
		ModelAndView mv = new ModelAndView();
		Contract po = dao.get(Contract.class, contract.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "合同已不存在");
		}
		contract.addtime = po.addtime;
		contract.userId = po.userId;
		contract.deptId = po.deptId;
		dao.saveOrUpdate(contract);
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView add(Contract contract){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		contract.userId = user.id;
		contract.deptId = user.deptId;
		contract.addtime = new Date();
//		contract.proid=0;
		dao.saveOrUpdate(contract);
		//添加办理步骤
		List<ContractProcessClass> cpcList = dao.listByParams(ContractProcessClass.class, "from ContractProcessClass where claid=?", contract.claid);
		for(ContractProcessClass cpc : cpcList){
			ContractProcess process = new ContractProcess();
			process.bianhao = contract.bianhao;
			process.buzhouId = cpc.id;
			process.contractId = contract.id;
			process.flag = 0;
			process.ordera = cpc.ordera;
			process.qian = cpc.isqian;
			process.title = cpc.title;
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Contract> page , ContractQuery query){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select c from Contract c where 1=1 ");
		if(query.claid!=null){
			hql.append(" and c.claid = ? ");
			params.add(query.claid);
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
		ContractProcessClass po = dao.getUniqueByParams(ContractProcessClass.class, new String[]{"title","claid"}, new Object[] {proClass.title, proClass.claid});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "步骤名称重复");
		}
		po =dao.getUniqueByParams(ContractProcessClass.class, new String[]{"ordera","claid"}, new Object[] {proClass.ordera, proClass.claid});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "步骤序号重复");
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
