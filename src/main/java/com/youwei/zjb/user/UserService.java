package com.youwei.zjb.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.BeanUtil;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.entity.PC;
import com.youwei.zjb.entity.User;
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
