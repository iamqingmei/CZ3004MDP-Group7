package robot;
import map.Map;
import map.Block;
import java.util.ArrayList;
import java.util.Stack;

//import robot.RobotConstants.DIRECTION;
public class Robot {
	private int posRow;
	private int posCol;
	
	public Robot(){
		posRow= -1;
		posCol= -1;
	}
	
	public Robot(int r, int c){
		posRow = r;
		posCol = c;
	}
	
	public void setRobotPos(int r, int c){
		posRow = r;
		posCol = c;
	}
	
	public int getRobotPosRow(){
		return posRow;
	}
	
	public int getRobotPosCol(){
		return posCol;
	}
	
	public boolean shortestPathAlgo(Map theMap){
		ArrayList<Block> open = new ArrayList<Block>();
		ArrayList<Block> closed = new ArrayList<Block>();
		Block[][] parents = new Block[20][15];
		Block[] neighbors = new Block[4];
		Block current = null;
		double[][] gScores = new double[20][15];
		//initialize gScores arrays
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				if (theMap.getBlock(i, j).getIsObstacle())
					gScores[i][j] = 9999;
				else
					gScores[i][j] = 0;
			}
		}

		//initialize parents
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 15; j++) {
							parents[i][j] = theMap.getBlock(19, 14);
						}
					}
				
		open.add(theMap.getBlock(this.posRow, this.posCol));
		
		
		do{
			current = minimumCostBlock(open,gScores); //get the block with min cost
			closed.add(current); //add the current block to the closed
			open.remove(current); //remove it from the open
			if(closed.contains(theMap.getGoalZone())){
				//Path found
				break;
			}
			/// set up its neighbors
			if (theMap.blockInRange(current.getRow() + 1,current.getCol())){
				neighbors[0] = theMap.getBlock(current.getRow() + 1,current.getCol());
				if (neighbors[0].getIsObstacle()){ 
					// if it is an obstacle, set null
					neighbors[0] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow() - 1,current.getCol())){
				neighbors[1] = theMap.getBlock(current.getRow() - 1,current.getCol());
				if (neighbors[1].getIsObstacle()){ 
					// if it is an obstacle, set null
					neighbors[1] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow(),current.getCol() - 1)){
				neighbors[2] = theMap.getBlock(current.getRow(),current.getCol() - 1);
				if (neighbors[2].getIsObstacle()){ 
					// if it is an obstacle, set null
					neighbors[2] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow(), current.getCol() + 1)){
				neighbors[3] = theMap.getBlock(current.getRow(),current.getCol() + 1);
				if (neighbors[3].getIsObstacle()){ 
					// if it is an obstacle, set null
					neighbors[3] = null; 
				}
			}
			for (int i=0;i<4;i++){
				if (neighbors[i] != null){		
					if (closed.contains(neighbors[i])){
					continue; 
					//this neighbor is already in the closed list 
					//ignore it
					}
				
					if (!(open.contains(neighbors[i]))){
						parents[neighbors[i].getRow()][neighbors[i].getCol()]=current;
						gScores[neighbors[i].getRow()][neighbors[i].getCol()] = gScores[current.getRow()][current.getCol()] + RobotConstants.MOVE_COST;
						open.add(neighbors[i]);
					}
					else{
						double currentGScore = gScores[neighbors[i].getRow()][neighbors[i].getCol()];
						double newGScore = gScores[current.getRow()][current.getCol()] + RobotConstants.MOVE_COST;
						if (newGScore < currentGScore){
							gScores[neighbors[i].getRow()][neighbors[i].getCol()] = newGScore;
							parents[neighbors[i].getRow()][neighbors[i].getCol()]=current;
						}
					}
				}
			}
			System.out.println("looping!");
		}while(!open.isEmpty());
		// Continue until there is no more available square in the open list (which means there is no path)  

		
		// Generating actual shortest Path by tracing from end to start
		Stack<Block> actualPath = new Stack<Block>();
		Block temp =theMap.getGoalZone();

		do{
			actualPath.push(temp);
			temp = parents[temp.getRow()][temp.getCol()];
		}while(temp.getRow()!=0 || temp.getCol()!=0);
		
		//print our the path
		System.out.println("the Path is: ");
		while(!actualPath.isEmpty()){
			temp = actualPath.pop();
			System.out.println("("+ temp.getRow() + " ,"+ temp.getCol()+ ")");
		}
		
		//testing!!!
//		for (int i = 0; i < 20; i++) {
//			for (int j = 0; j < 15; j++) {
//				if (parents[i][j].getRow() != 19 || parents[i][j].getCol() != 14){
//					System.out.println("("+ parents[i][j].getRow() + " ,"+ parents[i][j].getCol()+ ")");
//				}
//			}
//			System.out.println("\n");
//		}
		return true;
	}
	private Block minimumCostBlock(ArrayList<Block> theBlockList, double[][] gScores){
		int size = theBlockList.size();
		double minCost = 99999;
		Block result = null;
		for (int i=size-1;i>=0;i--){
			double gCost = gScores[(theBlockList.get(i).getRow())][(theBlockList.get(i).getCol())];
			double cost = gCost + costH(theBlockList.get(i));
			if (cost<minCost){
				minCost = cost;
				result = theBlockList.get(i);
			}
		}
		return result;
	}
	
	private int costH(Block b){ 
		//heuristic cost from the block to goal point
		int colDif = Math.abs(14 - b.getCol());
		int rowDif = Math.abs(19 - b.getRow());
		return (colDif + rowDif);
	}
}

