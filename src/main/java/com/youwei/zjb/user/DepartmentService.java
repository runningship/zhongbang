package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/dept/")
public class DepartmentService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView add(Department dept){
		if(dept.fid==null){
			dept.fid=1;
		}
		Department parent = dao.get(Department.class, dept.fid);
		dao.saveOrUpdate(dept);
		if(parent.path!=null){
			dept.path=parent.path+dept.id;
		}else{
			dept.path=String.valueOf(dept.id);
		}
		dao.saveOrUpdate(dept);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int deptId){
		ModelAndView mv = new ModelAndView();
		Department dept = dao.get(Department.class, deptId);
		mv.data.put("dept", JSONHelper.toJSON(dept));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Department dept){
		Department po = dao.get(Department.class, dept.id);
		po.namea = dept.namea;
		po.fid = dept.fid;
		Department parent = dao.get(Department.class, dept.fid);
		po.path = parent.path+po.id;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(int deptId){
		Department po = dao.get(Department.class, deptId);
		long count = dao.countHqlResult("from Department where fid=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "请先删除分公司");
		}
		count = dao.countHqlResult("from User where deptId=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "该分公司下有人员信息，请先删除公司人员信息");
		}
		dao.delete(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView listQuyu(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		String hql = "select namea as name , id as id ,fid as fid from Department where fid=1";
		page = dao.findPage(page, hql, true,new Object[]{});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listDept(Page<Map> page, DeptQuery query){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select d1.namea as quyu , d2.id as id , d2.namea as name from Department d1, Department d2 where d1.id=d2.fid and d1.id<>1 order by quyu ");
//		if(query.quyuId!=null){
//			hql.append(" and fid=? ");
//			params.add(query.quyuId);
//		}
		page = dao.findPage(page, hql.toString(), true,  params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getDeptTree(){
		ModelAndView mv = new ModelAndView();
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid "+
				"from Department child,Department parent  where child.fid = parent.id and parent.id<>1";
		List<Map> users = dao.listAsMap(hql);
		Map<Map, JSONArray> quyus = groupByQuyu(users);
		JSONArray arr = new JSONArray();
		for(Map map : quyus.keySet()){
			JSONObject key = new JSONObject();
			key.put("text", map.get("pname").toString());
			key.put("deptId",  map.get("did"));
			key.put("children", quyus.get(map));
			arr.add(key);
		}
		mv.data.put("result", arr.toString());
		return mv;
	}
	
	private Map<Map,JSONArray>  groupByQuyu(List<Map> users){
		Map<Map,JSONArray> quyus = new HashMap<Map,JSONArray>();
		
		for(Map user : users){
			if(!quyus.containsKey(user)){
				quyus.put(user, new JSONArray());
			}
			JSONArray arr = quyus.get(user);
			JSONObject dept = new JSONObject();
			dept.put("text", user.get("cname"));
			dept.put("deptId", user.get("did"));
			if(!arr.contains(dept)){
				quyus.get(user).add(dept);
			}
		}
		return quyus;
	}
}
