package com.youwei.zjb;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		String path = req.getServletPath();
//		
//		String fullPath = req.getServletContext().getRealPath(path);
//		System.out.println("realpath is "+fullPath);
//		File file = new File(fullPath);
//		String lastTime = new Date(file.lastModified()).toGMTString();
//		try{
//		String sinceTime = new Date(req.getHeader("If-Modified-Since")).toGMTString();
//		if(lastTime.equals(sinceTime)){
//			resp.setStatus(304);
//			super.service(req, resp);
//			return;
//		}
//		}catch(Exception ex){
//		 
//		}
//		resp.setDateHeader("Last-Modified",file.lastModified()); 
//		resp.setDateHeader("Expires",file.lastModified());
	}

//	@Override
//	protected long getLastModified(HttpServletRequest req) {
//		String path = req.getServletPath();
//		String fullPath = req.getServletContext().getRealPath(path);
//		System.out.println("realpath is "+fullPath);
//		File file = new File(fullPath);
//		return file.lastModified();
//	}
	
	
}
