package game;

import java.util.ArrayList;
import java.util.Random;

public class Dungeon {
	private ArrayList<DungeonChunk> chunks;
	public final byte MAXCHUNKS = 30;
	private Random gen;
	private static short cc;

	public Dungeon() {
		System.out.println("Dungeon initialized.");
		gen = new Random();
		chunks = new ArrayList<DungeonChunk>(MAXCHUNKS);
		for(byte i = 0;i < MAXCHUNKS;i++) {
			chunks.add(new DungeonChunk(MAXCHUNKS,(byte)3,(byte)9,i,gen.nextInt()));
		}
		cc = 0;
		System.out.println("Dungeon generated.");
	}

	public void changeFloor(short f) {
		if(f < MAXCHUNKS*DungeonChunk.MAXFLOORS && f > 0) {
			cc = (byte)(f/DungeonChunk.MAXFLOORS);
			chunks.get(cc).changeFloor((short)(f%DungeonChunk.MAXFLOORS));
		}
	}

	public void cycleFloor() {
		if(!chunks.get(cc).lastFloor()) {
			chunks.get(cc).nextFloor();
		}
		else {
			cycleChunk();
		}
	}

	public byte[][][] renderFloor() {
		return chunks.get(cc).renderFloor((short)(cc%DungeonChunk.MAXFLOORS));
	}

	public void cycleChunk() {
		if(cc+1 < chunks.size()) {
			cc++;
		}
		else {
			cc = 0;
		}
	}
}