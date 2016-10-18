package map;

public class Block {
	private boolean isObstacle;
	private boolean isVirtualWall;
	private boolean isExplored = true;
	private int row; //which row where the block is
	private int col; //which col where the block is

	//the path taken by the robot will be treated as confirm free
	private boolean confirmFree=false;
	
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
		if (this.confirmFree == true){
			System.out.println("Ooops, be careful, robot!");
			isObstacle=false;
			return;
		}
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

	public void setConfirmFree(boolean b){
		if (b==true){
			this.isObstacle=false;
		}
		this.confirmFree=b;
	}

	public boolean getConfirmFree(){
		return this.confirmFree;
	}
	
}
