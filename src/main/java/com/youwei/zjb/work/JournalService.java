package com.youwei.zjb.work;

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
import com.youwei.zjb.entity.Attachment;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.work.entity.Journal;
import com.youwei.zjb.work.entity.PYItem;

@Module(name="/journal")
public class JournalService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Journal journal){
		ModelAndView mv = new ModelAndView();
		if(journal.category==1){
			if(journal.qjdays==null){
				throw new GException(PlatformExceptionType.BusinessException, "请先填写请假天数");
			}
			if(journal.userId==null){
				throw new GException(PlatformExceptionType.BusinessException, "请先选择请假人");
			}
		}
		journal.addtime = new Date();
		journal.reply =0;
		User user = ThreadSession.getUser();
		if(journal.category!=1){
			journal.userId = user.id;
		}
		dao.saveOrUpdate(journal);
		//产生一个空的批阅项
		PYItem py = new PYItem();
		py.category = journal.category;
		py.conts = "";
		py.uid = user.id;
		py.uname = user.uname;
		py.addtime = new Date();
		py.bizId = journal.id;
		py.finish=0;
		dao.saveOrUpdate(py);
		mv.data.put("result", 0);
		mv.data.put("recordId", journal.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Journal journal = dao.get(Journal.class,id);
		List<Attachment> attachs = dao.listByParams(Attachment.class, new String[]{"bizType" , "recordId"}, new Object[]{"journal" , id});
		List<PYItem> pyList = dao.listByParams(PYItem.class, "from PYItem where bizId=? and category=?", id,journal.category);
		PYItem myPy = new PYItem();
		for(PYItem item : pyList){
			if(item.uid.equals(ThreadSession.getUser().id)){
				myPy = item;
				break;
			}
		}
		pyList.remove(myPy);
//		String sql = "select j.conta as conta,j.contb as contb,j.integral as integral ,a.bizType as bizType,a.recordId as recordId,a.filename as attachment from work_journal j LEFT JOIN attachment a on j.id=a.recordId where j.id=?";
//		List<Map> list = dao.listSql(sql, id);
		mv.data.put("attachs", JSONHelper.toJSONArray(attachs));
		mv.data.put("pyList", JSONHelper.toJSONArray(pyList));
		mv.data.put("journal", JSONHelper.toJSON(journal));
		mv.data.put("myPy", JSONHelper.toJSON(myPy));
		User targetUser = dao.get(User.class, journal.userId);
		mv.data.put("qid", targetUser.Department().Parent().id);
		mv.data.put("did", targetUser.Department().id);
		mv.data.put("uid", targetUser.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(OutQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select py.finish as py, j.id as id,d.namea as deptName ,q.namea as quyu, u.uname as uname,u.id as uid, j.title as title, "
				+ "j.addtime as addtime,j.starttime as starttime,j.endtime as endtime,j.qjdays as qjdays ,  j.reply as reply ,j.pingji as pingji from Journal j ,PYItem py, User u , Department d,Department q where j.userId=u.id and py.bizId=j.id and u.deptId=d.id and q.id=d.fid and py.uid=?");
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().id);
		hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeEnd, DateSeparator.Before, params));
		if(query.category!=null){
			hql.append(" and j.category=? and py.category=? ");
			params.add(query.category);
			params.add(query.category);
		}
		if(query.uid!=null){
			hql.append(" and u.id=? ");
			params.add(query.uid);
		}
		if(query.finish!=null){
			hql.append(" and py.finish=? ");
			params.add(query.finish);
		}
		if(StringUtils.isNotEmpty(query.pingji)){
			hql.append(" and j.pingji=? ");
			params.add(query.pingji);
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		//默认只能看到自己的数据
//		User user = ThreadSession.getUser();
//		hql.append(" and uid = ?");
//		params.add(user.id);
		page.orderBy = "j.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		DataHelper.escapeHtmlField(page.getResult(), "conta");
		DataHelper.fillDefaultValue(page.getResult(), "reply", PiYue.待批阅.getCode());
		if(query.category!=null && query.category==1){
			mv.data.put("page", JSONHelper.toJSON(page));
		}else{
			mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		}
		
		return mv;
	}
	
	@WebMethod
	public ModelAndView tongji(OutQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select d.namea as dname,q.namea as qname, u.uname as uname ,u.id as uid, sum(j.qjdays) as total from Journal j,User u,Department d,Department q where u.id=j.userId and u.deptId=d.id and d.fid=q.id and j.category=1");
		hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("j.starttime", query.addtimeEnd, DateSeparator.Before, params));
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}
		hql.append(" group by j.userId");
		
//		long total = dao.countHql("select count(*) from ("+hql.toString()+") xx"); 
//		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		List<Map> list = dao.listAsMap(hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Journal journal){
		ModelAndView mv = new ModelAndView();
		if(journal.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "id不能为空");
		}
		Journal po = dao.get(Journal.class, journal.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录已不存在");
		}
		po.title=journal.title;
		po.conta = journal.conta;
		po.starttime = journal.starttime;
		po.endtime = journal.endtime;
		po.qjdays = journal.qjdays;
		dao.saveOrUpdate(po);
		mv.data.put("result", 0);
		mv.data.put("recordId", po.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView piyue(int id, String contb , String pingji ){
		Journal po = dao.get(Journal.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "该记录已不存在");
		}
		po.reply=1;
		po.contb = contb;
		User user = ThreadSession.getUser();
		//批阅项
		PYItem pypo = dao.getUniqueByParams(PYItem.class, new String[]{"category" , "bizId","uid"}, new Object[]{po.category , po.id , user.id});
		if(pypo!=null){
			pypo.conts = contb;
			pypo.finish=1;
			dao.saveOrUpdate(pypo);
		}else{
			PYItem py = new PYItem();
			py.category = po.category;
			py.conts = contb;
			py.uid = user.id;
			py.uname = user.uname;
			py.addtime = new Date();
			py.bizId = po.id;
			py.finish=1;
			dao.saveOrUpdate(py);
		}
		if(pingji!=null){
			po.pingji = pingji;
		}
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Journal po = dao.get(Journal.class, id);
		if(po!=null){
			dao.delete(po);
		}
		mv.data.put("resul", 0);
		return mv;
	}
}
