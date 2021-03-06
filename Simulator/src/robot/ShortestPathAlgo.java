package robot;

import map.Map;
import map.MapConstants;
import map.Block;
import java.util.*;

import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;
import robot.Robot;

public class ShortestPathAlgo{
	private ArrayList<Block> open;
	private ArrayList<Block> closed;
	private HashMap<Block, Block> parents;
	private Block[] neighbors; //neighbor blocks
	private Block current; //current pos
	private DIRECTION curDir; //current robot dir
	private double[][] gScores; //stores the costG for all the blocks
	private int testingCount; //for testing uses
	private Robot thebot;
	private Map map;
	private Map realMap;

	public ShortestPathAlgo(Map theMap, Robot bot){
		thebot = bot;
		map = theMap;
		realMap=null;
		open = new ArrayList<Block>();
		closed = new ArrayList<Block>();
		parents = new HashMap<Block, Block>();
		neighbors = new Block[4];
		current = theMap.getBlock(bot.getRobotPosRow(), bot.getRobotPosCol());
		curDir = bot.getRobotCurDir();
		gScores = new double[MapConstants.MAP_ROW][MapConstants.MAP_COL];

		//initialize gScores arrays
		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
			for (int j = 0; j < MapConstants.MAP_COL; j++) {
				if (theMap.getBlock(i, j).getIsObstacle() || theMap.getBlock(i,j).getIsVirtualWall() || !theMap.getBlock(i,j).getIsExplored()){
					// System.out.println("block (" + i+ ", "+ j + ")");
					// System.out.println("obstacle? : " + theMap.getBlock(i, j).getIsObstacle());
					// System.out.println("Vwall?: " + theMap.getBlock(i,j).getIsVirtualWall());
					gScores[i][j] = RobotConstants.INFINITE_COST;
				}
				else{
					gScores[i][j] = -1;
				}
			}
		}				
		open.add(current);

		//initialize starting point
		gScores[bot.getRobotPosRow()][bot.getRobotPosCol()] = 0;
		testingCount =0;
	}

	public ShortestPathAlgo(Map theMap, Robot bot, Map theRealMap){
		realMap = theRealMap;
		thebot = bot;
		map = theMap;
		open = new ArrayList<Block>();
		closed = new ArrayList<Block>();
		parents = new HashMap<Block, Block>();
		neighbors = new Block[4];
		current = theMap.getBlock(bot.getRobotPosRow(), bot.getRobotPosCol());
		curDir = bot.getRobotCurDir();
		gScores = new double[MapConstants.MAP_ROW][MapConstants.MAP_COL];

		//initialize gScores arrays
		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
			for (int j = 0; j < MapConstants.MAP_COL; j++) {
				if (theMap.getBlock(i, j).getIsObstacle() || theMap.getBlock(i,j).getIsVirtualWall() || !theMap.getBlock(i,j).getIsExplored()){
					// System.out.println("block (" + i+ ", "+ j + ")");
					// System.out.println("obstacle? : " + theMap.getBlock(i, j).getIsObstacle());
					// System.out.println("Vwall?: " + theMap.getBlock(i,j).getIsVirtualWall());
					gScores[i][j] = RobotConstants.INFINITE_COST;
				}
				else{
					gScores[i][j] = -1;
				}
			}
		}				
		open.add(current);

		//initialize starting point
		gScores[bot.getRobotPosRow()][bot.getRobotPosCol()] = 0;
		testingCount =0;
	}

	public StringBuilder runShortestPath(Map theMap, int goalRow, int goalCol){
		System.out.println("Start to find the shortest path from (" + current.getCol() + ", " + current.getRow() + ") to goal (" + goalRow + ", " + goalCol + ")");	
		Stack<Block> path = new Stack<Block>();
		
		do{
			testingCount++;
			current = minimumCostBlock(open,gScores, goalRow, goalCol); //get the block with min cost
			if (parents.containsKey(current)){
				curDir = getTargetDir(parents.get(current).getRow(),parents.get(current).getCol(),curDir, current);
			}
			
			closed.add(current); //add the current block to the closed
			open.remove(current); //remove it from the open
			if(closed.contains(theMap.getBlock(goalRow, goalCol))){
				//Path found
				System.out.println("Path found!");
				// printGscores();
				path = getPath(theMap, goalRow, goalCol);
				// printShortestPath(path);
				return moveRobot(thebot,path, goalRow, goalCol);
			}
			/// set up its neighbors
			if (theMap.blockInRange(current.getRow() + 1,current.getCol())){
				neighbors[0] = theMap.getBlock(current.getRow() + 1,current.getCol());
				if (neighbors[0].getIsObstacle() || neighbors[0].getIsVirtualWall() || !neighbors[0].getIsExplored()){ 
					// if it is an obstacle, set null
					neighbors[0] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow() - 1,current.getCol())){
				neighbors[1] = theMap.getBlock(current.getRow() - 1,current.getCol());
				if (neighbors[1].getIsObstacle() || neighbors[1].getIsVirtualWall() || !neighbors[1].getIsExplored()){ 
					// if it is an obstacle, set null
					neighbors[1] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow(),current.getCol() - 1)){
				neighbors[2] = theMap.getBlock(current.getRow(),current.getCol() - 1);
				if (neighbors[2].getIsObstacle() || neighbors[2].getIsVirtualWall() || !neighbors[2].getIsExplored()){ 
					// if it is an obstacle, set null
					neighbors[2] = null; 
				}
			}
			if (theMap.blockInRange(current.getRow(), current.getCol() + 1)){
				neighbors[3] = theMap.getBlock(current.getRow(),current.getCol() + 1);
				if (neighbors[3].getIsObstacle() || neighbors[3].getIsVirtualWall() || !neighbors[3].getIsExplored()){ 
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
						parents.put(neighbors[i],current);
						gScores[neighbors[i].getRow()][neighbors[i].getCol()] = gScores[current.getRow()][current.getCol()] + costG(current, neighbors[i], curDir);
						open.add(neighbors[i]);
					}
					else{
						double currentGScore = gScores[neighbors[i].getRow()][neighbors[i].getCol()];
						double newGScore = gScores[current.getRow()][current.getCol()] + costG(current, neighbors[i], curDir);
						if (newGScore < currentGScore){
							gScores[neighbors[i].getRow()][neighbors[i].getCol()] = newGScore;
							parents.put(neighbors[i],current);
						}
					}
				}
			}
		}while(!open.isEmpty());
		// Continue until there is no more available square in the open list (which means there is no path)  
		System.out.println("Path not found!");
		// printGscores();
		return null;
	}

	private Stack<Block> getPath(Map theMap, int goalRow, int goalCol){
		// Generating actual shortest Path by tracing from end to start
		Stack<Block> actualPath = new Stack<Block>();
		Block temp =theMap.getBlock(goalRow, goalCol);

		while(true){
			actualPath.push(temp);
			temp = parents.get(temp);
			if (temp == null){
				break;
			}
		}
		return actualPath;
	}

	private StringBuilder moveRobot(Robot bot, Stack<Block> path, int goalRow, int goalCol){
		Block temp = path.pop();		
		DIRECTION targetDir = bot.getRobotCurDir();
		StringBuilder outputString = new StringBuilder("");
		MOVE m;
		while((bot.getRobotPosRow() != goalRow) || (bot.getRobotPosCol() != goalCol)){
			if (bot.getRobotPosRow() == temp.getRow() && bot.getRobotPosCol() ==temp.getCol()){
				temp = path.pop();
			}
			System.out.println("move from" + bot.getRobotPosRow() + ", " + bot.getRobotPosCol() + " to " + temp.getRow() + " , " + temp.getCol());
			targetDir = getTargetDir(bot.getRobotPosRow(), bot.getRobotPosCol(), bot.getRobotCurDir(), temp);
			if (bot.getRobotCurDir() != targetDir){
				// System.out.println("robot cur dir:" + bot.getRobotCurDir().toString());
				// System.out.println("target dir:" + targetDir.toString());
				m=getTargetMove(bot.getRobotCurDir(),targetDir);
				// System.out.println("move:" + m.toString());
				outputString.append(m.print(m));
				bot.moveRobot(m);
				if (realMap != null){
					bot.setSensors();
					bot.sense(this.map, this.realMap);
				}
			}
			else{ //alr pointing to the target direction
				// System.out.println("move: FORWARD");
				outputString.append("f");
				bot.moveRobot(MOVE.FORWARD);
				if (realMap != null){
					bot.setSensors();
					bot.sense(this.map, this.realMap);
				}
			}
			this.map.repaint();
		}
		System.out.println(outputString);
		return outputString;
	}

	private void printShortestPath(Stack<Block> actualPath){
		Block temp;
		System.out.println("looping " + testingCount +" times.");
		//print our the path
		System.out.println("the number of steps is :" + (actualPath.size() - 1));
		System.out.println("the Path is: ");
		while(!actualPath.isEmpty()){
			temp = actualPath.pop();
			System.out.print("("+ temp.getCol() + " ,"+ temp.getRow()+ ")");
		}
		System.out.println("\n");
		// printGscores();
	}

	// printGscores is for testing uses
	public	void printGscores(){
		for (int i = 0; i < MapConstants.MAP_ROW; i++) {
			for (int j = 0; j < MapConstants.MAP_COL; j++) {
				System.out.print(gScores[MapConstants.MAP_ROW - 1 -i][j]);
				System.out.print(";");
			}
			System.out.println("\n");
		}
	}
	private Block minimumCostBlock(ArrayList<Block> theBlockList, double[][] gScores, int goalRow, int getCol){
		int size = theBlockList.size();
		double minCost = RobotConstants.INFINITE_COST;
		Block result = null;
		for (int i=size-1;i>=0;i--){
//			if (gScores[(theBlockList.get(i).getRow())][(theBlockList.get(i).getCol())] != -1){
				double gCost = gScores[(theBlockList.get(i).getRow())][(theBlockList.get(i).getCol())];
				double cost = gCost + costH(theBlockList.get(i), goalRow, getCol);
				if (cost<minCost){
					minCost = cost;
					result = theBlockList.get(i);
				}
//			}
		}
		return result;
	}
	
	//calculate the heuristic cost from block b to goal
	private double costH(Block b, int goalRow, int goalCol){ 
		//heuristic cost from the block to goal point
		double move = (Math.abs(goalCol - b.getCol()) + Math.abs(goalRow - b.getRow())) * RobotConstants.MOVE_COST;
		double turn = 0;
		//the goal is at north east
		//when the block b is goal zone
		if (move == 0){
			return (move+turn);
		}

		if (goalCol - b.getCol() != 0 && goalRow - b.getRow() != 0){
			//not same col or same row
			//assume turn once
			turn = RobotConstants.TURN_COST;
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
		DIRECTION targetDir = getTargetDir(a.getRow(), a.getCol(), aDir, b);
		turn = turnCost(aDir, targetDir);
		return (move + turn);
	}
	

	private DIRECTION getTargetDir(int botR, int botC, DIRECTION botDir, Block b){
		if (botC - b.getCol() > 0){
			return DIRECTION.WEST;
		}
		else if (b.getCol() - botC > 0){
			return DIRECTION.EAST;
		}
		else{ //same col
			if (botR - b.getRow() > 0){
				return DIRECTION.SOUTH;
			}
			else if (b.getRow() - botR > 0){
				return DIRECTION.NORTH;
			}
			else{ //same pos
				System.out.println("2222!");
				return botDir;
			}
		}
	}

	//from direction a to b, what moves should robot do?
	private MOVE getTargetMove(DIRECTION a, DIRECTION b){
		switch (a){
			case NORTH:
				switch (b){
					case NORTH: return MOVE.ERROR; 
					case SOUTH: return MOVE.LEFT; 
					case WEST: return MOVE.LEFT; 
					case EAST: return MOVE.RIGHT; 
				}
				break;
			case SOUTH:
				switch (b){
					case NORTH: return MOVE.LEFT; 
					case SOUTH: return MOVE.ERROR; 
					case WEST: return MOVE.RIGHT; 
					case EAST: return MOVE.LEFT; 
				}
				break;
			case WEST:
				switch (b){
					case NORTH: return MOVE.RIGHT; 
					case SOUTH: return MOVE.LEFT; 
					case WEST: return MOVE.ERROR; 
					case EAST: return MOVE.LEFT; 
				}
				break;
			case EAST:
				switch (b){
					case NORTH: return MOVE.LEFT; 
					case SOUTH: return MOVE.RIGHT; 
					case WEST: return MOVE.LEFT; 
					case EAST: return MOVE.ERROR; 
				}
		}
		return MOVE.ERROR;
	}
	
}

		
		