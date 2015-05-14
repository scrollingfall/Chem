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
	private boolean cansubmit=false;
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
	private boolean noanswerchosen=true;
	public void gui()
	{
		frame.setSize(400,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		submituser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(username.getText().length()!=0)
				{
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
				frame.add(label);
				frame.revalidate();
				frame.repaint();
			}
		});
		frame.setVisible(true);
	}
	public SocketClient(){
		gui();
		network();
	}
	public void network(){
		/** Define a host server */
		String host = "localhost";
		/** Define a port */
		int port = 19999;
		StringBuffer instr = new StringBuffer();
		String TimeStamp;
		System.out.println("SocketClient initialized");
		try {
			/** Obtain an address object of the server */
			InetAddress address = InetAddress.getByName(host);
			/** Establish a socket connection */
			Socket connection = new Socket(address, port);
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
			while(cont)
			{
				BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
				/**Instantiate an InputStreamReader with the optional
				 * character encoding.
				 */

				isr = new InputStreamReader(bis, "US-ASCII");

				/**Read the socket's InputStream and append to a StringBuffer */
				int c;
				while ( (c = isr.read()) != 13)
					instr.append( (char) c);
				if(instr.toString().equals("endgame"))
					cont=false;
				else if(instr.toString().equals("start"))
				{
					while(noanswerchosen)
					{}
					noanswerchosen=true;
					osw.write(answer);
					osw.flush();
					answer="";
				}
			}
			/** Close the socket connection. */
			connection.close();
			System.out.println(instr);
		}
		catch (IOException f) {
			System.out.println("IOException: " + f);
		}
		catch (Exception g) {
			System.out.println("Exception: " + g);
		}
	}
}