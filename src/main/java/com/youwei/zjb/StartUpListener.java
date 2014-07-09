package com.youwei.zjb;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bc.sdak.SessionFactoryBuilder;
import org.bc.web.ModuleManager;
import org.hibernate.cfg.AvailableSettings;

import com.youwei.zjb.entity.User;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.im.IMServer;

public class StartUpListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			IMServer.forceStop();
			System.out.println("IM Server stop.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		initDataSource();
		initModule();
		try {
			IMServer.startUp();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void initModule() {
		ModuleManager.add("com.youwei.zjb");
	}

	public static synchronized void initDataSource(){
		Map<String,String> settings = new HashMap<String , String>();
//		settings.put(AvailableSettings.URL, "jdbc:mysql://localhost:3306/ihouse?characterEncoding=utf-8");
//		settings.put(AvailableSettings.USER, "root");
//		settings.put(AvailableSettings.PASS, "");
		settings.put(AvailableSettings.SHOW_SQL, "true");
//		settings.put(AvailableSettings.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.SQLServer2005Dialect");
		settings.put(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
		settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//		settings.put(AvailableSettings.HBM2DDL_AUTO, "update");
		settings.put(AvailableSettings.POOL_SIZE, "1");
		
		settings.put(AvailableSettings.PROXOOL_XML, "proxool.xml");//相对目录为classes
		settings.put(AvailableSettings.PROXOOL_EXISTING_POOL, "false");
		settings.put(AvailableSettings.PROXOOL_POOL_ALIAS, "mySqlProxool");
		
//		settings.put("annotated.packages", HouseRent.class.getPackage().getName());
		settings.put("annotated.packages", "com.youwei.zjb");
		SessionFactoryBuilder.applySettings(settings);
//		SimpDaoTool.getGlobalCommonDaoService().getUnique(User.class, 0);
	}
}
