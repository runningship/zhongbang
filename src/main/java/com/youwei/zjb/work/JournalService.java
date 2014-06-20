package com.youwei.zjb.work;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
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

@Module(name="/journal")
public class JournalService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Journal journal){
		ModelAndView mv = new ModelAndView();
		journal.addtime = new Date();
		journal.reply =0;
		User user = ThreadSession.getUser();
		if(user==null){
			journal.userId = 316;
		}else{
			journal.userId = user.id;
		}
		dao.saveOrUpdate(journal);
		mv.data.put("result", 0);
		mv.data.put("recordId", journal.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Journal journal = dao.get(Journal.class,id);
		List<Attachment> attachs = dao.listByParams(Attachment.class, new String[]{"bizType" , "recordId"}, new Object[]{"journal" , id});
		
//		String sql = "select j.conta as conta,j.contb as contb,j.integral as integral ,a.bizType as bizType,a.recordId as recordId,a.filename as attachment from work_journal j LEFT JOIN attachment a on j.id=a.recordId where j.id=?";
//		List<Map> list = dao.listSql(sql, id);
		mv.data.put("attachs", JSONHelper.toJSONArray(attachs));
		mv.data.put("journal", JSONHelper.toJSON(journal));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(OutQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select j.id as id,d.namea as deptName ,q.namea as quyu, u.uname as uname, j.conta as conta, "
				+ "j.addtime as addtime, j.reply as reply from Journal j , User u , Department d,Department q where j.userId=u.id and u.deptId=d.id and q.id=d.fid");
		List<Object> params = new ArrayList<Object>();
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("j.addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(query.category!=null){
			hql.append(" and j.category=? ");
			params.add(query.category);
		}
		//默认只能看到自己的数据
//		User user = ThreadSession.getUser();
//		hql.append(" and uid = ?");
//		params.add(user.id);
		
		page = dao.findPage(page, hql.toString(), true ,params.toArray());
		DataHelper.fillDefaultValue(page.getResult(), "reply", PiYue.待批阅.getCode());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Journal journal){
		ModelAndView mv = new ModelAndView();
		if(journal.id==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "id不能为空");
		}
		try{
			dao.saveOrUpdate(journal);
			mv.data.put("result", 0);
		}catch(Exception ex){
			LogUtil.log(Level.WARNING, "编辑工作日志失败", ex);
			mv.data.put("result", 1);
			mv.data.put("msg", ex.getMessage());
		}
		return mv;
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
