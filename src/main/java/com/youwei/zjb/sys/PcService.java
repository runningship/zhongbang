package com.youwei.zjb.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.entity.AuthCode;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/pc/")
public class PcService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
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
		
//		if(StringUtils.isEmpty(pc.baseboard) && StringUtils.isEmpty(pc.cpu) && StringUtils.isEmpty(pc.harddrive) && StringUtils.isEmpty(pc.bios)){
//			mv.data.put("result", "2");
//			mv.data.put("msg", "机器码为空，不能授权，可能是由于您安装的是精简版的操作系统.请运行安装目录下的 修复.bat 文件尝试修复");
//			return mv;
//		}
		
		Department comp = dao.getUniqueByKeyValue(Department.class, "fid", 0);
		AuthCode code = dao.getUniqueByKeyValue(AuthCode.class, "fidn", comp.id);
		if(code==null || code.authCode==null || !code.authCode.equals(pc.authCode)){
			mv.data.put("result", "3");
			mv.data.put("msg", "授权码不正确,请联系系统管理员");
			return mv;
		}
		
//		PC po = dao.getUniqueByParams(PC.class, new String[]{"deptId","baseboard","cpu","harddrive","bios"},	new Object[]{pc.deptId,pc.baseboard,pc.cpu,pc.harddrive,pc.bios});
		PC po = dao.getUniqueByParams(PC.class, new String[]{"deptId","uuid" , "ctime"},	new Object[]{pc.deptId , pc.uuid , pc.ctime});
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
		mv.data.put("page",JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page,PCQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select pc.id as id, pc.pcname as pcname, pc.addtime as addtime, pc.bios as bios, pc.cpu as cpu, pc.harddrive as harddrive, pc.baseboard as baseboard,"
				+ " pc.lock as lock, pc.beizhu as beizhu,d.path as xpath,d.namea as deptName,q.namea as quyu from PC pc, Department d , Department q  where pc.deptId=d.id and d.fid=q.id");
		List<Object> params = new ArrayList<Object>();
//		if(query.deptId!=null){
//			hql.append(" and deptId=? ");
//			params.add(query.deptId);
//		}
		if(query.lock!=null){
			hql.append(" and pc.lock=? ");
			params.add(query.lock);
		}
		if(query.id!=null){
			hql.append(" and pc.id=? ");
			params.add(query.id);
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and d.path like ?");
			params.add(query.xpath+"%");
		}
		page.orderBy = "pc.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView shenhe(Integer pcId){
		ModelAndView mv = new ModelAndView();
		PC pc = dao.get(PC.class, pcId);
		String operConts ="";
		User user = ThreadSession.getUser();
		if(pc!=null){
			if(pc.lock==1){
				pc.lock=0;
				operConts = "["+user.Department().namea+"-"+user.uname+ "] 取消了机器的授权 "+pc.id;
			}else{
				pc.lock = 1;
				operConts = "["+user.Department().namea+"-"+user.uname+ "] 授权了机器"+pc.id;
			}
			dao.saveOrUpdate(pc);
		}
		
		operService.add(OperatorType.授权记录, operConts);
		mv.data.put("result", 0);
		return mv;
	}
}
