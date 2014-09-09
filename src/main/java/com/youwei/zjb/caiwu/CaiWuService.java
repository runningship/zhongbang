package com.youwei.zjb.caiwu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.caiwu.entity.Finance;
import com.youwei.zjb.caiwu.entity.FinanceClass;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeClass;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/caiwu/")
public class CaiWuService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView addFenLei(FinanceClass fc){
		ModelAndView mv =new ModelAndView();
		dao.saveOrUpdate(fc);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView getFenLei(int id){
		ModelAndView mv = new ModelAndView();
		FinanceClass po = dao.get(FinanceClass.class, id);
		mv.data.put("fenlei", JSONHelper.toJSON(po));
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Finance po = dao.get(Finance.class, id);
		mv.data.put("finance", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Finance finance){
		ModelAndView mv = new ModelAndView();
		Finance po = dao.get(Finance.class, finance.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"财务信息已经不存在");
		}
		po.title = finance.title;
		po.conts = finance.conts;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(Finance finance){
		ModelAndView mv = new ModelAndView();
		Finance po = dao.getUniqueByKeyValue(Finance.class, "title", finance.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"该标题已存在");
		}
		User user = ThreadSession.getUser();
		finance.userId= user.id;
		finance.addtime = new Date();
		finance.sh=0;
		dao.saveOrUpdate(finance);
		mv.data.put("recordId", finance.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listFenLei(){
		ModelAndView mv = new ModelAndView();
		List<NoticeClass> list = dao.listByParams(NoticeClass.class, "from FinanceClass order by id desc");
		mv.data.put("list", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(FinanceQuery query, Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select n.id as id, n.title as title, n.addtime as addtime, nc.fenlei as classTitle, u.uname as uname,d.namea as dname from Finance n, FinanceClass nc , User u , Department d where n.claid=nc.id and u.id=n.userId and u.deptId=d.id");
		if(query.claid!=null){
			hql.append("  and n.claid=? ");
			params.add(query.claid);
		}
		hql.append(HqlHelper.buildDateSegment("n.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("n.addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		if(StringUtils.isNotEmpty(query.title)){
			hql.append(" and n.title like ? ");
			params.add("%"+query.title+"%");
		}
		page.orderBy = "n.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page , hql.toString() , true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}

	
	@WebMethod
	public ModelAndView deleteFenLei(int id){
		ModelAndView mv = new ModelAndView();
		FinanceClass po = dao.get(FinanceClass.class, id);
		long count = dao.countHqlResult("from Notice where claid=?", id);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException,"该分类下有财务信息，请先删除分类下的财务信息。");
		}
		if(po!=null){
			dao.delete(po);
		}
		//是否删除分类对应的数据
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Finance po = dao.get(Finance.class, id);
		if(po!=null){
			dao.delete(po);
		}
		dao.execute("delete from Attachment where bizType = 'caiwu' and recordId=? ", id);
		mv.data.put("msg", "删除成功");
		return mv;
	}
}
