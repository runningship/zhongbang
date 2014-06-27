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
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/user/zwtz/")
public class UserAdjustService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
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
		mv.data.put("fyTo", fyTo.uname);
		
		User kyTo = dao.get(User.class, adjust.kyTo);
		mv.data.put("kyTo", kyTo.uname);
		
		mv.data.put("reason", adjust.reason);
		mv.data.put("jiaojie", adjust.jiaojie);
		
		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='adjust' and r.userId=? and u.id=r.sprId",adjust.userId);
		mv.data.put("myId", 316);
		mv.data.put("spList", JSONHelper.toJSONArray(spList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(UserAdjust ua){
		UserAdjust po = dao.getUniqueByParams(UserAdjust.class, new String[]{"userId", "oldDeptId" , "newDeptId" , "pass"}, new Object[]{ua.userId , ua.oldDeptId , ua.newDeptId ,0});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "已提交过相同的职务调整申请");
		}
		if(ua.userId.equals(ua.fyTo) || ua.userId.equals(ua.kyTo)){
			throw new GException(PlatformExceptionType.BusinessException, 2, "客源或房源调整不正确");
		}
		ua.applyTime = new Date();
		ua.pass =0;
		User user = dao.get(User.class, ua.userId);
		ua.oldRoleId = user.roleId;
		dao.saveOrUpdate(ua);
		ModelAndView mv = new ModelAndView();
		mv.data.put("msg","申请成功");
		return  mv;
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
		RenShiReview po = dao.get(RenShiReview.class, spId);
		po.sh = 1;
		dao.saveOrUpdate(po);
		long count = dao.countHqlResult("from RenShiReview where userid=? and sh=0 and category='adjust' ", po.userId);
		if(count==0){
			UserAdjust adjust = dao.get(UserAdjust.class, adjustId);
			adjust.passTime = new Date();
			adjust.pass = 1;
			dao.saveOrUpdate(adjust);
			if(adjust.fyTo!=null){
				dao.execute("update House set uid = ? where uid = ?", adjust.fyTo , adjust.userId);
			}
			if(adjust.kyTo!=null){
				dao.execute("update Client set uid = ? where uid = ?", adjust.kyTo , adjust.userId);
			}
		}
		return new ModelAndView();
	}
}
