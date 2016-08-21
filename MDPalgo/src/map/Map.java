package map;

public class Map{
	private Block[][] blocks;

	public Map(){
		blocks = new Block [20][15];  
		for (int r=0; r<20; r++){
			for (int c=0; c<15; c++){
				blocks[r][c] = new Block(r,c);
			}
		}
	}

	public boolean isObstacle(int r, int c){
		return this.blocks[r][c].getIsObstacle();
	}

	public boolean isStartZone(int r, int c){
		if (r==0 && c==0){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isGoalZone(int r, int c){
		if (r==19 && c==14){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Block getGoalZone(){
		return blocks[19][14];
	}

//	public Block[][] getMap(){
//		return this.blocks;
//	}
	public boolean blockInRange(int r, int c){
		if (r<0 || c<0){
			return false;
		}
		if (r>19 || c>14){
			return false;
		}
		return true;
	}
	public Block getBlock(int r, int c){
		return this.blocks[r][c];
	}
}