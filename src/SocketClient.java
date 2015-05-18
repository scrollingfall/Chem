import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
public class SocketClient {
	public static void main(String[] args) {
		new SocketClient();
	}
	private JFrame frame=new JFrame();
	private JPanel login=new JPanel();
	private JTextField hostname=new JTextField();
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
	private volatile String input="";
	private volatile boolean noanswerchosen=true;
	private volatile boolean temp1=true;
	private volatile boolean invalidhostname=true;
	private volatile String host="";
	private volatile Socket connection;
	private volatile int port=19999;
	public void gui()
	{
		frame.setSize(400,200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		submituser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(username.getText().length()!=0&&username.getText().length()<=20)
				{
					username.setEditable(false);
					user=username.getText();
					frame.remove(login);
					frame.add(label);
					cansubmit=true;
					frame.repaint();
					frame.revalidate();
					try
					{
						host=hostname.getText();
						InetAddress address = InetAddress.getByName(host);
						/** Establish a socket connection */
						connection = new Socket(address, port);
						System.out.println("Socket Created, waiting to submit username");
						invalidhostname=false;
					}
					catch (Exception e2)
					{
						System.out.println(e2);
						invalidhostname=true;
						JOptionPane.showMessageDialog(frame,"Invalid Host Name: Please Try Again");
						cansubmit=false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Invalid Username: Usernames must contain at least \none character and no more than 20 characters");
					username.setText("");
				}
			}
		});
		login.setLayout(new GridBagLayout());
		JLabel hnl=new JLabel("Host Name:");
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=0.25;
		constraints.weighty=0.33;
		login.add(hnl,constraints);
		JLabel unl=new JLabel("Username:");
		constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=1;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=0.25;
		constraints.weighty=0.33;
		login.add(unl,constraints);
		constraints=new GridBagConstraints();
		constraints.gridx=1;
		constraints.gridy=0;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=0.75;
		constraints.weighty=0.33;
		login.add(hostname,constraints);
		constraints=new GridBagConstraints();
		constraints.gridx=1;
		constraints.gridy=1;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=0.75;
		constraints.weighty=0.33;
		login.add(username,constraints);
		constraints=new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=2;
		constraints.gridheight=1;
		constraints.gridwidth=2;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=1;
		constraints.weighty=0.33;
		login.add(submituser,constraints);
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
		//host = "localhost";
		//host="1589S22";
		/** Define a port */
		//int port = 19999;
		while(invalidhostname){}
		String TimeStamp;
		System.out.println("SocketClient initialized");
		try {
			/** Obtain an address object of the server */
			InetAddress address = InetAddress.getByName(host);
			/** Establish a socket connection */
			connection = new Socket(address, port);
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
			isr=new InputStreamReader(bis,"US-ASCII");
			Thread t=new Thread(new Runnable(){
				public void run()
				{
					try {
						while(true)
						{
							StringBuffer instr=new StringBuffer();
							int c=0;
							while((c=isr.read())!=13)
								instr.append((char)c);
							input=instr.toString();
						}
					} catch (IOException e) {
						System.out.println(e);
						return;
					}
				}
			});
			t.start();
			while(cont)
			{
				/**Read the socket's InputStream and append to a StringBuffer */
				//isr = new InputStreamReader(bis, "US-ASCII");
				//StringBuffer instr = new StringBuffer();
				//int c=0;
				//System.out.println("Waiting for server instructions");
				//while ( (c = isr.read()) != 13)
				//	instr.append( (char) c);
				//while((c=isr.read())!=-1){};
				while(input.equals("")){} //waits for instruction
				System.out.println("Server instruction recieved");
				if(input.equals("game"))
				{
					input="";
					cont=false;
				}
				else if(input.toString().equals("start"))
				{
					input="";
					System.out.println("waiting to submit answer");
					frame.remove(label);
					label.setText("Waiting");
					frame.add(choices);
					frame.repaint();
					frame.revalidate();
					//Scanner s=new Scanner(bis);
					//Thread t=new Thread(new Runnable(){
					//		public void run()
					//		{
					//			while (!s.hasNext()) {}
					//			String input = s.nextLine();
					//			System.out.println(input);
					//		}
					//	});
					//	t.start();
					while(noanswerchosen){
						if(input.equals("endq"))
						{
							input="";
							frame.remove(choices);
							label.setText("You didn't choose an answer.");
							frame.add(label);
							frame.revalidate();
							frame.repaint();
							System.out.println("no ");
							temp1=false;
							noanswerchosen=false;
						}
					}//System.out.print("");}
					System.out.println("answer chosen");
					if(!temp1)
					{
						noanswerchosen=true;
						temp1=true;
						osw.write("E"+(char)13);
						osw.flush();
					}
					else
					{
						noanswerchosen=true;
						osw.write(answer+(char)13);
						osw.flush();
						answer="";
					}
				}
				else if(input.substring(0,3).equals("sco"))
				{
					label.setText("Your current score is "+input.split(" ")[1]);
					input="";
				}
				//instr.delete(0, instr.length());
			}
			/** Close the socket connection. */
			connection.close();
			//System.out.println(instr);
		}
		//catch (IOException f) {
		//	System.out.println("IOException: " + f);
		//}
		catch (Exception g) {
			System.out.println("Exception: " + g);
			System.exit(0);
		}
	}
}