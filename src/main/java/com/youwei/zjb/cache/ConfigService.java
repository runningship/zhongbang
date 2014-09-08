package com.youwei.zjb.cache;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.entity.Config;

@Module(name="/config/")
public class ConfigService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		for(Object key : ConfigCache.keySet()){
			mv.data.put(key, ConfigCache.get(key.toString() , ""));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(Config cfg){
		ModelAndView mv = new ModelAndView();
		dao.saveOrUpdate(cfg);
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateQuyu(int id ,String py){
		ModelAndView mv = new ModelAndView();
		Config po = dao.getUnique(Config.class, id);
		po.pyShort = py;
		dao.execute("update House set houseNumber=? where quyu=?", po.name);
		return mv;
	}
	
	@WebMethod
	public ModelAndView reload(){
		ModelAndView mv = new ModelAndView();
		ConfigCache.reload();
		mv.returnText = "配置文件重新加载成功";
		return mv;
	}
}
