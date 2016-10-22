package communication;

import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.*;
import java.net.*;

public class CommMgr{
	private static CommMgr commMgr = null;

	private static final String HOST = "192.168.7.1"; //Raspberry Pi ip address

	private static final int PORT = 8080;   //Raspberry Pi port

	public static final String MSG_TYPE_ANDROID = "Android, ";
	public static final String MSG_TYPE_ARDUINO = "Arduino, ";

	private static Socket conn = null;

	private static BufferedOutputStream bos = null; 
	private static OutputStreamWriter osw = null;
	private static PrintWriter pw = null;
	private static BufferedWriter bw = null;
	private static BufferedReader br = null;

	private CommMgr(){
	}

	public static CommMgr getCommMgr(){
		if (commMgr == null){
			commMgr = new CommMgr();
		}
		return commMgr;
	}

	// if the time exceeds timeoutInMs, it will fail
	public boolean setConnection(int timeoutInMs){
		System.out.println("Starts to set Connection");
		try{
			conn = new Socket(HOST, PORT);

			bos = new BufferedOutputStream(conn.getOutputStream());
			osw = new OutputStreamWriter(bos);
			pw = new PrintWriter(bos, true);
			bw = new BufferedWriter(osw);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			System.out.println("setConnection() -->"+" Connection established successfully!");

			return true;
		} catch (UnknownHostException e){
			System.out.println("setConnection --> UnknownHostException");
		} catch (IOException e){
			System.out.println("setConnection --> IO Exception");
		} catch (Exception e){
			System.out.println("setConnection --> Exception");
		}

		System.out.println("Failed to establish connection!");
		return false;
	}

	public void closeConnection(){
		try {
			if (bos != null){
				bos.close();
			}
			if (osw != null){
				osw.close();
			}
			if (br != null){
				br.close();
			}

			if (conn != null){
				conn.close();
				conn = null;
			}
			System.out.println("Connection closed!");
		} catch (IOException e){
			System.out.println("closeConnection --> IO Exception");
		} catch (NullPointerException e){
			System.out.println("closeConnection --> Null Pointer Exception");
		} catch (Exception e){
			System.out.println("closeConnection --> Exception");
		}
	}

	public boolean sendMsg(String msg, String msgType){ 
		try {
			String outputMsg = msgType + msg;

			System.out.println("Sending out message: " + outputMsg);

			bw.write(outputMsg);
			bw.flush();

			// try{
			// 	TimeUnit.MILLISECONDS.sleep(500);
			// }
			// catch(InterruptedException e)
			// {
			//      System.out.println("send msg sleeping error!!!!!!");
			// } 

			// String ack = recvMsg();
			// while(!ack.equals("A")){
			// 	System.out.println("NOT ACK!!!");
			// 	ack = recvMsg();
			// }
			return true;

		} catch (IOException e){
			System.out.println("send message --> IO Exception");
		} catch (Exception e){
			System.out.println("send message --> Exception");
		}

		return false;
	}

	public String recvMsg(){
		System.out.println("Waiting for receiving msg!");
		try{
			String input = br.readLine();
			if (input != null && input.length() > 0){
				System.out.println(input);
				return input;
			}
		} catch (IOException e){
			System.out.println("receive message --> IO Exception");
		} catch (Exception e){
			System.out.println("receive message --> Exception");
		}

		return null;
	}

	public String recvSensorData(){
		Timer timer = new Timer();
		System.out.println("Waiting for sensor data!");
		try{
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					System.out.println("No response for sensor data");
					sendMsg("S","PC2AR");
				}
			}, new Date(), 3*1000);
			String input = br.readLine();
			if (input != null && input.length() > 0){
				System.out.println(input);
				timer.cancel();
				return input;
			}
		} catch (IOException e){
			System.out.println("receive message --> IO Exception");
		} catch (Exception e){
			System.out.println("receive message --> Exception");
		}

		return null;
	}

	public boolean isConnected(){
		return conn.isConnected();
	}
 
}

