package com.youwei.zjb.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class ConfigCache {

	private static Properties props = new Properties();
	
	private static final String confFilePath = "D:\\zjb.zb.properties";
	static{
		load();
	}
	private static void load(){
		try {
			File file = new File(confFilePath);
			if(file.exists()){
				props.load(FileUtils.openInputStream(file));
			}else{
				InputStream is = ConfigCache.class.getResourceAsStream("zjb.zb.properties");
				props.load(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		props.clear();
		load();
	}
	
	public static String get(String key){
		return props.getProperty(key);
	}
	
	public static Set<Object> keySet(){
		return props.keySet();
	}
}
