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
			nextMove = getNextMove();
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
		}while(bot.getRobotPosCol() != RobotConstants.GOAL_COL || bot.getRobotPosRow() != RobotConstants.GOAL_ROW);
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

	private MOVE getNextMove(){
		int botRow = bot.getRobotPosRow();
		int botCol = bot.getRobotPosCol();
		// LEFT side free:
		// checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2)
		// RIGHT side free:
		// checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2)
		// UP side free:
		// checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol)
		// DOWN side free:
		// checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1)
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		switch (bot.getRobotCurDir()){
			case NORTH: 
				if (checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol) && sensorData[4] == 1){
					return MOVE.FORWARD;
				}
				else if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2)){
					return MOVE.LEFT;
				}
				else if (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2) && sensorData[0] == 1){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("north error!");
					return MOVE.ERROR;
				}
			case EAST:
				if (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2) && sensorData[4] == 1 ){
					return MOVE.FORWARD;
				}
				else if (checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol)){
					return MOVE.LEFT;
				}
				else if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1) && sensorData[0] == 1){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("east error!");
					return MOVE.ERROR;
				}
			case SOUTH:
				if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1) && sensorData[4] == 1 ){
					return MOVE.FORWARD;
				}
				else if (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2)){
					return MOVE.LEFT;
				}
				else if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2) && sensorData[0] == 1){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("south error!");
					return MOVE.ERROR;
				}
			case WEST:
				if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2) && sensorData[4] == 1 ){
					return MOVE.FORWARD;
				}
				else if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1)){
					return MOVE.LEFT;
				}
				else if (checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol) && sensorData[0] == 1){
					return MOVE.RIGHT;
				}
				else{
					System.out.println("west error!");
					return MOVE.ERROR;
				}
			default:
				return MOVE.ERROR;
		}
		
		// switch (bot.getRobotCurDir()){
		// 	case NORTH:
		// 		if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2) && prevMov != MOVE.LEFT){
		// 			return MOVE.LEFT;
	 //            }
		// 		else if (checkStatus(botRow+2, botCol - 1) && checkStatus(botRow+2, botCol + 1) && checkStatus(botRow+2, botCol)){
		// 			return MOVE.FORWARD;
		// 		}
		// 		else if(checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2)){
		// 			return MOVE.RIGHT;
		// 		}
		// 	case SOUTH:
		// 		if (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2) && prevMov != MOVE.LEFT){
		// 			return MOVE.LEFT;
	 //            }
		// 		else if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1)){
		// 			return MOVE.FORWARD;
		// 		}
		// 		else if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2)){
		// 			return MOVE.RIGHT;
		// 		}
		// 	case WEST:
		// 		if (checkStatus(botRow+2, botCol-1) && checkStatus(botRow+2, botCol) && checkStatus(botRow+2, botCol+1) && prevMov != MOVE.LEFT){
		// 			return MOVE.LEFT;
	 //            }
		// 		else if (checkStatus(botRow-1, botCol-2) && checkStatus(botRow, botCol-2) && checkStatus(botRow+1, botCol-2)){
		// 			return MOVE.FORWARD;
		// 		}
		// 		else if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1)){
		// 			return MOVE.RIGHT;
		// 		}
		// 	case EAST:
		// 		if (checkStatus(botRow-2, botCol-1) && checkStatus(botRow-2, botCol) && checkStatus(botRow-2, botCol+1) && prevMov != MOVE.LEFT){
		// 			return MOVE.LEFT;
	 //            }
		// 		else if (checkStatus(botRow-1, botCol+2) && checkStatus(botRow, botCol+2) && checkStatus(botRow+1, botCol+2)){
		// 			return MOVE.FORWARD;
		// 		}
		// 		else if (checkStatus(botRow+2, botCol-1) && checkStatus(botRow+2, botCol) && checkStatus(botRow+2, botCol+1)){
		// 			return MOVE.RIGHT;
		// 		}
		// 	default:
		// 		return MOVE.LEFT;
		// }
	}

}