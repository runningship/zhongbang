package com.youwei.zjb.sys;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.admin.entity.AdminClass;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.entity.Qzy;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/sys/")
public class AuthorityService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	@Transactional
	@WebMethod
	public ModelAndView update(int roleId,String authData){
		if(StringUtils.isEmpty(authData)){
			throw new GException(PlatformExceptionType.BusinessException, "数据不能为空");
		}
		JSONArray json = JSONArray.fromObject(authData);
		dao.execute("delete from RoleAuthority where roleId=?", roleId);
		addChildren(roleId , json);
		User user = ThreadSession.getUser();
		Role role = dao.get(Role.class, roleId);
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了职务["+role.title+"]的权限";
		operService.add(OperatorType.权限记录, operConts);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView addRole(Role role){
		ModelAndView mv = new ModelAndView();
		role.flag = 1;
		role.sh = 1;
		dao.saveOrUpdate(role);
		User user = ThreadSession.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了职务["+role.title+"]";
		operService.add(OperatorType.权限记录, operConts);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView deleteRole(int roleId){
		ModelAndView mv = new ModelAndView();
		Role po = dao.get(Role.class,roleId);
		long count = dao.countHqlResult("from User where roleId=?", roleId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "请先删除该职务下的用户，该职务下目前有"+count+"个用户");
		}
		if(po!=null){
			dao.delete(po);
			dao.execute("update User u set u.roleId=? where u.roleId=?", 0,roleId);
		}
		User user = ThreadSession.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 删除了职务["+po.title+"]";
		operService.add(OperatorType.权限记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView addQzy(Qzy qzy){
		ModelAndView mv = new ModelAndView();
		Qzy po = dao.getUniqueByKeyValue(Qzy.class, "userId", qzy.userId);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "不能添加重复的签证员");
		}
		if(qzy.userId==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择签证员");
		}
		dao.saveOrUpdate(qzy);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteQzy(int id){
		ModelAndView mv = new ModelAndView();
		Qzy po = dao.get(Qzy.class, id);
		if(po!=null){
			dao.delete(po);
		}
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listQzy(){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		hql.append("select u.uname as uname,qzy.id as id ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r ,Qzy qzy where u.roleId = r.id and d.id = u.deptId and u.id=qzy.userId");
		List<Map> list = dao.listAsMap(hql.toString());
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView rolesList(Page<Role> page){
		ModelAndView mv = new ModelAndView();
		page = dao.findPage(page,  "from Role");
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getRoleMenus(int roleId){
		ModelAndView mv = new ModelAndView();
		try {
			String text = FileUtils.readFileToString(new File(ThreadSession.getHttpSession().getServletContext().getRealPath("/")+File.separator+"menus.json"), "utf8");
			JSONArray jarr = JSONArray.fromObject(text);
			jarr.add(DataHelper.getAdminClassAuth());
			List<RoleAuthority> list = dao.listByParams(RoleAuthority.class, new String[]{"roleId","type"}, new Object[]{ roleId ,"menu"});
			for(RoleAuthority ra : list){
				setSelected(ra ,jarr);
			}
			mv.data.put("result", 0);
			mv.data.put("data", jarr.toString());
			return mv;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new GException(PlatformExceptionType.BusinessException, "读取资源文件失败");
		}
	}
	
	private void setSelected(RoleAuthority ra, JSONArray jarr) {
		JSONObject state = new JSONObject();
		state.put("selected", true);
		for(int i=0;i<jarr.size();i++){
			JSONObject jobj = jarr.getJSONObject(i);
			if(jobj.getString("name").equals(ra.name)){
				jobj.put("state", state);
				return;
			}
			if(jobj.containsKey("children")){
				setSelected(ra , jobj.getJSONArray("children"));
			}
		}
	}

	private void addChildren(int roleId , JSONArray json) {
		for(int i=0;i<json.size();i++){
			RoleAuthority ra = new RoleAuthority();
			JSONObject jobj = json.getJSONObject(i);
			ra.name= jobj.getString("name");
			ra.type = jobj.getString("type");
			ra.roleId = roleId;
			dao.saveOrUpdate(ra);
			if(jobj.containsKey("children")){
				addChildren(roleId ,jobj.getJSONArray("children"));
			}
		}
	}

	@WebMethod
	public ModelAndView getRole(int roleId){
		ModelAndView mv = new ModelAndView();
		Role role = dao.get(Role.class, roleId);
		mv.data.put("role", JSONHelper.toJSON(role));
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateRole(int id,String title){
		Role role = dao.get(Role.class, id);
		if(role==null){
			throw new GException(PlatformExceptionType.BusinessException, "职务不存在");
		}
		role.title = title;
		dao.saveOrUpdate(role);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView getSubMenus(Authority authority , int userId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, userId);
		}
		if(user==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户不存在");
		}
		String hql = "from RoleAuthority where roleId=? and isMenu=1";
		List<RoleAuthority> list = dao.listByParams(RoleAuthority.class, hql, user.roleId);
		JSONArray results = new JSONArray();
		for(RoleAuthority ra : list){
			Authority au = Authority.valueOf(ra.name);
			results.add(au.toJSONObject());
		}
		mv.data.put("menus", JSONHelper.toJSONArray(results));
		return mv;
	}
	
	public void getTopMenus(){
		
	}
}
