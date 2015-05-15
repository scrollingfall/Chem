import java.net.*;
import java.io.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
public class SocketClient {
	public static void main(String[] args) {
		new SocketClient();
	}
	private JFrame frame=new JFrame();
	private JPanel login=new JPanel();
	private JTextField username=new JTextField();
	private JButton submituser=new JButton("Connect!");
	private volatile boolean cansubmit=false;
	private boolean cont=true;
	private String user="";
	private String answer="";
	private JLabel label=new JLabel("Waiting");
	private JPanel choices=new JPanel();
	private JButton a=new JButton("A");
	private JButton b=new JButton("B");
	private JButton c=new JButton("C");
	private JButton d=new JButton("D");
	private OutputStreamWriter osw;
	private InputStreamReader isr;
	private volatile boolean noanswerchosen=true;
	public void gui()
	{
		frame.setSize(400,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		submituser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(username.getText().length()!=0)
				{
					username.setEditable(false);
					user=username.getText();
					frame.remove(login);
					frame.add(label);
					cansubmit=true;
					frame.repaint();
					frame.revalidate();
				}
			}
		});
		login.setLayout(new GridLayout(2,1));
		login.add(username);
		login.add(submituser);
		frame.add(login);
		choices.setLayout(new GridLayout(1,4));
		a.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				answer="A";
				noanswerchosen=false;
				frame.remove(choices);
				label.setText("You chose A");
				frame.add(label);
				frame.revalidate();
				frame.repaint();
			}
		});
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				answer="B";
				noanswerchosen=false;
				frame.remove(choices);
				label.setText("You chose B");
				frame.add(label);
				frame.revalidate();
				frame.repaint();
			}
		});
		c.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				answer="C";
				noanswerchosen=false;
				frame.remove(choices);
				label.setText("You chose C");
				frame.add(label);
				frame.revalidate();
				frame.repaint();
			}
		});
		d.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				answer="D";
				noanswerchosen=false;
				frame.remove(choices);
				label.setText("You chose D");
				frame.add(label);
				frame.revalidate();
				frame.repaint();
			}
		});
		choices.add(a);
		choices.add(b);
		choices.add(c);
		choices.add(d);
		frame.setVisible(true);
	}
	public SocketClient(){
		gui();
		network();
	}
	public void network(){
		/** Define a host server */
		//String host = "localhost";
		String host="1589S22";
		/** Define a port */
		int port = 19999;
		
		String TimeStamp;
		System.out.println("SocketClient initialized");
		try {
			/** Obtain an address object of the server */
			InetAddress address = InetAddress.getByName(host);
			/** Establish a socket connection */
			Socket connection = new Socket(address, port);
			System.out.println("Socket Created, waiting to submit username");
			while(!cansubmit){}
			/** Instantiate a BufferedOutputStream object */
			BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
			/** Instantiate an OutputStreamWriter object with the optional character
			 * encoding.
			 */
			osw = new OutputStreamWriter(bos, "US-ASCII");
			//TimeStamp = Long.toString(System.currentTimeMillis());
			String process = user+(char)13;
			/** Write across the socket connection and flush the buffer */
			osw.write(process);
			osw.flush();
			/** Instantiate a BufferedInputStream object for reading
	            /** Instantiate a BufferedInputStream object for reading
			 * incoming socket streams.
			 */
			System.out.println("Username submitted");	
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			/**Instantiate an InputStreamReader with the optional
			 * character encoding.
			 */
			
			while(cont)
			{
				/**Read the socket's InputStream and append to a StringBuffer */
				isr = new InputStreamReader(bis, "US-ASCII");
				StringBuffer instr = new StringBuffer();
				int c=0;
				System.out.println("Waiting for server instructions");
				while ( (c = isr.read()) != 13)
					instr.append( (char) c);
				//while((c=isr.read())!=-1){};
				System.out.println("Server instruction recieved");
				if(instr.toString().equals("endgame"))
					cont=false;
				else if(instr.toString().equals("start"))
				{
					System.out.println("waiting to submit answer");
					frame.remove(label);
					frame.add(choices);
					frame.repaint();
					frame.revalidate();
					while(noanswerchosen){}//System.out.print("");}
					System.out.println("answer chosen");
					noanswerchosen=true;
					osw.write(answer+(char)13);
					osw.flush();
					answer="";
				}
				instr.delete(0, instr.length());
			}
			/** Close the socket connection. */
			connection.close();
			//System.out.println(instr);
		}
		catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
	}
}