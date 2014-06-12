package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用户部门或职务调整
 */
@Entity
public class UserAdjust {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer userId;
	
	public Integer oldDeptId;
	
	public Integer newDeptId;
	
	public Integer oldRoleId;
	
	public Integer newRoleId;
	
	public String reason;
	
	public String jiaojie;
	
	public Date applyTime;
	
	public Date passTime;
	
	/**
	 * 房源调整到指定人
	 */
	public Integer fyTo;
	
	/**
	 * 客源调整到指定人
	 */
	public Integer kyTo;
	
	/**
	 * 0 未审核，1 通过 ,2 不通过
	 */
	public Integer pass;
}
