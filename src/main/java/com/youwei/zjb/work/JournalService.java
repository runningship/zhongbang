package com.youwei.zjb.work;

import java.util.Date;
import java.util.logging.Level;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;

import com.youwei.zjb.work.entity.Journal;

public class JournalService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Journal journal){
		journal.addtime = new Date();
		journal.reply =0;
		dao.saveOrUpdate(journal);
	}
	
	
	public ModelAndView update(Journal jounal){
		ModelAndView mv = new ModelAndView();
		try{
			dao.saveOrUpdate(jounal);
			mv.data.put("result", 0);
		}catch(Exception ex){
			LogUtil.log(Level.WARNING, "编辑工作日志失败", ex);
			mv.data.put("result", 1);
			mv.data.put("msg", ex.getMessage());
		}
		return mv;
	}
	
	public void delete(int id){
		Journal po = dao.get(Journal.class, id);
		if(po!=null){
			dao.delete(po);
		}
	}
}
