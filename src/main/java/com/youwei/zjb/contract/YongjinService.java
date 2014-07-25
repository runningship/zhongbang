package com.youwei.zjb.contract;

import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.contract.entity.YongJin;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/contract/yongjin/")
public class YongjinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(YongJin yongjin){
		Date now = new Date();
		yongjin.dateA = now;
		yongjin.dateB = now;
		yongjin.dateC = now;
		yongjin.dateadd = now;
		dao.saveOrUpdate(yongjin);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView update(YongJin yongjin){
		YongJin po = dao.get(YongJin.class, yongjin.id);
		yongjin.dateA = po.dateA;
		yongjin.dateB = po.dateB;
		yongjin.dateC = po.dateC;
		yongjin.dateadd = po.dateC;
		dao.saveOrUpdate(yongjin);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		YongJin po = dao.get(YongJin.class, id);
		mv.data.put("yongjin", JSONHelper.toJSON(po));
		return mv;
	}
}
