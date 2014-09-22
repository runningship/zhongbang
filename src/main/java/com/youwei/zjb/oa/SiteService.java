package com.youwei.zjb.oa;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.oa.entity.SiteInfo;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa/sites/")
public class SiteService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(SiteInfo site){
		ModelAndView mv =new ModelAndView();
		dao.saveOrUpdate(site);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		SiteInfo po = dao.get(SiteInfo.class, id);
		mv.data.put("site", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(SiteInfo site){
		ModelAndView mv = new ModelAndView();
		SiteInfo po = dao.get(SiteInfo.class, site.id);
		po.conts = site.conts;
		po.ordera = site.ordera;
		po.title = site.title;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		List<SiteInfo> list = dao.listByParams(SiteInfo.class, "from SiteInfo order by ordera desc");
		mv.data.put("list", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		SiteInfo po = dao.get(SiteInfo.class, id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
}
