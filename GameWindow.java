package game;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
	protected static GameInstance g;
	public static boolean[] keyPressed;
	public static short[] charBounds = {'!','~'},keyIDs={KeyEvent.VK_UP,KeyEvent.VK_RIGHT,KeyEvent.VK_DOWN,KeyEvent.VK_LEFT};

	public GameWindow() {
		super(Game.name);
		g = new GameInstance();
		this.setSize(new Dimension(GameInstance.SX*GameInstance.fsx*2+16,GameInstance.SY*GameInstance.fsy*2+38));
		this.add(g);
		resetKeyPressed();
		this.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				boolean f = false;
				char c = e.getKeyChar();
				short i;
				for(i = 0;i < keyPressed.length && !f;i++) {
					keyPressed[i] = (c == i+charBounds[0]);
					if(keyPressed[i]) f = true;
				}
				for(i = 0;i < keyIDs.length && !f;i++) {
					keyPressed[i+keyPressed.length] = (e.getKeyCode() == keyIDs[i]);
					if(keyPressed[i+keyPressed.length]) f = true;
				}
			}
			public void keyReleased(KeyEvent e) {
				boolean f = false;
				char c = e.getKeyChar();
				short i;
				for(i = 0;i < keyPressed.length && !f;i++) {
					if(c == i+charBounds[0]) {
						keyPressed[i] = false;
						f = true;
					}
				}
				for(i = 0;i < keyIDs.length && !f;i++) {
					if(e.getKeyCode() == keyIDs[i]) {
						keyPressed[i+keyPressed.length] = false;
						f = true;
					}
				}
			}
			public void keyTyped(KeyEvent e) {}
		});
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		g.loopFloors();
	}

	public static boolean keyPressed(byte b) {
		if(b >= 0 && b < keyPressed.length) {
			return keyPressed[b];
		}
		else {
			return false;
		}
	}

	public static void resetKeyPressed() {
		keyPressed = new boolean[charBounds[1]-charBounds[0]+1+keyIDs.length];
	}
}