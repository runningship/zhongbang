package com.youwei.zjb.im;

import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.util.JSONHelper;

@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);

	@WebMethod
	public ModelAndView getContacts(int userId) {
		ModelAndView mv = new ModelAndView();
		List<Map> list = dao.listAsMap("select c.id as id ,c.ownerId as ownerId ,c.contactId as contactId ,c.group as group ,u.uname as contactName, "
				+ " u.tel as contactTel from User u,Contact c where c.contactId=u.id and c.ownerId=?", new Object[] { userId });
		mv.data.put("contacts",JSONHelper.toJSONArray(list));
		return mv;
	}
	public ModelAndView countUnReadMessage(int userId){
		ModelAndView mv = new ModelAndView();
		String hql = "select count(*) as total, senderId as contactId from Message where read=0 and receiverId=? group by senderId";
		List<Map> list = dao.listAsMap(hql, userId);
		mv.data.put("unreads",JSONHelper.toJSONArray(list));
		return mv;
	}
	
	public ModelAndView setRead(int myId, int contactId){
		ModelAndView mv = new ModelAndView();
		String hql = "update Message set read=1 where senderId=? and receiverId=? and read=0";
		dao.execute(hql, contactId, myId);
		mv.data.put("result", 0);
		return mv;
	}
}
