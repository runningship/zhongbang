package com.youwei.zjb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="uc_auth")
public class AuthCode {

	@Id
	public Integer id;
	
	public String authCode;
	
	public Integer num;
	
	@Column(name="sh")
	public Integer review;
	
	public Integer fidn;
}
