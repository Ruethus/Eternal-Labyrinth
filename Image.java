package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	private BufferedImage image;
	private File file;
	private int sx,sy;
	
	public Image(int[][] array) {
		image = new BufferedImage(array.length,array[0].length,BufferedImage.TYPE_INT_RGB);
		for(int y = 0;y < array[0].length;y++) {
			for(int x = 0;x < array.length;x++) {
				image.setRGB(x,y,array[x][y]);
			}
		}
		sx = array.length;
		sy = array[0].length;
	}
	
	public Image(byte[][] array) {
		image = new BufferedImage(array.length,array[0].length,BufferedImage.TYPE_INT_RGB);
		for(int y = 0;y < array[0].length;y++) {
			for(int x = 0;x < array.length;x++) {
				image.setRGB(x,y,(int)array[x][y]+128);
			}
		}
		sx = array.length;
		sy = array[0].length;
	}
	
	public Image(String name,String fileType,int sx,int sy) {
		image = new BufferedImage(sx,sy,BufferedImage.TYPE_INT_RGB);
		if(checkForFile(name,fileType)) {
			readFromFile(name,fileType);
		}
		else {
			System.out.println("Error reading image.");
		}
		this.sx = sx;
		this.sy = sy;
	}
	
	public static boolean checkForFile(String name,String fileType) {
			File f = new File(name+"."+fileType);
			return f.exists();
	}
	
	public int[][] getData() {
		int[][] tmp = new int[sx][sy];
		for(int y = 0;y < sy;y++) {
			for(int x = 0;x < sx;x++) {
				tmp[x][y] = image.getRGB(x,y);
			}
		}
		return tmp;
	}
	
	public int getData(int x,int y) {
		return image.getRGB(x,y);
	}
	
	public void printToFile(String name,String fileType) {
		try {
			file = new File(name+"."+fileType);
			file.setWritable(true);
			file.createNewFile();
			if(file.exists()) {
				ImageIO.write(image,fileType,file);
			}
			System.out.println("Printed file "+name+"."+fileType+" successfully.");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFromFile(String name,String fileType) {
		try {
			file = new File(name+"."+fileType);
			file.setWritable(true);
			file.createNewFile();
			if(file.exists()) {
				image = ImageIO.read(file);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setData(int x,int y,int RGB) {
		if(x >= 0 && x < sx && y >= 0 && y < sy)
			image.setRGB(x,y,RGB);
	}
	
	public BufferedImage getBufferedImage() {
		return image;
	}
}