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
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.user.entity.UserAdjust;
import com.youwei.zjb.user.entity.UserQuit;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/user/lizhi/")
public class UserQuitService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	@WebMethod
	public ModelAndView init(int lizhiId){
		ModelAndView mv = new ModelAndView();
		UserQuit uq = dao.get(UserQuit.class, lizhiId);
		User user = dao.get(User.class, uq.userId);
		mv.data.put("uname", user.uname);
		mv.data.put("oldTitle", user.getRole().title);
		
		mv.data.put("oldDeptName", user.Department().namea );
		
		User fyTo = dao.get(User.class, uq.fyTo);
		if(fyTo!=null){
			mv.data.put("fyTo", fyTo.uname);
		}
		User kyTo = dao.get(User.class, uq.kyTo);
		if(kyTo!=null){
			mv.data.put("kyTo", kyTo.uname);
		}
		mv.data.put("reason", uq.reason);
		mv.data.put("jiaojie", uq.jiaojie);
		
		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='quit' and r.userId=? and u.id=r.sprId",uq.userId);
		mv.data.put("myId", 316);
		mv.data.put("spList", JSONHelper.toJSONArray(spList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(UserQuit uq){
		ModelAndView mv = new ModelAndView();
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
		List<User> sprList = UserHelper.getUserWithAuthority("rs_lzsq_list");
		if(sprList==null || sprList.size()==0){
			throw new GException(PlatformExceptionType.BusinessException, 4, "没有用户拥有离职审核权限，请先在系统管理中设置离职整审核人.或者联系系统管理员为您处理");
		}
		dao.saveOrUpdate(uq);
		for(User spr : sprList){
			RenShiReview review = new RenShiReview();
			review.category=RenShiReview.Quit;
			review.sh=0;
			review.sprId = spr.id;
			review.userId = uq.userId;
			dao.saveOrUpdate(review);
		}
		mv.data.put("msg", "申请成功");
		return mv;
	}
	
	public void delete(int quitId){
		UserQuit po = dao.get(UserQuit.class, quitId);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	@WebMethod
	public ModelAndView listQuitApply(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		if(user==null){
			user = dao.get(User.class, 316);
		}
		hql.append("select uq.id as id, review.sh as lzsh,uq.applyTime as applyTime, u.uname as uname,u.id as uid ,r.title as title ,u.tel as tel,u.sfz as sfz, u.gender as gender,u.address as address,u.rqsj as rqsj, u.lzsj as lzsj,d.namea as deptName "
				+ "from User  u, Department d,Role r ,UserQuit uq, RenShiReview review where u.id=uq.userId and u.roleId = r.id and d.id = u.deptId and u.id=review.userId and review.sprId=? and review.category='"+RenShiReview.Quit+"' ");
		params.add(user.id);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
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
		UserQuit uq = dao.get(UserQuit.class, lizhiId);
		User user = dao.get(User.class, po.userId);
		if(count==0){
			uq.passTime = new Date();
			uq.pass = 1;
			dao.saveOrUpdate(uq);
			if(uq.fyTo!=null){
				dao.execute("update House set uid = ? where uid = ?", uq.fyTo , po.userId);
			}
			if(uq.kyTo!=null){
				dao.execute("update Client set uid = ? where uid = ?", uq.kyTo , po.userId);
			}
			user.flag = 1;
			user.lzsj = uq.leaveTime;
			dao.saveOrUpdate(user);
		}
		User operUser = ThreadSession.getUser();
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 审核通过了用户["+user.Department().namea+"-"+user.uname+"] 的离职申请";
		operService.add(OperatorType.人事记录, operConts);
		return new ModelAndView();
	}
}
