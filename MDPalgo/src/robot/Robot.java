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

	public void paintComponent(Graphics g) {
		System.out.println("Hi!");
		g.setColor(Color.RED);
		g.fillRect(40, 40, 36, 36);	
	}
			/*
			_offsetX = (_editorWidth - _robotWidth) / 2;
			_offsetY = (_editorHeight - _robotHeight) / 2;
			
			_robotGridSize = _robotWidth / RobotConstants.ROBOT_SIZE;
			_sensorGridSize = _robotGridSize / 6;
			
			// Calculate the robot grids
			_robotGrids = new RobotGrid[RobotConstants.ROBOT_SIZE][RobotConstants.ROBOT_SIZE];
			for(int robotRow = 0; robotRow < RobotConstants.ROBOT_SIZE; robotRow++)
			{
				for(int robotCol = 0; robotCol < RobotConstants.ROBOT_SIZE; robotCol++)
				{
					_robotGrids[robotRow][robotCol] = new RobotGrid(
							_offsetX + (robotCol * _robotGridSize),
							_offsetY + (robotRow * _robotGridSize));
				}
			}
			

		// Clear the robot graph
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, _editorWidth, _editorHeight);
		
		
		// Draw the robot grids
		for(int robotRow = 0; robotRow < RobotConstants.ROBOT_SIZE; robotRow++)
		{
			for(int robotCol = 0; robotCol < RobotConstants.ROBOT_SIZE; robotCol++)
			{
				g.setColor(RobotConstants.C_LINE);
				g.fillRect(_robotGrids[robotRow][robotCol].x,
						_robotGrids[robotRow][robotCol].y,
						_robotGridSize, _robotGridSize);
				
				Color gridColor = RobotConstants.C_UNEXPLORED;
				g.setColor(gridColor);
				g.fillRect(
						(_robotGrids[robotRow][robotCol].x + RobotConstants.LINE_WEIGHT),
						(_robotGrids[robotRow][robotCol].y + RobotConstants.LINE_WEIGHT),
						(_robotGridSize - (RobotConstants.LINE_WEIGHT * 2)),
						(_robotGridSize - (RobotConstants.LINE_WEIGHT * 2)));
			}
		}
		
		// Draw the robot outline - Reduced size
		g.setColor(RobotConstants.C_ROBOT_OUTLINE_EDITOR);
		g.fillOval(_offsetX + RobotConstants.LINE_WEIGHT + 20,
				_offsetY + RobotConstants.LINE_WEIGHT + 20,
				_robotWidth - (RobotConstants.LINE_WEIGHT * 2) - 40,
				_robotHeight - (RobotConstants.LINE_WEIGHT * 2) - 40);
		
		// Draw the robot - Reduced size
		g.setColor(RobotConstants.C_ROBOT_EDITOR);
		g.fillOval(_offsetX + (RobotConstants.LINE_WEIGHT * 4) + 20,
				_offsetY + (RobotConstants.LINE_WEIGHT * 4) + 20,
				_robotWidth - (RobotConstants.LINE_WEIGHT * 8) - 40,
				_robotHeight - (RobotConstants.LINE_WEIGHT * 8) - 40);
		
		// Draw the front of the robot
		g.setColor(RobotConstants.C_ROBOT_FRONT_EDITOR);
		g.fillArc(
				_offsetX + (RobotConstants.LINE_WEIGHT * 4)
						+ (_robotWidth - (RobotConstants.LINE_WEIGHT * 8)) / 4,
				_offsetY + (RobotConstants.LINE_WEIGHT * 4)
						- (_robotHeight - (RobotConstants.LINE_WEIGHT * 8)) / 6,
				(_robotWidth - (RobotConstants.LINE_WEIGHT * 8)) / 2,
				(_robotHeight - (RobotConstants.LINE_WEIGHT * 8)) / 2 + 80, -80, -20);
		
		}
		
	}
	
	private class RobotGrid {
		public int x;
		public int y;
		
		public int sensorX;
		public int sensorY;
		
		public RobotGrid(int x, int y) {
			this.x = x;
			this.y = y;
			
			this.sensorX = x + ((_robotGridSize - _sensorGridSize) / 2);
			this.sensorY = y + ((_robotGridSize - _sensorGridSize) / 2);
		}
	}
	*/
}

