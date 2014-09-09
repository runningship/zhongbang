package com.youwei.zjb.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.Config;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/config/")
public class ConfigService {

	static CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	private static Map<String,List<Config>> configItems = new HashMap<String,List<Config>>();
	
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
		validate(cfg);
		dao.saveOrUpdate(cfg);
		return mv;
	}
	
	private void validate(Config cfg){
		if(StringUtils.isEmpty(cfg.name)){
			throw new GException(PlatformExceptionType.BusinessException,"请先填写区域名称");
		}
		if(StringUtils.isEmpty(cfg.pyShort)){
			throw new GException(PlatformExceptionType.BusinessException,"请先填写区域编号");
		}
		Config po = dao.getUniqueByParams(Config.class, new String[]{"type","name"},new Object[]{"quyu" , cfg.name});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"已经存在相同名称的区域");
		}
		po = dao.getUniqueByParams(Config.class, new String[]{"type","pyShort"},new Object[]{"quyu" , cfg.pyShort});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"已经存在相同编号的区域");
		}
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Config po = dao.get(Config.class, id);
		if(po!=null){
			mv.data.put("quyu", JSONHelper.toJSON(po));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteQuyu(int id){
		ModelAndView mv = new ModelAndView();
		Config po = dao.get(Config.class, id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView updateQuyu(Config cfg){
		ModelAndView mv = new ModelAndView();
		Config po = dao.get(Config.class, cfg.id);
		if(StringUtils.isEmpty(cfg.name)){
			throw new GException(PlatformExceptionType.BusinessException,"请先填写区域名称");
		}
		if(StringUtils.isEmpty(cfg.pyShort)){
			throw new GException(PlatformExceptionType.BusinessException,"请先填写区域编号");
		}
		Config tmp = dao.getUniqueByParams(Config.class, new String[]{"type","name"},new Object[]{"quyu" , cfg.name});
		if(tmp!=null && tmp.id!=cfg.id){
			throw new GException(PlatformExceptionType.BusinessException,"已经存在相同名称的区域");
		}
		tmp = dao.getUniqueByParams(Config.class, new String[]{"type","pyShort"},new Object[]{"quyu" , cfg.pyShort});
		if(tmp!=null && tmp.id!=cfg.id){
			throw new GException(PlatformExceptionType.BusinessException,"已经存在相同编号的区域");
		}
		String oldName = po.name;
		String oldPy = po.pyShort;
		if(!po.name.equals(cfg.name) || !po.pyShort.equals(cfg.pyShort)){
			po.name = cfg.name;
			po.pyShort = cfg.pyShort;
			dao.saveOrUpdate(po);
			//refresh cache
			configItems.remove("quyu");
		}
		if(!oldName.equals(cfg.name)){
			dao.execute("update District set quyu=? where quyu=?", po.name, oldName);
			dao.execute("update House set quyu =? where quyu=?", po.name,oldName);
		}
		if(!oldPy.equals(cfg.pyShort)){
			//先改区域编号，再改房源编号
			dao.execute("update House set quyuCode =? where quyuCode=?", po.pyShort,oldPy);
			dao.execute("update House set houseNumber =CONCAT(quyuCode,'-',id) where quyuCode=?", cfg.pyShort);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView reload(){
		ModelAndView mv = new ModelAndView();
		ConfigCache.reload();
		mv.returnText = "配置文件重新加载成功";
		return mv;
	}
	
	@WebMethod
	public ModelAndView listQuyu(){
		ModelAndView mv = new ModelAndView();
		configItems.remove("quyu");
		List<Config> list = getConfigList("quyu");
		
		mv.data.put("quyus", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public static List<Config> getConfigList(String type){
		if(!configItems.containsKey(type)){
			List<Config> list = dao.listByParams(Config.class, "from Config where type=?", type);
			if(list!=null){
				configItems.put(type, list);
			}
		}
		
		return configItems.get(type);
	}
}
