package com.youwei.zjb.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 角色资源表(权限表)，即角色具有哪些菜单
 */
@Entity
public class RoleAuthority {
	
	@Id
	public Integer id;
	
	public Integer roleId;
	
	public String category;
	
	public String authName;
	
}
