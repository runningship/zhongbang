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

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.entity.Attachment;

public class FileUploadServlet extends HttpServlet {

	static final int MAX_SIZE = 1024000*5;
//	static final String BaseFileDir = "F:\\temp\\upload";
	static final String BaseFileDir = ConfigCache.get("upload_dir","F:\\temp\\upload");
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
		ServletOutputStream out = null;
		
		JSONObject result = new JSONObject();
		result.put("result", 0);
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			out = response.getOutputStream();
			response.setContentType("text/plain");
			String bizType;
			Integer recordId;
			try{
				bizType = request.getParameter("bizType");
				if(StringUtils.isEmpty(bizType)){
					response.setStatus(500);
					out.write("bizType should not be empty ".getBytes());
					return;
				}
				recordId = Integer.valueOf(request.getParameter("recordId"));
			}catch(Exception ex){
				response.setStatus(500);
				out.write("recordId should be number ".getBytes());
				return;
			}
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item : items){
				if(item.isFormField()){
					continue;
				}
				if(item.getSize()<=0){
					throw new RuntimeException("no file selected.");
				}else{
					if(item.getSize()>=MAX_SIZE){
						throw new RuntimeException("file size exceed 5M");
					}else{
						FileUtils.copyInputStreamToFile(item.getInputStream(), new File(BaseFileDir
								+ File.separator + bizType + File.separator
								+ recordId + File.separator + item.getName()));
					}
				}
				Attachment attach = new Attachment();
				attach.bizType = bizType;
				attach.recordId = recordId;
				attach.filename = item.getName();
				fileService.add(attach);
			}
			result.put("msg", "success");
			out.write(result.toString().getBytes());
		} catch (Exception e) {
			result.put("result", 1);
			result.put("msg", e.getMessage());
			try {
				out.write(result.toString().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
