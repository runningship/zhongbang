package com.youwei.zjb.contract.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_pact_yongjin")
public class YongJin {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer Id;
	
	public String beizhu;
	
	/**
	 * 1 佣金收费，3 代收费
	 */
	public Integer flag;
	
	@Column(name="proid")
	public Integer contractId;
	
	public Date dateA;
	
	public Date dateB;
	
	public Date dateC;
	
	public Float qian_a;
	
	public Float qian_b;
	
	public Float qian_c;
	
	/**
	 * 办理人
	 */
	public String qian_r;
}
