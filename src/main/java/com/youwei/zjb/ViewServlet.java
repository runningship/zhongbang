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

import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.JSONHelper;


public class ViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf8");
		String path = req.getPathInfo();
		resp.setContentType(getMimeType(path));
		if(!path.endsWith(".html")){
			return;
		}
		String filePath = req.getServletContext().getRealPath("/")+path;
		String html = FileUtils.readFileToString(new File(filePath));
		Document doc = Jsoup.parse(html);
		User user = ThreadSession.getUser();
		if(user==null){
			user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, 316);
		}
		List<RoleAuthority> authList = user.getRole().Authorities();
		JSONArray arr = JSONHelper.toJSONArray(authList);
		doc.body().append("<span id='authorities' style='display:none' >"+arr.toString()+"</span>");
		
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
