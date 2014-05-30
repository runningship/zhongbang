package com.youwei.zjb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.zjb.Resource;
import com.youwei.zjb.SimpDaoTool;

/**
 * 
 *角色表，职务表
 */
@Entity
@Table(name="uc_unit")
public class Role {

	@Id
	public Integer id;
	
	/**
	 * 公司id
	 */
	public Integer cid;
	
	/**
	 * 职务，相当于角色名称
	 */
	public String title;
	
	/**
	 * 角色说明
	 */
	public String cont;
	
	public Integer flag;
	
	public Integer sh;
	
	public Integer num;
	
	public List<Resource> getResources(){
		List<Resource> result = new ArrayList<Resource>();
		List<RoleResource> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(RoleResource.class, new String[]{"roleId"}, new Object[]{id});
		if(list==null){
			return result;
		}
		for(RoleResource rr : list){
			result.add(Resource.valueOf(rr.resource));
		}
		return result;
	}
}
