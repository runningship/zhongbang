package com.youwei.zjb.files;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.entity.Attachment;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/attachment")
public class FileService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);

	public void add(Attachment attach) {
		// 检查文件是否存在
		Attachment po = dao.getUniqueByParams(Attachment.class, new String[] { "bizType", "recordId", "filename" }, new Object[] { attach.bizType, attach.recordId, attach.filename });
		if (po == null) {
			attach.uploadTime = new Date();
			dao.saveOrUpdate(attach);
		}
	}

	@WebMethod
	public ModelAndView delete(Integer id) {
		ModelAndView mv = new ModelAndView();
		Attachment po = dao.get(Attachment.class, id);
		if (po != null) {
			dao.delete(po);
			String filepath = FileUploadServlet.BaseFileDir + File.separator + po.bizType + File.separator + po.recordId + File.separator + po.filename;
			FileUtils.deleteQuietly(new File(filepath));
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(String bizType,int recordId){
		ModelAndView mv = new ModelAndView();
		List<Attachment> list = dao.listByParams(Attachment.class,  new String[]{"bizType" , "recordId"}, new Object[]{bizType , recordId});
		for(Attachment attach : list){
			try {
				attach.filename_encoded = URLEncoder.encode(attach.filename, "utf8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		mv.data.put("data", JSONHelper.toJSONArray(list));
		return mv;
	}
}
