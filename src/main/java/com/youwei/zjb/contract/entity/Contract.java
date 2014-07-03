package com.youwei.zjb.contract.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="work_pact")
public class Contract {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer Id;
	
	@Column(name="uid")
	public Integer userId;
	
	@Column(name="did")
	public Integer deptId;
	
	public String bianhao;
	
	/**
	 * 房主
	 */
	public String lxr_f;
	
	public String tel_f;
	
	/**
	 * 客户
	 */
	public String lxr_k;
	
	public String tel_k;
	
	public String addr;
	
	public Float mianji;
	
	public Float zjia;
	
	public Float djia;
	
	public Date addtime;
	
	public Integer flag;
	
	public String conts;
	
	public String beizhu;
	
	public Integer claid;
	
	public String chanquan;
	
	public Float shoufu;
	
	public Float daikuan;
	
	/**
	 * 贷款类型
	 */
	public String daikuan_lx;
	
	public String yongtu;
	
	/**
	 * 签约日期
	 */
	@Column(name="datetime")
	public Date signdate;
	
	public String datetime_gh;
	
	public String remark;
	
	public String uid_yw_nm;
	
	public Integer proid;
	
	public Integer yongjin_a1;
	
	public Integer yongjin_a2;
	
	public Integer yongjin_b1;
	
	public Integer yongjin_b2;
}
