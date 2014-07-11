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
	public Integer id;
	
	/**
	 * 添加人
	 */
	@Column(name="uid")
	public Integer userId;
	
	/**
	 * 业务员
	 */
	@Column(name="uid_yw")
	public Integer ywUserId;
	
	/**
	 * 添加人部门
	 */
	@Column(name="did")
	public Integer deptId;
	
	/**
	 * 业务员部门
	 */
	@Column(name="did_yw")
	public Integer ywDeptId;
	
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
	
	/**
	 * 5 出售,6 出租
	 */
	@Column(nullable=false)
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
	
	/**
	 * 预约事项
	 */
	@Column(name="datetime_gh")
	public String otherItems;
	
	public String remark;
	
	public String uid_yw_nm;
	
	/**
	 * 当前步骤
	 */
	public Integer proid;
	
	/**
	 * 甲方应收佣金
	 */
	public Integer yongjin_a1;
	
	/**
	 * 甲方实收佣金
	 */
	public Integer yongjin_a2;
	
	/**
	 * 乙方应收佣金
	 */
	public Integer yongjin_b1;
	
	/**
	 * 乙方实收佣金
	 */
	public Integer yongjin_b2;
}
