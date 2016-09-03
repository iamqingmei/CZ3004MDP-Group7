
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

	// The frame used for main menu buttons
	private static JPanel _mainButtons = null;

	private static Map theMap = null;

	private static Robot bot;

	private static boolean runFastestPath = false;

	private static int botSpeed = 300; //default 300

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


		displayEverythings();

		while(true){
			System.out.print("");
			if (runFastestPath){
				System.out.println("entered!");
				FastestPath();
			}
			continue;
		}
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
		_buttonsCards = new JPanel(new CardLayout());
		
		// Initialize the main CardLayout
		initMainLayout();
		
		// Initialize the buttons CardLayout
		initButtonsLayout();
		
		// Add CardLayouts to content pane
		Container contentPane = _appFrame.getContentPane();
		contentPane.add(_mainCards, BorderLayout.CENTER);
		contentPane.add(_buttonsCards, BorderLayout.SOUTH);
		
		// Display the application
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void initMainLayout() {
		
		// Initialize the Map for simulation
		_mainCards.add(theMap, "MAIN");
		
		CardLayout cl = ((CardLayout) _mainCards.getLayout());
	    cl.show(_mainCards, "MAIN");
		
	}

	private static void initButtonsLayout() {
		
		// Initialize the buttons used in main menu
		_mainButtons = new JPanel();
		addMainMenuButtons();
		_buttonsCards.add(_mainButtons, "MAIN_BUTTONS");
		
		// Show the real map (main menu) buttons by default
		CardLayout cl = ((CardLayout) _buttonsCards.getLayout());
		cl.show(_buttonsCards, "MAIN_BUTTONS");
	}

	private static void addMainMenuButtons() {
		// Main menu buttons
		// Exploration button
		JButton btn_Exploration = new JButton("Exploration");
		btn_Exploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_Exploration.setMargin(new Insets(5, 5, 5, 5));
		btn_Exploration.setFocusPainted(false);
		btn_Exploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_Exploration);


		//Shortest Path button
		JButton btn_ShortestPath = new JButton("Fastest Path");
		btn_ShortestPath.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_ShortestPath.setMargin(new Insets(5, 5, 5, 5));
		btn_ShortestPath.setFocusPainted(false);
		btn_ShortestPath.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				runFastestPath = true;
				System.out.println("miao true!");
			}
		});
		_mainButtons.add(btn_ShortestPath);

		// Load Map button
		JButton btn_LoadMap = new JButton("Load Map");
		btn_LoadMap.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_LoadMap.setMargin(new Insets(5, 5, 5, 5));
		btn_LoadMap.setFocusPainted(false);
		btn_LoadMap.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_LoadMap);

		// Set speed of robot (speed of X steps per second ) button
		JButton btn_Speed = new JButton("Robot Speed");
		btn_Speed.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_Speed.setMargin(new Insets(5, 5, 5, 5));
		btn_Speed.setFocusPainted(false);
		btn_Speed.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_Speed);

		// Time-limited Exploration button
		JButton btn_TimeExploration = new JButton("Time-limited");
		btn_TimeExploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_TimeExploration.setMargin(new Insets(5, 5, 5, 5));
		btn_TimeExploration.setFocusPainted(false);
		btn_TimeExploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_TimeExploration);

		// Coverage-limited Exploration button
		JButton btn_CoverageExploration = new JButton("Coverage-limited");
		btn_CoverageExploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_CoverageExploration.setMargin(new Insets(5, 5, 5, 5));
		btn_CoverageExploration.setFocusPainted(false);
		btn_CoverageExploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_CoverageExploration);
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
				TimeUnit.MILLISECONDS .sleep(botSpeed);
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
			theMap.repaint();
		}
	}

	private static void FastestPath(){
		bot.setRobotPos(1,1);
		theMap.repaint();
		ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		Stack<Block> path = shortestPath.runShortestPath(theMap, 13, 18);
		moveRobot(bot, path);
		runFastestPath = false;
	}
}
