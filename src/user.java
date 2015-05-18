//import java.io.*;
//import java.util.*;
import java.net.*;
public class user implements Comparable<user>{
	private String username;
	private Socket socket;
	private long submittime;
	private int score;
	private String answer;
	public user(String user, Socket s)
	{
		username=user;
		socket=s;
		score=0;
		answer="F";
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String s)
	{
		answer=s;
	}
	public String getUsername() {
		return username;
	}
	public void setTime(long l)
	{
		submittime=l;
	}
	public long getTime()
	{
		return submittime;
	}
	public void addScore(int s)
	{
		score+=s;
	}
	public int getScore()
	{
		return score;
	}
	public Socket getSocket() {
		return socket;
	}
	public int compare(user o1, user o2) {
		return -1*(new Integer(o1.getScore()).compareTo(new Integer(o2.getScore())));
	}
	@Override
	public int compareTo(user o) {
		// TODO Auto-generated method stub
		return -1*new Integer(this.getScore()).compareTo(new Integer(o.getScore()));
	}
}
