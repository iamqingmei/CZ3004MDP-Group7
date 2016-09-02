import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import robot.Robot;
//import robot.RobotConstants.DIRECTION;

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


	public static void main(String[] args) {
		bot = new Robot(1,1);
		theMap = new Map();
		theMap.setObstacle(8, 14);
		theMap.setObstacle(9, 14);
		theMap.setObstacle(10, 14);
		theMap.setObstacle(6,0);
		theMap.setObstacle(7,0);
		theMap.setObstacle(9,9);

		ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		shortestPath.runShortestPath(theMap, 13, 18);
		shortestPath.printGscores();
		bot.setRobotPos(13,18);
		shortestPath.reset(theMap, bot);
		shortestPath.runShortestPath(theMap, 1, 1);
		shortestPath.printGscores();

		displayEverythings();
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
		_mainCards.add(bot, "MAIN");
		
		// // Initialize the robot map, used for exploration and shortest path
		// _robotMap = new RobotMap(theMap);
		// _mainCards.add(_robotMap, "ROBOT MAP");
		
		// Show the real map (main menu) by default
		CardLayout cl = ((CardLayout) _mainCards.getLayout());
	    cl.show(_mainCards, "MAIN");
	    // cl.show(_mainCards, "ROBOT CONFIG");
		
	}

}
