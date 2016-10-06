package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class DungeonChunk {
	protected boolean f,pim = false;
	protected boolean[][] wall,cn;
	protected boolean[][][] vision;
	protected byte floorCount,type;
	protected byte[][] stairsUp,stairsDown;
	protected byte[][][] tiles;
	protected static final byte SX=45,SY=30,CSX=5,CSY=5,TYPECOUNT=1,MAXFLOORS=5;
	protected static int mr;
	protected Random gen;
	protected short minLevel,maxLevel,cf,chn;
	protected static short activeRecursions,ld;
	private static final short MAXCHUNKS = (SX/CSX)*(SY/CSY);

	public DungeonChunk(byte floorCount,short minLevel,short maxLevel,short chn,int seed) {
		// System.out.println("Dungeon chunk initialized.");
		this.chn = chn;
		cf = 0;
		this.floorCount = floorCount;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		tiles = new byte[floorCount][SX][SY];
		gen = new Random(seed);
		this.type = 0;//(byte)(gen.nextInt()%TYPECOUNT);
		generate();
		// System.out.println("Floors initialized.");
	}

	protected void generate() {
		// System.out.println("Beginning floor generation.");
		boolean b;
		mr = 0;
		stairsUp = new byte[floorCount][2];
		stairsDown = new byte[floorCount][2];
		for(byte i = 0;i < floorCount;i++) {
			b = true;
			// System.out.println("------------------------------------------------");
			// System.out.println("Floor B"+(i+1)+"F initialized.");
			// Seed/reseed the floor in anticipation of generation
			for(byte y = 0;y < SY;y++) {
				for(byte x = 0;x < SX;x++) {
					tiles[i][x][y] = 0;
				}
			}
			while(b) {
				wall = new boolean[SX][SY];
				/* ----- Dungeon-style Floor ----- *\
				|* Room-based generator that makes *|
				|* winding passages and spacious   *|
				|* chambers for creatures to roam. *|
				\* ------------------------------- */
				if(type == 0) {
					for(byte y = 0;y < SY;y++) {
						for(byte x = 0;x < SX;x++) {
							wall[x][y] = true;
						}
					}
					byte[][] chunks = new byte[MAXCHUNKS][6]; // Format for each chunk's data: {x,y,sx,sy}
					short j = 0;
					for(byte y = 0;y < SY/CSY;y++) {
						for(byte x = 0;x < SX/CSX;x++) {
							chunks[j][0] = (byte) (gen.nextInt()%(CSX-2)+x*CSX+1);
							chunks[j][1] = (byte) (gen.nextInt()%(CSY-2)+y*CSY+1);
							chunks[j][2] = (byte) ((gen.nextInt()%(CSX)));
							chunks[j][3] = (byte) ((gen.nextInt()%(CSY)));
							chunks[j][4] = (byte) ((gen.nextInt()%(CSX)));
							chunks[j][5] = (byte) ((gen.nextInt()%(CSY)));
							j++;
						}
					}
					// Carve out the rooms/hallway seeds
					for(j = 0;j < MAXCHUNKS;j++) {
						for(byte y = (byte)(chunks[j][1]-chunks[j][3]);y <= chunks[j][1]+chunks[j][5];y++) {
							for(byte x = (byte)(chunks[j][0]-chunks[j][2]);x <= chunks[j][0]+chunks[j][4];x++) {
								if(x > 0 && x < SX-1 && y > 0 && y < SY-1) {
									wall[x][y] = false;
								}
							}
						}
					}
					// System.out.println("Room seeds placed.");
					// Carve out the hallways
					boolean[] h = new boolean[MAXCHUNKS];
					short[] k = {-1,1,-(SY/CSY),(SY/CSY)};
					for(j = 0;j < MAXCHUNKS;j++) {
						for(byte l = 0;l < k.length;l++) {
							if((j+k[l] >= 0 && j+k[l] < MAXCHUNKS) && !h[j]) {
								h[j] = true;
								byte x0 = chunks[j][0],x1 = chunks[j+k[l]][0],x = x0;
								byte y0 = chunks[j][1],y1 = chunks[j+k[l]][1],y = y0;
								while(x != x1 || y != y1) {
									if(x > 0 && x < SX-1 && y > 0 && y < SY-1) {
										wall[x][y] = false;
									}
									if((gen.nextBoolean() || y == y1) && x != x1) {
										if(x1 > x) {
											x++;
										}
										else {
											x--;
										}
									}
									else {
										if(y1 > y) {
											y++;
										}
										else {
											y--;
										}
									}
									//h[j+k[l]] = true;
								}
								if(x > 0 && x < SX-1 && y > 0 && y < SY-1) {
									wall[x][y] = false;
								}
							}
						}
					}
					// System.out.println("Hallways carved.");
					activeRecursions = 0;
					ArrayList<byte[][]> ss = new ArrayList<byte[][]>(1);
					ArrayList<short[]> d = new ArrayList<short[]>(1);
					short md = 0;
					mr = 0;
					for(j = 0;j < MAXCHUNKS;j++) {
						for(short l = 0;l < MAXCHUNKS;l++) {
							if(l != j && isPath(chunks[j][0],chunks[j][1],chunks[l][0],chunks[l][1])) {
								byte[][] y = {{chunks[j][0],chunks[j][1]},{chunks[l][0],chunks[l][1]}};
								ss.add(y);
								ld = (short) ((chunks[j][0]-chunks[l][0])*(chunks[j][1]-chunks[l][1]));
								short[] td = {ld};
								d.add(td);
								if(ld > d.get(md)[0]){
									md = (short)(d.size()-1);
								}
							}
						}
					}
					stairsUp[i][0] = ss.get(md)[0][0];
					stairsUp[i][1] = ss.get(md)[0][1];
					stairsDown[i][0] = ss.get(md)[1][0];
					stairsDown[i][1] = ss.get(md)[1][1];
					purgeFloor(i);
					byte[][] bf = new byte[SX][SY];
					short c = 0;
					for(byte y = 0;y < SY;y++) {
						for(byte x = 0;x < SX;x++) {
							if(wall[x][y]) {
								tiles[i][x][y] = 1;
								bf[x][y] = 127;
							}
							else {
								tiles[i][x][y] = 0;
								bf[x][y] = -128;
								c++;
							}
						}
					}
					tiles[i][stairsUp[i][0]][stairsUp[i][1]] = 2;
					tiles[i][stairsDown[i][0]][stairsDown[i][1]] = 3;
					if(pim && i < MAXFLOORS) {
						Image img = new Image(bf);
						img.setData(stairsUp[i][0],stairsUp[i][1],0xFF0000);
						img.setData(stairsDown[i][0],stairsDown[i][1],0x00FF00);
						img.printToFile("F"+(chn+1)+"-"+(i+1),"png");
					}
					// System.out.println("Floor is "+(100*c/(SX*SY))+"% efficient.");
					// System.out.println(mr+" recursions");
					mr = 0;
					b = false;
				}
			}
		}
	}

	private boolean isPath(byte x,byte y,byte dx,byte dy) {
		boolean d;
		if(activeRecursions == 0) {
			cn = new boolean[SX][SY];
			f = false;
		}
		if(f) {
			return false;
		}
		else if(!notWall(x,y) || !notWall(dx,dy)) {
			return false;
		}
		else if(x == dx && y == dy) {
			f = true;
			return true;
		}
		else if(cn[x][y]) {
			return false;
		}
		else {
			activeRecursions++;
			mr++;
			cn[x][y] = true;
			if(notWall((byte)(x-1),y) && isPath((byte)(x-1),y,dx,dy)) {
				activeRecursions--;
				return true;
			}
			if(notWall(x,(byte)(y-1)) && isPath(x,(byte)(y-1),dx,dy)) {
				activeRecursions--;
				return true;
				}
			if(notWall((byte)(x+1),y) && isPath((byte)(x+1),y,dx,dy)) {
				activeRecursions--;
				return true;
			}
			if(notWall(x,(byte)(y+1)) && isPath(x,(byte)(y+1),dx,dy)) {
				activeRecursions--;
				return true;
			}
		}
		activeRecursions--;
		return false;
	}
	
	private void purgeFloor(short i) {
		cn = new boolean[SX][SY];
		flagContiguous(stairsUp[i][0],stairsUp[i][1]);
		for(byte y = 0;y < SY;y++) {
			for(byte x = 0;x < SX;x++) {
				wall[x][y] = !cn[x][y];
			}
		}
	}
	
	private void flagContiguous(byte x,byte y) { // Works similarly to isPath(), but aims instead to flag all contiguous tiles 
		if(notWall(x,y) && !cn[x][y]) {
			cn[x][y] = true;
			flagContiguous((byte)(x-1),y);
			flagContiguous((byte)(x+1),y);
			flagContiguous(x,(byte)(y-1));
			flagContiguous(x,(byte)(y+1));
		}
		
	}

	private boolean notWall(short x,short y) {
		if(x > 0 && x < SX-1 && y > 0 && y < SY-1) {
			return !wall[x][y];
		}
		else {
			return false;
		}
	}

	public byte[][][] renderFloor(short n) {
		byte[][][] bf = new byte[3][SY][SX];
		for(byte y = 0;y < SY;y++) {
			for(byte x = 0;x < SX;x++) {
				if(tiles[n][x][y] == 0) {
					bf[0][y][x] = 11;
					bf[1][y][x] = 3;
					bf[2][y][x] = 0;
				}
				else if(tiles[n][x][y] == 1){
					bf[0][y][x] = 3;
					bf[1][y][x] = 7;
					bf[2][y][x] = 0;
				}
				else if(tiles[n][x][y] == 2) {
					bf[0][y][x] = 28;
					bf[1][y][x] = 9;
					bf[2][y][x] = 0;
				}
				else if(tiles[n][x][y] == 3) {
					bf[0][y][x] = 30;
					bf[1][y][x] = 14;
					bf[2][y][x] = 0;
				}
			}
		}
		return bf;
	}
	
	public void changeFloor(short f) {
		if(f < MAXFLOORS && f >= 0) {
			cf = f;
		}
	}
	
	public void nextFloor() {
		if(cf+1 < MAXFLOORS) {
			cf++;
		}
	}
	
	public boolean lastFloor() {
		return cf+1 == MAXFLOORS;
	}
}