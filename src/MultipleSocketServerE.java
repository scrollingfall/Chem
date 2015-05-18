import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class MultipleSocketServerE implements Runnable {
	private static volatile ArrayList<user>users=new ArrayList<user>();
	private static ArrayList<Question>qs=new ArrayList<Question>();
	private static int currentq=0;
	private static volatile HashMap<Socket,user>hm=new HashMap<Socket,user>();
	private Socket connection;
	//private String TimeStamp;
	private int ID;
	private static volatile boolean game=true;
	//private static volatile boolean waitingforgametostart=true;
	private static volatile boolean accepting=false;
	private volatile boolean accepting2=true;
	private static volatile long starttime;
	private static volatile long endtime;
	private InputStreamReader isr;
	private static JButton finq=new JButton("See Results");
	private static JPanel results=new JPanel();
	private static ServerSocket socket1;
	private static void initQ()
	{
		try {
			Scanner scan=new Scanner(new File("questions.txt"));
			int t=Integer.parseInt(scan.nextLine());
			for(int i=0;i<t;i++)
			{
				String q=scan.nextLine();
				boolean b=Boolean.parseBoolean(scan.nextLine());
				String filename="";
				if(b)
					filename=scan.nextLine();
				ArrayList<String>anss=new ArrayList<String>();
				anss.add(scan.nextLine());
				anss.add(scan.nextLine());
				anss.add(scan.nextLine());
				anss.add(scan.nextLine());
				String ca=scan.nextLine();
				if(b)
					qs.add(new Question(q,anss,ca, filename));
				else
					qs.add(new Question(q,anss,ca));
			}
			scan.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	private static JLabel scoreboard, rank, name,score,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s1n,s2n,s3n,s4n,s5n,s6n,s7n,s8n,s9n,s10n,s1s,s2s,s3s,s4s,s5s,s6s,s7s,s8s,s9s,s10s;
	//private static JPanel leftside;
	private static JPanel qpanel=new JPanel();
	private static JPanel qqpanel=new JPanel();
	private static JLabel correctansis,numa,numb,numc,numd,nume;
	private static ArrayList<JLabel>labels;
	private static volatile boolean acceptingnewconnections=true;
	public static void main(String[]args)
	{
		initQ();
		qpanel.setLayout(new GridBagLayout());
		qpanel.setBackground(Color.WHITE);
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=0;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.2;
		c.weighty=0.2;
		qpanel.add(finq,c);


		results.setLayout(new GridBagLayout());
		results.setBackground(Color.WHITE);


		/*/
		c=new GridBagConstraints();
		leftside=new JPanel();
		leftside.setLayout(new GridBagLayout());
		leftside.setBackground(Color.WHITE);
		leftside.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=12;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.6;
		results.add(leftside, c);
		//*/
		c=new GridBagConstraints();
		correctansis=new JLabel("The correct answer was "+qs.get(currentq).getCorrectanswer());
		correctansis.setHorizontalAlignment(JLabel.CENTER);
		//correctansis.setVerticalAlignment(JLabel.CENTER);
		correctansis.setFont(new Font("Times New Roman",Font.PLAIN,36));
		correctansis.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(correctansis,c);
		c=new GridBagConstraints();
		numa=new JLabel(" people chose A");
		numa.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		numa.setFont(new Font("Times New Roman",Font.PLAIN,30));
		numa.setHorizontalAlignment(JLabel.CENTER);
		c.gridx=0;
		c.gridy=2;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(numa,c);
		c=new GridBagConstraints();
		numb=new JLabel(" people chose B");
		numb.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		numb.setFont(new Font("Times New Roman",Font.PLAIN,30));
		numb.setHorizontalAlignment(JLabel.CENTER);
		c.gridx=0;
		c.gridy=4;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(numb,c);
		c=new GridBagConstraints();
		numc=new JLabel(" people chose C");
		numc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		numc.setFont(new Font("Times New Roman",Font.PLAIN,30));
		numc.setHorizontalAlignment(JLabel.CENTER);
		c.gridx=0;
		c.gridy=6;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(numc,c);
		c=new GridBagConstraints();
		numd=new JLabel(" people chose D");
		numd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		numd.setFont(new Font("Times New Roman",Font.PLAIN,30));
		numd.setHorizontalAlignment(JLabel.CENTER);
		c.gridx=0;
		c.gridy=8;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(numd,c);
		c=new GridBagConstraints();
		nume=new JLabel(" people didn't answer");
		nume.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nume.setFont(new Font("Times New Roman",Font.PLAIN,30));
		nume.setHorizontalAlignment(JLabel.CENTER);
		c.gridx=0;
		c.gridy=10;
		c.gridwidth=1;
		c.gridheight=2;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.1;
		results.add(nume,c);

		c=new GridBagConstraints();
		scoreboard=new JLabel("High Scores");
		scoreboard.setHorizontalAlignment(JLabel.CENTER);
		scoreboard.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		scoreboard.setFont(new Font("Times New Roman",Font.PLAIN,36));
		//scoreboard.setBackground(Color.WHITE);
		c.gridx=1;
		c.gridy=0;
		c.gridwidth=3;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.5;
		c.weighty=0.05;
		results.add(scoreboard,c);
		c=new GridBagConstraints();
		rank=new JLabel("Rank");
		c.gridx=1;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(rank,c);
		c=new GridBagConstraints();
		name=new JLabel("Username");
		c.gridx=2;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(name,c);
		c=new GridBagConstraints();
		score=new JLabel("Score");
		c.gridx=3;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(score,c);
		c=new GridBagConstraints();
		s1=new JLabel("1");
		c.gridx=1;
		c.gridy=2;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s1,c);
		c=new GridBagConstraints();
		s2=new JLabel("2");
		c.gridx=1;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s2,c);
		c=new GridBagConstraints();
		s3=new JLabel("3");
		c.gridx=1;
		c.gridy=4;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s3,c);
		c=new GridBagConstraints();
		s4=new JLabel("4");
		c.gridx=1;
		c.gridy=5;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s4,c);
		c=new GridBagConstraints();
		s5=new JLabel("5");
		c.gridx=1;
		c.gridy=6;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s5,c);
		c=new GridBagConstraints();
		s6=new JLabel("6");
		c.gridx=1;
		c.gridy=7;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s6,c);
		c=new GridBagConstraints();
		s7=new JLabel("7");
		c.gridx=1;
		c.gridy=8;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s7,c);
		c=new GridBagConstraints();
		s8=new JLabel("8");
		c.gridx=1;
		c.gridy=9;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s8,c);
		c=new GridBagConstraints();
		s9=new JLabel("9");
		c.gridx=1;
		c.gridy=10;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s9,c);
		c=new GridBagConstraints();
		s10=new JLabel("10");
		c.gridx=1;
		c.gridy=11;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.04;
		c.weighty=0.05;
		results.add(s10,c);

		c=new GridBagConstraints();
		s1n=new JLabel();
		c.gridx=2;
		c.gridy=2;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s1n,c);
		c=new GridBagConstraints();
		s2n=new JLabel();
		c.gridx=2;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s2n,c);
		c=new GridBagConstraints();
		s3n=new JLabel();
		c.gridx=2;
		c.gridy=4;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s3n,c);
		c=new GridBagConstraints();
		s4n=new JLabel();
		c.gridx=2;
		c.gridy=5;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s4n,c);
		c=new GridBagConstraints();
		s5n=new JLabel();
		c.gridx=2;
		c.gridy=6;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s5n,c);
		c=new GridBagConstraints();
		s6n=new JLabel();
		c.gridx=2;
		c.gridy=7;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s6n,c);
		c=new GridBagConstraints();
		s7n=new JLabel();
		c.gridx=2;
		c.gridy=8;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s7n,c);
		c=new GridBagConstraints();
		s8n=new JLabel();
		c.gridx=2;
		c.gridy=9;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s8n,c);
		c=new GridBagConstraints();
		s9n=new JLabel();
		c.gridx=2;
		c.gridy=10;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s9n,c);
		c=new GridBagConstraints();
		s10n=new JLabel();
		c.gridx=2;
		c.gridy=11;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s10n,c);

		c=new GridBagConstraints();
		s1s=new JLabel();
		c.gridx=3;
		c.gridy=2;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s1s,c);
		c=new GridBagConstraints();
		s2s=new JLabel();
		c.gridx=3;
		c.gridy=3;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s2s,c);
		c=new GridBagConstraints();
		s3s=new JLabel();
		c.gridx=3;
		c.gridy=4;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s3s,c);
		c=new GridBagConstraints();
		s4s=new JLabel();
		c.gridx=3;
		c.gridy=5;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s4s,c);
		c=new GridBagConstraints();
		s5s=new JLabel();
		c.gridx=3;
		c.gridy=6;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s5s,c);
		c=new GridBagConstraints();
		s6s=new JLabel();
		c.gridx=3;
		c.gridy=7;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s6s,c);
		c=new GridBagConstraints();
		s7s=new JLabel();
		c.gridx=3;
		c.gridy=8;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s7s,c);
		c=new GridBagConstraints();
		s8s=new JLabel();
		c.gridx=3;
		c.gridy=9;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s8s,c);
		c=new GridBagConstraints();
		s9s=new JLabel();
		c.gridx=3;
		c.gridy=10;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s9s,c);
		c=new GridBagConstraints();
		s10s=new JLabel();
		c.gridx=3;
		c.gridy=11;
		c.gridwidth=1;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.23;
		c.weighty=0.05;
		results.add(s10s,c);

		c=new GridBagConstraints();
		JButton next=new JButton("Next Question");
		c.gridx=0;
		c.gridy=12;
		c.gridwidth=4;
		c.gridheight=1;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=0.05;
		results.add(next,c);

		labels=new ArrayList<JLabel>();
		//labels.add(scoreboard);
		labels.add(rank);
		labels.add(name);
		labels.add(score);
		labels.add(s1);
		labels.add(s2);
		labels.add(s3);
		labels.add(s4);
		labels.add(s5);
		labels.add(s6);
		labels.add(s7);
		labels.add(s8);
		labels.add(s9);
		labels.add(s10);
		labels.add(s1n);
		labels.add(s2n);
		labels.add(s3n);
		labels.add(s4n);
		labels.add(s5n);
		labels.add(s6n);
		labels.add(s7n);
		labels.add(s8n);
		labels.add(s9n);
		labels.add(s10n);
		labels.add(s1s);
		labels.add(s2s);
		labels.add(s3s);
		labels.add(s4s);
		labels.add(s5s);
		labels.add(s6s);
		labels.add(s7s);
		labels.add(s8s);
		labels.add(s9s);
		labels.add(s10s);
		for(JLabel label:labels)
		{
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			//label.setBackground(Color.WHITE);
			label.setVerticalAlignment(JLabel.TOP);
			label.setFont(new Font("Times New Roman",Font.PLAIN,30));
		}

		JFrame frame=new JFrame();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		//frame.setSize(200,200);
		frame.setBackground(Color.WHITE);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					for(user u:users)
					{
						try{
							String returnCode = "game" + (char) 13;
							BufferedOutputStream os = new BufferedOutputStream(u.getSocket().getOutputStream());
							OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
							osw.write(returnCode);
							osw.flush();
							socket1.close();
						}
						catch (Exception e1) {
							System.out.println(e1);
						}
					}
					System.exit(0);
				}
			}
		});
		JButton start=new JButton();
		try {
			start.setText("<html><center>Host Name: "+InetAddress.getLocalHost().getHostName()+"<br><br><br>Click To Start Game</center></html>");
		} catch (Exception e1) {
			System.out.println(e1);
			System.exit(0);
		}
		start.setFont(new Font("Times New Roman",Font.PLAIN,96));
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				acceptingnewconnections=false;
				try {
					Socket temporaryconnection=new Socket("localhost", 19999);
					temporaryconnection.close();
				} catch (Exception e1) {
					System.out.println("fake client attempted to end .accept() method: "+e1);
				}
				frame.remove(start);
				Question q=qs.get(0);
				qqpanel=new JPanel();
				qqpanel.setBackground(Color.WHITE);
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=1;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				c.weightx=0.2;
				c.weighty=0.8;
				if(q.isHasImage())
				{
					qqpanel.setLayout(new GridLayout(1,2));
					JTextArea ta=new JTextArea();
					ta.setEditable(false);
					ta.setFont(new Font("Times New Roman", Font.PLAIN,42));
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
					ta.append(q.getQuestion()+"\n\n");
					ArrayList<String>temp=q.getAnswers();
					for(int i=0;i<temp.size();i++)
						ta.append((char)(i+65)+": "+temp.get(i)+"\n");
					qqpanel.add(ta);
					JLabel picLabel=new JLabel(new ImageIcon(q.getImage()));
					qqpanel.add(picLabel);
				}
				else
				{
					qqpanel.setLayout(new GridLayout(1,1));
					JTextArea ta=new JTextArea();
					ta.setEditable(false);
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
					ta.setFont(new Font("Times New Roman", Font.PLAIN,42));
					ta.append(q.getQuestion()+"\n\n");
					ArrayList<String>temp=q.getAnswers();
					for(int i=0;i<temp.size();i++)
						ta.append((char)(i+65)+": "+temp.get(i)+"\n");
					qqpanel.add(ta);
				}			
				qpanel.add(qqpanel,c);
				//currentq++;
				frame.add(qpanel);
				frame.repaint();
				frame.revalidate();
				starttime=System.currentTimeMillis();
				accepting=true; 
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
				//waitingforgametostart=false;

			}
		});

		next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				currentq++;
				if(currentq==qs.size())
				{
					frame.setVisible(false);
					for(user u:users)
					{
						try{
							String returnCode = "game" + (char) 13;
							BufferedOutputStream os = new BufferedOutputStream(u.getSocket().getOutputStream());
							OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
							osw.write(returnCode);
							osw.flush();
							socket1.close();
						}
						catch (Exception e) {
							System.out.println(e);
						}
					}
					System.exit(0);
				}
				frame.remove(results);
				if(currentq==qs.size()-1)
				{
					next.setText("Finish Game");
				}
				Question q=qs.get(currentq);
				qpanel.remove(qqpanel);
				qqpanel=new JPanel();
				qqpanel.setBackground(Color.WHITE);
				GridBagConstraints c=new GridBagConstraints();
				c.gridx=0;
				c.gridy=0;
				c.gridwidth=1;
				c.gridheight=1;
				c.fill=GridBagConstraints.BOTH;
				c.weightx=0.2;
				c.weighty=0.8;

				if(q.isHasImage())
				{
					qqpanel.setLayout(new GridLayout(1,2));
					JTextArea ta=new JTextArea();
					ta.setEditable(false);
					ta.setFont(new Font("Times New Roman", Font.PLAIN,42));
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
					ta.append(q.getQuestion()+"\n\n");
					ArrayList<String>temp=q.getAnswers();
					for(int i=0;i<temp.size();i++)
						ta.append((char)(i+65)+": "+temp.get(i)+"\n");
					qqpanel.add(ta);
					JLabel picLabel=new JLabel(new ImageIcon(q.getImage()));
					qqpanel.add(picLabel);
				}
				else
				{
					qqpanel.setLayout(new GridLayout(1,1));
					JTextArea ta=new JTextArea();
					ta.setEditable(false);
					ta.setLineWrap(true);
					ta.setWrapStyleWord(true);
					ta.setFont(new Font("Times New Roman", Font.PLAIN,42));
					ta.append(q.getQuestion()+"\n\n");
					ArrayList<String>temp=q.getAnswers();
					for(int i=0;i<temp.size();i++)
						ta.append((char)(i+65)+": "+temp.get(i)+"\n");
					qqpanel.add(ta);
				}
				qpanel.add(qqpanel,c);
				//currentq++;
				frame.add(qpanel);
				frame.repaint();
				frame.revalidate();
				starttime=System.currentTimeMillis();
				accepting=true;
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
				//waitingforgametostart=false;
				//implement creating the question screen

			}
		});
		//results.add(next);
		finq.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				accepting=false;
				endtime=System.currentTimeMillis();
				for(user u: users)
				{
					try{
						String returnCode = "endq" + (char) 13;
						BufferedOutputStream os = new BufferedOutputStream(u.getSocket().getOutputStream());
						OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
						osw.write(returnCode);
						osw.flush();
						//socket1.close();
					}
					catch (Exception e) {
						System.out.println(e);
					}
				}
				frame.remove(qpanel);
				createresults();
				//s1n.setText("test");
				frame.add(results);
				frame.revalidate();
				frame.repaint();
				for(user u: users)
				{
					try{
						String returnCode = "sco "+Integer.toString(u.getScore()) + (char) 13;
						BufferedOutputStream os = new BufferedOutputStream(u.getSocket().getOutputStream());
						OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
						osw.write(returnCode);
						osw.flush();
					}
					catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		});

		//GridBagConstraints c=new GridBagConstraints();
		frame.add(start);
		frame.setVisible(true);
		int port = 19999;
		int count = 0;
		try{
			socket1 = new ServerSocket(port);
			System.out.println("MultipleSocketServer Initialized");
			//System.out.println("Hostname: "+socket1.getInetAddress().getHostName());
			System.out.println("Hostname: "+InetAddress.getLocalHost().getHostName());
			while (acceptingnewconnections) {
				Socket connection = socket1.accept();
				Runnable runnable = new MultipleSocketServerE(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int getMaxFittingFontSize(Graphics g, Font font, String string, int width, int height){
		int minSize = 0;
		int maxSize = 288;
		int curSize = font.getSize();

		while (maxSize - minSize > 2){
			FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
			int fontWidth = fm.stringWidth(string);
			int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

			if ((fontWidth > width) || (fontHeight > height)){
				maxSize = curSize;
				curSize = (maxSize + minSize) / 2;
			}
			else{
				minSize = curSize;
				curSize = (minSize + maxSize) / 2;
			}
		}

		return curSize;
	}
	public static void createresults()
	{
		String correct=qs.get(currentq).getCorrectanswer();
		int a=0;
		int b=0;
		int c=0;
		int d=0;
		int e=0;
		int f=0;
		for(user u:users)
		{
			if(u.getAnswer().equals(correct))
			{
				u.addScore(100-(int)((u.getTime()-starttime)/((endtime-starttime)/100)) );
			}
			switch (u.getAnswer()){
			case "A":a++;
			break;
			case "B":b++;
			break;
			case "C":c++;
			break;
			case "D":d++;
			break;
			case "E":e++;
			default: f++;
			}
			u.setAnswer("F");
		}
		Collections.sort(users);
		if(a!=1)
			numa.setText(a+" people chose A");
		else
			numa.setText("1 person chose A");
		if(b!=1)
			numb.setText(b+" people chose B");
		else
			numb.setText("1 person chose B");
		if(c!=1)
			numc.setText(c+" people chose C");
		else
			numc.setText("1 person chose C");
		if(d!=1)
			numd.setText(d+" people chose D");
		else
			numd.setText("1 person chose D");
		if(e!=1)
			nume.setText(e+" people chose E");
		else
			nume.setText("1 person chose E");
		for(int i=0;i<users.size()&&i<10;i++)
		{
			labels.get(i+13).setText(users.get(i).getUsername());
			labels.get(i+23).setText(""+users.get(i).getScore());
		}
	}
	public MultipleSocketServerE(Socket s, int i) 
	{
		this.connection = s;
		this.ID = i;
	}
	public void run() {
		if (acceptingnewconnections) {
			//while(waitingforgametostart)
			user u = new user("Default", connection);
			try {
				BufferedInputStream is = new BufferedInputStream(
						connection.getInputStream());
				isr = new InputStreamReader(is);
				int character;
				StringBuffer process = new StringBuffer();
				while ((character = isr.read()) != 13) {
					//System.out.println(character);
					process.append((char) character);
				}
				u = new user(process.toString(), connection);
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
				while (game) {
					if (accepting && accepting2) {
						character = 0;
						process = new StringBuffer();
						while ((character = isr.read()) != 13) {
							process.append((char) character);
						}
						//user u=hm.get(connection);
						u.setTime(System.currentTimeMillis());
						u.setAnswer(process.toString());
						System.out.println(u.getUsername() + " "
								+ process.toString());
						accepting2 = false;
						/*
						TimeStamp = new java.util.Date().toString();
						String returnCode = "MultipleSocketServer repsonded at "+ TimeStamp + (char) 13;
						BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
						OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
						osw.write(returnCode);
						osw.flush();
						 */
					} else if (!accepting)
						accepting2 = true;
				}
				connection.close();
			} catch (Exception e) {
				System.out.println("Client "+ID+" generated "+e);
				users.remove(u);
				return;
			}
			return;
		}
	}
}