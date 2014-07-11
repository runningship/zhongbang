package com.youwei.zjb.contract;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
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
		List<ContractProcess> processList = dao.listByParams(ContractProcess.class, new String[]{"contractId"}, new Object[]{ process.contractId });
		mv.data.put("actions", JSONHelper.toJSONArray(processList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView doProcess(ContractProcess vo){
		ModelAndView mv = new ModelAndView();
		ContractProcess process = dao.get(ContractProcess.class, vo.id);
		if(vo.blr==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "请选择办理人");
		}
		if(vo.endtime==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "请选择办理结束日期");
		}
		process.flag = 2;
		process.blr = vo.blr;
		process.conts = vo.conts;
		process.endtime = vo.endtime;
		dao.saveOrUpdate(process);
		List<ContractProcess> processList = dao.listByParams(ContractProcess.class, new String[]{"contractId"}, new Object[]{ process.contractId });
		mv.data.put("actions", JSONHelper.toJSONArray(processList));
		return mv;
	}
}
