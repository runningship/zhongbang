package com.youwei.zjb.user;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.youwei.zjb.util.DataHelper;
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
//		List<User> users = UserHelper.getUserWithAuthority("rs_rz_list");
		List<User> users = UserHelper.getUserWithAuthority("rs_rz_list","rs_rz_data" , user);
		
		StringBuilder ids = new StringBuilder();
		for(int i=0;i<users.size();i++){
			ids.append(users.get(i).id);
			if(i<users.size()-1){
				ids.append(",");
			}
		}
		if(!StringUtils.isEmpty(ids.toString())){
			List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='join' and r.userId=? and u.id=r.sprId and sprId in ("+ids.toString()+")",userId);
			mv.data.put("spList", JSONHelper.toJSONArray(spList));
		}
//		List<Map> spList = dao.listAsMap("select r.id as id, r.sprId as sprId, u.uname as spr , r.sh as sh from User u, RenShiReview r where r.category='join' and r.userId=? and u.id=r.sprId and u.sh=1 and u.flag=0",userId);
		
		mv.data.put("roles", JSONHelper.toJSONArray(roles));
		mv.data.put("rqtjs", RuQiTuJin.toJsonArray());
		mv.data.put("user", JSONHelper.toJSON(user,DataHelper.dateSdf.toPattern()));
		mv.data.put("myId", ThreadSession.getUser().id);
		mv.data.getJSONObject("user").put("cid", comp.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int rid){
		RenShiReview po = dao.get(RenShiReview.class, rid);
		dao.delete(po);
		long count = dao.countHqlResult("from RenShiReview where userid=? and sh=0 and category='join' ", po.userId);
		if(count==0){
			User user = dao.get(User.class, po.userId);
			dao.delete(user);
		}
		return new ModelAndView();
	}
	@WebMethod
	public ModelAndView pass(int spId){
		ModelAndView mv = new ModelAndView();
		RenShiReview po = dao.get(RenShiReview.class, spId);
		po.sh = 1;
		dao.saveOrUpdate(po);
//		List<User> users = UserHelper.getUserWithAuthority("rs_rz_list");
		User target = dao.get(User.class, po.userId);
		List<User> users = UserHelper.getUserWithAuthority("rs_rz_list","rs_rz_data" , target);
		StringBuilder ids = new StringBuilder();
		for(int i=0;i<users.size();i++){
			ids.append(users.get(i).id);
			if(i<users.size()-1){
				ids.append(",");
			}
		}
//		long count = dao.countHqlResult("from RenShiReview r,User u where r.userId=? and r.sh=0 and category='join' and u.id=r.sprId and u.flag=0 and u.sh=1 ", po.userId);
		long count = dao.countHqlResult("from RenShiReview r  where r.userId=? and r.sh=0 and category='join' and r.sprId in ("+ids.toString()+") ", po.userId);
		if(count==0){
			User user = dao.get(User.class, po.userId);
			user.sh=1;
			dao.saveOrUpdate(user);
			int delcount = dao.execute("delete from RenShiReview r  where r.userId=? and r.sh=0 and category='join' and r.sprId not in ("+ids.toString()+") ", po.userId);
		}
		User operUser = ThreadSession.getUser();
		User rzUser = dao.get(User.class, po.userId);
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 审核通过了["+rzUser.Department().namea+"-"+rzUser.uname+"]的入职申请";
		operService.add(OperatorType.人事记录, operConts);
		mv.data.put("msg", "审批成功");
		return mv;
	}
}
