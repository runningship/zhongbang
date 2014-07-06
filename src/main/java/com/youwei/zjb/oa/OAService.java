package com.youwei.zjb.oa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeClass;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.util.JSONHelper;

public class OAService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public ModelAndView addFenLei(NoticeClass nc){
		dao.saveOrUpdate(nc);
		return new ModelAndView();
	}
	
	public ModelAndView addNotice(Notice notice , String receivers){
		if(StringUtils.isEmpty(receivers)){
			throw new GException(PlatformExceptionType.BusinessException, 1, "接受者不能为空");
		}
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		notice.userId = user.id;
		notice.addtime = new Date();
		dao.saveOrUpdate(notice);
		//TODO 是否默认发给自己一份
		for(String receiver: receivers.split(";")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.read = 0;
			nr.receiverId = Integer.valueOf(receiver);
			dao.saveOrUpdate(nr);
		}
		return new ModelAndView();
	}
	
	public ModelAndView listFenLei(){
		ModelAndView mv = new ModelAndView();
		List<NoticeClass> list = dao.listByParams(NoticeClass.class, "from NoticeClass");
		mv.data.put("list", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public ModelAndView listNotice(int claid, Page<Notice> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		String hql = "select n from Notice n, NoticeReceiver nr where n.id=nr.noticeId and nr.receiverId=? and n.claid=?";
		params.add(user.id);
		params.add(claid);
		List<Notice> list = dao.listByParams(Notice.class, hql , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	public ModelAndView listNoticeAndGroup(int gsize){
		ModelAndView mv = new ModelAndView();
		Page<Notice> page = new Page<Notice>();
		page.setPageSize(gsize);
		List<NoticeClass> fenLeis = dao.listByParams(NoticeClass.class, "from NoticeClass");
		for(NoticeClass nc : fenLeis){
			ModelAndView imv = listNotice(nc.id , page);
			mv.data.put(nc.id, imv.data.getJSONObject("page").get("data"));
		}
		return mv;
	}
	
	@Transactional
	public ModelAndView deleteNotice(int noticeId){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, noticeId);
		if(po!=null){
			dao.delete(po);
		}
		dao.execute("delete from NoticeReceiver where noticeId = ? ", noticeId);
		return mv;
	}
}
