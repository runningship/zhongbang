package com.youwei.zjb;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
		String clazz = path.replace("/", ".");
		clazz = clazz.replace(".html", "");
//		if(clazz.startsWith(".")){
//			clazz = StringUtils.removeStart(clazz, ".");
//		}
		
		//com.youwei.zjb.view.client.client_list
		SessionHelper.updateSession(req);
//		req.getSession().setMaxInactiveInterval(20);
		resp.setContentType(getMimeType(path));
		if(!path.endsWith(".html")){
			return;
		}
		User user = UserSessionCache.getUser(req.getSession().getId());
		if(user==null){
			//返回登录
//			return;
			resp.sendRedirect(req.getContextPath()+"/login/login.html");
			return;
		}
		String filePath = req.getServletContext().getRealPath("/")+path;
		String html = FileUtils.readFileToString(new File(filePath),"utf-8");
		html = html.replace("$${userId}", user.id.toString());
		html = html.replace("$${myName}", user.uname);
		html = html.replace("$${myTel}", user.tel==null? "": user.tel);
		Document doc = Jsoup.parse(html);
		
		clazz = "com.youwei.zjb.view"+clazz;
		String dataScope = req.getParameter("dataScope");
		try {
			Class<?> pageClass = Class.forName(clazz);
			Method init = pageClass.getDeclaredMethod("initPage", Document.class ,String.class);
			Object page = pageClass.newInstance();
			init.invoke(page, doc , dataScope);
		} catch (IllegalArgumentException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException ex) {
			LogUtil.warning("page init failed.");
		}catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		
		String authParent = req.getParameter("authParent");
		List<RoleAuthority> authList = user.getRole().Authorities();
//		JSONArray arr = JSONHelper.toJSONArray(authList);
		if(!user.isSuper){
			Elements list = doc.getElementsByAttribute("auth");
			for(Element e : list){
				String target = e.attr("auth");
				if(StringUtils.isEmpty(target)){
					continue;
				}
				if(authParent!=null){
					target = target.replace("$${authParent}", authParent);
				}
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
		}else{
			System.out.println("super admin login...");
		}
		
		
		//process include
		Elements list = doc.getElementsByTag("include");
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
