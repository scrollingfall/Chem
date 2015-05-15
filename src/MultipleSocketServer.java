import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MultipleSocketServer implements Runnable {
	private static volatile ArrayList<user>users=new ArrayList<user>();
	private static ArrayList<Question>qs=new ArrayList<Question>();
	private static int currentq=0;
	private static volatile HashMap<Socket,user>hm=new HashMap<Socket,user>();
	private Socket connection;
	private String TimeStamp;
	private int ID;
	private static volatile boolean game=true;
	private static volatile boolean waitingforgametostart=true;
	private static volatile boolean accepting=false;
	private boolean accepting2=true;
	private static volatile long starttime;
	private static volatile long endtime;
	private InputStreamReader isr;
	private static JButton finq=new JButton("See Results");
	private static JPanel results=new JPanel();
	private static void initQ()
	{
		
	}
	public static void main(String[]args)
	{
		initQ();
		JFrame frame=new JFrame();
		frame.setSize(200,200);
		frame.setBackground(Color.WHITE);
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
		JButton next=new JButton("Next Question");
		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				frame.remove(results);
				for(user u:users)
				{
					try{
						accepting=true;
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
				//implement creating the question screen
				frame.add(finq);
				frame.repaint();
				frame.revalidate();
				starttime=System.currentTimeMillis();
			}
		});
		results.add(next);
		finq.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				accepting=false;
				endtime=System.currentTimeMillis();
				frame.remove(finq);
				//createresults();
				frame.add(results);
				frame.revalidate();
				frame.repaint();
			}
		});
		results.setLayout(new GridBagLayout());
		//GridBagConstraints c=new GridBagConstraints();
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
	public static void createresults()
	{
		String correct=qs.get(currentq).getCorrectanswer();
		for(user u:users)
		{
			if(u.getAnswer().equals(correct))
			{
				u.addScore(100-(int)((u.getTime()-starttime)/((endtime-starttime)/100)) );
			}
		}
		Collections.sort(users);
	}
	public MultipleSocketServer(Socket s, int i) 
	{
		this.connection = s;
		this.ID = i;
	}
	public void run() {
		//while(waitingforgametostart)
		{
			try {
				BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
				isr = new InputStreamReader(is);
				int character;
				StringBuffer process = new StringBuffer();
				while((character = isr.read()) != 13) {
				//System.out.println(character);
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
				//System.exit(0);
			}
		}
		while(game)
		{
			if(accepting&&accepting2)
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
					System.out.println(u.getUsername()+" "+process.toString());
					accepting2=false;
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
			else if(!accepting)
				accepting2=true;
		}
		try {
			connection.close();
		}
		catch (IOException e){}
	}
}