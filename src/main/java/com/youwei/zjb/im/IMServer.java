package com.youwei.zjb.im;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.bc.sdak.utils.LogUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class IMServer extends WebSocketServer{

	private static IMServer instance =null;
	private IMServer() throws UnknownHostException {
		super(new InetSocketAddress(Inet4Address.getLocalHost(), 9099));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println(conn);
		
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// TODO Auto-generated method stub
		
	}

}
