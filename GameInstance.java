package game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class GameInstance extends BackgroundPanel{
	public static boolean[][][] font;
	public static byte zx=2,zy=2;
	protected static byte[][][] image;
	public static final char[] charSet = {' ','!','#','$','%','&','*','(',')','\'','+',',','-','.','/','0','1','2','3','4','5','6','7','8','9',':',';','<','=','>','?','@','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','[','\\',']','^','_','`','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','{','|','}','~'};
	public static Dungeon d;
	public static final int[] colorPalette = {0x140c1c,0x442434,0x31346d,0x4e4a4e,0x757161,0x8595a1,0x6dc3ca,0xdeeed6,0x854c30,0xd04648,0xd27d2c,0xdad45e,0xd2aa99,0x346524,0x6daa2c,0x597dce};
	public static LogWindow l;
	public static short fontSize,fsx=6,fsy=8,SX=DungeonChunk.SX+3+LogWindow.SX,SY=DungeonChunk.SY+3+4;
	
	public GameInstance() {
		super(renderBlankFrame());
		this.setSize(new Dimension(DungeonChunk.SX*zx,DungeonChunk.SY*zy));
		this.setStyle(SCALED);
		System.out.println("Game instance initialized.");
		d = new Dungeon();
		l = new LogWindow();
		importFont();
		l.append("Log Initialized.");
		this.setImage(render());
	}
	
	public void loopFloors() {
		byte i = 0;
		while(true) {
			long a = System.currentTimeMillis();
			while(System.currentTimeMillis()-a < 5000) {}
			d.cycleFloor();
			this.setImage(render());
		}
	}
	
	public static BufferedImage renderBlankFrame() {
		BufferedImage b = new BufferedImage(DungeonChunk.SX,DungeonChunk.SY,BufferedImage.TYPE_INT_RGB);
		for(short y = 0;y < DungeonChunk.SY;y++) {
			for(short x = 0;x < DungeonChunk.SX;x++) {
				b.setRGB(x,y,0xFFFFFF);
			}
		}
		return b;
	}
	
	public void importFont() {
		short ffsx=(short)(16*fsx),ffsy=(short)(9*fsy),i=0;
		fontSize = (short)((ffsx*ffsy)/(fsy*fsx));
		Image f = new Image("font","png",ffsx,ffsy);
		boolean[][] fnt = new boolean[ffsy][ffsx];
		for(short y = 0;y < ffsy;y++) {
			for(short x = 0;x < ffsx;x++) {
				fnt[y][x] = f.getData(x,y) != 0xFFFF00FF;
			}
		}
		font = new boolean[fontSize][fsy][fsx];
		for(short y = 0;y < ffsy/fsy;y++) {
			for(short x = 0;x < ffsx/fsx;x++) {
				for(short by = 0;by < fsy;by++) {
					for(short bx = 0;bx < fsx;bx++) {
						font[i][by][bx] = fnt[y*fsy+by][x*fsx+bx];
						if(font[i][by][bx]) {
						}
						else {
						}
					}
				}
				i++;
			}
		}
	}
	
	public int getColor(byte id) {
		if(id >= 0 && id < 16) {
			return colorPalette[id];
		}
		else {
			return 0xFF00FF;
		}
	}
	
	public int getColor(int id) {
		if(id >= 0 && id < 16) {
			return colorPalette[id];
		}
		else {
			return 0xFF00FF;
		}
	}
	
	public BufferedImage render() {
		image = new byte[3][SY][SX];
		byte[][][] floorImage = d.renderFloor();//,logImage = l.render();
		BufferedImage img = new BufferedImage(SX*fsx,SY*fsy,BufferedImage.TYPE_INT_RGB);
		for(short y = 0;y < SY;y++) {
			for(short x = 0;x < SX;x++) {
				byte f;
				int cl,bcl;
				if(x > 0 && y > 0 && x <= DungeonChunk.SX && y <= DungeonChunk.SY) {
					f = floorImage[0][y-1][x-1];
					cl = getColor(floorImage[1][y-1][x-1]);
					bcl = getColor(floorImage[2][y-1][x-1]);
				}
				else if(x > DungeonChunk.SX+1 && y > 0 && x < DungeonChunk.SX+2+LogWindow.SX && y < SY-1) {
					f = 1;//logImage[0][y-1][x-DungeonChunk.SX-2];
					cl = 0;//getColor(logImage[0][y-1][x-DungeonChunk.SX-2]);
					bcl = 0;//getColor(logImage[0][y-1][x-DungeonChunk.SX-2]); 
				}
				else {
					f = 0x10;
					cl = getColor(5);
					bcl = getColor(0);
				}
				for(short iy = 0;iy < fsy;iy++) {
					for(short ix = 0;ix < fsx;ix++) {
						if(font[f][iy][ix]) {
							img.setRGB(x*fsx+ix,y*fsy+iy,cl);
						}
						else {
							img.setRGB(x*fsx+ix,y*fsy+iy,bcl);
						}
					}
				}
			}
		}
		return img;
	}
	
	public static byte charID(char c) {
		for(byte b = 0;b < charSet.length;b++) {
			if(charSet[b] == c) {
				return b;
			}
		}
		return 0;
	}
}