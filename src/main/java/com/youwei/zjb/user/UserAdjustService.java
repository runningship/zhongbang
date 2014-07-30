package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/user/zwtz/")
public class UserAdjustService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	@WebMethod
	public ModelAndView init(int adjustId){
		ModelAndView mv = new ModelAndView();
		UserAdjust adjust = dao.get(UserAdjust.class, adjustId);
		User user = dao.get(User.class, adjust.userId);
		mv.data.put("uname", user.uname);
		mv.data.put("oldTitle", user.getRole().title);
		
		Role newRole = dao.get(Role.class, adjust.newRoleId);
		mv.data.put("newTitle", newRole.title);
		
		mv.data.put("oldDeptName", user.Department().namea );
		Department newDept = dao.get(Department.class, adjust.newDeptId);
		mv.data.put("newDeptName", newDept.namea);
		
		User fyTo = dao.get(User.class, adjust.fyTo);
		if(fyTo!=null){
			mv.data.put("fyTo", fyTo.uname);
		}
		User kyTo = dao.get(User.class, adjust.kyTo);
		if(kyTo!=null){
			mv.data.put("kyTo", kyTo.uname);
		}
		mv.data.put("reason", adjust.reason);
		mv.data.put("jiaojie", adjust.jiaojie);
		
		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='adjust' and r.userId=? and u.id=r.sprId",adjust.userId);
		mv.data.put("myId", ThreadSession.getUser().id);
		mv.data.put("spList", JSONHelper.toJSONArray(spList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(UserAdjust ua){
		UserAdjust po = dao.getUniqueByParams(UserAdjust.class, new String[]{"userId", "oldDeptId" , "newDeptId" , "pass"}, new Object[]{ua.userId , ua.oldDeptId , ua.newDeptId ,0});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"已提交过相同的职务调整申请");
		}
		if(ua.userId.equals(ua.fyTo) || ua.userId.equals(ua.kyTo)){
			throw new GException(PlatformExceptionType.BusinessException, "客源或房源调整不正确");
		}
		List<User> sprList = UserHelper.getUserWithAuthority("rs_zwtz_list");
		if(sprList==null || sprList.size()==0){
			throw new GException(PlatformExceptionType.BusinessException, "没有用户拥有职务审核权限，请先在系统管理中设置职务调整审核人.或者联系系统管理员为您处理");
		}
		ua.applyTime = new Date();
		ua.pass =0;
		User user = dao.get(User.class, ua.userId);
		ua.oldRoleId = user.roleId;
		dao.saveOrUpdate(ua);
		for(User spr : sprList){
			RenShiReview review = new RenShiReview();
			review.category=RenShiReview.Adjust;
			review.sh=0;
			review.sprId = spr.id;
			review.userId = user.id;
			dao.saveOrUpdate(review);
		}
		ModelAndView mv = new ModelAndView();
		mv.data.put("msg","申请成功");
		return  mv;
	}
	
	@WebMethod
	public ModelAndView listAdjust(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		hql.append("select ud.id as id, review.sh as tzsh,ud.applyTime as applyTime, u.uname as uname,u.id as uid ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r ,UserAdjust ud, RenShiReview review where u.id=ud.userId and u.roleId = r.id and d.id = u.deptId and u.id=review.userId and review.sprId=? and review.category='"+RenShiReview.Adjust+"' ");
		params.add(user.id);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(String applyTimeStart , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select d.namea as deptName,u.address as address ,u.gender as gender, u.uname as uname, r.title as title, u.tel as tel, ad.reason as reason, ad.applyTime as applyTime "
				+ " ,ad.id as id from User u , UserAdjust ad, Role r ,Department d  where u.id=ad.userId and ad.newRoleId=r.id and u.deptId=d.id");
		List<Object> params = new ArrayList<Object>();
		hql.append(HqlHelper.buildDateSegment("applyTime", applyTimeStart, DateSeparator.After, params ));
		page = dao.findPage(page , hql.toString(), true, params.toArray());
		mv.data.put("page",  JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public void delete(int adjustId){
		UserAdjust po = dao.get(UserAdjust.class, adjustId);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	@Transactional
	@WebMethod
	public ModelAndView pass(int adjustId , int spId){
		ModelAndView mv = new ModelAndView();
		RenShiReview po = dao.get(RenShiReview.class, spId);
		po.sh = 1;
		dao.saveOrUpdate(po);
		long count = dao.countHqlResult("from RenShiReview where userid=? and sh=0 and category='"+RenShiReview.Adjust+"' ", po.userId);
		UserAdjust adjust = dao.get(UserAdjust.class, adjustId);
		User user = dao.get(User.class, adjust.userId);
		Department oldDept = dao.get(Department.class,adjust.oldDeptId);
		Department newDept = dao.get(Department.class,adjust.newDeptId);
		if(count==0){
			adjust.passTime = new Date();
			adjust.pass = 1;
			dao.saveOrUpdate(adjust);
			user.roleId = adjust.newRoleId;
			user.deptId = adjust.newDeptId;
			user.orgpath = newDept.path+user.id;
			dao.saveOrUpdate(user);
			if(adjust.fyTo!=null){
				dao.execute("update House set uid = ? where uid = ?", adjust.fyTo , adjust.userId);
			}
			if(adjust.kyTo!=null){
				dao.execute("update Client set uid = ? where uid = ?", adjust.kyTo , adjust.userId);
			}
		}
		User operUser = ThreadSession.getUser();
		Role oldRole = dao.get(Role.class, adjust.oldRoleId);
		Role newRole = dao.get(Role.class, adjust.newRoleId);
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 审核通过了用户["+user.Department().namea+"-"+user.uname+"] 从["
				+oldDept.Parent().namea+"-"+oldDept.namea+"-"+oldRole.title+"]调整到["+newDept.Parent().namea+"-"+newDept.namea+"-"+newRole.title+"]";
		operService.add(OperatorType.人事记录, operConts);
		mv.data.put("msg", "审核成功");
		return new ModelAndView();
	}
}
