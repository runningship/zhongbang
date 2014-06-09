package com.youwei.zjb.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 操作记录表
 */
@Entity
@Table(name="sys_record")
public class OperRecord {

	@Id
	public Integer id;
	
	public Integer cid;
	
	public Integer qid;
	
	public Integer did;
	
	public Integer uid;
	
	public String uname;
	
	public String dname;
	
	public String qname;
	
	public String cname;
	
	public Date addtime;
	
	public String ip;
	
	public String pcma;
	
	public String fenlei;
	
	/**
	 * 操作内容
	 */
	public String conts;
	
	public String beizhu;
	
	public Integer yesno;
}
