package robot;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import robot.RobotConstants;
import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;



public class Robot extends JPanel {
	private int posRow;
	private int posCol;
	private DIRECTION robotCurDir;
	private int botSpeed = 300;
	
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

	public void setRobotSpeed(int s){
		botSpeed = s;
	}
	
	public void moveRobot(MOVE m){
		try{
				TimeUnit.MILLISECONDS .sleep(botSpeed);
			}
			catch(InterruptedException e)
			{
			     System.out.println("Miao!");
			}
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
			
			default:
				System.out.println("Error!");
				break;
			}
		}
			
}

