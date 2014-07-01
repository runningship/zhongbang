package com.youwei.zjb.pact.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_pack_process_class")
public class ContractProcessClass {
	
	@Id
	public Integer id;
	
	public Integer claId;
	
	public String title;
	
	public String conts;
	
	public Integer isqian;
	
	public Integer cid;
	
	public Integer ordera;
	
}
