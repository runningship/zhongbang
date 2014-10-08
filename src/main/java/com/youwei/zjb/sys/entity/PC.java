package com.youwei.zjb.sys.entity;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

/**
 * 授权的电脑
 */
@Entity
@Table(name="uc_pc")
public class PC {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	@Column(name="did")
	public Integer deptId;
	
	@Column(name="lname")
	public String pcname;
	
	public Date addtime;
	
//	@Column(name="code_ma")
	public String baseboard="";
	
	public String cpu="";
	
	public String harddrive="";
	
	public String bios="";
	
	public String uuid="";
	@Column(name="llock")
	public Integer lock;
	
	@Column(name="regnumber")
	public String authCode;
	
	public String beizhu;
	
	public Long ctime;
}
