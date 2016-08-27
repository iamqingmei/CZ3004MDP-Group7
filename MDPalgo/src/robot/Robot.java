package robot;

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

	public DIRECTION getRobotCurDir(){
		return robotCurDir;
	}
}

