package robot;

import java.util.*;

import map.MapConstants;
import map.Map;
import map.Block;

import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;
import robot.Robot;
import robot.RobotConstants;

public class ExplorationAlgo{
	private Map exMap, realMap;
	private Robot bot;
	private ArrayList<Block> pathTaken= new ArrayList<Block>();
	private int exploredArea;
	private int[] sensorData;

	public ExplorationAlgo(Map explorationMap, Map rMap, Robot thebot){
		this.exMap = explorationMap;
		this.realMap = rMap;
		this.bot = thebot;
	}

	public void runExploration(){
		bot.setSensors();
		sensorData = bot.sense(exMap, realMap);
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		exploredArea = countExploredArea();
		System.out.println("exploredArea: " + exploredArea);
		exMap.repaint();
		looping(RobotConstants.STARTING_ROW,RobotConstants.STARTING_COL);

		// continue to explore the Unexplored area
		while (exploredArea!=300){
			System.out.println("there are still unexplored areas!!!!");
			Block nearestUnexplored = nearestUnexploredGrid();
			System.out.println("Nearest Unexplored Grid is: " + nearestUnexplored.getRow() + ", " + nearestUnexplored.getCol());
			Block nearbyOb = nearbyObstacle(nearestUnexplored);
			if (nearbyOb!=null){
				System.out.println("Nearby Obstacle: " + nearbyOb.getRow() + ", " + nearbyOb.getCol());
				exploredGridNearOb(nearbyOb);
			}
			else{
				exploredGridNearOb(nearestUnexplored);
			}
		}
		System.out.println("All grids are explored!");

		//go back to start zone
		ShortestPathAlgo goBackToStart = new ShortestPathAlgo(exMap,bot);
		goBackToStart.runShortestPath(exMap,1,1);

		//after back to the start zone
		//turn to North (Ready for shortest path finding)
		turnRobotDir(DIRECTION.NORTH);
		
	}

	private void turnRobotDir(DIRECTION dir){
		System.out.println("robot facing: " + bot.getRobotCurDir());
		while(bot.getRobotCurDir() != dir){
			bot.moveRobot(MOVE.RIGHT);
			bot.setSensors();
			sensorData = bot.sense(exMap, realMap);
			exMap.repaint();
			System.out.println("robot facing: " + bot.getRobotCurDir());
		}
	}
	// return the explored gird which is near obstacle
	private Block exploredGridNearOb(Block obs){
		int obsRow = obs.getRow();
		int obsCol = obs.getCol();
		Block mark = null;
		DIRECTION dir = null;
		if (checkFreeToGo(obsRow-2,obsCol)){ //south
			//facing east
			mark = exMap.getBlock(obsRow-2,obsCol);
			dir = DIRECTION.EAST;
		}
		else if (checkFreeToGo(obsRow+2,obsCol)){ //north
			//facing west
			mark = exMap.getBlock(obsRow+2,obsCol);
			dir = DIRECTION.WEST;
		}
		else if (checkFreeToGo(obsRow,obsCol-2)){ //west
			//facing south
			mark = exMap.getBlock(obsRow,obsCol-2);
			dir = DIRECTION.SOUTH;
		}
		else if (checkFreeToGo(obsRow,obsCol+2)){ //east
			//facing north
			mark = exMap.getBlock(obsRow,obsCol+2);
			dir = DIRECTION.NORTH;
		}
		// go to the mark point
		ShortestPathAlgo spa = new ShortestPathAlgo(exMap,bot,realMap);
		spa.runShortestPath(exMap,mark.getRow(),mark.getCol());
		// bot.setSensors();
		// sensorData = bot.sense(exMap, realMap);
		exploredArea = countExploredArea();
		turnRobotDir(dir);
		System.out.println("bot current pos: " + bot.getRobotPosRow() +", " + bot.getRobotPosCol());
		System.out.println("Mark: " + mark.getRow() + ", " + mark.getCol());
		looping(mark.getRow(),mark.getCol());
		return mark;
	}


	// return true if b is not a virtual wall nor obstacle and alr explored
	private boolean checkFreeToGo(int r, int c){
		if (r>=0 && r<MapConstants.MAP_ROW && c>=0 && c<MapConstants.MAP_COL){
			Block b = exMap.getBlock(r,c);
			return (b.getIsExplored() && !b.getIsVirtualWall() && !b.getIsObstacle());
		}
		return false;
	}
	private void looping(int r, int c){
		MOVE nextMove = null;
		MOVE prevMov = null;
		do{
			pathTaken.add(exMap.getBlock(bot.getRobotPosRow(),bot.getRobotPosCol()));
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			System.out.println("move: " + nextMove);
			bot.moveRobot(nextMove);
			bot.setSensors();
			sensorData = bot.sense(exMap, realMap);
			exploredArea = countExploredArea();
			System.out.println("exploredArea: " + exploredArea);
			exMap.repaint();
			if (exploredArea==300){
				return;
			}
		}while(bot.getRobotPosCol() != c || bot.getRobotPosRow() != r); //back to the START zone
	}

	private int countExploredArea(){
		int result = 0;
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				if (exMap.getBlock(r,c).getIsExplored()){
					result++;
					// System.out.println("r,c: " + r +", " + c);
				}
			}
		}
		return result;
	}

	//return true if its explored and its not a obstacle
	private boolean checkStatus(int r, int c){
		boolean res = false;
		if (r>=0 && r<MapConstants.MAP_ROW && c>=0 && c<MapConstants.MAP_COL){
			res = (exMap.getBlock(r,c).getIsExplored() && (!exMap.getBlock(r,c).getIsObstacle())); //explored and not obstacle
		}
		// System.out.println("checkStatus for block " +r+", "+c+" : "+res);
		return res;
	}

	//return true if west side is free
	private boolean wSideFree(){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		return(checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2));
	}
	//return true if east side is free
	private boolean eSideFree(){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		return (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2));
	}
	//return true if north side is free
	private boolean nSideFree(){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		return (checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol));
	}
	//return true if south side is free
	private boolean sSideFree(){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		return (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1));
	}

	//follow left hand side
	private MOVE getNextMove(MOVE prevMov){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		System.out.println("bot current pos: " + bot.getRobotPosRow() +", " + bot.getRobotPosCol());
		System.out.println("bot current dir: " + bot.getRobotCurDir());
		switch (bot.getRobotCurDir()){
			case NORTH: 
				if (nSideFree() && !wSideFree()){
					return MOVE.FORWARD;
				}
				else if (wSideFree()){
					if (prevMov!= MOVE.LEFT){
						return MOVE.LEFT;
					}
					return MOVE.FORWARD;
				}
				else if (eSideFree() && !nSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("north error!");
					if (nSideFree()){
						return MOVE.FORWARD;
					}
					return MOVE.RIGHT;
				}
			case EAST:
				if (eSideFree() && !nSideFree() ){
					// System.out.println("here?!!!");
					return MOVE.FORWARD;
				}
				else if (nSideFree()){
					// System.out.println("222?!!!");
					if (prevMov!= MOVE.LEFT){
						// System.out.println("3333?!!!");
						return MOVE.LEFT;
					}
					// System.out.println("44444?!!!");
					return MOVE.FORWARD;
				}
				else if (sSideFree() && !eSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("east error!");
					if (eSideFree()){
						return MOVE.FORWARD;
					}
					return MOVE.RIGHT;
				}
			case SOUTH:
				if (sSideFree() && !eSideFree() ){
					return MOVE.FORWARD;
				}
				else if (eSideFree()){
					if (prevMov!= MOVE.LEFT){
						return MOVE.LEFT;
					}
					return MOVE.FORWARD;
				}
				else if (wSideFree() && !sSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("south error!");
					if (sSideFree()){
						return MOVE.FORWARD;
					}
					return MOVE.RIGHT;
				}
			case WEST:
				if (wSideFree() && !sSideFree() ){
					return MOVE.FORWARD;
				}
				else if (sSideFree()){
					if (prevMov!= MOVE.LEFT){
						return MOVE.LEFT;
					}
					return MOVE.FORWARD;
				}
				else if (nSideFree() && !wSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("west error!");
					if (wSideFree()){
						return MOVE.FORWARD;
					}
					return MOVE.RIGHT;
				}
			default:
				System.out.println("default error!");
				return MOVE.FORWARD;
		}
	}

	
	//for time limited
	public void runExploration(int timeInSecond){
		long start = System.currentTimeMillis();
		long end = start + timeInSecond*1000; // 60 seconds * 1000 ms/sec
		MOVE nextMove = null;
		MOVE prevMov = null;
		bot.setSensors();
		sensorData = bot.sense(exMap, realMap);
		exploredArea = countExploredArea();
		System.out.println("exploredArea: " + exploredArea);
		exMap.repaint();
		do{
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			System.out.println("move: " + nextMove);
			bot.moveRobot(nextMove);
			bot.setSensors();
			sensorData = bot.sense(exMap, realMap);
			exploredArea = countExploredArea();
			System.out.println("exploredArea: " + exploredArea);
			exMap.repaint();
		}while((bot.getRobotPosCol() != 1 || bot.getRobotPosRow() != 1) && (System.currentTimeMillis() < end));

		if (exploredArea!=300){
			System.out.println("there are still unexplored areas!!!!");
		}
		//after back to the start zone
		//turn to North (Ready for shortest path finding)
		while(bot.getRobotCurDir() != DIRECTION.NORTH){
			bot.moveRobot(MOVE.RIGHT);
		}
	}

	//for coverage limited
	public void runExploration(long coverageLimited){
		MOVE nextMove = null;
		MOVE prevMov = null;
		bot.setSensors();
		sensorData = bot.sense(exMap, realMap);
		exploredArea = countExploredArea();
		System.out.println("exploredArea: " + exploredArea);
		exMap.repaint();
		do{
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			System.out.println("move: " + nextMove);
			bot.moveRobot(nextMove);
			bot.setSensors();
			sensorData = bot.sense(exMap, realMap);
			exploredArea = countExploredArea();
			System.out.println("exploredArea: " + exploredArea);
			exMap.repaint();
		}while((bot.getRobotPosCol() != 1 || bot.getRobotPosRow() != 1) && (exploredArea < (coverageLimited * MapConstants.MAP_SIZE /100)));

		if (exploredArea!=300){
			System.out.println("there are still unexplored areas!!!!");
		}

		//go back to start zone
		ShortestPathAlgo goBackToStart = new ShortestPathAlgo(exMap,bot);
		goBackToStart.runShortestPath(exMap,1,1);
		
		//after back to the start zone
		//turn to North (Ready for shortest path finding)
		while(bot.getRobotCurDir() != DIRECTION.NORTH){
			bot.moveRobot(MOVE.RIGHT);
		}
	}

	private Block nearestUnexploredGrid(){
		Block rBlock = nearestRowUnexploredGrid();
		Block cBlock = nearestColUnexploredGrid();
		int rDis = Math.abs(rBlock.getRow() - bot.getRobotPosRow()) + Math.abs(rBlock.getCol() - bot.getRobotPosCol());
		int cDis = Math.abs(cBlock.getRow() - bot.getRobotPosRow()) + Math.abs(cBlock.getCol() - bot.getRobotPosCol());
		if (rDis < cDis){
			return rBlock;
		}
		return cBlock;

	}
	private Block nearestRowUnexploredGrid(){
		for (int r=0;r<MapConstants.MAP_ROW;r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				if (!exMap.getBlock(r,c).getIsExplored()){
					return exMap.getBlock(r,c);
				}
			}
		}
		return null;
	}

	private Block nearestColUnexploredGrid(){
		for (int c=0; c<MapConstants.MAP_COL; c++){
			for (int r=0;r<MapConstants.MAP_ROW;r++){
				if (!exMap.getBlock(r,c).getIsExplored()){
					return exMap.getBlock(r,c);
				}
			}
		}
		return null;
	}

	private Block nearbyObstacle(Block blk){
		int c = blk.getCol();
		int r = blk.getRow();
		if (exMap.getBlock(r,c+1).getIsObstacle()){
			return exMap.getBlock(r,c+1);
		}
		else if (exMap.getBlock(r,c-1).getIsObstacle()){
			return exMap.getBlock(r,c-1);
		}
		else if (exMap.getBlock(r+1,c).getIsObstacle()){
			return exMap.getBlock(r+1,c);
		}
		else if (exMap.getBlock(r-1,c).getIsObstacle()){
			return exMap.getBlock(r-1,c);
		}
		else{
			return null;
		}
	}

	private void printPathTaken(){
		int n = pathTaken.size();
		for (int i=0;i<n;i++){
			System.out.print("(" + pathTaken.get(i).getRow() + "," + pathTaken.get(i).getCol() + "), ");
		}
	}
}