package com.youwei.zjb.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.sys.Authority;

/**
 * 
 *角色表，职务表
 */
@Entity
@Table(name="uc_unit")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	
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
	
	public List<Authority> getResources(){
		List<Authority> result = new ArrayList<Authority>();
		List<RoleAuthority> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(RoleAuthority.class, new String[]{"roleId"}, new Object[]{id});
		if(list==null){
			return result;
		}
		for(RoleAuthority rr : list){
			result.add(Authority.valueOf(rr.name));
		}
		return result;
	}
}
