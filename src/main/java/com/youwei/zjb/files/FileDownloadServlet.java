package com.youwei.zjb.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileDownloadServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bizType = req.getParameter("bizType");
		String recordId = req.getParameter("recordId");
		String fileName = req.getParameter("file");
		ServletOutputStream out = resp.getOutputStream();
		File file = new File(FileUploadServlet.BaseFileDir+File.separator+bizType+File.separator+recordId+File.separator+fileName);
		if(file.exists()==false){
			out.write("file not found".getBytes());
			return;
		}
		resp.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
        resp.addHeader("Content-Length", "" + file.length());
		FileInputStream ins = FileUtils.openInputStream(file);
		IOUtils.copy(ins, out);
		IOUtils.closeQuietly(ins);
	}

}
