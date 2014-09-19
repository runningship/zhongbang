package com.youwei.zjb.caiwu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FinanceProcess {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer financeId;
	
	/**
	 * 处理人
	 */
	public String uname;
	
	public String conts;
	
	/**
	 * 0 未处理，1 已处理
	 */
	public Integer flag;
	
	public Date addtime;
	
	public Integer ordera;
	
	/**
	 * 财务分类
	 */
	public Integer claid;
	
	/**
	 * 处理人
	 */
	public Integer processorId;
}
