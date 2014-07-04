package com.youwei.zjb.admin;

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

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.admin.entity.AdminClass;
import com.youwei.zjb.admin.entity.AdminTable;
import com.youwei.zjb.admin.entity.ProcessClass;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.admin.entity.Process;
import com.youwei.zjb.entity.User;

@Module(name="/admin/")
public class AdminService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="class/list")
	public ModelAndView listAdminClass(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		page = dao.findPage(page,"select id as id ,title as title from AdminClass  where fid<>0 and flag=? ", true,  new Object[]{1});
		mv.data.put("result", 0);
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getAdminClass(int classId){
		ModelAndView mv = new ModelAndView();
		AdminClass po = dao.get(AdminClass.class, classId);
		mv.data.put("result", 0);
		mv.data.put("adminClass", JSONHelper.toJSON(po));
		User user = ThreadSession.getUser();
		if(user==null){
			user = new User();
			user.id = 316;
			user.uname = "管理员";
		}
		mv.data.put("user", JSONHelper.toJSON(user));
		return mv;
	}
	
	@WebMethod()
	public ModelAndView getTable(int tableId){
		ModelAndView mv = new ModelAndView();
		AdminTable po = dao.get(AdminTable.class, tableId);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "表格已不存在");
		}
		List<Process> processList = dao.listByParams(Process.class, "from Process where tableId=? order by ordera", tableId);
		mv.data.put("result", 0);
		mv.data.put("adminTable", JSONHelper.toJSON(po));
		
		mv.data.put("processList", JSONHelper.toJSONArray(processList));
		if(po.userId==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "未指定表格id，或表格已不存在");
		}
		User user = dao.get(User.class, po.userId);
		AdminClass cla = dao.get(AdminClass.class, po.classId);
		mv.data.put("adminClass", cla.title);
		mv.data.put("myId", po.userId);
		mv.data.put("username", user.uname);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listTable(AdminQuery query,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select t.title as title , u.uname as uname, c.title  as classTitle ,t.addtime as addtime "
				+ ",t.id as id from AdminTable t, User u, AdminClass c where t.userId=u.id and t.classId=c.id and t.sh=1 ");
		List<Object> params = new ArrayList<Object>();
		if(query.classId!=null){
			hql.append(" and t.classId=? ");
			params.add(query.classId);
		}
		if(StringUtils.isNotEmpty(query.title)){
			hql.append(" and t.title like ? ");
			params.add("%"+query.title+"%");
		}
		page= dao.findPage(page, hql.toString(), true , params.toArray());
		mv.data.put("result", 0);
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listProcessClass(Integer classId){
		ModelAndView mv = new ModelAndView();
		List<ProcessClass> list = dao.listByParams(ProcessClass.class, "from ProcessClass where adminClassId=? order by ordera",classId);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listProcess(Integer tableId){
		ModelAndView mv = new ModelAndView();
		List<Process> list = dao.listByParams(Process.class, "from Process where tableId=? order by ordera",tableId);
		mv.data.put("result", 0);
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addTable(AdminTable table){
		ModelAndView mv = new ModelAndView();
		List<ProcessClass> processClassList = dao.listByParams(ProcessClass.class, new String[]{"adminClassId"}, new Object[]{table.classId});
		table.addtime = new Date();
		table.sh=1;
		dao.saveOrUpdate(table);
		for(ProcessClass pc : processClassList){
			Process pro = new Process();
			pro.addtime = new Date();
			pro.adminClassId = table.classId;
			pro.creatorId = table.userId;
			pro.processorId = pc.userId;
			pro.uname = pc.username;
			pro.ordera = pc.ordera;
			pro.tableId = table.id;
			pro.flag = 0;
			dao.saveOrUpdate(pro);
		}
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteTable(int tableId){
		ModelAndView mv = new ModelAndView();
		AdminTable table = dao.get(AdminTable.class, tableId);
		if(table==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "未指定表格id，或表格已不存在");
		}
		dao.delete(table);
		dao.execute("delete from Process where tableId=?", tableId);
		mv.data.put("msg", "删除成功");
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteProcessClass(int pcId){
		ModelAndView mv = new ModelAndView();
		ProcessClass proClass = dao.get(ProcessClass.class, pcId);
		if(proClass==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "未指定步骤id，或步骤已不存在");
		}
		dao.delete(proClass);
//		dao.execute("delete from Process where tableId=?", tableId);
		mv.data.put("msg", "删除成功");
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateTable(AdminTable vo){
		ModelAndView mv = new ModelAndView();
		AdminTable po = dao.get(AdminTable.class, vo.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "未指定表格id，或表格已不存在");
		}
		po.title = vo.title;
		po.conts = vo.conts;
		dao.saveOrUpdate(po);
		mv.data.put("result", 0);
		mv.data.put("msg", "保存成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateProcess(int pid , String conts){
		ModelAndView mv = new ModelAndView();
		Process po = dao.get(Process.class, pid);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "未指定办理步骤或已不存在");
		}
		po.conts = conts;
		po.flag =1 ;
		dao.saveOrUpdate(po);
		mv.data.put("result", 0);
		mv.data.put("msg", "保存成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView addProcessClass(ProcessClass pc){
		ModelAndView mv = new ModelAndView();
		User user = dao.get(User.class, pc.userId);
		pc.username = user.uname;
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
	
	@WebMethod
	public ModelAndView updateTemplate(Integer adminClassId,String template){
		ModelAndView mv = new ModelAndView();
		AdminClass ac = dao.get(AdminClass.class, adminClassId);
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
