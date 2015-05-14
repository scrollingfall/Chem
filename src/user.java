import java.io.*;
import java.net.*;
public class user {
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
		answer="E";
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
}
