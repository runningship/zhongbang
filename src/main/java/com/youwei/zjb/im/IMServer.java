package com.youwei.zjb.im;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.youwei.zjb.im.entity.Message;

public class IMServer extends WebSocketServer{

	private static IMServer instance =null;
	private static Map<Integer,WebSocket> conns = new HashMap<Integer,WebSocket>();
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	private IMServer() throws UnknownHostException {
		super(new InetSocketAddress(Inet4Address.getByName("localhost"), 9099));
	}

	public static void startUp() throws UnknownHostException{
		if(instance==null){
			instance = new IMServer();
		}
		instance.start();
		LogUtil.info("IM server started on port 9099");
	}
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Integer removed = null;
		for(Integer key : conns.keySet()){
			if(conns.get(key) == conn){
				removed = key;
				break;
			}
		}
		if(removed!=null){
			conns.remove(removed);
		}
		System.out.println(conn+" removed ");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println(message);
		JSONObject data = JSONObject.fromObject(message);
		if("login".equals(data.getString("type"))){
			conns.put(data.getInt("userId"), conn);
		}if("msg".equals(data.getString("type"))){
			sendMsg(conn,data);
		}
	}

	private void sendMsg(WebSocket conn, JSONObject data) {
		Integer recvId = data.getInt("reveiverId");
		if(recvId==null){
			conn.send("无效的消息体，接受者不能为空");
		}
		data.put("sendTime", System.currentTimeMillis());
		WebSocket recv = conns.get(recvId);
		if(recv==null){
			
		}
		
		Integer recvType = data.getInt("receiverType");
		if(recvType==1){
			//个人
			recv.send(data.toString());
		}
		Message dbMsg = new Message();
		dbMsg.addtime = new Date();
		dbMsg.content = data.getString("content");
		dbMsg.senderId = data.getInt("senderId");
		dbMsg.receiverId = recvId;
		dbMsg.receiverType=1;
		dao.saveOrUpdate(dbMsg);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

}
