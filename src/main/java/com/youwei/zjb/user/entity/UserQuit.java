package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *用户离职
 */
@Entity
public class UserQuit {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer userId;
	
	public String reason;
	
	public Integer pass;
	
	public String jiaojie;
	
	public Integer fyTo;
	
	public Integer kyTo;
	
	public Date applyTime;
	
	public Date leaveTime;
	
	public Date passTime;
	
}
