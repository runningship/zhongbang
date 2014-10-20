package com.youwei.zjb.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RenShiReview {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 入职人员
	 */
	public Integer userId;
	
	//待审核项,入职审核没有
	public Integer itemId;
	
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
	
	public transient static final String Join = "join";
	public transient static final String Adjust = "adjust";
	public transient static final String Quit = "quit";
}
