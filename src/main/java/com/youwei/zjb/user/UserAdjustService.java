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

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

public class UserAdjustService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(UserAdjust ua){
		UserAdjust po = dao.getUniqueByParams(UserAdjust.class, new String[]{"userId", "oldDeptId" , "newDeptId" , "pass"}, new Object[]{ua.userId , ua.oldDeptId , ua.newDeptId ,0});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "已提交过相同的职务调整申请");
		}
		ua.applyTime = new Date();
		ua.pass =0;
		User user = dao.get(User.class, ua.userId);
		ua.oldRoleId = user.roleId;
		dao.saveOrUpdate(ua);
	}
	
	public ModelAndView list(String applyTimeStart , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname, r.title as title, u.tel as tel, ad.reason as reason, ad.applyTime as applyTime "
				+ "from User u , UserAdjust ad, Role r where u.id=ad.userId and ad.newRoleId=r.id ");
		List<Object> params = new ArrayList<Object>();
		hql.append(HqlHelper.buildDateSegment("applyTime", applyTimeStart, DateSeparator.After, params ));
		page = dao.findPage(page , hql.toString(), true, params.toArray());
		mv.data.put("data",  JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
	
	public void delete(int adjustId){
		UserAdjust po = dao.get(UserAdjust.class, adjustId);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	@Transactional
	public void review(int adjustId , int pass){
		UserAdjust adjust = dao.get(UserAdjust.class, adjustId);
		adjust.passTime = new Date();
		adjust.pass = pass;
		dao.saveOrUpdate(adjust);
		if(adjust.fyTo!=null){
			dao.execute("update House set uid = ? where uid = ?", adjust.fyTo , adjust.userId);
		}
		if(adjust.kyTo!=null){
			dao.execute("update Client set uid = ? where uid = ?", adjust.kyTo , adjust.userId);
		}
	}
}
