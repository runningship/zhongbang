package com.youwei.zjb.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RenShiReview {

	@Id
	public Integer id;
	
	public Integer userId;
	
	/**
	 * 审批人
	 */
	public Integer sprId;
	
	/**
	 * 审批结果
	 * 0,不通过，1通过
	 */
	public Integer sh;
	
	//入职，调整，离职
	//join , adjust , quit
	public String category;
}
