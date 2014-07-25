package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.cache.UserSessionCache;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/user/")
public class UserService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	/**
	 * 3级联动下拉框
	 * @return
	 */
	@WebMethod
	public ModelAndView getUserTree(){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		String code = "";
		if(user!=null){
			List<RoleAuthority> authorities = user.getRole().Authorities();
			code = user.orgpath;
			for(RoleAuthority ra : authorities){
				if("data_dept".equals(ra.name)){
					code = user.Department().path;
				}
				if("data_quyu".equals(ra.name)){
					code = String.valueOf(user.Department().getParent().path);
				}
				if("data_all".equals(ra.name)){
					code = "";
				}
			}
		}
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid ,u.uname as user ,u.id as userId "+
					"from Department child,Department parent , User u where child.fid = parent.id and child.id=u.deptId and u.sh=1 and u.flag <> 1  and u.orgpath like '"+code+"%'";
		List<Map> users = dao.listAsMap(hql);
		Map<String, JSONArray> quyus = groupByQuyu(users);
		Map<String, JSONArray> depts = groupByDeptId(users);
		JSONArray root = merge(quyus,depts,users);
//		mv.contentType="text/plain";
		mv.data.put("result", root.toString());
		return mv;
	}
	
	@WebMethod
	public ModelAndView initIndex(){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		try{
			mv.data.put("username", user.uname);
		}catch(Exception ex){
 			ex.printStackTrace();
		}
		mv.data.put("role", user.getRole().title);
		mv.data.put("userId", user.id);
		return mv;
	}
	
	/**
	 * 树状选人控件
	 * @param noticeId
	 * @return
	 */
	@WebMethod
	public ModelAndView getUserTree2(int noticeId){
		ModelAndView mv = new ModelAndView();
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid ,u.uname as user ,u.id as userId ,u.hunyin as hunyin "+
					"from Department child,Department parent , User u where child.fid = parent.id and child.id=u.deptId and u.flag <> 1  ";
		List<Map> users = dao.listAsMap(hql);
		Map<String, JSONArray> quyus = groupByQuyu(users);
		Map<String, JSONArray> depts = groupByDeptId(users);
		
		List<NoticeReceiver> receivers = dao.listByParams(NoticeReceiver.class, new String[]{"noticeId"}, new Object[]{noticeId});
		for(JSONArray arr : depts.values()){
			for(int i=0;i<arr.size();i++){
				JSONObject obj = arr.getJSONObject(i);
				for(NoticeReceiver nr : receivers){
					if(String.valueOf(nr.receiverId).equals(obj.getString("userId"))){
						System.out.println(nr.receiverId);
						JSONObject state = new JSONObject();
						state.put("selected", true);
						obj.put("state", state);
					}
				}
				
			}
		}
		JSONArray root = merge(quyus,depts,users);
//		mv.contentType="text/plain";
		mv.data.put("result", 0);
		mv.data.put("data", root.toString());
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
	public ModelAndView changePwd(String oldPwd,String newPwd, String newPwdRepeat){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(oldPwd)){
			throw new GException(PlatformExceptionType.BusinessException, "原密码不能为空");
		}
		if(StringUtils.isEmpty(newPwd)){
			throw new GException(PlatformExceptionType.BusinessException, "新密码不能为空");
		}
		if(newPwd.length()<6 || newPwd.length()>20){
			throw new GException(PlatformExceptionType.BusinessException, "密码长度应为6-20位");
		}
		if(StringUtils.isEmpty(newPwdRepeat)){
			throw new GException(PlatformExceptionType.BusinessException, "重复新密码不能为空");
		}
		if(!newPwd.equals(newPwdRepeat)){
			throw new GException(PlatformExceptionType.BusinessException, "两次输入的新密码不一样");
		}
		User po = dao.get(User.class, ThreadSession.getUser().id);
		if(!SecurityHelper.Md5(oldPwd).equals(po.pwd)){
			throw new GException(PlatformExceptionType.BusinessException, "原密码错误");
		}
		po.pwd = SecurityHelper.Md5(newPwd);
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(User user){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(user.uname)){
			throw new GException(PlatformExceptionType.BusinessException,"用户名不能为空");
		}
		if(user.deptId==null){
			user.deptId = -1;
		}
		Department dept = dao.get(Department.class, user.deptId);
		if(dept==null){
			throw new GException(PlatformExceptionType.BusinessException, "没有指定用户所属公司");
		}
		User po = dao.getUniqueByKeyValue(User.class, "sfz" , user.sfz);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,  "身份证号已经存在");
		}
		List<User> sprList = UserHelper.getUserWithAuthority("rs_rz_list");
		if(sprList==null || sprList.size()==0){
			throw new GException(PlatformExceptionType.BusinessException,  "没有用户拥有入职登记审核权限，请先在系统管理中设置入职登记审核人.或者联系系统管理员为您处理");
		}
		user.addtime = new Date();
		user.sh = 0;
		user.flag = 0;
		user.lock = 0;
		user.orgpath = dept.path+user.id;
		user.pwd = SecurityHelper.Md5(DataHelper.User_Default_Password);
		
		dao.saveOrUpdate(user);
//		 添加审批项
		for(User spr : sprList){
			RenShiReview review = new RenShiReview();
			review.category=RenShiReview.Join;
			review.sh=0;
			review.sprId = spr.id;
			review.userId = user.id;
			dao.saveOrUpdate(review);
		}
		User operUser = ThreadSession.getUser();
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 添加了用户["+user.Department().namea+"-"+user.uname+"]";
		operService.add(OperatorType.人事记录, operConts);
		mv.data.put("msg", "添加用户成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listRuZhi(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		hql.append("select review.sh as rzsh, u.uname as uname,u.id as uid ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r , RenShiReview review where u.sh=0 and u.roleId = r.id and d.id = u.deptId and u.id=review.userId and review.sprId=? and review.category='join' ");
		params.add(user.id);
		query.sh=null;
		fillQuery(query,hql,params);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listLiZhi(UserQuery query , Page<Map> page){
		query.sh=null;
		query.lizhi=1;
		return list(query,page);
	}
	
	private void fillQuery(UserQuery query,StringBuilder hql, List<Object> params){
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
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and u.tel like ? ");
			params.add("%"+query.tel+"%");
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
		if(query.sh!=null){
			hql.append(" and u.sh = ? ");
			params.add(query.sh);
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
	}
	@WebMethod
	public ModelAndView list(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		hql.append("select u.uname as uname,u.id as uid ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r where u.roleId = r.id and d.id = u.deptId ");
		fillQuery(query,hql,params);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView superLogin(User user){
		User po = dao.get(User.class, user.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户名不存在");
		}
		UserSessionCache.putSession(ThreadSession.getHttpServletRequest().getSession().getId(), user.id, "super_login");
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView login(User user,PC pc){
		ModelAndView mv = new ModelAndView();
		if(user.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户名不存在");
		}
		User po = dao.get(User.class, user.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户名不存在");
		}
		if(!po.pwd.equals(SecurityHelper.Md5(user.pwd))){
			throw new GException(PlatformExceptionType.BusinessException, "密码不正确");
		}
		if(!SecurityHelper.validate(pc)){
			LogUtil.info("未授权的机器,pc="+BeanUtil.toString(pc)+",user="+BeanUtil.toString(user));
			throw new GException(PlatformExceptionType.BusinessException, "机器未授权,请重新授权");
		}
		mv.data.put("result", "0");
		mv.data.put("msg", "登录成功");
		ThreadSession.setUser(user);
		UserSessionCache.putSession(ThreadSession.getHttpServletRequest().getSession().getId(), user.id, ThreadSession.getIp());
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 登录成功";
		operService.add(OperatorType.登录记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView logout(PC pc){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user!=null){
			UserSessionCache.removeUserSession(user.id);
		}
		return mv;
	}

	private JSONArray merge(Map<String, JSONArray> quyus ,Map<String, JSONArray> depts , List<Map> users){
		JSONArray root = new JSONArray();
		for(String key : quyus.keySet()){
			JSONArray jarr = quyus.get(key);
			for(int i=0;i<jarr.size();i++){
				JSONObject dept = jarr.getJSONObject(i);
				dept.put("children", depts.get(dept.get("text")));
//				dept.put("children", depts.get(dept.get("name")));
			}
			JSONObject jobj = new JSONObject();
			jobj.put("text", key);
//			jobj.put("name", key);
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
			dept.put("text", user.get("cname"));
//			dept.put("name", user.get("cname"));
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
			json.put("text", user.get("user"));
//			json.put("name", user.get("user"));
			json.put("userId", user.get("userId"));
			deptUsers.get(user.get("cname")).add(json);
		}
		return deptUsers;
	}
	
}
