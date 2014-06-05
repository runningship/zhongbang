package com.youwei.zjb.files;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.entity.Attachment;

public class FileUploadServlet extends HttpServlet {

	static final int MAX_SIZE = 1024000;
	static final String BaseFileDir = "F:\\temp\\upload";
	String rootPath, successMessage;
	FileService fileService = new FileService();

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// get path in which to save file
		rootPath = config.getInitParameter("RootPath");
		if (rootPath == null) {
			rootPath = "/";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out;
		
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			out = response.getOutputStream();
			response.setContentType("text/plain");
			Integer bizType;
			Integer recordId;
			try{
				bizType = Integer.valueOf(request.getParameter("bizType"));
				recordId = Integer.valueOf(request.getParameter("recordId"));
			}catch(Exception ex){
				out.write("参数 bizType 或 recordId 必须为数字类型".getBytes());
				return;
			}
			List<FileItem> items = upload.parseRequest(request);
			String result = "";
			FileItem item = items.get(0);
			if(item.getSize()<=0){
				result="no file selected.";
			}else{
				if(item.getSize()>=MAX_SIZE){
					result = "file size exceed 1M";
				}else{
					FileUtils.copyInputStreamToFile(item.getInputStream(), new File(BaseFileDir
							+ File.separator + bizType + File.separator
							+ recordId + File.separator + item.getName()));
					result = "success";
				}
			}
			Attachment attach = new Attachment();
			attach.bizType = bizType;
			attach.recordId = recordId;
			attach.filename = item.getName();
			fileService.add(attach);
			out.write(result.getBytes());
		} catch (IOException | FileUploadException e) {
			e.printStackTrace();
		}
	}
}
