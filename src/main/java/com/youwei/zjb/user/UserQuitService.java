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

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.entity.User;
import com.youwei.zjb.user.entity.UserQuit;
import com.youwei.zjb.util.JSONHelper;

public class UserQuitService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(UserQuit uq){
		UserQuit po = dao.getUniqueByParams(UserQuit.class, new String[]{"userId" , "pass"}, new Object[]{uq.userId , uq.pass});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "已提交过相同的离职申请");
		}
		uq.pass = 0;
		uq.applyTime = new Date();
		dao.saveOrUpdate(uq);
	}
	
	public void delete(int quitId){
		UserQuit po = dao.get(UserQuit.class, quitId);
		if(po!=null){
			dao.delete(po);
		}
	}
	
	public ModelAndView list(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname, u.tel as tel ,uq.applyTime as applyTime ,uq.reason as reason "
				+ "from User u , UserQuit uq where u.id=uq.userId and uq.pass=0");
		List<Object> params = new ArrayList<Object>();
		page = dao.findPage(page , hql.toString(), true, params.toArray());
		mv.data.put("data",  JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
	
	public void review(int quitId , int pass){
		UserQuit po = dao.get(UserQuit.class, quitId);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, 1, "该离职申请已不存在");
		}
		po.pass = pass;
		po.passTime = new Date();
		dao.saveOrUpdate(po);
		if(po.fyTo!=null){
			dao.execute("update House set uid = ? where uid = ?", po.fyTo , po.userId);
		}
		if(po.kyTo!=null){
			dao.execute("update Client set uid = ? where uid = ?", po.kyTo , po.userId);
		}
		User user = dao.get(User.class, po.userId);
		user.flag = 1;
		dao.saveOrUpdate(user);
	}
}
