package com.youwei.zjb.oa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NoticeReceiver {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer receiverId;
	
	public Integer noticeId;
	
	public Integer read;
}
