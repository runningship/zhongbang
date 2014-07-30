package com.youwei.zjb.oa;

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

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Attachment;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeClass;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa/")
public class OAService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView addFenLei(NoticeClass nc){
		ModelAndView mv =new ModelAndView();
		dao.saveOrUpdate(nc);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView getFenLei(int id){
		ModelAndView mv = new ModelAndView();
		NoticeClass po = dao.get(NoticeClass.class, id);
		mv.data.put("fenlei", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod(name="desk/init")
	public ModelAndView deskInit(){
		ModelAndView mv = new ModelAndView();
		List<NoticeClass> fenLeiList = dao.listByParams(NoticeClass.class, "from NoticeClass");
		mv.data.put("fenlei", JSONHelper.toJSONArray(fenLeiList));
		User user = ThreadSession.getUser();
		Page<Map> page = new Page<Map>();
		page.setPageSize(5);
		for(NoticeClass fenlei: fenLeiList){
			StringBuilder hql = new StringBuilder("select  n.id as noticeId, n.title as title, n.addtime as addtime, nc.fenlei as classTitle, u.uname as uname from Notice n, NoticeReceiver nr , NoticeClass nc , User u where n.id=nr.noticeId and n.claid=nc.id and u.id=n.userId and n.claid=? and nr.receiverId=? order by n.addtime desc");
			Page<Map> noticeList = dao.findPage(page, hql.toString(),true, new Object[]{fenlei.id , user.id});
			mv.data.put(fenlei.fenlei, JSONHelper.toJSONArray(noticeList.getResult()));
		}
		AttenceService as = new AttenceService();
		ModelAndView asmv = as.listMy();
		mv.data.put("attences", asmv.data.getJSONArray("attences"));
		mv.data.put("now", DataHelper.sdf.format(new Date()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getNotice(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		dao.execute("update NoticeReceiver set hasRead=1 where noticeId=? and receiverId=?", id,ThreadSession.getUser().id);
		mv.data.put("notice", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView updateNotice(Notice notice , String receivers){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, notice.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"公告已经不存在");
		}
		if(StringUtils.isEmpty(receivers)){
			throw new GException(PlatformExceptionType.BusinessException, "接受者不能为空");
		}
		po.title = notice.title;
		po.conts = notice.conts;
		dao.saveOrUpdate(po);
		dao.execute("delete from NoticeReceiver where noticeId=? ", po.id);
		
		User user = ThreadSession.getUser();
		if(!receivers.contains(user.id.toString())){
			receivers +=","+user.id;
		}
		for(String receiver: receivers.split(",")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.hasRead = 0;
			nr.receiverId = Integer.valueOf(receiver);
			dao.saveOrUpdate(nr);
		}
		return mv;
	}
	@WebMethod
	public ModelAndView addNotice(Notice notice , String receivers){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(receivers)){
			throw new GException(PlatformExceptionType.BusinessException, "接受者不能为空");
		}
		Notice po = dao.getUniqueByKeyValue(Notice.class, "title", notice.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"该标题已存在");
		}
		User user = ThreadSession.getUser();
		notice.userId = user.id;
		notice.addtime = new Date();
		dao.saveOrUpdate(notice);
		if(!receivers.contains(user.id.toString())){
			receivers +=","+user.id;
		}
		//TODO 是否默认发给自己一份
		for(String receiver: receivers.split(",")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.hasRead = 0;
			nr.receiverId = Integer.valueOf(receiver);
			dao.saveOrUpdate(nr);
		}
		mv.data.put("recordId", notice.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listFenLei(){
		ModelAndView mv = new ModelAndView();
		List<NoticeClass> list = dao.listByParams(NoticeClass.class, "from NoticeClass");
		mv.data.put("list", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listNotice(OAQuery query, Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("select n.id as id, n.title as title, n.addtime as addtime, nc.fenlei as classTitle, u.uname as uname from Notice n, NoticeReceiver nr , NoticeClass nc , User u where n.id=nr.noticeId and n.claid=nc.id and u.id=n.userId and nr.receiverId=?");
		params.add(user.id);
		if(query.claid!=null){
			hql.append("  and n.claid=? ");
			params.add(query.claid);
		}
		page = dao.findPage(page , hql.toString() , true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	public ModelAndView listNoticeAndGroup(int gsize){
		ModelAndView mv = new ModelAndView();
		Page<Map> page = new Page<Map>();
		page.setPageSize(gsize);
		List<NoticeClass> fenLeis = dao.listByParams(NoticeClass.class, "from NoticeClass");
		for(NoticeClass nc : fenLeis){
			OAQuery query = new OAQuery();
			query.claid = nc.id;
			ModelAndView imv = listNotice(query , page);
			mv.data.put(nc.id, imv.data.getJSONObject("page").get("data"));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteFenLei(int id){
		ModelAndView mv = new ModelAndView();
		NoticeClass po = dao.get(NoticeClass.class, id);
		if(po!=null){
			dao.delete(po);
		}
		//是否删除分类对应的数据
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView deleteNotice(int noticeId){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, noticeId);
		if(po!=null){
			dao.delete(po);
		}
		dao.execute("delete from NoticeReceiver where noticeId = ? ", noticeId);
		dao.execute("delete from Attachment where bizType = 'oa' and recordId=? ", noticeId);
		mv.data.put("msg", "删除成功");
		return mv;
	}
}
