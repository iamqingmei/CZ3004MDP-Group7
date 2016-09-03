import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.concurrent.TimeUnit;
import java.util.*;

import map.Block;
import robot.Robot;
import robot.RobotConstants.MOVE;
import robot.RobotConstants.DIRECTION;

import map.Map; 
import robot.ShortestPathAlgo;
public class Simulator {
	// JFrame for the application
	private static JFrame _appFrame = null;
	
	// JPanel for laying out different views
	private static JPanel _mainCards = null;
	private static JPanel _buttonsCards = null;

	// The frame used for configuring the robot
	private static JPanel _robotConfig = null;

	// The frame used for main menu buttons
	private static JPanel _mainButtons = null;
	
	// The frame used for robot configuration menu buttons
	private static JPanel _robotConfigButtons = null;
	
	// The frame used for exploration & shortest path buttons
	private static JPanel _robotMapButtons = null;

	/**
	 * The 'Save MDF Strings' JButton
	 */
	private static JButton _btn_saveMDFStrings = null;
	
	/**
	 * The 'Emergency Recalibration' JButton
	 */
	private static JButton _btn_emergencyRecalibration = null;

	private static Map theMap = null;

	private static Robot bot;


	public static void main(String[] args){
		bot = new Robot(1,1);
		theMap = new Map(bot);
		theMap.setObstacle(5, 0);
		theMap.setObstacle(5, 1);
		theMap.setObstacle(5, 2);
		theMap.setObstacle(5,3);
		theMap.setObstacle(0,13);
		theMap.setObstacle(1,13);
		theMap.setObstacle(2,13);
		theMap.setObstacle(3,13);
		theMap.setObstacle(9,9);
		theMap.setObstacle(10,16);
		theMap.setObstacle(11,16);
		theMap.setObstacle(12,16);
		theMap.setObstacle(13,16);
		theMap.setObstacle(14,16);

		// ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		// shortestPath.runShortestPath(theMap, 13, 18);
		// shortestPath.printGscores();
		
		// shortestPath.reset(theMap, bot);
		// shortestPath.runShortestPath(theMap, 1, 1);
		// shortestPath.printGscores();

		displayEverythings();
		try{
				TimeUnit.MILLISECONDS .sleep(150);
			}
			catch(InterruptedException e)
			{
			     System.out.println("Miao!");
			}

		ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		Stack<Block> path = shortestPath.runShortestPath(theMap, 13, 18);
		moveRobot(bot, path);
		// Block temp;
		// System.out.println("path is empty?: " + path.isEmpty());
		// while(!path.isEmpty()){
		// 	try{
		// 		TimeUnit.MILLISECONDS .sleep(300);
		// 	}
		// 	catch(InterruptedException e)
		// 	{
		// 	     System.out.println("Miao!");
		// 	}
		// 	temp = path.pop();
		// 	bot.setRobotPos(temp.getRow(),temp.getCol());
		// 	System.out.println("set robot as ("+ temp.getCol() + " ,"+ temp.getRow()+ ")");
		// }

	}

	private static void displayEverythings(){
		// Main frame for displaying everything
		_appFrame = new JFrame();
		_appFrame.setTitle("MDP Group 7 Simulator");
		_appFrame.setSize(new Dimension(800, 700));
		_appFrame.setResizable(false);
		
		// Center the main frame in the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		_appFrame.setLocation(dim.width/2 - _appFrame.getSize().width/2, dim.height/2 - _appFrame.getSize().height/2);
		
		// Create the CardLayouts for storing the different views
		_mainCards = new JPanel(new CardLayout());
		// _buttonsCards = new JPanel(new CardLayout());
		
		// Initialize the main CardLayout
		initMainLayout();
		
		// Initialize the buttons CardLayout
		// initButtonsLayout();
		
		// Add CardLayouts to content pane
		Container contentPane = _appFrame.getContentPane();
		contentPane.add(_mainCards, BorderLayout.CENTER);
		// contentPane.add(_buttonsCards, BorderLayout.SOUTH);
		
		// Display the application
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void initMainLayout() {
		
		// Initialize the Map for simulation
		_mainCards.add(theMap, "MAIN");
		
		// Initialize the robot configuration frame
		// _mainCards.add(bot, "ROBOT CONFIG");
		
		// // Initialize the robot map, used for exploration and shortest path
		// _robotMap = new RobotMap(theMap);
		// _mainCards.add(_robotMap, "ROBOT MAP");
		
		// Show the real map (main menu) by default
		CardLayout cl = ((CardLayout) _mainCards.getLayout());
	    cl.show(_mainCards, "MAIN");

	    theMap.setFocusable(true);
		theMap.requestFocusInWindow();
	    // cl.show(_mainCards, "ROBOT CONFIG");
		
	}

	//robot is going to block b
	//which direction it should turn to
	private static DIRECTION getTargetDir(int botR, int botC, DIRECTION botDir, Block b){
		if (botC - b.getCol() > 0){
			return DIRECTION.EAST;
		}
		else if (b.getCol() - botC > 0){
			return DIRECTION.WEST;
		}
		else{ //same col
			if (botR - b.getRow() > 0){
				return DIRECTION.SOUTH;
			}
			else if (b.getRow() - botR > 0){
				return DIRECTION.NORTH;
			}
			else{ //same pos
				System.out.println("2222!");
				return botDir;
			}
		}
	}

	//from direction a to b, what moves should robot do?
	private static MOVE getTargetMove(DIRECTION a, DIRECTION b){
		switch (a){
			case NORTH:
				switch (b){
					case NORTH: return MOVE.ERROR; 
					case SOUTH: return MOVE.LEFT; 
					case WEST: return MOVE.LEFT; 
					case EAST: return MOVE.RIGHT; 
				}
				break;
			case SOUTH:
				switch (b){
					case NORTH: return MOVE.LEFT; 
					case SOUTH: return MOVE.ERROR; 
					case WEST: return MOVE.RIGHT; 
					case EAST: return MOVE.LEFT; 
				}
				break;
			case WEST:
				switch (b){
					case NORTH: return MOVE.RIGHT; 
					case SOUTH: return MOVE.LEFT; 
					case WEST: return MOVE.ERROR; 
					case EAST: return MOVE.LEFT; 
				}
				break;
			case EAST:
				switch (b){
					case NORTH: return MOVE.LEFT; 
					case SOUTH: return MOVE.RIGHT; 
					case WEST: return MOVE.LEFT; 
					case EAST: return MOVE.ERROR; 
				}
		}
		System.out.println("1111!");
		return MOVE.ERROR;
	}

	// given the path which robot should go
	// move the robot according to the path
	private static void moveRobot(Robot bot, Stack<Block> path){
		Block temp = path.pop();		
		DIRECTION targetDir = bot.getRobotCurDir();
		while(!path.isEmpty()){
			if (bot.getRobotPosRow() == temp.getRow() && bot.getRobotPosCol() ==temp.getCol() ){
				temp = path.pop();
			}
			System.out.println("move from" + bot.getRobotPosRow() + ", " + bot.getRobotPosCol() + " to " + temp.getRow() + " , " + temp.getCol());
			targetDir = getTargetDir(bot.getRobotPosRow(), bot.getRobotPosCol(), bot.getRobotCurDir(), temp);
			try{
				TimeUnit.MILLISECONDS .sleep(300);
			}
			catch(InterruptedException e)
			{
			     System.out.println("Miao!");
			}
			if (bot.getRobotCurDir() != targetDir){
				System.out.println("robot cur dir:" + bot.getRobotCurDir().toString());
				System.out.println("target dir:" + targetDir.toString());
				System.out.println("move:" + getTargetMove(bot.getRobotCurDir(),targetDir).toString());
				bot.moveRobot(getTargetMove(bot.getRobotCurDir(),targetDir));
			}
			else{ //alr pointing to the target direction
				System.out.println("move: FORWARD");
				bot.moveRobot(MOVE.FORWARD);
			}
		}
	}

}
