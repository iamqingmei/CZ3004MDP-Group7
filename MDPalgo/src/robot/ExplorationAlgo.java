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
	private ArrayList<Block> pathTaken;
	private int repeatedArea = 0;
	private int exploredArea;
	private int[] sensorData;

	public ExplorationAlgo(Map explorationMap, Map rMap, Robot thebot){
		this.exMap = explorationMap;
		this.realMap = rMap;
		this.bot = thebot;
	}

	public void runExploration(){
		MOVE nextMove = null;
		MOVE prevMov = null;
		bot.setSensors();
		sensorData = bot.sense(exMap, realMap);
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		for (int i = 0; i<5; i++){
			System.out.println(i + ": " + sensorData[i]);
		}
		exploredArea = countExploredArea();
		System.out.println("exploredArea: " + exploredArea);
		exMap.repaint();
		do{
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			// if next cell is already explored then
				// repeatedArea += 1
			System.out.println("move: " + nextMove);
			bot.moveRobot(nextMove);
			bot.setSensors();
			sensorData = bot.sense(exMap, realMap);
			// for (int i = 0; i<5; i++){
			// 	System.out.println(i + ": " + sensorData[i]);
			// }
			exploredArea = countExploredArea();
			System.out.println("exploredArea: " + exploredArea);
			exMap.repaint();
		}while(bot.getRobotPosCol() != 1 || bot.getRobotPosRow() != 1);
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
		if (r>=0 && r<MapConstants.MAP_ROW && c>=0 && c<MapConstants.MAP_COL){
			return (exMap.getBlock(r,c).getIsExplored() && (!exMap.getBlock(r,c).getIsObstacle())); //explored and not obstacle
		}
		return false;
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

	private MOVE getNextMove(MOVE prevMov){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		switch (bot.getRobotCurDir()){
			case NORTH: 
				if (nSideFree() && !wSideFree()){
					return MOVE.FORWARD;
				}
				else if (wSideFree() && prevMov!= MOVE.LEFT){
					return MOVE.LEFT;
				}
				else if (eSideFree() && !nSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("north error!");
					return MOVE.FORWARD;
				}
			case EAST:
				if (eSideFree() && !nSideFree() ){
					return MOVE.FORWARD;
				}
				else if (nSideFree() && prevMov!= MOVE.LEFT){
					return MOVE.LEFT;
				}
				else if (sSideFree() && !eSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("east error!");
					return MOVE.FORWARD;
				}
			case SOUTH:
				if (sSideFree() && !eSideFree() ){
					return MOVE.FORWARD;
				}
				else if (eSideFree() && prevMov!= MOVE.LEFT){
					return MOVE.LEFT;
				}
				else if (wSideFree() && !sSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("south error!");
					return MOVE.FORWARD;
				}
			case WEST:
				if (wSideFree() && !sSideFree() ){
					return MOVE.FORWARD;
				}
				else if (sSideFree() && prevMov!= MOVE.LEFT){
					return MOVE.LEFT;
				}
				else if (nSideFree() && !wSideFree()){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("west error!");
					return MOVE.FORWARD;
				}
			default:
				return MOVE.LEFT;
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
		for (int i = 0; i<5; i++){
			System.out.println(i + ": " + sensorData[i]);
		}
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
	}

	//for coverage limited
	public void runExploration(long coverageLimited){
		MOVE nextMove = null;
		MOVE prevMov = null;
		bot.setSensors();
		sensorData = bot.sense(exMap, realMap);
		for (int i = 0; i<5; i++){
			System.out.println(i + ": " + sensorData[i]);
		}
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
		}while((bot.getRobotPosCol() != 1 || bot.getRobotPosRow() != 1) && (exploredArea < coverageLimited));
	}
}