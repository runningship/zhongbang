package com.youwei.zjb.asset.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_res")
public class OfficeSupply {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="uid")
	public Integer userId;
	
	@Column(name="did")
	public Integer deptId;
	
	public Date addtime;
	
	@Column(name="names")
	public String title;
	
	@Column(name="num")
	public Integer count;
	
	public String beizhu;
	
	public Float djia;
	
	public Float zjia;
	
	/**
	 * 相关人
	 */
	@Column(name="uname")
	public String xgr;
	
	/**
	 * 0 未审核，1 已审核(通过),2 审核不通过
	 */
	public Integer shenhe;
}
