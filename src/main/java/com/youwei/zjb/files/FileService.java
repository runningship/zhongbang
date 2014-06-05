package com.youwei.zjb.files;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.entity.Attachment;

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

	public void delete(Integer id) {
		Attachment po = dao.get(Attachment.class, id);
		if (po != null) {
			dao.delete(po);
			String filepath = FileUploadServlet.BaseFileDir + File.separator + po.bizType + File.separator + po.recordId + File.separator + po.filename;
			FileUtils.deleteQuietly(new File(filepath));
		}
	}
}
