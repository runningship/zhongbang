package com.youwei.zjb.im.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contact {

	@Id
	public Integer id;
	
	public Integer ownerId;
	
	public Integer contactId;
	
	public String group;

	public Contact(){
		
	}
	public Contact(Integer id, Integer ownerId, Integer contactId, String group , String cname) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.contactId = contactId;
		this.group = group;
		this.cname = cname;
	}
	public String cname;
}
