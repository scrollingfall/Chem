import java.io.*;

import javax.imageio.*;

import java.awt.*;
import java.util.*;
public class Question {
	private String question;
	private boolean hasImage;
	private ArrayList<String>answers;
	private String correctanswer;
	private File imgfile;
	private Image image;
	public Question(String q,ArrayList<String>a, String c)
	{
		question=q;
		hasImage=false;
		answers=a;
		correctanswer=c;
	}
	public Question(String q, ArrayList<String>a, String c, String filename)
	{
		question=q;
		answers=a;
		correctanswer=c;
		hasImage=true;
		imgfile=new File(filename);
		try {
			image=ImageIO.read(imgfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public boolean isHasImage() {
		return hasImage;
	}
	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}
	public ArrayList<String> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	public String getCorrectanswer() {
		return correctanswer;
	}
	public void setCorrectanswer(String correctanswer) {
		this.correctanswer = correctanswer;
	}
	public File getImgfile() {
		return imgfile;
	}
	public void setImgfile(File imgfile) {
		this.imgfile = imgfile;
		hasImage=true;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
}
