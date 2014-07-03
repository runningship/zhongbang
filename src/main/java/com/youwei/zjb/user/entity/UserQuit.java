package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Column;
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
	
	@Column(nullable=false)
	public String reason;
	
	public Integer pass;
	
	@Column(nullable=false)
	public String jiaojie;
	
	public Integer fyTo;
	
	public Integer kyTo;
	
	public Date applyTime;
	
	@Column(nullable=false)
	public Date leaveTime;
	
	public Date passTime;
	
}
