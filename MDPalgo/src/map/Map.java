package map;

import map.MapConstants;
public class Map{
	private Block[][] blocks;

	public Map(){
		blocks = new Block [MapConstants.MAP_ROW][MapConstants.MAP_COL];  
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				blocks[r][c] = new Block(r,c);
				if (r==0 || c ==0 || r == MapConstants.MAP_ROW-1 || c == MapConstants.MAP_COL-1){
					blocks[r][c].setVirtualWall(true);
				}
			}
		}
		//goal point is not virtual wall!
		blocks[MapConstants.GOAL_ROW][MapConstants.GOAL_COL].setVirtualWall(false);
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
		if (r==MapConstants.GOAL_ROW && c == MapConstants.GOAL_COL){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Block getGoalZone(){
		return blocks[MapConstants.GOAL_ROW][MapConstants.GOAL_COL];
	}

//	public Block[][] getMap(){
//		return this.blocks;
//	}
	public boolean blockInRange(int r, int c){
		if (r<0 || c<0){
			return false;
		}
		if (r>=MapConstants.MAP_ROW || c>=MapConstants.MAP_COL){
			return false;
		}
		return true;
	}
	public Block getBlock(int r, int c){
		return this.blocks[r][c];
	}

	public void setObstacle(int r, int c){
		this.blocks[r][c].setObstacle();
		if (r >= 1){
			this.blocks[r-1][c].setVirtualWall(true);
			if (c < MapConstants.MAP_COL - 1){
				this.blocks[r-1][c+1].setVirtualWall(true);
			}
			if (c >= 1){
				this.blocks[r-1][c-1].setVirtualWall(true);
			}
		}
		if (c >= 1){
			this.blocks[r][c-1].setVirtualWall(true);
			if (r < MapConstants.MAP_ROW - 1){
				this.blocks[r+1][c-1].setVirtualWall(true);
			}	
		}
		if (r < MapConstants.MAP_ROW - 1){
			this.blocks[r+1][c].setVirtualWall(true);
			if (c < MapConstants.MAP_COL - 1){
				this.blocks[r+1][c+1].setVirtualWall(true);
			}
		}
		if (c < MapConstants.MAP_COL - 1){
			this.blocks[r][c+1].setVirtualWall(true);
		}	
	}
}