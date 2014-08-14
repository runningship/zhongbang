package com.youwei.zjb;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.entity.User;
import com.youwei.zjb.entity.UserSession;
import com.youwei.zjb.util.SessionHelper;

public class SessionFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String path = req.getPathInfo();
		HttpSession session = req.getSession();
		if(path.equals("/user/login")){
			ThreadSession.setHttpSession(session);
			chain.doFilter(request, response);
			return;
		}
		
		if(session.isNew()){
			//has an old session id?
			String oldSessionId = getClientSid(req);
			if(StringUtils.isEmpty(oldSessionId)){
				//a fresh connection
			}else{
				// update session
				UserSession us = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(UserSession.class, "sessionId", oldSessionId);
				if(us==null){
					//have to login again.
					relogin(req,resp);
					return;
				}else{
					if(us.userId==null){
						relogin(req,resp);
						return;
					}
					User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, us.userId);
					if(user==null){
						SimpDaoTool.getGlobalCommonDaoService().delete(us);
						relogin(req,resp);
						return;
					}
					SessionHelper.initHttpSession(session,user , us);
					ThreadSession.setHttpSession(session);
				}
			}
		}else{
			if("true".equals(session.getAttribute("relogin"))){
				//重新登录操作，什么也不做
			}else{
				if(session.getAttribute("user")==null){
					relogin(req, resp);
					return;
				}
				ThreadSession.setHttpSession(session);
			}
			
		}
		chain.doFilter(request, response);
	}

	private void relogin(HttpServletRequest req , HttpServletResponse resp) throws IOException{
		HttpSession session = req.getSession();
		session.setAttribute("relogin", "true");
		resp.sendRedirect(req.getContextPath()+"/login/login.html");
	}
	
	@Override
	public void destroy() {
		
	}

	private String getClientSid(HttpServletRequest req){
		String oldSessionId = "";
		if(req.getCookies()==null){
			return "";
		}
		for(Cookie coo : req.getCookies()){
			if("JSESSIONID".equals(coo.getName())){
				oldSessionId = coo.getValue();
				break;
			}
		}
		return oldSessionId;
	}
}
