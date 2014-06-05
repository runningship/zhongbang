package org.bc.dietary.test.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import sun.misc.BASE64Encoder;

import com.youwei.zjb.util.SecurityHelper;

public class JPushTest {

	@Test
	public void testPush() throws IOException{
		String master_secret="2b575a1dd863f6890d30b25e";
//		String md5 = SecurityHelper.Md5("654321"+"3"+""+master_secret).toUpperCase();
		URL url = new URL("https://api.jpush.cn/v3/push");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// 发送POST请求必须设置如下两行
//		conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        BASE64Encoder encoder = new BASE64Encoder();
        conn.setRequestProperty("Authorization", "Basic "+encoder.encode(master_secret.getBytes()));
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        JSONObject params = new JSONObject();
        params.put("platform", "android");
        JSONObject audience = new JSONObject();
        JSONArray alias = new JSONArray();
        alias.add("123");
        audience.put("alias", alias);
        params.put("audience", audience);
        
        JSONObject notification = new JSONObject();
        notification.put("alert", "hello zjb");
        params.put("notification", notification);
        
        osw.write(params.toString());
        osw.flush();
        osw.close();
        conn.disconnect();
	}
}
