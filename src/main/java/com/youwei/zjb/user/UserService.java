package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
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
import com.youwei.zjb.KeyConstants;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
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
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid ,u.uname as user ,u.id as userId ,u.hunyin as hunyin "+
					"from Department child,Department parent , User u where child.fid = parent.id and child.id=u.deptId and u.flag <> 1  ";
		List<Map> users = dao.listAsMap(hql);
		Map<String, JSONArray> quyus = groupByQuyu(users);
		Map<String, JSONArray> depts = groupByDeptId(users);
		JSONArray root = merge(quyus,depts,users);
		mv.contentType="text/plain";
		mv.data.put("result", root.toString());
		return mv;
	}
	
	@WebMethod
	public ModelAndView authorities(){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		mv.data.put("authorities", JSONHelper.toJSONArray(user.getRole().Authorities()));
		return mv;
	}
	@WebMethod
	public ModelAndView allRoles(){
		ModelAndView mv = new ModelAndView();
		List<Role> roles = dao.listByParams(Role.class, "from Role");
		mv.data.put("roles", JSONHelper.toJSONArray(roles));
		mv.data.put("rqtjs", RuQiTuJin.toJsonArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(User user){
		ModelAndView mv = new ModelAndView();
		if(user.deptId==null){
			user.deptId = -1;
		}
		Department dept = dao.get(Department.class, user.deptId);
		if(dept==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "没有指定用户所属公司");
		}
		User po = dao.getUniqueByKeyValue(User.class, "sfz" , user.sfz);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "身份证号已经存在");
		}
		user.addtime = new Date();
		user.sh = 0;
		user.lock = 0;
		user.orgpath = dept.path+user.id;
		dao.saveOrUpdate(user);
		//TODO 添加审批项
		mv.data.put("msg", "添加用户成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		hql.append("select u.uname as uname,u.id as uid ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName from User  u, Department d,Role r where u.roleId = r.id and d.id = u.deptId ");
		if(StringUtils.isNotEmpty(query.name)){
			hql.append(" and u.uname like ? ");
			params.add("%"+query.name+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ?");
			params.add(query.xpath+"%");
		}
		if(query.lizhi!=null){
			hql.append(" and u.flag=?");
			params.add(query.lizhi);
		}else{
			hql.append(" and u.flag<>1 ");
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
		hql.append(" and u.sh = ? ");
		params.add(query.sh);
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
		mv.data.put("page", JSONHelper.toJSON(page));
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
		ThreadSession.getHttpServletRequest().getSession().setAttribute(KeyConstants.Session_User, user);
		return mv;
	}

	private JSONArray merge(Map<String, JSONArray> quyus ,Map<String, JSONArray> depts , List<Map> users){
		JSONArray root = new JSONArray();
		for(String key : quyus.keySet()){
			JSONArray jarr = quyus.get(key);
			for(int i=0;i<jarr.size();i++){
				JSONObject dept = jarr.getJSONObject(i);
				dept.put("children", depts.get(dept.get("name")));
			}
			JSONObject jobj = new JSONObject();
			jobj.put("name", key);
			jobj.put("deptId", getDeptName(users,key));
			jobj.put("children", jarr);
			root.add(jobj);
		}
		return root;
	}
	
	private String getDeptName(List<Map> users ,String deptName){
		if(StringUtils.isEmpty(deptName)){
			return "";
		}
		for(Map user : users){
			if(deptName.equals(user.get("pname"))){
				return user.get("qid").toString();
			}
		}
		return "";
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
