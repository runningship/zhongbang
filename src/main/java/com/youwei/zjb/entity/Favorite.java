package com.youwei.zjb.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Favorite {

	@Id
	public Integer id;
	
	public Integer userId;
	
	public Integer houseId;
}
