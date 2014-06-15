package com.youwei.zjb.im;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.im.entity.Contact;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);

	@WebMethod
	public ModelAndView getContacts(int userId) {
		ModelAndView mv = new ModelAndView();
		List<Map> list = dao.listAsMap("select c.id as id ,c.ownerId as ownerId ,c.contactId as contactId ,c.ugroup as group ,u.uname as contactName, "
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
	
	@WebMethod
	public ModelAndView search(int ownerId,String txt){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select id as userId, uname as uname from User  where id<> "+ownerId);
		if(StringUtils.isNotEmpty(txt)){
			hql.append(" and (tel like ? or uname like ?)");
			params.add("%"+txt+"%");
			params.add("%"+txt+"%");
		}
		Page<Map> page = dao.findPage(new Page<Map>() ,hql.toString() ,true , params.toArray());
		mv.data.put("contacts", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addContact(Contact contact){
		ModelAndView mv = new ModelAndView();
		Contact po = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.ownerId,contact.contactId});
		if(po==null){
			dao.saveOrUpdate(contact);
		}
		mv.data.put("result", new JSONObject());
		return mv;
	}
	
	@WebMethod
	public ModelAndView delContact(int ownerId , int contactId){
		ModelAndView mv = new ModelAndView();
		dao.execute("delete from Contact where ownerId=? and contactId=?", ownerId , contactId);
		mv.data.put("result", 0);
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
