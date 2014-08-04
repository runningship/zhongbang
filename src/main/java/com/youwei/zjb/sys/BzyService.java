package com.youwei.zjb.sys;

import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.sys.entity.Bzy;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/bzy/")
public class BzyService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Bzy bzy){
		ModelAndView mv = new ModelAndView();
		Bzy po = dao.getUniqueByKeyValue(Bzy.class, "userId", bzy.userId);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "不能添加重复的办证员");
		}
		if(bzy.userId==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择签证员");
		}
		dao.saveOrUpdate(bzy);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Bzy po = dao.get(Bzy.class, id);
		if(po!=null){
			dao.delete(po);
		}
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		hql.append("select u.uname as uname,bzy.id as id ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r ,Bzy bzy where u.roleId = r.id and d.id = u.deptId and u.id=bzy.userId");
		List<Map> list = dao.listAsMap(hql.toString());
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
}
