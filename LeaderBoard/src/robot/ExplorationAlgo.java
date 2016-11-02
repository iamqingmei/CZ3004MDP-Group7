package robot;

import java.util.*;
import java.util.concurrent.TimeUnit;
import map.MapConstants;
import map.Map;
import map.Block;

import communication.CommMgr;
import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;
import robot.Robot;
import robot.RobotConstants;


public class ExplorationAlgo{
	private Map exMap;
	private Robot bot;
	private int exploredArea;
	private int[] sensorData;
	private long start = 0;
	private long end = 0;
	private boolean passedGoalZone = false;

	public static boolean emergencyCalibration = false;

	public ExplorationAlgo(Map explorationMap, Robot thebot){
		this.exMap = explorationMap;
		this.bot = thebot;
	}

	public void runExploration(){
		start = System.currentTimeMillis();
		end = start + 320*1000; // 320 seconds * 1000 ms/sec
		pathTakenIsConfirmedFree();
		bot.setSensors();
		sensorData = bot.sense(exMap);
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		exploredArea = countExploredArea();
		exMap.repaint();

		looping(RobotConstants.STARTING_ROW,RobotConstants.STARTING_COL);

		//go back to start zone
		System.out.println("start to calculate the FastestPath to go back to start zone");
		ShortestPathAlgo goBackToStart = new ShortestPathAlgo(exMap,bot);
		StringBuilder output = goBackToStart.runShortestPath(exMap,1,1);
		System.out.println("Fastest Path is : " + output);
		if (output.length() != 0){
			//only send out the string when it is not empty
		    CommMgr.getCommMgr().sendMsg("X" + output.toString(), "PC2AR");
		}

		//after back to the start zone
		//turn to North (Ready for shortest path finding)

		while(bot.getRobotCurDir() != DIRECTION.NORTH){
			bot.moveRobot(MOVE.RIGHT);
			CommMgr.getCommMgr().sendMsg("R","PC2AR");
			exMap.repaint();

			try{
				TimeUnit.MILLISECONDS.sleep(RobotConstants.MSG_DELAY);
			}
			catch(InterruptedException e)
			{
			     System.out.println("send msg sleeping error!!!!!!");
			}
			exMap.MDFString();

		}
		return;
	}

	private void looping(int r, int c){
		MOVE nextMove = null;
		MOVE prevMov = null;
		do{
			//the path taken is confirmed free
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			CommMgr.getCommMgr().sendMsg(nextMove.print(nextMove), "PC2AR"); //send to arduino
			bot.moveRobot(nextMove);
			bot.setSensors(); 
			pathTakenIsConfirmedFree();
			sensorData = bot.sense(exMap); //wait for receive sensor data
			exploredArea = countExploredArea();
			exMap.repaint();

			exMap.MDFString(); //send map layout and robot position to android
			try{
				TimeUnit.MILLISECONDS.sleep(RobotConstants.MSG_DELAY);
			}
			catch(InterruptedException e)
			{
			     System.out.println("send msg sleeping error!!!!!!");
			}
			
			if (exploredArea==MapConstants.MAP_SIZE){
				//return when the whole map is explored
				return;
			}
			if (System.currentTimeMillis() > end){
				System.out.println("Time is up! Directly go back to start zone");
				return;
			}
			if (bot.getRobotPosCol() == RobotConstants.GOAL_COL && bot.getRobotPosRow() == RobotConstants.GOAL_ROW){
				System.out.println("!!!!!!!!!!!!!!!!GOAL ZONE PASSED!!!!!!!!!!!!!!!!");
				passedGoalZone = true;
			}
			if (passedGoalZone == true && exploredArea > MapConstants.MAP_SIZE / 100 * RobotConstants.COVERAGE_PERCENTAGE){
				// if the robot alr passed goal zone AND
				// explored area is more than the coverage limitation
				// directly return to the starting zone
				System.out.println("Hit the coverage limitation!");
				return;
			} 
		}while(bot.getRobotPosCol() != c || bot.getRobotPosRow() != r);
	}

	private int countExploredArea(){
		int result = 0;
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				if (exMap.getBlock(r,c).getIsExplored()){
					result++;
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
					return MOVE.FORWARD;
				}
				else if (nSideFree()){
					if (prevMov!= MOVE.LEFT){
						return MOVE.LEFT;
					}
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
				return MOVE.RIGHT;
		}
	}
	
	private void pressAnyKeyToContinue(){ 
        System.out.println("Press any key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
	}

	private void pathTakenIsConfirmedFree(){
		int r = bot.getRobotPosRow();
		int c = bot.getRobotPosCol();
		exMap.getBlock(r,c).setConfirmFree(true);
		exMap.getBlock(r+1,c).setConfirmFree(true);
		exMap.getBlock(r-1,c).setConfirmFree(true);
		exMap.getBlock(r,c+1).setConfirmFree(true);
		exMap.getBlock(r+1,c+1).setConfirmFree(true);
		exMap.getBlock(r-1,c+1).setConfirmFree(true);
		exMap.getBlock(r,c-1).setConfirmFree(true);
		exMap.getBlock(r+1,c-1).setConfirmFree(true);
		exMap.getBlock(r-1,c-1).setConfirmFree(true);
	}
}