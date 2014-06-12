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
import org.bc.sdak.utils.BeanUtil;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/user/")
public class UserService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getUserTree(){
		ModelAndView mv = new ModelAndView();
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid ,u.uname as user ,u.id as userId "+
					"from Department child,Department parent , User u where child.fid = parent.id and child.id=u.deptId";
		List<Map> users = dao.listAsMap(hql);
		Map<String, JSONArray> quyus = groupByQuyu(users);
		Map<String, JSONArray> depts = groupByDeptId(users);
		JSONArray root = merge(quyus,depts);
		mv.contentType="text/plain";
		mv.data.put("result", root.toString());
		return mv;
	}
	
	public void add(User user){
		if(user.deptId==null){
			user.deptId = -1;
		}
		Department dept = dao.get(Department.class, user.deptId);
		if(dept==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "没有指定用户所属公司");
		}
		user.orgpath = dept.path+user.id;
		dao.saveOrUpdate(user);
	}
	
	public ModelAndView list(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		hql.append("select u.uname as uname,r.title as title ,u.tel as tel, u.gender as gender,u.address as address from User  u, Role r where u.roleId = r.id");
		if(StringUtils.isNotEmpty(query.name)){
			hql.append(" and u.uname like ? ");
			params.add("%"+query.name+"%");
		}
		if(StringUtils.isNotEmpty(query.sfz)){
			hql.append(" and u.sfz like ? ");
			params.add("%"+query.sfz+"%");
		}
		if(StringUtils.isNotEmpty(query.address)){
			hql.append(" and u.address like ? ");
			params.add("%"+query.address+"%");
		}
		if(query.gender!=null){
			hql.append(" and u.gender=?");
			params.add(query.gender);
		}
		if(query.roleId!=null){
			hql.append(" and u.roleId=?");
			params.add(query.roleId);
		}
		if(query.hunyin!=null){
			hql.append(" and u.hunyin=?");
			params.add(query.hunyin);
		}
		if(query.ageStart!=null){
			hql.append(" and u.age>=?");
			params.add(query.ageStart);
		}
		if(query.ageEnd!=null){
			hql.append(" and u.age<=?");
			params.add(query.ageEnd);
		}
		hql.append(HqlHelper.buildDateSegment("rqsj", query.rqtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("rqsj", query.rqtimeEnd, DateSeparator.Before, params));
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("users", JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(User user,PC pc){
		ModelAndView mv = new ModelAndView();
		if(user.id==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "用户名不存在");
			return mv;
		}
		User po = dao.get(User.class, user.id);
		if(po==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "用户名不存在");
			return mv;
		}
		if(!po.pwd.equals(SecurityHelper.Md5(user.pwd))){
			mv.data.put("result", "2");
			mv.data.put("msg", "密码不正确");
			return mv;
		}
		if(!SecurityHelper.validate(pc)){
			mv.data.put("result", "3");
			mv.data.put("msg", "机器未授权,请重新授权");
			LogUtil.info("未授权的机器,pc="+BeanUtil.toString(pc)+",user="+BeanUtil.toString(user));
			return mv;
		}
		mv.data.put("result", "0");
		mv.data.put("msg", "登录成功");
		return mv;
	}

	private JSONArray merge(Map<String, JSONArray> quyus ,Map<String, JSONArray> depts){
		JSONArray root = new JSONArray();
		for(String key : quyus.keySet()){
			JSONArray jarr = quyus.get(key);
			for(int i=0;i<jarr.size();i++){
				JSONObject dept = jarr.getJSONObject(i);
				dept.put("children", depts.get(dept.get("name")));
			}
			JSONObject jobj = new JSONObject();
			jobj.put("name", key);
			jobj.put("children", jarr);
			root.add(jobj);
		}
		return root;
	}
	private Map<String,JSONArray>  groupByQuyu(List<Map> users){
		Map<String,JSONArray> quyus = new HashMap<String,JSONArray>();
		for(Map user : users){
			if(!quyus.containsKey(user.get("pname"))){
				quyus.put(user.get("pname").toString(), new JSONArray());
			}
			JSONArray arr = quyus.get(user.get("pname"));
			JSONObject dept = new JSONObject();
			dept.put("name", user.get("cname"));
			dept.put("deptId", user.get("did"));
			if(!arr.contains(dept)){
				quyus.get(user.get("pname")).add(dept);
			}
		}
		return quyus;
	}
	private Map<String,JSONArray> groupByDeptId(List<Map> users){
		Map<String,JSONArray> deptUsers = new HashMap<String,JSONArray>();
		for(Map user : users){
			if(!deptUsers.containsKey(user.get("cname"))){
				deptUsers.put(user.get("cname").toString(), new JSONArray());
			}
			JSONObject json = new JSONObject();
			json.put("name", user.get("user"));
			json.put("userId", user.get("userId"));
			deptUsers.get(user.get("cname")).add(json);
		}
		return deptUsers;
	}
	
}
