package com.youwei.zjb.sys;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.sys.entity.AuthCode;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/pc/")
public class PcService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(PC pc){
		ModelAndView mv = new ModelAndView();
		if(pc==null){
			return mv;
		}
		
		if(pc.deptId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "授权失败，没有店id");
			return mv;
		}
		
		if(StringUtils.isEmpty(pc.mac) && StringUtils.isEmpty(pc.cpu) && StringUtils.isEmpty(pc.harddrive) && StringUtils.isEmpty(pc.uuid)){
			mv.data.put("result", "2");
			mv.data.put("msg", "机器码为空，不能授权，可能是由于您安装的是精简版的操作系统.");
			return mv;
		}
		
		Department comp = dao.getUniqueByKeyValue(Department.class, "fid", 0);
		AuthCode code = dao.getUniqueByKeyValue(AuthCode.class, "fidn", comp.id);
		if(code==null || code.authCode==null || !code.authCode.equals(pc.authCode)){
			mv.data.put("result", "3");
			mv.data.put("msg", "授权码不正确,请联系系统管理员");
			return mv;
		}
		
		PC po = dao.getUniqueByParams(PC.class, new String[]{"deptId","mac","cpu","harddrive","uuid"},	new Object[]{pc.deptId,pc.mac,pc.cpu,pc.harddrive,pc.uuid});
		if(po==null){
			pc.addtime = new Date();
			pc.lock=0;
			dao.saveOrUpdate(pc);
		}
		
		mv.data.put("result", "0");
		mv.data.put("msg", "授权成功，等待审核");
		return mv;
	}
	
	@WebMethod
	public ModelAndView unAuthList(Page<PC> page){
		ModelAndView mv = new ModelAndView();
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC where lock=0 or lock is null");
		mv.data.put("result",JSONHelper.toJSON(page));
		return mv;
	}
	
	public Page<PC> authorizedList(Page<PC> page,int deptId){
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC where lock='1' and deptId=?",deptId);
		return page;
	}
	
	@WebMethod
	public ModelAndView unlock(Integer pcId){
		ModelAndView mv = new ModelAndView();
		PC pc = dao.get(PC.class, pcId);
		if(pc!=null){
			pc.lock=1;
			dao.saveOrUpdate(pc);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	public void lock(int pcId){
		PC pc = dao.get(PC.class, pcId);
		if(pc!=null){
			pc.lock=0;
			dao.saveOrUpdate(pc);
		}
	}

}
