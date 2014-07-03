package com.youwei.zjb;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.Handler;
import org.bc.web.ModelAndView;
import org.bc.web.ModuleManager;

import com.youwei.zjb.entity.User;


public class GrandServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf8");
		resp.setContentType("text/html");
		String path = req.getPathInfo();
		User u = (User)req.getSession().getAttribute(KeyConstants.Session_User);
		if(u!=null){
			ThreadSession.setUser(u);
		}
		ThreadSession.setHttpServletRequest(req);
		LogUtil.info(path);
		if("/".equals(path)){
			processRootRequest(req, resp);
			return;
		}
		Handler handler = ModuleManager.getHandler(path);
		if(handler==null){
			resp.getWriter().println("404 : page not found");
			return;
		}
		Object manager =null;
		try {
			manager = TransactionalServiceHelper.getTransactionalService(handler.getModuleClass());
//			manager = handler.getModuleClass().newInstance();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(manager==null){
			resp.getWriter().println("404 : page not found");
			return;
		}
        if(StringUtils.isEmpty(handler.getMethod())){
        	resp.getWriter().println("method not found");
        	return;
        }
		try{
			Object[] params = buildParamForMethod(manager,handler.getMethod(),req);
			ModelAndView mv = ServletHelper.call(manager,handler.getMethod(),params);
			if(mv.redirect!=null){
				resp.sendRedirect(mv.redirect);
			}else if(mv.jsp==null){
				if(StringUtils.isNotEmpty(mv.contentType)){
					resp.setContentType(mv.contentType);
				}
				if(StringUtils.isNotEmpty(mv.returnText)){
					resp.getWriter().write(mv.returnText);
				}else{
					resp.getWriter().write(mv.data.toString());
				}
			}else{
				ServletHelper.fillMV(req,mv);
				RequestDispatcher rd = req.getRequestDispatcher(mv.jsp);
				if(rd==null){
					resp.getWriter().println("404 : page not found");
				}else{
					rd.forward(req, resp);
				}
			}
		}catch(InvocationTargetException ex){
			JSONObject jobj = new JSONObject();
			resp.setStatus(500);
			if(! (ex.getTargetException() instanceof GException)){
				ex.getTargetException().printStackTrace();
				jobj.put("result",500);
				jobj.put("msg", ex.getTargetException().getMessage());
			}else{
				GException target = (GException) ex.getTargetException();
				jobj.put("result",target.getCode());
				jobj.put("msg", target.getMessage());
			}
			resp.getWriter().println(jobj.toString());
		}catch(GException ex){
			ex.printStackTrace();
			JSONObject jobj = new JSONObject();
			jobj.put("result",500);
			resp.setStatus(500);
			String msg = ex.getMessage();
			if(StringUtils.isEmpty(msg)){
				msg = ex.getStackTrace()[0].toString();
			}
			jobj.put("msg", msg);
			resp.getWriter().println(jobj.toString());
		} catch(Exception ex){
			ex.printStackTrace();
			ex.printStackTrace(resp.getWriter());
			//go to error page 
			LogUtil.log(Level.SEVERE,"internal server error",ex);
		} finally{
			
		}
	}

	private void processRootRequest(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		resp.getWriter().println("you are not allowed to access root url");
	}
	
	private Object[] buildParamForMethod(Object manager,String method, HttpServletRequest req){
		Object[] params = null;
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = null;
		try {
			cc = pool.getCtClass(manager.getClass().getName());
		} catch (NotFoundException e) {
			pool.appendClassPath(new ClassClassPath(manager.getClass()));
		}
		if(cc==null){
			//get again
			try {
				cc = pool.getCtClass(manager.getClass().getName());
			} catch (NotFoundException ex) {
				try {
					cc = pool.getCtClass(manager.getClass().getSuperclass().getName());
				} catch (NotFoundException e) {
					LogUtil.log(Level.WARNING, "class not found", ex);
					return new Object[]{};
				}
			}
		}
		for(CtMethod cm : cc.getDeclaredMethods()){
			if(cm.getName().equals(method)){
				LogUtil.info("start to build parameters for "+manager.getClass().getName()+"."+method);
				params = ServletHelper.buildParamters(cm , req);
			}
		}
		return params;
	}
}
