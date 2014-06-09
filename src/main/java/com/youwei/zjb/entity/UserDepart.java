package com.youwei.zjb.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserDepart {
	
	@Id
	public Integer id;
	
	public Integer userId;
	
	public Integer deptId;
	
	public Integer roleId;
	
	
}
