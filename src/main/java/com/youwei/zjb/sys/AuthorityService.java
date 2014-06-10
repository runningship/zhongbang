package com.youwei.zjb.sys;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.entity.RoleAuthority;

public class AuthorityService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Transactional
	public void update(String category,int roleId,String authNames){
		if(StringUtils.isEmpty(authNames)){
			return;
		}
		dao.execute("delete from RoleAuthority where category=?", category);
		String[] authList = authNames.split(",");
		for(String auth : authList){
			RoleAuthority ra = new RoleAuthority();
			ra.category = category;
			ra.roleId = roleId;
			ra.authName = auth;
			dao.saveOrUpdate(ra);
		}
	}
}
