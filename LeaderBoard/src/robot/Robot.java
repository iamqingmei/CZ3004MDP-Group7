package robot;


import java.util.concurrent.TimeUnit;

import communication.CommMgr;
import robot.RobotConstants;
import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;
import robot.Sensor;
import map.Map;



public class Robot{
	private int posRow;
	private int posCol;
	private DIRECTION robotCurDir;
	private int botSpeed = 50;
	public Sensor longFront;
	public Sensor shortRF;
	public Sensor shortLF;
	public Sensor shortR;
	public Sensor shortL;
	
	public Robot(){
		posRow= -1;
		posCol= -1;
		robotCurDir = RobotConstants.STARTING_DIR;
		longFront = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol,this.robotCurDir);
		shortRF = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol + 1,this.robotCurDir);
		shortLF = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol - 1,this.robotCurDir);
		shortR = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow+ 1, this.posCol + 1,leftRightDirection(MOVE.RIGHT));
		shortL = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow+ 1, this.posCol - 1,leftRightDirection(MOVE.LEFT));
	}
	
	public Robot(int r, int c){
		posRow = r;
		posCol = c;
		robotCurDir = RobotConstants.STARTING_DIR;
		longFront = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol,this.robotCurDir);
		shortRF = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol + 1,this.robotCurDir);
		shortLF = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow + 1,this.posCol - 1,this.robotCurDir);
		shortR = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow+ 1, this.posCol + 1,leftRightDirection(MOVE.RIGHT));
		shortL = new Sensor(RobotConstants.SENSOR_SHORT_RANGE,this.posRow+ 1, this.posCol - 1,leftRightDirection(MOVE.LEFT));
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

	public DIRECTION getRobotCurDir(){
		return robotCurDir;
	}

	public void setRobotSpeed(int s){
		botSpeed = s;
	}
	
	public void moveRobot(MOVE m){
		// try{
		// 	TimeUnit.MILLISECONDS.sleep(botSpeed);
		// }
		// catch(InterruptedException e)
		// {
		//      System.out.println("robot sleep error!");
		// }
		switch (m){
			case FORWARD:
				switch (robotCurDir){
					case NORTH:
						posRow ++;
						break;
					case SOUTH:
						posRow --;
						break;
					case WEST:
						posCol --;
						break;
					case EAST:
						posCol ++;
						break;
				}
				break;
			case RETURN:
				switch (robotCurDir){
					case NORTH:
						posRow --;
						break;
					case SOUTH:
						posRow ++;
						break;
					case WEST:
						posCol ++;
						break;
					case EAST:
						posCol --;
						break;
				}
				break;
			case RIGHT:
				robotCurDir = leftRightDirection(MOVE.RIGHT);
				// System.out.println("robot facing:" +  robotCurDir);
				break;
			case LEFT:
				robotCurDir = leftRightDirection(MOVE.LEFT);
				// System.out.println("robot facing:" +  robotCurDir);
				break;
			
			default:
				System.out.println("Error!");
				break;
			}
		}

	public void setSensors(){
		switch (robotCurDir){
			case NORTH:
				longFront.setSensor(this.posRow + 1,this.posCol,this.robotCurDir);
				shortRF.setSensor(this.posRow + 1,this.posCol + 1,this.robotCurDir);
				shortLF.setSensor(this.posRow + 1,this.posCol - 1,this.robotCurDir);
				shortR.setSensor(this.posRow+ 1, this.posCol + 1,leftRightDirection(MOVE.RIGHT));
				shortL.setSensor(this.posRow+ 1, this.posCol - 1,leftRightDirection(MOVE.LEFT));
				break;
			case SOUTH:
				longFront.setSensor(this.posRow - 1,this.posCol,this.robotCurDir);
				shortRF.setSensor(this.posRow - 1,this.posCol - 1,this.robotCurDir);
				shortLF.setSensor(this.posRow - 1,this.posCol + 1,this.robotCurDir);
				shortR.setSensor(this.posRow- 1, this.posCol - 1,leftRightDirection(MOVE.RIGHT));
				shortL.setSensor(this.posRow- 1, this.posCol + 1,leftRightDirection(MOVE.LEFT));
				break;
			case EAST:
				longFront.setSensor(this.posRow,this.posCol + 1,this.robotCurDir);
				shortRF.setSensor(this.posRow - 1,this.posCol + 1,this.robotCurDir);
				shortLF.setSensor(this.posRow + 1,this.posCol + 1,this.robotCurDir);
				shortR.setSensor(this.posRow- 1, this.posCol + 1,leftRightDirection(MOVE.RIGHT));
				shortL.setSensor(this.posRow+ 1, this.posCol + 1,leftRightDirection(MOVE.LEFT));
				break;
			case WEST:
				longFront.setSensor(this.posRow,this.posCol - 1,this.robotCurDir);
				shortRF.setSensor(this.posRow + 1,this.posCol - 1,this.robotCurDir);
				shortLF.setSensor(this.posRow - 1,this.posCol - 1,this.robotCurDir);
				shortR.setSensor(this.posRow+ 1, this.posCol - 1,leftRightDirection(MOVE.RIGHT));
				shortL.setSensor(this.posRow- 1, this.posCol - 1,leftRightDirection(MOVE.LEFT));
				break;
		}
		
	}

	public int[] sense(Map simExMap){
		int[] result = new int[5];
	
		String sensorData = CommMgr.getCommMgr().recvMsg();
		
		// System.out.println("sensorData: "+ sensorData);
		
		String sub = sensorData.substring(1);
		String[] parts = sub.split(":");

		result[0] = roundToGrid(Integer.parseInt(parts[2]));
		result[1] = roundToGrid(Integer.parseInt(parts[3]));
		result[2] = roundToGrid(Integer.parseInt(parts[1]));
		result[3] = roundToGrid(Integer.parseInt(parts[4]));
		result[4] = roundToGrid(Integer.parseInt(parts[0]));

		// System.out.println("sensorData in gird: ");
		// for (int i=0;i<5;i++){
		// 	System.out.printf("%d, ", result[i]);
		// }

		longFront.sense(simExMap,result[0]);
		shortRF.sense(simExMap,result[1]);
		shortLF.sense(simExMap,result[2]);
		shortR.sense(simExMap,result[3]);
		shortL.sense(simExMap,result[4]);
		return result;
	}
	
	private int roundToGrid(Integer cm){
		int base = cm/10;
		if (cm%10>=5){
			return ++base;
		}
		return base;
	}


	private DIRECTION leftRightDirection(MOVE m){
		if ( m == MOVE.RIGHT){
			switch (robotCurDir){
				case NORTH:
					// System.out.println("RIGHT EAST!");
					return DIRECTION.EAST;
				case SOUTH:
					return DIRECTION.WEST;
				case WEST:
					return DIRECTION.NORTH;
				case EAST:
					return DIRECTION.SOUTH;
			}
		}
		else{ //left
			switch (robotCurDir){
				case NORTH:
				// System.out.println("LEFT WEST!");
					return DIRECTION.WEST;
				case SOUTH:
					return DIRECTION.EAST;
				case WEST:
					return DIRECTION.SOUTH;
				case EAST:
					return DIRECTION.NORTH;
			}
		}
		return robotCurDir;		
	}

	 	
}

