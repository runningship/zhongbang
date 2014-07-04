package com.youwei.zjb.sys;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.RuQiTuJin;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/sys/")
public class AuthorityService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Transactional
	@WebMethod
	public ModelAndView update(int roleId,String authData){
		if(StringUtils.isEmpty(authData)){
			throw new GException(PlatformExceptionType.BusinessException, 1, "数据不能为空");
		}
		JSONArray json = JSONArray.fromObject(authData);
		dao.execute("delete from RoleAuthority where roleId=?", roleId);
		addChildren(roleId , json);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView addRole(Role role){
		ModelAndView mv = new ModelAndView();
		role.flag = 1;
		role.sh = 1;
		dao.saveOrUpdate(role);
		mv.data.put("msg", "添加成功");
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
			HttpServletRequest req = ThreadSession.getHttpServletRequest();
			String text = FileUtils.readFileToString(new File(req.getServletContext().getRealPath("/")+File.separator+"menus.json"), "utf8");
			JSONArray jarr = JSONArray.fromObject(text);
			List<RoleAuthority> list = dao.listByParams(RoleAuthority.class, new String[]{"roleId","type"}, new Object[]{ roleId ,"menu"});
			for(RoleAuthority ra : list){
				setSelected(ra ,jarr);
			}
			mv.data.put("result", 0);
			mv.data.put("data", jarr.toString());
			return mv;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new GException(PlatformExceptionType.BusinessException, 1, "读取资源文件失败");
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

	public void updateRole(int userId ,int roleId){
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, userId);
		}
		if(user==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "用户不存在");
		}
		user.roleId = roleId;
		dao.saveOrUpdate(user);
	}
	
	@WebMethod
	public ModelAndView getSubMenus(Authority authority , int userId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, userId);
		}
		if(user==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "用户不存在");
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
