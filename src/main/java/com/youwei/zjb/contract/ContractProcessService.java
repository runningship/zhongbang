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
import com.youwei.zjb.entity.User;
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
		User bizman = dao.get(User.class, process.blrId);
		if(bizman!=null){
			mv.data.put("bizman_qid", bizman.Department().Parent().id);
			mv.data.put("bizman_did", bizman.deptId);
		}
		mv.data.put("process", JSONHelper.toJSON(process));
		return mv;
	}
	@WebMethod
	public ModelAndView doProcess(ContractProcess vo){
		ModelAndView mv = new ModelAndView();
		ContractProcess process = dao.get(ContractProcess.class, vo.id);
		if(vo.blrId==null){
			throw new GException(PlatformExceptionType.BusinessException, "请选择办理人");
		}
		if(vo.endtime==null){
			throw new GException(PlatformExceptionType.BusinessException, "请选择办理结束日期");
		}
		User bizman = dao.get(User.class, vo.blrId);
		process.flag = 2;
		process.blrId = vo.blrId;
		process.blr = bizman.uname;
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
		User bizman = dao.get(User.class, vo.blrId);
		po.blrId = vo.blrId;
		po.blr = bizman.uname;
		dao.saveOrUpdate(po);
		return mv;
	}
}
