package com.youwei.zjb;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.Handler;
import org.bc.web.ModelAndView;
import org.bc.web.ModuleManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.cache.UserSessionCache;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SessionHelper;


public class ViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf8");
		String path = req.getPathInfo();
		SessionHelper.updateSession(req);
		resp.setContentType(getMimeType(path));
		if(!path.endsWith(".html")){
			return;
		}
		User user = UserSessionCache.getUser(req.getSession().getId());
		if(user==null){
			//返回登录
//			return;
			user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, 316);
			UserSessionCache.putSession(req.getSession().getId(), user.id, "test");
		}
		String filePath = req.getServletContext().getRealPath("/")+path;
		String html = FileUtils.readFileToString(new File(filePath));
		Document doc = Jsoup.parse(html);
		List<RoleAuthority> authList = user.getRole().Authorities();
//		JSONArray arr = JSONHelper.toJSONArray(authList);
		Elements list = doc.getElementsByAttribute("auth");
		for(Element e : list){
			String target = e.attr("auth");
			boolean auth = false;
			for(RoleAuthority ra : authList){
				if(ra.name.equals(target)){
					auth = true;
					break;
				}
			}
			if(auth==false){
				e.remove();
			}
		}
		
		//process include
		list = doc.getElementsByTag("include");
		for(Element e : list){
			String src = e.attr("src");
			try{
				File f = new File(filePath);
				String str = FileUtils.readFileToString(new File(f.getParentFile().getPath()+File.separator+src));
				e.html(str);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		resp.getWriter().write(doc.html());
	}
	
	private String getMimeType(String path){
		if(path.endsWith(".css")){
			return "text/css";
		}
		if(path.endsWith(".js")){
			return "text/javascript";
		}
		if(path.endsWith(".html")){
			return "text/html";
		}
		if(path.endsWith(".jpeg")){
			return "image/jpeg";
		}
		return "";
	}
}
