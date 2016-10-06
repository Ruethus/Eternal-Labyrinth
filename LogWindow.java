package game;

import java.util.ArrayList;

public class LogWindow {
	public static final byte SX=17,SY=35,SL=127;
	private static ArrayList<String> log;
	
	public LogWindow() {
		log = new ArrayList<String>(0);
	}
	
	public void append(String e) {
		log.add(e);
		if(log.size() >= SL) {
			while(log.size() >= SL) {
				log.remove(0);
			}
		}
	}
	
	public byte[][][] render() {
		byte[][][] img = new byte[3][SY][SX],t;
		byte c=0,i,lr=0,l;
		for(i = 0;i < log.size() && c < SL;i++) {
			c += countLines(log.get(log.size()-1-i));
		}
		for(byte j = 0;j < i;j++) {
			l = countLines(log.get(log.size()-1-j));
			t = renderString(log.get(log.size()-1-j));
			if(lr+l <= SL) {
				
			}
		}
		return img;
	}
	
	private byte[][][] renderString(String text) {
		byte[][][] s = new byte[3][countLines(text)][SX];
		byte x=0,y=0,wc=0;
		for(byte i = 0;i < text.length();i++) {
			if(text.charAt(i) != ' ') {
				s[0][y][x] = GameInstance.charID(text.charAt(i));
				s[1][y][x] = 7;
				s[2][y][x] = 0;
				wc++;
				x++;
				if(x == SX) {
					x = wc;
					y++;
				}
			}
		}
		return s;
	}
	
	private byte countLines(ArrayList<String> text) {
		byte l = 0;
		for(int i = 0;i < text.size();i++) {
			l += countLines(text.get(i));
		}
		return l;
	}
	
	private byte countLines(String text) {
		byte wc=0,lc=0,lines=0;
		for(byte i = 0;i < text.length();i++) {
			if(text.charAt(i) != ' ') {
				wc++;
				lc++;
				if(lc > SX) {
					lc = wc;
					lines++;
				}
			}
		}
		return lines;
	}
}
