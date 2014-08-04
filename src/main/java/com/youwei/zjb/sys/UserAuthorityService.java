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
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.UserAuthority;

@Module(name="/userAuth/")
public class UserAuthorityService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Transactional
	@WebMethod
	public ModelAndView update(int userId,String authData){
		if(StringUtils.isEmpty(authData)){
			throw new GException(PlatformExceptionType.BusinessException, "数据不能为空");
		}
		JSONArray json = JSONArray.fromObject(authData);
		dao.execute("delete from UserAuthority where userId=?", userId);
		addChildren(userId , json);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView getUserMenus(int userId){
		ModelAndView mv = new ModelAndView();
		try {
			HttpServletRequest req = ThreadSession.getHttpServletRequest();
			String text = FileUtils.readFileToString(new File(req.getServletContext().getRealPath("/")+File.separator+"menus.json"), "utf8");
			JSONArray jarr = JSONArray.fromObject(text);
			List<UserAuthority> list = dao.listByParams(UserAuthority.class, new String[]{"userId","type"}, new Object[]{ userId ,"menu"});
			for(UserAuthority ra : list){
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
	
	private void addChildren(int userId , JSONArray json) {
		for(int i=0;i<json.size();i++){
			UserAuthority ra = new UserAuthority();
			JSONObject jobj = json.getJSONObject(i);
			ra.name= jobj.getString("name");
			ra.type = jobj.getString("type");
			ra.userId = userId;
			dao.saveOrUpdate(ra);
			if(jobj.containsKey("children")){
				addChildren(userId ,jobj.getJSONArray("children"));
			}
		}
	}
	
	private void setSelected(UserAuthority ra, JSONArray jarr) {
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
}
