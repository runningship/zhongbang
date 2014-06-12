package com.youwei.zjb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 角色资源表(权限表)，即角色具有哪些菜单
 */
@Entity
public class RoleAuthority {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer roleId;
	
	public String name;
	
	/**
	 * module,menu,func
	 */
	public String type;
	
	public transient static final String Seporator = ",";
}
