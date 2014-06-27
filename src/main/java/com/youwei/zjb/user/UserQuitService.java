package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.user.entity.UserQuit;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/user/lizhi/")
public class UserQuitService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView init(int lizhiId){
		ModelAndView mv = new ModelAndView();
		UserQuit uq = dao.get(UserQuit.class, lizhiId);
		User user = dao.get(User.class, uq.userId);
		mv.data.put("uname", user.uname);
		mv.data.put("oldTitle", user.getRole().title);
		
		mv.data.put("oldDeptName", user.Department().namea );
		
		User fyTo = dao.get(User.class, uq.fyTo);
		mv.data.put("fyTo", fyTo.uname);
		
		User kyTo = dao.get(User.class, uq.kyTo);
		mv.data.put("kyTo", kyTo.uname);
		
		mv.data.put("reason", uq.reason);
		mv.data.put("jiaojie", uq.jiaojie);
		
		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='quit' and r.userId=? and u.id=r.sprId",uq.userId);
		mv.data.put("myId", 316);
		mv.data.put("spList", JSONHelper.toJSONArray(spList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(UserQuit uq){
		uq.pass = 0;
		uq.applyTime = new Date();
		UserQuit po = dao.getUniqueByParams(UserQuit.class, new String[]{"userId" , "pass"}, new Object[]{uq.userId , uq.pass});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "已提交过相同的离职申请");
		}
		if(uq.userId==null){
			throw new GException(PlatformExceptionType.BusinessException, 2, "离职人员不能为空");
		}
		if(uq.userId.equals(uq.fyTo) || uq.userId.equals(uq.kyTo)){
			throw new GException(PlatformExceptionType.BusinessException, 3, "客源或房源调整不正确");
		}
		dao.saveOrUpdate(uq);
		return new ModelAndView();
	}
	
	public void delete(int quitId){
		UserQuit po = dao.get(UserQuit.class, quitId);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select uq.id as id, u.uname as uname, d.namea as deptName,r.title as title ,u.gender as gender, u.tel as tel ,uq.applyTime as applyTime ,uq.reason as reason "
				+ "from User u , UserQuit uq ,Role r, Department d where u.id=uq.userId and u.roleId=r.id and u.deptId=d.id and uq.pass=0");
		List<Object> params = new ArrayList<Object>();
		page = dao.findPage(page , hql.toString(), true, params.toArray());
		mv.data.put("page",  JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView pass(int lizhiId ,int spId){
		RenShiReview po = dao.get(RenShiReview.class, spId);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "该离职申请已不存在");
		}
		po.sh = 1;
		dao.saveOrUpdate(po);
		long count = dao.countHqlResult("from RenShiReview where userid=? and sh=0 and category='quit' ", po.userId);
		if(count==0){
			UserQuit uq = dao.get(UserQuit.class, lizhiId);
			uq.passTime = new Date();
			uq.pass = 1;
			dao.saveOrUpdate(uq);
			if(uq.fyTo!=null){
				dao.execute("update House set uid = ? where uid = ?", uq.fyTo , po.userId);
			}
			if(uq.kyTo!=null){
				dao.execute("update Client set uid = ? where uid = ?", uq.kyTo , po.userId);
			}
			User user = dao.get(User.class, po.userId);
			user.flag = 1;
			user.lzsj = uq.leaveTime;
			dao.saveOrUpdate(user);
		}
		return new ModelAndView();
	}
}
