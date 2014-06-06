package com.youwei.zjb.admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.admin.entity.AdminClass;
import com.youwei.zjb.admin.entity.AdminTable;
import com.youwei.zjb.admin.entity.ProcessClass;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.admin.entity.Process;

@Module(name="/admin/")
public class AdminService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="class/list")
	public ModelAndView listAdminClass(){
		ModelAndView mv = new ModelAndView();
		List<Map> list = dao.listAsMap("select id as id ,name as name from AdminClass  where fid<>0 and flag=? ",  new Object[]{1});
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public ModelAndView listTable(Integer classId){
		ModelAndView mv = new ModelAndView();
		List<Map> list = dao.listAsMap(HqlHelper.getLazyHql(AdminTable.class)+" where sh=1 and  classId=?", classId);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public ModelAndView listProcessClass(Integer classId){
		ModelAndView mv = new ModelAndView();
		List<ProcessClass> list = dao.listByParams(ProcessClass.class, "from ProcessClass where adminClassId=? order by ordera",classId);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public ModelAndView listProcess(Integer tableId){
		ModelAndView mv = new ModelAndView();
		List<Process> list = dao.listByParams(Process.class, "from Process where tableId=? order by ordera",tableId);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public void addTable(AdminTable table){
		List<ProcessClass> processClassList = dao.listByParams(ProcessClass.class, new String[]{"adminClassId"}, new Object[]{table.classId});
		table.addtime = new Date();
		table.sh=0;
		dao.saveOrUpdate(table);
		for(ProcessClass pc : processClassList){
			Process pro = new Process();
			pro.addtime = new Date();
			pro.adminClassId = table.classId;
			pro.creatorId = table.userId;
			pro.processorId = pc.userId;
			pro.name = pc.username;
			pro.ordera = pc.ordera;
			pro.tableId = table.id;
			pro.flag = 0;
			dao.saveOrUpdate(pro);
		}
	}
	
	public ModelAndView addProcessClass(ProcessClass pc){
		ModelAndView mv = new ModelAndView();
		if(pc.adminClassId==null || pc.userId==null || pc.ordera==null){
				throw new GException(PlatformExceptionType.BusinessException, 1, "参数不能为空");
		}
		ProcessClass po = dao.getUniqueByParams(ProcessClass.class, new String[]{"adminClassId","userId"}, new Object[]{pc.adminClassId , pc.userId});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "存在相同的操作步骤人");
		}
		po = dao.getUniqueByParams(ProcessClass.class, new String[]{"adminClassId","ordera"}, new Object[]{pc.adminClassId , pc.ordera});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 3, "存在相同的操作步骤序号");
		}
		dao.saveOrUpdate(pc);
		mv.data.put("result", 0);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	public ModelAndView updateTableTemplate(Integer classId,String template){
		ModelAndView mv = new ModelAndView();
		AdminClass ac = dao.get(AdminClass.class, classId);
		if(ac!=null){
			ac.template = template;
			dao.saveOrUpdate(ac);
			mv.data.put("result", 0);
			mv.data.put("msg", "编辑模板成功.");
		}else{
			throw new GException(PlatformExceptionType.BusinessException, 1, "要修改的行政类别已经不存在");
		}
		return mv;
	}
}
