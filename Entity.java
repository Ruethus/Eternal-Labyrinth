package game;

import java.util.ArrayList;
import java.util.Random;

public class Entity {
	protected boolean unique;
	protected static Random gen;
	protected short str,sli,sta,sol,sav,spr,ser,hpm,thpm,hp,epm,tepm,ep,level;
	protected static final short MAXRACES=63,MAXLEVEL=999;
	protected static short[][] races; // Entry format: {str,sli,sta,sol,sav,spr,dungeonAff,caveAff,mazeAff,dunRuinAff,mazRuinAff} (length = 11)
	private static String[] c,r,v,s;

	public Entity(boolean unique,byte race,short level) {
		this.unique = unique;
		if(race == -1) { // This is used to generate player stats
			short vr = (short)((level-3)*6);
			ser = (short)(gen.nextInt()%(MAXLEVEL*2+1)-MAXLEVEL);
			short[] stats = {3,3,3,3,3,3};
			for(short i = 0;i < vr;i++) {
				byte j = (byte)(gen.nextInt()%6);
				if(stats[j] < 999) {
					stats[j]++;
				}
				else {
					i--;
				}
			}
		}
	}

	public static void initEntities(int seed) {
		gen = new Random(seed);
		initNameGen();
		genRaces();
	}

	private static void initNameGen() {
		String[] mc = {"b","c","ch","d","f","g","gh","h","j","k","m","n","p","ph","t","th","v","w","x","y","z"},
				 mr = {"l","r"},
				 mv = {"a","ae","ai","ao","au","ay","e","ee","ei","eu","ey","i","ie","io","o","oa","oe","oi","ou","oy","u","ue","ui","uo","uy"},
				 ms = {"cvr","rvr","cv","rv","vr","cvc","cvrc","crvc","crv"};
		c = new String[mc.length];
		r = new String[mr.length];
		v = new String[mv.length];
		s = new String[ms.length];
	}

	private static void genRaces() {
		races = new short[MAXRACES][11];
		short[] human = {0,0,0,0,0,0,0,0,0,0,0},elf = {-13,0,-25,13,0,25,0,-25,25,0,0},dwarf = {13,0,25,-13,0,-25,0,25,-25,0,0};
		races[0] = human;;
		races[1] = elf;
		races[2] = human;
		short[] t = new short[11];
		for(byte i = 0;i < MAXRACES-3;i++) {
			for(byte j = 0;j < 11;j++) {
				races[i][j] = (short)(gen.nextInt()%201-100);
			}
		}
	}

	private static String genName() {
		byte sc = (byte)(gen.nextInt()%5);
		String n="",ts,t;
		for(byte b0 = 0;b0 < sc;b0++) {
			ts = s[gen.nextInt()%s.length];
			t = "";
			for(byte b1 = 0;b1 < ts.length();b1++) {
				if(ts.charAt(b1) == 'c') {
					t += c[gen.nextInt()%c.length];
				}
				else if(ts.charAt(b1) == 'r') {
					t += r[gen.nextInt()%r.length];
				}
				else {
					t += v[gen.nextInt()%v.length];
				}
			}
			n += t;
		}
		return "";
	}
	
	public static short rollDice(short minVal,short maxVal,short serendipity) {
		byte rc;
		short o;
		if(serendipity >= 0) {
			rc = (byte)(serendipity/200+1);
			if(gen.nextInt()%200 < serendipity%200) {
				rc++;
			}
			o = minVal;
			for(byte i = 0;i < rc;i++) {
				short j = (short)(gen.nextInt()%(maxVal-minVal)+minVal);
				if(j < o) {
					o = j;
				}
			}
			return o;
		}
		else {
			rc = (byte)(serendipity/-200+1);
			if(gen.nextInt()%200 < (0-serendipity)%200) {
				rc++;
			}
			o = maxVal;
			for(byte i = 0;i < rc;i++) {
				short j = (short)(gen.nextInt()%(maxVal-minVal)+minVal);
				if(j < o) {
					o = j;
				}
			}
			return o;
		}
	}
}