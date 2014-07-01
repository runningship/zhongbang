package com.youwei.zjb.pact.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="work_pack")
public class Contract {

	@javax.persistence.Id
	public Integer Id;
	
	public Integer userId;
	
	public Integer deptId;
	
	public String bianhao;
	
	public String lxr_f;
	
	public String tel_f;
	
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
	
	public Float daikun;
	
	public String daikun_lx;
	
	public String yongtu;
	
	public Date datetime;
	
	public String datetime_gh;
	
	public String remark;
	
	public String uid_yw_nm;
	
	public Integer proid;
	
	public Integer yongjin_a1;
	
	public Integer yongjin_a2;
	
	public Integer yongjin_b1;
	
	public Integer yongjin_b2;
}
