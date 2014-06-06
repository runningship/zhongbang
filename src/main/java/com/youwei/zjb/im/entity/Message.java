package com.youwei.zjb.im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Message {
	
	@Id
	public Integer id;
	
	public Integer senderId;
	
	public Integer receiverId;
	
	/**
	 * 1,个人 2,群组
	 */
	public Integer receiverType;
	
	public String content;
	
	public Date addtime;
}
