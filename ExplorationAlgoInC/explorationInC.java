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
	private ArrayList<Block> pathTaken;
	private int[] sensorData;

	public static boolean emergencyCalibration = false;

	public ExplorationAlgo(Map explorationMap, Robot thebot){
		this.exMap = explorationMap;
		this.bot = thebot;
	}

	// public void setEmergencyCalibration(boolean b){
	// 	emergencyCalibration = b;
	// }



	public void runExploration(){
		bot.setSensors();
		sensorData = bot.sense(exMap);
		// sensorData[0] = longFront
		// sensorData[1] = shortRF
		// sensorData[2] = shortLF
		// sensorData[3] = shortR
		// sensorData[4] = shortL
		// System.out.println("exploredArea: " + exploredArea);
		exMap.repaint();
		exMap.mapDescriptor();
		try{
			TimeUnit.MILLISECONDS.sleep(50);
		}
		catch(InterruptedException e)
		{
		     System.out.println("send msg sleeping error!!!!!!");
		} 
		looping(1,1);

		turnRobotDir(DIRECTION.NORTH);
		return;
	}

	private void turnRobotDir(DIRECTION dir){
		// System.out.println("robot facing: " + bot.getRobotCurDir());
		while(bot.getRobotCurDir() != dir){
			bot.moveRobot(MOVE.RIGHT);
			// pressAnyKeyToContinue();
			if (emergencyCalibration == true){
				System.out.println("Emergency Calibration!!!!");
				CommMgr.getCommMgr().sendMsg("M","PC2AR");
				emergencyCalibration = false;
			}
			CommMgr.getCommMgr().sendMsg("R","PC2AR");
			bot.setSensors();
			sensorData = bot.sense(exMap);
			exMap.repaint();
			exMap.mapDescriptor();
			// System.out.println("robot facing: " + bot.getRobotCurDir());
		}
	}

	private void looping(int r, int c){
		MOVE nextMove = null;
		MOVE prevMov = null;
		do{
			prevMov = nextMove;
			nextMove = getNextMove(prevMov);
			// System.out.println("move: " + nextMove);
			// pressAnyKeyToContinue();
			if (emergencyCalibration == true){
				System.out.println("Emergency Calibration!!!!");
				CommMgr.getCommMgr().sendMsg("M","PC2AR");
				emergencyCalibration = false;
			}
			CommMgr.getCommMgr().sendMsg(nextMove.print(nextMove), "PC2AR"); //send to arduino
			bot.moveRobot(nextMove);
			bot.setSensors(); 
			sensorData = bot.sense(exMap); //wait for receive sensor data
			exploredArea = countExploredArea();
			// System.out.println("exploredArea: " + exploredArea);
			exMap.repaint();
			exMap.mapDescriptor(); //send map layout and robot position to android
			try{
				TimeUnit.MILLISECONDS.sleep(50);
			}
			catch(InterruptedException e)
			{
			     System.out.println("send msg sleeping error!!!!!!");
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
}