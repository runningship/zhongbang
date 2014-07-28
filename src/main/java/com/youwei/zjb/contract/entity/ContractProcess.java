package com.youwei.zjb.contract.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_pact_process")
public class ContractProcess {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="pid")
	public Integer contractId;
	
	/**
	 * 步骤类别
	 */
	@Column(name="proid")
	public Integer buzhouId;
	
	public String title;
	
	public String conts;
	
	/**
	 * 0 需要选择签证人 , 2不需要选择签证人
	 */
	@Column(name="qian_is")
	public Integer qian;
	
	/**
	 * 状态
	 * 0 未完成，2 已完成
	 */
	public Integer flag;
	
	public Integer ordera;
	
	public String bianhao;
	
	@Column(name="addtime")
	public Date endtime;
	
	/**
	 * 办理人id
	 */
	@Column(name="foruid")
	public Integer blrId;
	/**
	 * 办理人
	 */
	@Column(name="foruname")
	public String blr;
	
}
