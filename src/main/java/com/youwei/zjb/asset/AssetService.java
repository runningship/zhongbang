package com.youwei.zjb.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.asset.entity.Asset;
import com.youwei.zjb.util.JSONHelper;

/**
 * 资产管理
 */
@Module(name="/assets/")
public class AssetService {
	
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Asset asset){
		ModelAndView mv = new ModelAndView();
		asset.addtime = new Date();
		asset.edittime = new Date();
		dao.saveOrUpdate(asset);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int assetId){
		ModelAndView mv = new ModelAndView();
		Asset asset = dao.get(Asset.class , assetId);
		mv.data.put("asset", JSONHelper.toJSON(asset));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Asset> page, String title){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from Asset where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(title)){
			hql.append(" and name like ?");
			params.add("%"+title+"%");
		}
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Asset asset){
		ModelAndView mv = new ModelAndView();
		asset.edittime = new Date();
		dao.saveOrUpdate(asset);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		Asset asset = dao.get(Asset.class, id);
		if(asset!=null){
			dao.delete(asset);
		}
		return mv;
	}
}
