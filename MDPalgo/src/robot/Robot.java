package robot;


import map.Map;
import map.MapConstants;
import map.Block;
import java.util.ArrayList;
import java.util.Stack;

import robot.RobotConstants.DIRECTION;
public class Robot {
	private int posRow;
	private int posCol;
	private DIRECTION robotCurDir;
	
	public Robot(){
		posRow= -1;
		posCol= -1;
		robotCurDir = RobotConstants.STARTING_DIR;
	}
	
	public Robot(int r, int c){
		posRow = r;
		posCol = c;
		robotCurDir = RobotConstants.STARTING_DIR;
	}
	
	public void setRobotPos(int r, int c){
		posRow = r;
		posCol = c;
		robotCurDir = RobotConstants.STARTING_DIR;
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
		Block[][] parents = new Block[MapConstants.MAP_ROW][MapConstants.MAP_COL];
		Block[] neighbors = new Block[4];
		Block current = theMap.getBlock(RobotConstants.STARTING_ROW, RobotConstants.STARTING_COL);
		DIRECTION curDir = this.robotCurDir;
		double[][] gScores = new double[MapConstants.MAP_ROW][MapConstants.MAP_COL];
		//initialize gScores arrays
		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
			for (int j = 0; j < MapConstants.MAP_COL; j++) {
				if (theMap.getBlock(i, j).getIsObstacle()){
					gScores[i][j] = RobotConstants.INFINITE_COST;
				}
				else{
					gScores[i][j] = -1;
				}
			}
		}

		//initialize parents
					for (int i = 0; i < MapConstants.MAP_ROW; i++) {
						for (int j = 0; j < MapConstants.MAP_COL; j++) {
							parents[i][j] = theMap.getBlock(RobotConstants.STARTING_ROW, RobotConstants.STARTING_COL);
						}
					}
				
		open.add(theMap.getBlock(RobotConstants.STARTING_ROW, RobotConstants.STARTING_COL));
		
		//initialize starting point
		gScores[RobotConstants.STARTING_ROW][RobotConstants.STARTING_COL] = 0;
		
		int testingCount =0;
		do{
			testingCount++;
			current = minimumCostBlock(open,gScores); //get the block with min cost
			curDir = getTargetDir(parents[current.getRow()][current.getCol()],current);
//			System.out.println(current.getRow() + ", " + current.getCol());
//			System.out.println("size of open: " + open.size());
//			for (int i = 0; i < MapConstants.MAP_ROW; i++) {
//				for (int j = 0; j < MapConstants.MAP_COL; j++) {
//					System.out.print(gScores[i][j]);
//					System.out.print(";");
//				}
//				System.out.println("\n");;
//			}
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
						gScores[neighbors[i].getRow()][neighbors[i].getCol()] = gScores[current.getRow()][current.getCol()] + costG(current, neighbors[i], curDir);
						open.add(neighbors[i]);
					}
					else{
						double currentGScore = gScores[neighbors[i].getRow()][neighbors[i].getCol()];
						double newGScore = gScores[current.getRow()][current.getCol()] + costG(current, neighbors[i], curDir);
						if (newGScore < currentGScore){
							gScores[neighbors[i].getRow()][neighbors[i].getCol()] = newGScore;
							parents[neighbors[i].getRow()][neighbors[i].getCol()]=current;
						}
					}
				}
			}
//			System.out.println("looping!");
		}while(!open.isEmpty());
		// Continue until there is no more available square in the open list (which means there is no path)  

		System.out.println("looping " + testingCount +" times.");
		// Generating actual shortest Path by tracing from end to start
		Stack<Block> actualPath = new Stack<Block>();
		Block temp =theMap.getGoalZone();

		do{
			actualPath.push(temp);
			temp = parents[temp.getRow()][temp.getCol()];
		}while(temp.getRow()!=0 || temp.getCol()!=0);
		
		//print our the path
		System.out.println("the number of steps is :" + actualPath.size());
		System.out.println("the Path is: ");
		while(!actualPath.isEmpty()){
			temp = actualPath.pop();
			System.out.print("("+ temp.getRow() + " ,"+ temp.getCol()+ ")");
		}
		
		//testing!!!
//		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
//			for (int j = 0; j < MapConstants.MAP_COL; j++) {
//				if (parents[i][j].getRow() != 19 || parents[i][j].getCol() != 14){
//					System.out.println("("+ parents[i][j].getRow() + " ,"+ parents[i][j].getCol()+ ")");
//				}
//			}
//			System.out.println("\n");
//		}
		System.out.println("\n");
		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
			for (int j = 0; j < MapConstants.MAP_COL; j++) {
				System.out.print(gScores[i][j]);
				System.out.print(";");
			}
			System.out.println("\n");;
		}
		return true;
	}
	private Block minimumCostBlock(ArrayList<Block> theBlockList, double[][] gScores){
		int size = theBlockList.size();
		double minCost = RobotConstants.INFINITE_COST;
		Block result = null;
		for (int i=size-1;i>=0;i--){
//			if (gScores[(theBlockList.get(i).getRow())][(theBlockList.get(i).getCol())] != -1){
				double gCost = gScores[(theBlockList.get(i).getRow())][(theBlockList.get(i).getCol())];
				double cost = gCost + costH(theBlockList.get(i));
				if (cost<minCost){
					minCost = cost;
					result = theBlockList.get(i);
				}
//			}
		}
		return result;
	}
	
	//calculate the heuristic cost from block b to goal
	private double costH(Block b){ 
		//heuristic cost from the block to goal point
		double move = (Math.abs(RobotConstants.GOAL_COL - b.getCol()) + Math.abs(RobotConstants.GOAL_ROW - b.getRow())) * RobotConstants.MOVE_COST;
		double turn = 0;
		//the goal is at north east
		//when the block b is goal zone
		if (move == 0){
			return (move+turn);
		}
//		//when the block b is not goal zone
//		if (RobotConstants.GOAL_COL - b.getCol() == 0){
//			//at same col
//			//assume turn once
//			turn = 1*RobotConstants.TURN_COST;
//		}
//		else{ //not at same col
//			if (RobotConstants.GOAL_ROW - b.getRow() == 0){
//				//not same col but same row
//				//assume turn once
//				turn = 1*RobotConstants.TURN_COST;
//			}
//			else{ 
		if (RobotConstants.GOAL_COL - b.getCol() != 0 && RobotConstants.GOAL_ROW - b.getRow() != 0){
			//not same col or same row
			//assume turn twice
			turn = 2*RobotConstants.TURN_COST;
			}
		return (move+turn);
	}
	
	private double turnCost(DIRECTION a, DIRECTION b){
		//calculate the turn cost from a to b
		int numOfTurn = Math.abs(a.ordinal()-b.ordinal());
		if (numOfTurn > 2){
			numOfTurn = numOfTurn % 2;
		}
		return (numOfTurn * RobotConstants.TURN_COST);
	}
	
	//calculate the actual cost from a to b (its neighbor)
	private double costG(Block a, Block b, DIRECTION aDir){
		double move = RobotConstants.MOVE_COST;
		//since its moving to the neighbor, move_cost always 1
		double turn = 0;
		DIRECTION targetDir = getTargetDir(a, b);
		turn = turnCost(aDir, targetDir);
		return (move + turn);
	}
	
	//from block a to block b
	//which direction should turn to
	private DIRECTION getTargetDir(Block a, Block b){
		DIRECTION targetDir = RobotConstants.STARTING_DIR;
		if (a.getCol() - b.getCol() > 0){
			targetDir = DIRECTION.WEST;
		}
		else if (b.getCol() - a.getCol() > 0){
			targetDir = DIRECTION.EAST;
		}
		else{ //same col
			if (a.getRow() - b.getRow() > 0){
				targetDir = DIRECTION.SOUTH;
			}
			else if (b.getRow() - a.getRow() > 0){
				targetDir = DIRECTION.NORTH;
			}
			else{ //same pos
				return targetDir;
			}
		}
		return targetDir;
	}
}

