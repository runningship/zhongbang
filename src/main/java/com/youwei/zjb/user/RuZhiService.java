package com.youwei.zjb.user;

import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Department;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.entity.RenShiReview;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/user/ruzhi/")
public class RuZhiService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	@WebMethod
	public ModelAndView init(int userId){
		ModelAndView mv = new ModelAndView();
		User user = dao.get(User.class, userId);
		Department dept = dao.get(Department.class,  user.deptId);
		Department comp = dao.get(Department.class, dept.fid);
		List<Role> roles = dao.listByParams(Role.class, "from Role");
		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='join' and r.userId=? and u.id=r.sprId",userId);
		mv.data.put("roles", JSONHelper.toJSONArray(roles));
		mv.data.put("rqtjs", RuQiTuJin.toJsonArray());
		mv.data.put("user", JSONHelper.toJSON(user));
		mv.data.put("myId", ThreadSession.getUser().id);
		mv.data.getJSONObject("user").put("cid", comp.id);
		mv.data.put("spList", JSONHelper.toJSONArray(spList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView pass(int spId){
		ModelAndView mv = new ModelAndView();
		RenShiReview po = dao.get(RenShiReview.class, spId);
		po.sh = 1;
		dao.saveOrUpdate(po);
		long count = dao.countHqlResult("from RenShiReview where userid=? and sh=0 and category='join' ", po.userId);
		if(count==0){
			User user = dao.get(User.class, po.userId);
			user.sh=1;
			dao.saveOrUpdate(user);
		}
		User operUser = ThreadSession.getUser();
		User rzUser = dao.get(User.class, po.userId);
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 审核通过了["+rzUser.Department().namea+"-"+rzUser.uname+"]的入职申请";
		operService.add(OperatorType.人事记录, operConts);
		mv.data.put("msg", "审批成功");
		return mv;
	}
}
