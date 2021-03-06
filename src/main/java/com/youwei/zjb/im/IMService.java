package com.youwei.zjb.im;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.im.entity.Contact;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);

	
	@WebMethod
	public ModelAndView getContacts(int userId) {
		ModelAndView mv = new ModelAndView();
		//and u.flag=0 离职的由用户自行从好友列表中删除
		List<Map> list = dao.listAsMap("select c.id as id ,c.ownerId as ownerId ,c.contactId as contactId ,c.ugroup as group ,u.uname as contactName, "
				+ " u.tel as contactTel,d.namea as deptName,u.avatar as avatar from User u,Contact c,Department d where c.contactId=u.id and d.id=u.deptId and u.sh=1  and c.ownerId=?", new Object[] { userId });
		for(Map map : list){
			int uid = (int) map.get("contactId");
			if(IMServer.isUserOnline(uid)){
				map.put("state", "在线");
				map.put("state_class", "online");
			}else{
				map.put("state", "离线");
				map.put("state_class", "offline");
			}
		}
		DataHelper.fillDefaultValue(list, "avatar", 0);
		mv.data.put("contacts",JSONHelper.toJSONArray(list));
		List<Map> list2 = countUnReadMessage(userId);
		mv.data.put("unreads", JSONHelper.toJSONArray(list2));
		return mv;
	}
	public List<Map> countUnReadMessage(int userId){
		//需要过滤掉不在自己好友列表里面的
		String hql = "select count(*) as total, m.senderId as senderId from Message m,Contact c where c.contactId=m.senderId and c.ownerId=? "
				+ " and m.hasRead=0 and m.receiverId=? group by senderId";
		List<Map> list = dao.listAsMap(hql, userId , userId);
		return list;
	}
	
	@WebMethod
	public ModelAndView close(){
		ModelAndView mv = new ModelAndView();
		try {
			IMServer.forceStop();
		} catch (Exception e) {
			throw new RuntimeException("try to close IM Server fail.",e);
		}
		mv.data.put("msg","IM Server closed.");
		return mv;
	}
	@WebMethod
	public ModelAndView status(){
		ModelAndView mv = new ModelAndView();
		JSONArray arr = new JSONArray();
		for(Integer uid : IMServer.conns.keySet()){
			User u = dao.get(User.class, uid);
			if(u!=null){
				arr.add(u.uname);
			}
		}
		mv.data.put("onlien users", arr);
		return mv;
	}
	@WebMethod
	public ModelAndView start(){
		ModelAndView mv = new ModelAndView();
		try {
			IMServer.startUp();
		} catch (Throwable e) {
			throw new RuntimeException("try to start IM Server fail.",e);
		}
		mv.data.put("msg","IM Server started.");
		return mv;
	}
	
	@WebMethod
	public ModelAndView allAvatars(){
		ModelAndView mv = new ModelAndView();
		JSONArray arr = new JSONArray();
		for(int i=1;i<90;i++){
			JSONObject jobj = new JSONObject();
			jobj.put("avatarId", i);
			arr.add(jobj);
		}
		mv.data.put("avatars",arr);
		return mv;
	}
	
	@WebMethod
	public ModelAndView setAvatar(int userId,int avatarId){
		ModelAndView mv = new ModelAndView();
		User user = dao.get(User.class, userId);
		user.avatar = avatarId;
		dao.saveOrUpdate(user);
		return mv;
	}
	
	@WebMethod
	public ModelAndView search(IMQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select u.id as userId, u.uname as uname,d.namea as deptName,u.tel as tel from User u,Department d  where u.deptId = d.id and u.flag=0 and  u.id<> "+ThreadSession.getUser().id);
		if(StringUtils.isNotEmpty(query.search)){
			hql.append(" and (tel like ? or uname like ?)");
			params.add("%"+query.search+"%");
			params.add("%"+query.search+"%");
		}
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ?");
			params.add(query.xpath+"%");
		}
		page = dao.findPage(page,hql.toString() ,true , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addContact(Contact contact){
		ModelAndView mv = new ModelAndView();
		Contact po = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.ownerId,contact.contactId});
		if(po==null){
			dao.saveOrUpdate(contact);
		}
		
		Contact po2 = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.contactId,contact.ownerId});
		if(po2==null){
			Contact contact2 = new Contact();
			contact2.ownerId = contact.contactId;
			contact2.contactId = contact.ownerId;
			dao.saveOrUpdate(contact2);
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
	
	@WebMethod
	public ModelAndView setRead(int myId, int contactId){
		ModelAndView mv = new ModelAndView();
		String hql = "update Message set hasRead=1 where senderId=? and receiverId=? and hasRead=0";
		dao.execute(hql, contactId, myId);
		mv.data.put("result", 0);
		return mv;
	}
}
