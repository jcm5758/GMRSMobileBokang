package com.geurimsoft.bokangnew.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.geurimsoft.bokangnew.conf.AppConfig;
 
import android.util.Log;

public class SocketClient extends Thread
{
	
	private String serverIP;	// 서버 IP
	private int port;			// 소켓 통신용 포트번호
	private String msg;			// 전송 메시지
	private String encryptCode;	// 암호화 코드
	private String returnString = null;
	
	public SocketClient(String serverIP, int port, String msg, String encryptCode) 
	{
		
		this.serverIP = serverIP;
		this.port = port;
		this.msg = msg;
		this.encryptCode = encryptCode;
		
	}
	
	public String getReturnString()
	{
		return this.returnString;
	}
	
	@Override
	public void run() 
	{
		this.connect();
		super.run();
	}
	
	private void connect()
	{
	
//		Log.d(AppConfig.TAG, "connect() is called.");
		
		Socket soc = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		String str;

		try
		{
			
			// 소켓 연결
			soc = new Socket(this.serverIP, this.port);

			// 서버로부터의 입력 모드
			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));			

			// 서버로의 전송 모드
			out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			
			out.write(AES.encrypt(msg, encryptCode) + "\n");
			out.flush();
			
			// 서버로부터의 입력 받아오기
			str = in.readLine();
			if (str != null)
			{
				returnString = AES.decrypt(str, encryptCode);
			}
			
			// 종료 메시지
			out.write("exit\n");
			out.flush();
			
			// 접속 끊기
			soc.close();
			
		}
		catch (Exception ex)
		{
			Log.d(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : connect() : " + ex.toString());
			return;
		}
		
	}
	
 
}
