package com.youwei.zjb.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.asset.entity.Asset;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

/**
 * 资产管理
 */
@Module(name="/assets/")
public class AssetService {
	
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Asset asset){
		if(asset.deptId==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择分公司");
		}
		ModelAndView mv = new ModelAndView();
		asset.addtime = new Date();
		asset.edittime = new Date();
		User user = ThreadSession.getUser();
//		asset.deptId = user.deptId;
		asset.userId = user.id;
		if(asset.zjia==null){
			asset.zjia = asset.djia*asset.count;
		}
		dao.saveOrUpdate(asset);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int assetId){
		ModelAndView mv = new ModelAndView();
		Asset asset = dao.get(Asset.class , assetId);
		Department dept = dao.get(Department.class, asset.deptId);
		mv.data= JSONHelper.toJSON(asset);
		if(dept!=null){
			mv.data.put("qid", dept.Parent().id);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page, AssetsQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder sum = new StringBuilder("select sum(a.count) as totalCount,sum(a.zjia) as totalPrice from Asset a ,Department d where d.id=a.deptId ");
		
		StringBuilder hql = new StringBuilder("select a.id as id, a.djia as djia, a.zjia as zjia ,a.count as count, a.beizhu as beizhu,a.name as name, a.addtime as edittime,"
				+ " d.namea as deptName,q.namea as quyu from Asset a,Department d, Department q where d.id=a.deptId and q.id=d.fid");
		List<Object> params = new ArrayList<Object>();
		List<Object> sumparams = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(query.title)){
			hql.append(" and a.name like ?");
			sum.append(" and a.name like ?");
			params.add("%"+query.title+"%");
			sumparams.add("%"+query.title+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and d.path like ?");
			sum.append(" and d.path like ?");
			params.add(query.xpath+"%");
			sumparams.add(query.xpath+"%");
		}
		hql.append(HqlHelper.buildDateSegment("a.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("a.addtime", query.addtimeEnd, DateSeparator.Before, params));
		
		sum.append(HqlHelper.buildDateSegment("a.addtime", query.addtimeStart, DateSeparator.After, sumparams));
		sum.append(HqlHelper.buildDateSegment("a.addtime", query.addtimeEnd, DateSeparator.Before, sumparams));
		
		
		hql.append(" order by a.addtime desc");
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		
		List<Map> result = dao.listAsMap(sum.toString(), sumparams.toArray());
		mv.data.put("totalCount", result.get(0).get("totalCount"));
		mv.data.put("totalPrice", result.get(0).get("totalPrice"));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Asset asset){
		if(asset.deptId==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择分公司");
		}
		ModelAndView mv = new ModelAndView();
		asset.edittime = new Date();
		Asset po = dao.get(Asset.class, asset.id);
		asset.addtime = po.addtime;
//		asset.deptId = po.deptId;
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
