package com.youwei.zjb.applet;

import java.applet.Applet;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TestApplet extends Applet{
	//keytool -genkey -alias zjb -keyalg RSA -validity 7 -keystore zjb.keystore
	private static final long serialVersionUID = -4544858019601644504L;

	public void init()  
	   {  
	      JLabel label = new JLabel("Not a Hello, World applet,我去d", SwingConstants.CENTER);  
	      add(label);  
	   }
}
