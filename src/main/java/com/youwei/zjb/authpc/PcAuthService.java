package com.youwei.zjb.authpc;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.BusinessExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.AuthCode;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.PC;

public class PcAuthService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(PC pc){
		if(pc==null){
			return;
		}
		
		if(StringUtils.isEmpty(pc.mac) && StringUtils.isEmpty(pc.cpu) && StringUtils.isEmpty(pc.harddrive) && StringUtils.isEmpty(pc.uuid)){
			throw new GException(BusinessExceptionType.MachineCodeEmpty,"机器码为空，不能授权，可能是由于您安装的是精简版的操作系统.");
		}
		
		Department comp = service.getUniqueByKeyValue(Department.class, "fid", 0);
		AuthCode code = service.getUniqueByKeyValue(AuthCode.class, "fidn", comp.id);
		if(code==null || code.authCode==null || !code.authCode.equals(pc.authCode)){
			throw new GException(BusinessExceptionType.AuthCodeError,"授权码不正确");
		}
		
		SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(pc);
	}
	
	public Page<PC> unAuthList(Page<PC> page){
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC where lock='0' ");
		return page;
	}
	
	public Page<PC> authorizedList(Page<PC> page,int deptId){
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC where lock='1' and deptId=?",deptId);
		return page;
	}
	
	public void unlock(int pcId){
		PC pc = service.get(PC.class, pcId);
		if(pc!=null){
			pc.lock="1";
			service.saveOrUpdate(pc);
		}
	}
	
	public void lock(int pcId){
		PC pc = service.get(PC.class, pcId);
		if(pc!=null){
			pc.lock="0";
			service.saveOrUpdate(pc);
		}
	}
	
	public boolean validate(PC pc){
		List<PC> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(PC.class, new String[]{"deptId"}, new Object[]{pc.deptId});
		if(list==null){
			return false;
		}
		if(hasMac(list,pc)){
			return true;
		}
		if(hasCPU(list,pc)){
			return true;
		}
		if(hasHarddrive(list,pc)){
			return true;
		}
		if(hasUUID(list,pc)){
			return true;
		}
		return false;
	}
	
	private boolean hasMac(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.mac)){
			return false;
		}
		for(PC pc : list){
			if(pc.mac!=null && pc.mac.contains(target.mac)){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasCPU(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.cpu)){
			return false;
		}
		for(PC pc : list){
			if(pc.cpu!=null && pc.mac.contains(target.cpu)){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasHarddrive(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.harddrive)){
			return false;
		}
		for(PC pc : list){
			if(pc.harddrive!=null && pc.mac.contains(target.harddrive)){
				return true;
			}
		}
		return false;
	}
	
	private boolean hasUUID(List<PC> list, PC target){
		if(StringUtils.isEmpty(target.uuid)){
			return false;
		}
		for(PC pc : list){
			if(pc.uuid!=null && pc.mac.contains(target.uuid)){
				return true;
			}
		}
		return false;
	}
}
