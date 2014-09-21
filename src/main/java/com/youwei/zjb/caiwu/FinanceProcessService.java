package com.youwei.zjb.caiwu;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.admin.entity.AdminClass;
import com.youwei.zjb.admin.entity.AdminTable;
import com.youwei.zjb.admin.entity.Process;
import com.youwei.zjb.admin.entity.ProcessClass;
import com.youwei.zjb.caiwu.entity.Finance;
import com.youwei.zjb.caiwu.entity.FinanceClass;
import com.youwei.zjb.caiwu.entity.FinanceProcess;
import com.youwei.zjb.caiwu.entity.FinanceProcessClass;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/caiwu/process")
public class FinanceProcessService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	//添加办理步骤
	@WebMethod
	public ModelAndView addClass(FinanceProcessClass fc){
		ModelAndView mv = new ModelAndView();
		//办理人
		User user = dao.get(User.class, fc.uid);
		fc.username = user.uname;
		
		FinanceProcessClass po = dao.getUniqueByParams(FinanceProcessClass.class, new String[]{"claid","uid"}, new Object[]{fc.claid , fc.uid});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "存在相同的操作步骤人");
		}
		po = dao.getUniqueByParams(FinanceProcessClass.class, new String[]{"claid","ordera"}, new Object[]{fc.claid , fc.ordera});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "存在相同的操作步骤序号");
		}
		dao.saveOrUpdate(fc);
		mv.data.put("result", 0);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteClass(int id){
		ModelAndView mv = new ModelAndView();
		FinanceProcessClass proClass = dao.get(FinanceProcessClass.class, id);
		if(proClass==null){
			throw new GException(PlatformExceptionType.BusinessException, "未指定步骤，或步骤已不存在");
		}
		dao.delete(proClass);
		mv.data.put("msg", "删除成功");
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listClass(Integer claid){
		//claid 财务分类id
		ModelAndView mv = new ModelAndView();
		List<FinanceProcessClass> list = dao.listByParams(FinanceProcessClass.class, "from FinanceProcessClass where claid=? order by ordera",claid);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateTemplate(Integer id,String template){
		ModelAndView mv = new ModelAndView();
		FinanceClass ac = dao.get(FinanceClass.class, id);
		if(ac!=null){
			ac.template = template;
			dao.saveOrUpdate(ac);
			mv.data.put("result", 0);
			mv.data.put("msg", "编辑模板成功.");
		}else{
			throw new GException(PlatformExceptionType.BusinessException, "要修改的财务类别已经不存在");
		}
		return mv;
	}
	
	//审核
	@WebMethod
	public ModelAndView shenhe(int id , String conts){
		ModelAndView mv = new ModelAndView();
		FinanceProcess po = dao.get(FinanceProcess.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "未指定办理步骤或已不存在");
		}
		po.conts = conts;
		po.flag =1 ;
		dao.saveOrUpdate(po);
		//如果所有步骤都完成，则更新主表，审核通过
		long count = dao.countHqlResult("from FinanceProcess where financeId=? and  flag=0 ", po.financeId);
		if(count==0){
			Finance table = dao.get(Finance.class, po.financeId);
			table.sh=1;
			dao.saveOrUpdate(table);
		}
		mv.data.put("result", 0);
		mv.data.put("msg", "保存成功");
		return mv;
	}
}
