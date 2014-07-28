package com.youwei.zjb.contract;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.contract.entity.Contract;
import com.youwei.zjb.contract.entity.ContractProcess;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/contract/process/")
public class ContractProcessService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView ignore(int id){
		ModelAndView mv = new ModelAndView();
		ContractProcess process = dao.get(ContractProcess.class, id);
		process.flag=2;
		dao.saveOrUpdate(process);
		//更新合同的当前步骤
		Contract contract = dao.get(Contract.class, process.contractId);
		List<ContractProcess> list = dao.listByParams(ContractProcess.class, "from ContractProcess where contractId=? and ordera>? order by ordera", process.contractId,process.ordera);
		if(!list.isEmpty()){
			contract.proid = list.get(0).buzhouId;
		}else{
			//所有步骤全部完成
			contract.proid = -1;
		}
		dao.saveOrUpdate(contract);
		List<ContractProcess> processList = dao.listByParams(ContractProcess.class,"from ContractProcess where contractId=? order by ordera ",process.contractId);
		mv.data.put("actions", JSONHelper.toJSONArray(processList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		ContractProcess process = dao.get(ContractProcess.class, id);
		mv.data.put("process", JSONHelper.toJSON(process));
		return mv;
	}
	@WebMethod
	public ModelAndView doProcess(ContractProcess vo){
		ModelAndView mv = new ModelAndView();
		ContractProcess process = dao.get(ContractProcess.class, vo.id);
		if(vo.blr==null){
			throw new GException(PlatformExceptionType.BusinessException, "请选择办理人");
		}
		if(vo.endtime==null){
			throw new GException(PlatformExceptionType.BusinessException, "请选择办理结束日期");
		}
		process.flag = 2;
		process.blr = vo.blr;
		process.conts = vo.conts;
		process.endtime = vo.endtime;
		dao.saveOrUpdate(process);
		//更新合同的当前步骤
		Contract contract = dao.get(Contract.class, process.contractId);
		List<ContractProcess> list = dao.listByParams(ContractProcess.class, "from ContractProcess where contractId=? and ordera>? order by ordera", process.contractId,process.ordera);
		if(!list.isEmpty()){
			contract.proid = list.get(0).buzhouId;
		}else{
			//所有步骤全部完成
			contract.proid = -1;
		}
		dao.saveOrUpdate(contract);
		List<ContractProcess> processList = dao.listByParams(ContractProcess.class,"from ContractProcess where contractId=? order by ordera ",process.contractId);
		mv.data.put("actions", JSONHelper.toJSONArray(processList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(ContractProcess vo){
		ModelAndView mv = new ModelAndView();
		ContractProcess po = dao.get(ContractProcess.class, vo.id);
		po.conts = vo.conts;
		po.endtime = vo.endtime;
		po.blr = vo.blr;
		dao.saveOrUpdate(po);
		return mv;
	}
}
