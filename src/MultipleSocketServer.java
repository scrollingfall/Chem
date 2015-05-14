import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MultipleSocketServer implements Runnable {
	private static ArrayList<user>users=new ArrayList<user>();
	private static HashMap<Socket,user>hm=new HashMap<Socket,user>();
	private Socket connection;
	private String TimeStamp;
	private int ID;
	private static boolean game=true;
	private static boolean waitingforgametostart=true;
	private static boolean accepting=false;
	private static long starttime;
	private InputStreamReader isr;
	private static JButton finq=new JButton("See Results");
	private static JPanel results;
	public static void main(String[]args)
	{
		JFrame frame=new JFrame();
		frame.setSize(100,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton start=new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				frame.remove(start);
				for(user u:users)
				{
					try{
						String returnCode = "start" + (char) 13;
						BufferedOutputStream os = new BufferedOutputStream(u.getSocket().getOutputStream());
						OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
						osw.write(returnCode);
						osw.flush();
					}
					catch (Exception e) {
						System.out.println(e);
					}
				}
				waitingforgametostart=false;
				frame.add(finq);
				frame.repaint();
				frame.revalidate();
				starttime=System.currentTimeMillis();
			}
		});
		finq.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				accepting=false;
				frame.remove(finq);
				frame.add(start);
				frame.revalidate();
				frame.repaint();
			}
		});
		frame.add(start);
		frame.setVisible(true);
		int port = 19999;
		int count = 0;
		try{
			ServerSocket socket1 = new ServerSocket(port);
			System.out.println("MultipleSocketServer Initialized");
			while (true) {
				Socket connection = socket1.accept();
				Runnable runnable = new MultipleSocketServer(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		}
		catch (Exception e) {}
	}
	public MultipleSocketServer(Socket s, int i) 
	{
		this.connection = s;
		this.ID = i;
	}
	public void run() {
		while(waitingforgametostart)
		{
			try {
				BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
				isr = new InputStreamReader(is);
				int character;
				StringBuffer process = new StringBuffer();
				while((character = isr.read()) != 13) {
					process.append((char)character);
				}
				user u=new user(process.toString(),connection);
				users.add(u);
				hm.put(connection, u);
				System.out.println(process);
				/*
			TimeStamp = new java.util.Date().toString();
			String returnCode = "MultipleSocketServer repsonded at "+ TimeStamp + (char) 13;
			BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
			osw.write(returnCode);
			osw.flush();
				 */
			}
			catch (Exception e) {
				System.out.println(e);
			}
		}
		while(game)
		{
			if(accepting)
			{
				try {
					int character;
					StringBuffer process = new StringBuffer();
					while((character = isr.read()) != 13) {
						process.append((char)character);
					}
					user u=hm.get(connection);
					u.setTime(System.currentTimeMillis());
					u.setAnswer(process.toString());
					/*
				TimeStamp = new java.util.Date().toString();
				String returnCode = "MultipleSocketServer repsonded at "+ TimeStamp + (char) 13;
				BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
				OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
				osw.write(returnCode);
				osw.flush();
					 */
				}
				catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		try {
			connection.close();
		}
		catch (IOException e){}
	}
}