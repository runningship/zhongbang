package com.youwei.zjb.contract.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_pact_process_class")
public class ContractProcessClass {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer claId;
	
	public String title;
	
	public String conts;
	
	public Integer isqian;
	
	public Integer cid;
	
	public Integer ordera;
	
}
