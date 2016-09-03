package robot;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import robot.RobotConstants;
import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;



public class Robot extends JPanel {
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
	
	public void moveRobot(MOVE m){
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
						posCol ++;
						break;
					case EAST:
						posCol --;
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
						posCol --;
						break;
					case EAST:
						posCol ++;
						break;
				}
				break;
			case RIGHT:
				switch (robotCurDir){
					case NORTH:
						robotCurDir = DIRECTION.EAST;
						break;
					case SOUTH:
						robotCurDir = DIRECTION.WEST;
						break;
					case WEST:
						robotCurDir = DIRECTION.NORTH;
						break;
					case EAST:
						robotCurDir = DIRECTION.SOUTH;
						break;
					}
				break;
			case LEFT:
				switch (robotCurDir){
					case NORTH:
						robotCurDir = DIRECTION.WEST;
						break;
					case SOUTH:
						robotCurDir = DIRECTION.EAST;
						break;
					case WEST:
						robotCurDir = DIRECTION.SOUTH;
						break;
					case EAST:
						robotCurDir = DIRECTION.NORTH;
						break;
					}
				break;
			}
		}
			
}

