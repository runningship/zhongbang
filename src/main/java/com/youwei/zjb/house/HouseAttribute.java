package com.youwei.zjb.house;

public enum HouseAttribute {

	私盘(7), 
	公盘( 6), 
	特盘(4), 
	封盘( 5),
	钥匙房(2);
    private int code;  
    // 构造方法  
    private HouseAttribute(int code) {  
        this.code = code;  
    }
	public int getCode() {
		return code;
	}  
	
}
