package map;

public class Block {
	private boolean isObstacle;
	private boolean isVirtualWall;
	private boolean isExplored = true;
	private int row; //which row where the block is
	private int col; //which col where the block is
	
	public Block(){
		isObstacle = false;
		row = -1;
		col = -1;
	}
	
	public Block(int theRow, int theCol){
		this.row = theRow;
		this.col = theCol;
		isObstacle = false;
		isExplored = true;
	}
	
	public void setObstacle(boolean b){
		isObstacle = b;
	}
	
	public boolean getIsObstacle(){
		return this.isObstacle;
	}

	public int getRow(){
		return this.row;
	}

	public int getCol(){
		return this.col;
	}

	public void setVirtualWall(boolean b){
		this.isVirtualWall = b;
	}
	
	public boolean getIsVirtualWall(){
		return this.isVirtualWall;
	}

	public boolean getIsExplored(){
		return this.isExplored;
	}

	public void setIsExplored(boolean b){
		this.isExplored = b;
	}


	
}
